#!/bin/bash

log=/mnt/boot/logs/pre_upgrade_fix.log

upgrade_schema720_721() {
    # current dir should contains folder "upgrade"
    chmod +x upgrade/schema/upgrade.sh
    num_of_cpu=$(lscpu | grep "^CPU(s):.*" |  sed 's/[^0-9]*//g')
    num_of_workers=2
    if [ "$num_of_cpu" -gt 20 ]; then
        num_of_workers=20
    fi
    
    echo "Upgrade to 7.2.0 schema ..."
    if ! upgrade/schema/upgrade.sh impala.service.consul upgrade/schema/v7.2.0/upgrade.conf $num_of_workers >> $log 2>&1; then
        echo "Upgrade 7.2.0 schema run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi

    echo "Upgrade to 7.2.1 schema ..."
    if ! upgrade/schema/upgrade.sh impala.service.consul upgrade/schema/v7.2.1/upgrade.conf $num_of_workers >> $log 2>&1; then
        echo "Upgrade 7.2.1 schema run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

upgrade_schema_722() {
    # current dir should contains folder "upgrade"
    chmod +x upgrade/schema/upgrade.sh
    num_of_cpu=$(lscpu | grep "^CPU(s):.*" |  sed 's/[^0-9]*//g')
    num_of_workers=2
    if [ "$num_of_cpu" -gt 20 ]; then
        num_of_workers=20
    fi
    
    echo "Upgrade to 7.2.2 schema ..."
    if ! upgrade/schema/upgrade.sh impala.service.consul upgrade/schema/v7.2.2/upgrade.conf $num_of_workers >> $log 2>&1; then
        echo "Upgrade 7.2.2 schema run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

upgrade_schema_725() {
    # current dir should contains folder "upgrade"
    chmod +x upgrade/schema/upgrade.sh
    num_of_cpu=$(lscpu | grep "^CPU(s):.*" |  sed 's/[^0-9]*//g')
    num_of_workers=2
    if [ "$num_of_cpu" -gt 20 ]; then
        num_of_workers=20
    fi
    
    echo "Upgrade to 7.2.5 schema ..."
    if ! upgrade/schema/upgrade.sh impala.service.consul upgrade/schema/v7.2.5/upgrade.conf $num_of_workers >> $log 2>&1; then
        echo "Upgrade 7.2.5 schema run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

upgrade_schema_726() {

    # current dir should contains folder "upgrade"
    chmod +x upgrade/schema/upgrade.sh
    num_of_cpu=$(lscpu | grep "^CPU(s):.*" |  sed 's/[^0-9]*//g')
    num_of_workers=2
    if [ "$num_of_cpu" -gt 20 ]; then
        num_of_workers=20
    fi
    
    echo "Upgrade to fazbd v7.2.6 schema ..."
    if ! upgrade/schema/upgrade.sh impala.service.consul upgrade/schema/v7.2.6/upgrade.conf $num_of_workers >> $log 2>&1; then
        echo "Upgrade to 7.2.6 schema run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi

    chmod +x upgrade/schema/v7.2.6/remend_tinyint_cols.sh
    echo "Fix incorrect tinyint columns ..."
    if ! upgrade/schema/v7.2.6/remend_tinyint_cols.sh >> $log 2>&1; then
        echo "Fix incorrect tinyint columns run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

upgrade_schema_740() {
    # current dir should contains folder "upgrade"
    chmod +x upgrade/schema/upgrade.sh
    num_of_cpu=$(lscpu | grep "^CPU(s):.*" |  sed 's/[^0-9]*//g')
    num_of_workers=2
    if [ "$num_of_cpu" -gt 20 ]; then
        num_of_workers=20
    fi
    
    echo "Upgrade to fazbd v7.4.0 (faz-v7.4.1) schema ..." | tee -a $log
    if ! upgrade/schema/upgrade.sh impala.service.consul upgrade/schema/v7.4.0/upgrade.conf $num_of_workers >> $log 2>&1; then
        echo "Upgrade 7.4.0 schema run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

integrity_check(){
  echo "Check and re-creating views."
  curl -X POST http://data-catalog-server.service.consul.:8080/datacatalog/v1/storages/integrity/check/start
}

upgrade_schema720_721
upgrade_schema_722
upgrade_schema_725
upgrade_schema_726
upgrade_schema_740

integrity_check

echo "Finished schema upgrade" | tee -a "$log"
