#!/bin/bash

DATABASE="db_log_public"
out_log_file=/mnt/boot/logs/remend_tinyint_column_output.log

INCORRECT_COL_TYPE="SMALLINT"
CORRECT_COL_TYPE="TINYINT"


declare -A tinyintColumnsMap
# v7.0.1
tinyintColumnsMap["fmg_event"]="cpuusage cpuiowait memusage swapusage diskusage diskfreegb ioutil"
tinyintColumnsMap["faz_event"]="cpuusage cpuiowait memusage swapusage diskusage diskfreegb ioutil"
# v7.0.3
tinyintColumnsMap["fpx_traffic"]="signal snr"
tinyintColumnsMap["fpx_event"]="snr"
# v7.2.0
tinyintColumnsMap["fgt_event"]="advpnsc"
tinyintColumnsMap["fgt_gtp"]="timeoutdelete"

echo Start to Fix TINYINT columns ...
echo The output log can be found at ${out_log_file}

for table_id in "${!tinyintColumnsMap[@]}"; do

  tbl_pattern="__*_${table_id}"
  tbl_names=(`impala-shell -d ${DATABASE} -B -q "show tables like '${tbl_pattern}'" 2>>${out_log_file}`)

  tinyintCols=(${tinyintColumnsMap[${table_id}]})
  for tbl_name in "${tbl_names[@]}"; do
      tbl_def=`impala-shell -d ${DATABASE} -B -q "show create table ${tbl_name}" 2>>${out_log_file}`

      for tinyint_col_name in "${tinyintCols[@]}"; do

        if [[ ${tbl_def} == *"${tinyint_col_name} ${INCORRECT_COL_TYPE}"* ]]; then
          impala-shell -d ${DATABASE} -q "alter TABLE ${tbl_name} drop column ${tinyint_col_name} " 2>>${out_log_file}
          impala-shell -d ${DATABASE} -q "alter TABLE ${tbl_name} add column ${tinyint_col_name} ${CORRECT_COL_TYPE}" 2>>${out_log_file}
        fi
      done
  done
done

echo Fix TINYINT columns Complete!
echo The output log can be found at ${out_log_file}