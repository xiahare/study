#!/bin/bash
set -e

PARAM_HASH_ID=$1
PARAM_START_DATE=$2
PARAM_END_DATE=$3
FIXED_TIME_00="00:00:00"
FIXED_TIME_59="23:59:59"
PARAM_START_TIME="${PARAM_START_DATE} ${FIXED_TIME_00}"
PARAM_END_TIME="${PARAM_END_DATE} ${FIXED_TIME_59}"


log=/mnt/boot/logs/rebuil_special_facet.log
data_server_URL=http://data-server.service.consul.:8080
IMPALA_IP=impala.service.consul.
DATABASE="db_log_public"
STORAGE_ID=root

clean_old_facet() {
  echo "Cleaning old facet [${PARAM_HASH_ID}]" | tee -a $log
  # 0.0, Span sub time range
  int1day=86400 # 86400=24*3600

  start_timestamp=$(date -d ${PARAM_START_DATE} +%s)
  end_timestamp=$(date -d ${PARAM_END_DATE} +%s)

  arr_sub_start_time=()
  arr_sub_end_time=()
  current_timestamp="$start_timestamp"
  while [ "$current_timestamp" -le "$end_timestamp" ]; do
      current_date=$(date -d "@$current_timestamp" +%Y-%m-%d)
      next_date=$(date -d "@$((current_timestamp + int1day))" +%Y-%m-%d)
      arr_sub_start_time+=("$current_date")
      arr_sub_end_time+=("$next_date")
      echo "[$current_date -> $next_date)" | tee -a $log
      current_timestamp=$((current_timestamp + int1day))  # 86400=24*3600
  done

  # level 2 facet_array
  sql_find_level2_list="select hash_id from __root_facet_define where hash_pid=${PARAM_HASH_ID} union all select hash_id from __root_facet_define where hash_pid=(select hash_id from __root_facet_define where hash_pid=${PARAM_HASH_ID})"
  declare -a arr_hash_id_level2=(`impala-shell -i ${IMPALA_IP} -d ${DATABASE} -B -q "${sql_find_level2_list}"`)
  echo "arr_hash_id_level2: [${arr_hash_id_level2[@]}]" | tee -a $log

  for((i = 0; i<${#arr_sub_start_time[@]}; i++)); do

      sub_start_time=${arr_sub_start_time[i]}
      sub_end_time=${arr_sub_end_time[i]}
      echo "Deleting [${sub_start_time} - ${sub_end_time}) " | tee -a $log

      sql_del_main_facet="delete from __${STORAGE_ID}_facet_process where start_time>='${sub_start_time}' and  start_time<'${sub_end_time}' and hash_id=${PARAM_HASH_ID};delete from __root_facet_result where start_time>='${sub_start_time}' and  start_time<'${sub_end_time}' and hash_id=${PARAM_HASH_ID};"
      impala-shell -i ${IMPALA_IP} -d ${DATABASE} -q "${sql_del_main_facet}" 2>&1 | tee -a $log

      for hash_id_level2 in "${arr_hash_id_level2[@]}"
      do
        sql_del_level2_facet="delete from __${STORAGE_ID}_facet_process where start_time>='${sub_start_time}' and  start_time<'${sub_end_time}' and hash_id=${hash_id_level2};delete from __root_facet_result where start_time>='${sub_start_time}' and  start_time<'${sub_end_time}' and hash_id=${hash_id_level2};"
        impala-shell -i ${IMPALA_IP} -d ${DATABASE} -q "${sql_del_level2_facet}" 2>&1 | tee -a $log
      done
  done
}


generate_special_facet(){
  echo "Generating facet [${PARAM_HASH_ID}]." | tee -a $log

  local response
  # start task
  response=$(curl -X POST "${data_server_URL}/data/v2/facets/mgmt/unroll/run/start" -H "accept: */*" -H "Content-Type: application/json" -d "{  \"hash_id_list\": [\"${PARAM_HASH_ID}\"], \"timerange\": {    \"start\": \"${PARAM_START_TIME}\",    \"end\": \"${PARAM_END_TIME}\"  }}" | tee -a $log)

  # return taskId
  taskId=$(echo "$response" | jq -r '.taskId')

  # test print taskId
  echo "Task ID: $taskId" | tee -a $log

  # fetch 

  RUNNING="running"
  SUCCESS="success"
  FAIL="fail"
  SLEEP_SECOND=10
  MAX_FETCH_LOOP=8640  # 1day, loop every 10s

  local i=0
  for ((i=0; i<MAX_FETCH_LOOP; i++)); do
      #  curl fetch ,save to response 
      response=$(curl -X GET "${data_server_URL}/data/v2/facets/async/fetch?taskId=${taskId}" -H "accept: */*")

      echo "Fetch [${i}]-th, time [$((i*SLEEP_SECOND))s]"  | tee -a $log
      
      taskStatus=$(echo $response | jq -r '.taskStatus')

      # check taskStatus 
      if [ "$taskStatus" = ${SUCCESS} ]; then
          # success
          echo "Task completed successfully."
          echo "Response: $response"  | tee -a $log
          break
      elif [ "$taskStatus" = ${RUNNING} ]; then
          # wait
          sleep ${SLEEP_SECOND}
      else
          # fail
          echo "Unexpected taskStatus: $taskStatus"
          echo "Response: $response"  | tee -a $log
          break
      fi
  done
  echo "Generated facet [${PARAM_HASH_ID}]." | tee -a $log
}

echo "More logs can be found in ${log} . Please use the command: " | tee -a $log
echo "    tail -fn200 ${log} ." | tee -a $log

clean_old_facet
generate_special_facet

echo "Finished re-build facet [${PARAM_HASH_ID}]." | tee -a "$log"

echo "More logs can be found in ${log} . Please use the command: " | tee -a $log
echo "    tail -fn200 ${log} ." | tee -a $log
