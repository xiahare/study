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

echo "------ drop facet bak start ---------"
echo "------ dropping facet bak tables"

drop_facet_table(){
	table_id=$1
	arr_table_name=(`impala-shell -i ${IMPALA_IP} -d ${DATABASE} -B -q "show tables like '__*_${table_id}_bak'"`)
	for each_table_name in "${arr_table_name[@]}"
	do
	  bak_name=${each_table_name}

	  impala-shell -i ${IMPALA_IP} -d ${DATABASE} -q "DROP TABLE IF EXISTS ${bak_name};"
	done
}

drop_facet_table "facet_define"
drop_facet_table "facet_process"
drop_facet_table "facet_result"

echo "------ drop facet bak end ---------"
date
