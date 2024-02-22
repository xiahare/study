#!/bin/bash

log=/mnt/boot/logs/pre_upgrade_fix.log

upgrade_schema701() {
    # current dir should contains folder "upgrade"
    chmod +x upgrade/schema/upgrade.sh
    system_platform=$(awk -F '=' '{print $2}' /etc/platform.conf)
    num_of_workers=1
    if [ "$system_platform" = "HW" ]; then
        num_of_workers=20
    fi
    if ! upgrade/schema/upgrade.sh impala.service.consul upgrade/schema/v7.0.1/upgrade.conf $num_of_workers >> $log 2>&1; then
        echo "Upgrade schema run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

upgrade_schema_703() {
    # current dir should contains folder "upgrade"
    chmod +x upgrade/schema/upgrade.sh
    system_platform=$(awk -F '=' '{print $2}' /etc/platform.conf)
    num_of_workers=1
    if [ "$system_platform" = "HW" ]; then
        num_of_workers=20
    fi
    if ! upgrade/schema/upgrade.sh impala.service.consul upgrade/schema/v7.0.3/upgrade.conf $num_of_workers >> $log 2>&1; then
        echo "Upgrade schema run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

rename_facet_tables() {
    chmod +x upgrade/schema/v7.0.3/rename_ipfix_to_hyperscale.sh
    echo "Rename facet tables ..."
    if ! upgrade/schema/v7.0.3/rename_ipfix_to_hyperscale.sh impala.service.consul >> $log 2>&1; then
        echo "Rename facet tables run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

upgrade_schema_703p() {
    # current dir should contains folder "upgrade"
    chmod +x upgrade/schema/upgrade.sh
    system_platform=$(awk -F '=' '{print $2}' /etc/platform.conf)
    num_of_workers=1
    if [ "$system_platform" = "HW" ]; then
        num_of_workers=20
    fi
    if ! upgrade/schema/upgrade.sh impala.service.consul upgrade/schema/v7.0.3-patch/upgrade.conf $num_of_workers >> $log 2>&1; then
        echo "Upgrade schema run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

upgrade_schema_704() {
    # current dir should contains folder "upgrade"
    chmod +x upgrade/schema/upgrade.sh
    num_of_cpu=$(lscpu | grep "^CPU(s):.*" |  sed 's/[^0-9]*//g')
    num_of_workers=2
    if [ "$num_of_cpu" -gt 20 ]; then
        num_of_workers=20
    fi
    if ! upgrade/schema/upgrade.sh impala.service.consul upgrade/schema/v7.0.4/upgrade.conf $num_of_workers >> $log 2>&1; then
        echo "Upgrade schema run into error, exit abort upgrade with error" | tee -a $log
        exit 1
    fi
}

upgrade_schema701
upgrade_schema_703
rename_facet_tables
upgrade_schema_703p
upgrade_schema_704

echo "Finished schema upgrade" | tee -a "$log"
