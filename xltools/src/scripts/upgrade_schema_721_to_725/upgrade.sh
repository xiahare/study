#!/bin/bash

set -o monitor
trap "handle_chld" CHLD

handle_chld() {
    for i in ${!pids[@]}; do
        if [ ! -d /proc/${pids[i]} ]; then
            wait ${pids[i]}
            echo "Stopped ${pids[i]}; exit code: $?"
            unset pids[i]
        fi
    done
}

# arguments
IMPALA_IP=$1
if [ -z "$IMPALA_IP" ]
then echo "IMPALA_IP must be given as 1st argument"
exit 1
fi

PSQL_IP=stolon-proxy.service.consul.

# schema changes to be read from a config file
CONFFILE=$2 # path of the conf file
# TODO check file empty or not
if [ -f "$CONFFILE" ]
  then source $CONFFILE
else
  echo "upgrade.conf file must be given as 2nd argument"
  exit 1
fi

NUM_OF_WORKERS=$3
if [ -z "$NUM_OF_WORKERS" ]
then echo "NUM_OF_WORKERS must be given as 3rd argument"
exit 1
fi

echo "------ check configurations ---------"

# database schemas
DATABASE="db_log_public"

echo "------> check database connection <---------"
retries=0
while true
do
    resNum=`impala-shell -i ${IMPALA_IP} -d ${DATABASE} -q "select 1" | grep "| 1 |" | wc -l`
    if [ $resNum -ne 2 ]
    then
        echo "Testing impala ${DATABASE} connection ..."
    else
        echo "${DATABASE} is OK."
        break
    fi
    echo retries $retries
    if [ $retries -ge 20 ]; then
        # check if system_setup_done is true
        system_setup_done=$(consul kv get config/system_setup_done)
        if [ "$system_setup_done" == "false" ]; then
            echo "the database is not set up yet, skipping the schema upgrade "
            exit 0
        else
            echo "$(date) Reached the maximum number of tries, going to exit"
            exit 1
        fi
    fi
    retries=$((retries  + 1))
    sleep 5
done

