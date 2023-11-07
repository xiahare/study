Please follow the steps :

# 1. On local host, copy scripts tar to fazbd controller blade : aaa.xxx.yyy.zzz
scp upgrade_schema_721_to_725.tar root@aaa.xxx.yyy.zzz:/data2/

# 2. Login to fazbd controller blade: aaa.xxx.yyy.zzz
ssh root@aaa.xxx.yyy.zzz

# 3. On fazbd controller, extract the tar
cd /data2
tar -xvf upgrade_schema_721_to_725.tar

# 4. run the scripts
cd upgrade_schema_721_to_725
chmod +x run.sh
sh run.sh


# Note:
# It need several minutes to execute.
# The output file will be found in /mnt/boot/logs/rerun_upgrade_722_725.log
# After it finishes, pls wait for additional several minutes to check if facet diagnosis works. ( Because some servers need to sync up the database schema changes )