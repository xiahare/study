
# 1. ssh to fazbd controller (NOT admin console)
     NOTE: If you have ssh to fazbd admin console, you can use the command `exec ssh root@198.18.1.2`

# 2. Change to the directory to /data2 and mkdir a new dir
    $ cd /data2
    $ mkdir rebuild_facet
    $ cd /rebuild_facet

# 3. Copy the file of `rebuild_special_facet.sh` to fazbd controller (NOT admin console).
    Method 1： You can use the scp FROM your local host which can access to fazbd controller
        $ scp /local/path/rebuild_special_facet.sh root@<fazbd_controller_ip>:/data2/rebuild_facet/
    Method 2： On fazbd controller, you can create a file named rebuild_special_facet.sh in the dir of /data2/rebuild_facet/, 
               and copy/paste the text content into this new file, save it.
               Grant the file of exe role.
         $ vi /data2/rebuild_facet/rebuild_special_facet.sh
         $ <copy/paste/save>
         $ chmod +x /data2/rebuild_facet/rebuild_special_facet.sh
# 4. Run the script in the background. 

         $ nohup sh rebuild_special_facet.sh 989760109194424201 "2023-11-20" "2023-11-29" >> output.log &
          We can see the logs in the file of `tail -fn200 /mnt/boot/logs/rebuil_special_facet.log` and `output.log`
        NOTE: It might take several hours. So, it is better to check next day.
            
```

Command usages:

# p1: facet_id, such as ,
# p2: start_date, such as, "2023-11-18"
# p3: end_date (included), such as, "2023-11-19"


# run a command in the background
nohup sh rebuild_special_facet.sh 989760109194424201 "2023-11-20" "2023-11-29" >> output.log &

# common command
sh rebuild_special_facet.sh 989760109194424201 "2023-11-20" "2023-11-29"

```

     
