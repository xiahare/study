#!/bin/bash


log=/mnt/boot/logs/rerun_upgrade_722_725.log

upgrade_schema_722() {
    # current dir should contains folder "upgrade"
    chmod +x upgrade.sh
    num_of_cpu=$(lscpu | grep "^CPU(s):.*" |  sed 's/[^0-9]*//g')
    num_of_workers=2
    if [ "$num_of_cpu" -gt 20 ]; then
        num_of_workers=20
    fi

    echo "Upgrade to 7.2.2 schema ..."
    if ! ./upgrade.sh impala.service.consul upgrade_722.conf $num_of_workers >> $log 2>&1; then
        echo "Upgrade 7.2.2 schema run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

drop_facet_bak_tables(){
    chmod +x drop_facet_bak_tables.sh

    echo "Dropping facet bak tables created from 7.2.1..."
    if ! ./drop_facet_bak_tables.sh impala.service.consul >> $log 2>&1; then
        echo "Drop facet bak tables run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

upgrade_schema_725() {
    # current dir should contains folder "upgrade"
    chmod +x upgrade.sh
    num_of_cpu=$(lscpu | grep "^CPU(s):.*" |  sed 's/[^0-9]*//g')
    num_of_workers=2
    if [ "$num_of_cpu" -gt 20 ]; then
        num_of_workers=20
    fi

    echo "Upgrade to 7.2.5 schema ..."
    if ! ./upgrade.sh impala.service.consul upgrade_725.conf $num_of_workers >> $log 2>&1; then
        echo "Upgrade 7.2.5 schema run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

integrity_check(){
  echo "Check and re-creating views."
  curl -X POST http://data-catalog-server.service.consul.:8080/datacatalog/v1/storages/integrity/check/start
}
echo "More logs can be found in ${log} . Please use the command: " | tee -a $log
echo "    tail -fn200 ${log} ." | tee -a $log

upgrade_schema_722
drop_facet_bak_tables
upgrade_schema_725
integrity_check

echo "Finished re-upgrading." | tee -a "$log"
echo "Several minutes later, it will sync up to some  and then please check if it fixes the schema upgrade issues." | tee -a "$log"
echo "More logs can be found in ${log} . Please use the command: " | tee -a $log
echo "    tail -fn200 ${log} ." | tee -a $log
