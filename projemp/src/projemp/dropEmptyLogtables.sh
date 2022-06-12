#!/bin/bash

# arguments
IMPALA_IP=impala.service.consul.
PSQL_IP=stolon-proxy.service.consul.
DATABASE=db_log_public
OUTPUT_FILE=drop_results.log
OUTPUT_SQL=drop_empty_tables.sql
# 0 is enable
EXCUTING_DISABLE=1

echo "------ retrieve physical tables ---------"
tablesStr=$(env PGPASSWORD=postgres psql -h $PSQL_IP -U postgres -d metastore -c "select \"TBL_NAME\" from \"TBLS\" where \"TBL_NAME\" ~ '^__.*_.*' and \"TBL_NAME\" not like '%facet%' and \"TBL_NAME\" not like '%ioc%' and \"TBL_TYPE\" != 'VIRTUAL_VIEW';")
tempArray=(${tablesStr// | | / })
# because result will be like "TBL_NAME ------------------------ ... (xxx rows)"
# escape the first 2 items and last 2 items here
tablesArray=("${tempArray[@]:2:${#tempArray[@]}-4}")
echo "fetched ${#tablesArray[@]} physical tables"

echo "" > $OUTPUT_FILE
echo "" > $OUTPUT_SQL

for table in "${tablesArray[@]}"
do
  table_count=`impala-shell -i ${IMPALA_IP} -d ${DATABASE} -B -q "SELECT COUNT(*) FROM (SELECT 1 FROM $table LIMIT 1) t"`
  if [ ${table_count} -eq 0 ]
  then
    echo "DROPPING TABLE : $table" >> $OUTPUT_FILE
    echo "DROP TABLE IF EXISTS $table" >> $OUTPUT_SQL
    if [ $EXCUTING_DISABLE -eq 0 ]
    then
      `impala-shell -i ${IMPALA_IP} -d ${DATABASE} -B -q "DROP TABLE IF EXISTS $table"`
    fi
  else
    echo "$table is not empty!"  >> $OUTPUT_FILE
  fi
done