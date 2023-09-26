#!/bin/bash

DATABASE="db_log_public"
out_log_file=output.log

PREFIX_NEW="new"
PREFIX_OLD="old"
PREFIX_BAK="bak"
normal_tbl_pattern="__*_facet_result"
create_tbl_tpl_file="_createT_facet_result_tpl.sql"
init_partition_value="2000-01-01T00:00:00.000000Z"

showPartitions=0

normal_tbls=(`impala-shell -d db_log_public -B -q "show tables like '${normal_tbl_pattern}'" 2>>${out_log_file}`)
bak_tbls=(`impala-shell -d db_log_public -B -q "show tables like '${PREFIX_BAK}${normal_tbl_pattern}'" 2>>${out_log_file}`)
new_tbls=(`impala-shell -d db_log_public -B -q "show tables like '${PREFIX_NEW}${normal_tbl_pattern}'" 2>>${out_log_file}`)
old_tbls=(`impala-shell -d db_log_public -B -q "show tables like '${PREFIX_OLD}${normal_tbl_pattern}'" 2>>${out_log_file}`)

create_table(){

	local ori_tbl_name=$1
	local flag_tbl_name=$2
	local tbl_name=${flag_tbl_name}${ori_tbl_name}
	
	impala-shell -d ${DATABASE} -B -f ${flag_tbl_name}${create_tbl_tpl_file} --var=table_name="${tbl_name}"  --var=init_partition="${init_partition_value}" 2>>${out_log_file}
	
	local tmp=`impala-shell -d ${DATABASE} -B -q "ALTER TABLE ${tbl_name} DROP RANGE PARTITION VALUES < '${init_partition_value}'" 2>>${out_log_file}`
	
	local result=$(impala-shell -d ${DATABASE} -B -q "show range partitions ${ori_tbl_name}" 2>>${out_log_file})
	
	IFS=$'\n' read -r -d '' -a partitions <<< "$result"
	
	local i=0;
	for ((i=0; i<${#partitions[@]}; i++)); do
	    local partition=${partitions[i]}
	
	   
	    local partition_with_quotes=$(echo "$partition" | sed -E "s/([0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9:.]+Z)/'\1'/g")
	
	    local tmp=`impala-shell -d ${DATABASE} -B -q "ALTER TABLE ${tbl_name} ADD RANGE PARTITION ${partition_with_quotes}" 2>>${out_log_file}`
	    
	done

}

create_new_tables(){

	local new_tbl_count=${#new_tbls[@]}
	
	local i=0;
	for ((i=0; i<${#normal_tbls[@]}; i++)); do
		echo Creating New Table : ${PREFIX_NEW}${normal_tbls}
	  local table_name=${normal_tbls[i]}
		create_table ${table_name} ${PREFIX_NEW}
	done	

}

create_old_tables(){

	local old_tbl_count=${#old_tbls[@]}
	
	local i=0;
	for ((i=0; i<${#normal_tbls[@]}; i++)); do
		echo Creating Old Table : ${PREFIX_NEW}${normal_tbls}
	  local table_name=${normal_tbls[i]}
		create_table ${table_name} ${PREFIX_OLD}
	done	

}


clean_table(){
	local tmp_tbl_name=$1
	
	impala-shell -d ${DATABASE} -B -q "DROP TABLE IF EXISTS ${tmp_tbl_name} " 2>>${out_log_file}	
}

clean_new_tables(){
	local new_tbl_count=${#new_tbls[@]}
	local i=0;
	for ((i=0; i<${new_tbl_count}; i++)); do
	  local table_name=${new_tbls[i]}
		clean_table ${table_name}
	done
}

clean_old_tables(){
	local old_tbl_count=${#old_tbls[@]}
	local i=0;
	for ((i=0; i<${old_tbl_count}; i++)); do
	  local table_name=${old_tbls[i]}
		clean_table ${table_name}
	done
}

rename_table(){
  local ori_tbl=$1
  local tar_tbl=$2
  impala-shell -d ${DATABASE} -B -q "ALTER TABLE ${ori_tbl} rename to ${tar_tbl} " 2>>${out_log_file}	
}

backup_tables(){
	# rename normal to bak_*
	local i=0;
	for ((i=0; i<${#normal_tbls[@]}; i++)); do
	  local ori_tbl_name=${normal_tbls[i]}
	  local tar_tbl_name=${PREFIX_BAK}${normal_tbls[i]}
		rename_table ${ori_tbl_name} ${tar_tbl_name}
	done	
}

recover_tables(){
	# rename bak__* to normal
	local i=0;
	for ((i=0; i<${#bak_tbls[@]}; i++)); do
	  local ori_tbl_name=${bak_tbls[i]}
	  local tar_tbl_name="${bak_tbls[i]#${PREFIX_BAK}}"
		rename_table ${ori_tbl_name} ${tar_tbl_name}
	done	
}

use_new_tables(){
	# rename new__* to normal
	local i=0;
	for ((i=0; i<${#new_tbls[@]}; i++)); do
	  local ori_tbl_name=${new_tbls[i]}
	  local tar_tbl_name="${new_tbls[i]#${PREFIX_NEW}}"
		rename_table ${ori_tbl_name} ${tar_tbl_name}
	done	
}
	
use_old_tables(){
	# rename old__* to normal
	local i=0;
	for ((i=0; i<${#old_tbls[@]}; i++)); do
	  local ori_tbl_name=${old_tbls[i]}
	  local tar_tbl_name="${old_tbls[i]#${PREFIX_OLD}}"
		rename_table ${ori_tbl_name} ${tar_tbl_name}
	done	
}

release_new_tables(){
	# rename normal to new__*
	local i=0;
	for ((i=0; i<${#normal_tbls[@]}; i++)); do
	  local ori_tbl_name=${normal_tbls[i]}
	  local tar_tbl_name=${PREFIX_NEW}${normal_tbls[i]}
		rename_table ${ori_tbl_name} ${tar_tbl_name}
	done	
}

release_old_tables(){
	# rename normal to old__*
	local i=0;
	for ((i=0; i<${#normal_tbls[@]}; i++)); do
	  local ori_tbl_name=${normal_tbls[i]}
	  local tar_tbl_name=${PREFIX_OLD}${normal_tbls[i]}
		rename_table ${ori_tbl_name} ${tar_tbl_name}
	done	
}

show_status(){

	echo ======== Normal TABLES  ======== 
	display_tables_info "${normal_tbls[@]}"

	echo ======== bak TABLES  ======== 
	display_tables_info "${bak_tbls[@]}"	
	
	echo ======== new TABLES  ======== 
	display_tables_info "${new_tbls[@]}"
	
	echo ======== old TABLES  ======== 
	display_tables_info "${old_tbls[@]}"

}

display_tables_info(){

	local tmp_tbls=("$@")
	
	echo "    |--"
	
	for ((i=0; i<${#tmp_tbls[@]}; i++)); do
			echo "    |${tmp_tbls[i]}"
	done		


  if (( showPartitions == 1 )) ; then
    echo ========  PARTITIONS  ======== 
		local i=0;
		for ((i=0; i<${#tmp_tbls[@]}; i++)); do
				local tbl_name=${tmp_tbls[i]}
				echo "  ------- [${tbl_name}] PARTITIONS----------"
		    impala-shell -d ${DATABASE} -B -q "show range partitions ${tbl_name}" 2>>${out_log_file}
		done		
  fi  	

}

main() {
	local cmd_options1="$1"
  case "${cmd_options1}" in
		
    status)

      if [ "$2" == "-showPartitions" ]; then
        showPartitions=1
      else
        showPartitions=0
      fi      
      show_status
      ;;
    createNew)
      create_new_tables
      ;;
    createOld)
      create_old_tables
      ;;      
    cleanNew)
      clean_new_tables
      ;;
    cleanOld)
      clean_old_tables
      ;;
    backup)
      backup_tables
      ;;
    recover)
      recover_tables
      ;;
    useNew)
      use_new_tables
      ;;
    useOld)
      use_old_tables
      ;;
    releaseNew)
      release_new_tables
      ;;
    releaseOld)
      release_old_tables
      ;;
    *)
      echo "Invalid command. Usage: $0 options"
      echo "------- options --------"
      echo "status: show facet_result tables info. Show partitions info with -showPartitions "
      echo "createNew : create new pk facet_result tables "
      echo "createOld : create new pk facet_result tables "
      echo "cleanNew : clean new tables"
      echo "cleanOld : clean old tables"
      echo "backup : backup current tables"
      echo "recover : recover current tables"
      echo "useNew : use new tables"
      echo "useOld : use old tables"  
      echo "releaseNew : release new tables"
      echo "releaseOld : releaseß old tables"          
      exit 1
      ;;
  esac
	local local_time=$(date +"%Y-%m-%d %H:%M:%S")
	local utc_time=$(date -u +"%Y-%m-%d %H:%M:%S")  
  echo Run [${cmd_options1}] at Local Time: ${local_time} UTC Time: ${utc_time} | tee -a ${out_log_file}
}

main "$@"