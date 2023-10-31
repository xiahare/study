#!/bin/bash

set -o monitor

# arguments
IMPALA_IP=impala.service.consul

# database schemas
DATABASE="db_log_public"

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