echo "------> check consul connection <---------"
retries=0
checkConsul() { curl -s http://127.0.0.1:8500/v1/status/leader | grep -q ":"; }
while true
do
    if ! checkConsul; then
        echo "Testing consul connection ..."
    else
        echo "consul is OK."
        adomMapping=$(consul kv get adoms/mapping)
        if [ "$adomMapping" == "" ]; then
            echo "[Warning]:Adom in consul is empty!Skip~~"
            exit 0
        fi
        echo adomMapping: ${adomMapping}
        break
    fi
    echo retries $retries
    if [ $retries -ge 20 ]; then
        echo "$(date) Reached the maximum number of tries, going to exit"
        exit 1
    fi
    retries=$((retries  + 1))
    sleep 5
done

echo "------ retrieve storage groups and adoms ---------"

declare -A adomInfoMap

# remove these 4 charactors: "{}_ and convert to array
# {"3":"__root","107":"__root"} -> 3:root 107:root
arr_adom_sg=(`echo ${adomMapping//,/ }|tr -d \"{}_`)

for adom_sg in "${arr_adom_sg[@]}"
do

  # 3:root -> 3 root
  adom_sg_item=(${adom_sg//:/ })
  # put into map
  adomInfoMap[${adom_sg_item[0]}]=${adom_sg_item[1]}
done

echo "------ retrieve physical tables ---------"
tablesStr=$(env PGPASSWORD=postgres psql -h $PSQL_IP -U postgres -d metastore -c "select \"TBL_NAME\" from \"TBLS\" where \"TBL_NAME\" ~ '^__.*_.*' and \"TBL_TYPE\" != 'VIRTUAL_VIEW';")
tempArray=(${tablesStr// | | / })
# because result will be like "TBL_NAME ------------------------ ... (xxx rows)"
# escape the first 2 items and last 2 items here
tablesArray=("${tempArray[@]:2:${#tempArray[@]}-4}")
echo "fetched ${#tablesArray[@]} physical tables"

baseQuery=$''

for table in "${tablesArray[@]}"
do
  tabTokens=(${table//_/ }) # split with underscore
  tabTokenLen=${#tabTokens[@]} # element num

  logDefinition="${tabTokens[1]}"
  for((i=2;i<tabTokenLen;i++));do
    logDefinition="${logDefinition}_${tabTokens[i]}" # assembly log def like fgt_traffic
  done
  columnToRemove="${removeColumnsMap[${logDefinition}]}"
  if [ -n "$columnToRemove" ]
  then
    colArray=($columnToRemove)
	for col in "${colArray[@]}"
	do
	  baseQuery+="ALTER TABLE ${table} Drop COLUMN ${col};"
	done
  fi
  columnToChangedAdd="${newColumnsSeparateMap[${logDefinition}]}"
  if [ -n "$columnToChangedAdd" ]
  then
    baseQuery+="ALTER TABLE ${table} ADD IF NOT EXISTS COLUMNS ${columnToChangedAdd};"
  fi
  columnToAdd="${newColumnsMap[${logDefinition}]}"
  if [ -n "$columnToAdd" ]
  then
    baseQuery+="ALTER TABLE ${table} ADD IF NOT EXISTS COLUMNS ${columnToAdd};"
  fi
  columnToRename="${renameColumnsMap[${logDefinition}]}"
  if [ -n "$columnToRename" ]
  then
    oIFS="$IFS"
    IFS=","
    declare -a columnToRenameArray=(${columnToRename})
    for col in ${columnToRenameArray[@]}
    do
      baseQuery+="ALTER TABLE ${table} CHANGE ${col};"
    done
    IFS="$oIFS"
    unset oIFS
  fi
  columnToLogidChange="${removeLogidRequiredMap[${logDefinition}]}"
  if [ -n "$columnToLogidChange" ]
  then
    baseQuery+="ALTER TABLE ${table} ALTER COLUMN ${columnToLogidChange};"
  fi
  
  if [ -n "$columnToAdd$columnToRename$columnToRemove" ]
  then
    baseQuery+="DROP VIEW IF EXISTS ${table}_view;"
  fi
done

echo "------ retrieve adom views ---------"
adomViewsStr=$(env PGPASSWORD=postgres psql -h $PSQL_IP -U postgres -d metastore -c "select \"TBL_NAME\" from \"TBLS\" where \"TBL_NAME\" ~ '^[0-9]+_*' and \"TBL_NAME\" not like '%facet%' and \"TBL_TYPE\" = 'VIRTUAL_VIEW';")
tempArray=(${adomViewsStr// | | / })
# because result will be like "TBL_NAME ------------------------ ... (xxx rows)"
# escape the first 2 items and last 2 items here
adomViewsArray=("${tempArray[@]:2:${#tempArray[@]}-4}")
echo "fetched ${#adomViewsArray[@]} adom views"

for i in $(seq 1 "$NUM_OF_WORKERS"); do
    eval "worker$i=$''"
done

idx=0
for view in "${adomViewsArray[@]}"
do
  viewTokens=(${view//_/ })
  viewTokenLen=${#viewTokens[@]} # element num
  viewAdom=${viewTokens[0]}

  logDefinition="${viewTokens[1]}"
  for((i=2;i<viewTokenLen;i++));do
    logDefinition="${logDefinition}_${viewTokens[i]}" # assembly log def like fgt_traffic
  done

  tableUpdate="${newColumnsMap[${logDefinition}]}"
  tableUpdateRename="${renameColumnsMap[${logDefinition}]}"
  storageGroup="${adomInfoMap[${viewAdom}]}"
  if [ -n "${tableUpdate}${tableUpdateRename}" ] && [ -n "$storageGroup" ]
  then
    tablename="__${storageGroup}_${logDefinition}"
    n=$((idx%NUM_OF_WORKERS+1))
    ((idx++))
    dropView="DROP VIEW IF EXISTS ${view};"
    eval "worker$n+=\"$dropView\""
  fi
done

echo "------ Generating SQL: remove tables with views---------"
# removeTableSQLArray without ";"
declare -a removeTableSQLArray=()
# removeTableArray from upgrade.conf
for table_id in "${removeTableArray[@]}"
do
	arr_table_name=(`impala-shell -i ${IMPALA_IP} -d ${DATABASE} -B -q "show tables like '__*_${table_id}'"`)
	for each_table_name in "${arr_table_name[@]}"
	do
		removeTableSQLArray+=("DROP TABLE IF EXISTS ${each_table_name}")
	done
	arr_table_name=(`impala-shell -i ${IMPALA_IP} -d ${DATABASE} -B -q "show tables like '__*_${table_id}_view'"`)
	for each_table_name in "${arr_table_name[@]}"
	do
		removeTableSQLArray+=("DROP VIEW IF EXISTS ${each_table_name}")
	done
	arr_table_name=(`impala-shell -i ${IMPALA_IP} -d ${DATABASE} -B -q "show tables like '*_${table_id}'"`)
	for each_table_name in "${arr_table_name[@]}"
	do
		removeTableSQLArray+=("DROP VIEW IF EXISTS ${each_table_name}")
	done
done

echo "------ upgrade start ---------"
date

echo -e "base table query: $baseQuery"
oIFS="$IFS"
IFS=";"
declare -a sqlArray=(${baseQuery})
batch_sql_count=50
batch_sql=$''
# merge removeTablseSQLArray
sqlArray=("${sqlArray[@]}" "${removeTableSQLArray[@]}")
for sql in ${sqlArray[@]}
do
  batch_sql+="${sql};"
  if [ $((batch_sql_idx%batch_sql_count)) -eq $((batch_sql_count-1)) ] || [ ${#sqlArray[@]} -eq $((batch_sql_idx+1)) ]
  then
    impala-shell -i "${IMPALA_IP}" -d "${DATABASE}" -c -q "$batch_sql"
    batch_sql=$''
  fi
  ((batch_sql_idx++))
  echo "sql[${batch_sql_idx}]"
done
IFS="$oIFS"
unset oIFS

# run adomQuery concurrently with NUM_OF_WORKERS 
for i in $(seq 1 "$NUM_OF_WORKERS"); do
   worker=worker${i}
   if [ -z "${!worker}" ] 
   then 
     echo "worker$i nothing to do"
   else
     echo "adom table query worker$i: ${!worker}"
     impala-shell -i "${IMPALA_IP}" -d "${DATABASE}" -c -q "${!worker}" &
     pids+=($!)
   fi
done

# wait until all suceed
while [ ${#pids[@]} -gt 0 ]; do echo "Waiting for impala-shell: ${pids[*]}"; sleep 2; done

echo "------ upgrade end ---------"
date
