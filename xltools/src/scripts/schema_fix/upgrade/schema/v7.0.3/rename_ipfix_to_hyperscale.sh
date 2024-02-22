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
        echo "$(date) Reached the maximum number of tries, going to exit"
        exit 1
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

echo "------ upgrade start ---------"
date

echo "------ rename ipfix to hyperscale---------"

arr_fgt_ipfix_table_name=(`impala-shell -i ${IMPALA_IP} -d ${DATABASE} -B -q "show tables like '__*_fgt_ipfix'"`)
for each_fgt_ipfix_table_name in "${arr_fgt_ipfix_table_name[@]}"
do
  old_name=${each_fgt_ipfix_table_name}
  new_name=${each_fgt_ipfix_table_name//ipfix/hyperscale}
  # __root_fgt_ipfix
  # newCol=`impala-shell -i ${IMPALA_IP} -d ${DATABASE} -B -q "describe ${each_fgt_ipfix_table_name}"|grep dtime`
  newCol=`impala-shell -i ${IMPALA_IP} -d ${DATABASE} -B -q "describe ${old_name}"|grep dtime`
  if [ "${newCol}" == "" ] ; then
    echo Warning: The table [${old_name}] need to add column "dtime,action,source" first. Could not rename.
    continue
  else

    impala-shell -i ${IMPALA_IP} -d ${DATABASE} -q "ALTER TABLE ${old_name} RENAME TO ${new_name};DROP VIEW IF EXISTS ${old_name}_view;"
  fi

done

arr_fgt_ipfix_pattern_name=(`impala-shell -i ${IMPALA_IP} -d ${DATABASE} -B -q "show tables like '*_fgt_ipfix'"`)
for each_fgt_ipfix_pattern_name in "${arr_fgt_ipfix_pattern_name[@]}"
do
  # __root_fgt_ipfix or 3_fgt_ipfix
  if [[ ${each_fgt_ipfix_pattern_name} == __* ]]; then
  	# start with __
    continue
  else
    adom_view=${each_fgt_ipfix_pattern_name}
    view_tokens=(${each_fgt_ipfix_pattern_name//_/ })
    adom=${view_tokens[0]}
    storage=${adomInfoMap[${adom}]}
    table_name=${adom_view//${adom}/__${storage}}
    
    exist_table=`impala-shell -i ${IMPALA_IP} -d ${DATABASE} -B -q "show tables '${table_name}'"`
    if [ ${exist_table} == ${table_name} ]; then
      continue
    else 
      impala-shell -i ${IMPALA_IP} -d ${DATABASE} -q "DROP VIEW IF EXISTS ${adom_view};"
    fi
    
  fi
done

echo "------ upgrade end ---------"
date
