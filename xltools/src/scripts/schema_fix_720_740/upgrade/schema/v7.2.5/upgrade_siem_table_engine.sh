#!/bin/bash
set -ex

#Usage:  upgrade_siem_table_engine.sh SHARD_CNT (ex: fazbd_clickhouse_upgrade_database_schema_engine.sh 13)

SHARD_CNT=$1
DATABASE_NAME=db_log_public

for ((SHARD_NAME=0; SHARD_NAME<${SHARD_CNT}; ++SHARD_NAME))
do
    echo "update database schema engine at pod ${SHARD_NAME}..."
    CH_POD_NAME=$(kubectl --kubeconfig=/root/.kube/config get pod -n db -o wide | grep ch-clickhouse-shard${SHARD_NAME}- | awk '{print $1}')
    for POD in ${CH_POD_NAME}
    do
        TABLES=$(kubectl --kubeconfig=/root/.kube/config exec -i -n db "${POD}" -- /bin/bash -c "clickhouse-client -u admin --password \$CLICKHOUSE_ADMIN_PASSWORD --query \"SELECT table FROM system.tables
    WHERE database = '${DATABASE_NAME}' and engine = 'ReplicatedReplacingMergeTree'
    format TSVRaw;\"")
        for TABLE in ${TABLES}
        do
            for POD in ${CH_POD_NAME}
            do
                echo "pod ${POD} table ${TABLE} updating."
                kubectl --kubeconfig=/root/.kube/config exec -i -n db "${POD}" -- /bin/bash -c "clickhouse-client -u admin --password \$CLICKHOUSE_ADMIN_PASSWORD --query \"detach table ${DATABASE_NAME}.${TABLE}\""
                zookeeper-client -server ch-standalonekeeper.service.consul.:9181 deleteall /clickhouse/tables/shard${SHARD_NAME}/${DATABASE_NAME}/${TABLE}
                kubectl --kubeconfig=/root/.kube/config exec -i -n db ${POD} -- /bin/bash -c "sed -i 's/ReplicatedReplacingMergeTree/ReplicatedMergeTree/g' /var/lib/clickhouse/metadata/${DATABASE_NAME}/${TABLE}.sql"
            done
            for POD in ${CH_POD_NAME}
            do
                kubectl --kubeconfig=/root/.kube/config exec -i -n db "${POD}" -- /bin/bash -c "clickhouse-client -u admin --password \$CLICKHOUSE_ADMIN_PASSWORD --query \"attach table ${DATABASE_NAME}.${TABLE}\""
                kubectl --kubeconfig=/root/.kube/config exec -i -n db "${POD}" -- /bin/bash -c "clickhouse-client -u admin --password \$CLICKHOUSE_ADMIN_PASSWORD --query \"SYSTEM RESTORE REPLICA ${DATABASE_NAME}.${TABLE}\""
                echo "pod ${POD} table ${TABLE} updated."
            done
            echo "table ${TABLE} updated."
        done
    done
    echo "shard ${SHARD_NAME} updated."
done

echo "update done."
