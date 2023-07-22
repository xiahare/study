package com.fortidata.fazconnector.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertPrepareSQL {
    public static void main(String[] args) {

        // SELECT `utmsubtype`,`countav`,`dstcountry`,`utmaction`,`itime`,`countssl`,`countweb`,`rcvdbyte`,`dstip`,`threattyps`,`countdns`,`app`,`action`,`user`,`srcip`,`appid`,`type`,`devtype`,`countwaf`,`countdlp`,`utmref`,`countssh`,`osname`,`unauthuser`,`countips`,`sentbyte`,`devid`,`srcname`,`service`,`epid`,`countemail`,`countapp`,`euid`,`countff`,`fctuid`,`itime` as pk__itime,`devid` as pk__devid,`vd` as pk__vd,`id` as pk__id FROM `db_log_public`.`3_fgt_traffic` WHERE (`itime` <= ? and `itime` >= ?) and (`srcip` = ?) and (`itime` >= ?) and (`itime` < ?) limit 10001 [paramArray]: ["2023-07-11 10:00:01","2023-07-11 05:13:23","9.8.7.6","2023-07-11 05:13:23","2023-07-11 09:49:56"]

        String sqlLog = "SELECT `utmsubtype`,`countav`,`dstcountry`,`utmaction`,`itime`,`countssl`,`countweb`,`rcvdbyte`,`dstip`,`threattyps`,`countdns`,`app`,`action`,`user`,`srcip`,`appid`,`type`,`devtype`,`countwaf`,`countdlp`,`utmref`,`countssh`,`osname`,`unauthuser`,`countips`,`sentbyte`,`devid`,`srcname`,`service`,`epid`,`countemail`,`countapp`,`euid`,`countff`,`fctuid`,`itime` as pk__itime,`devid` as pk__devid,`vd` as pk__vd,`id` as pk__id FROM `db_log_public`.`3_fgt_traffic` WHERE (`itime` <= ? and `itime` >= ?) and (`srcip` = ?) and (`itime` >= ?) and (`itime` < ?) limit 10001 [paramArray]: [\"2023-07-11 10:00:01\",\"2023-07-11 05:13:23\",\"9.8.7.6\",\"2023-07-11 05:13:23\",\"2023-07-11 09:49:56\"]";
        String excutableSQL = convertToSQL(sqlLog);
        System.out.println(excutableSQL);
    }

    private static String convertToSQL(String sqlLog){
        String[] sql_params = sqlLog.split(" \\[paramArray\\]: ");
        String prepareStatement = sql_params[0];
        String params = sql_params[1];
        if(0==sql_params[1].indexOf("[")&&(sql_params[1].length()-1)==sql_params[1].lastIndexOf("]")){
            params = sql_params[1].substring(1,sql_params[1].length()-1);
        }
        List<String> paramsList = Arrays.stream(params.split(",")).map(o -> {
            String ret;
            if (0 == o.indexOf("\"") && (o.length() - 1) == o.lastIndexOf("\"")) {
                ret = "'" + o.substring(1, o.length() - 1) + "'";
            } else {
                ret = o;
            }
            return ret;
        }).collect(Collectors.toList());

        return String.format(prepareStatement.replace("?","%s"),paramsList.toArray(new String[0]));
    }

}
