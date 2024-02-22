#!/bin/bash

log=/mnt/boot/logs/pre_upgrade_fix.log

json_file="FGTTraffic.json"

parse_json_table() {
    # JSON file path
    if [ ! -f "$json_file" ]; then
        echo "JSON file not found: $json_file"
        exit 1
    fi

    # declare -A col_array_name_type
    col_array_name_type=()
    # parse columns array
    col_array_name_type=($(jq -c '.columns[] | .name, .type' "$json_file"))

    # column structure
    : '
       "columns": [
     	{
           "name": "itime",
           "type": "timestamp",
           "primaryKey": "true",
           "pkOrder" : "0",

           "precision" : 0,
           "scale" : 0,
           "required" : "true",
           "source":
             "timestamp(from_unixtime(facEvent.itime/1000, yyyy-MM-dd HH:mm:ss))"
           ,
           "sparkType": "TimestampType"
         },
    '


    echo "Columns:"
    for ((i = 0; i < ${#col_array_name_type[@]}; i += 2)); do
        name="${col_array_name_type[i]}"
        type="${col_array_name_type[i + 1]}"
        echo "Name: $name, Type: $type"
    done

    echo "Array Size: ${#col_array_name_type[@]}"
}


parse_json_table

