package xl.tools.sql;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertPrepareSQL {

    private final static String prefix_path = "src/main/resources/";

    public static void main(String[] args) throws IOException {

        String filename = "prepare_sql.log";
        File file = new File(prefix_path+filename);
        String sqlLog = FileUtils.readFileToString(file, "UTF-8");


        // SELECT `utmsubtype`,`countav`,`dstcountry`,`utmaction`,`itime`,`countssl`,`countweb`,`rcvdbyte`,`dstip`,`threattyps`,`countdns`,`app`,`action`,`user`,`srcip`,`appid`,`type`,`devtype`,`countwaf`,`countdlp`,`utmref`,`countssh`,`osname`,`unauthuser`,`countips`,`sentbyte`,`devid`,`srcname`,`service`,`epid`,`countemail`,`countapp`,`euid`,`countff`,`fctuid`,`itime` as pk__itime,`devid` as pk__devid,`vd` as pk__vd,`id` as pk__id FROM `db_log_public`.`3_fgt_traffic` WHERE (`itime` <= ? and `itime` >= ?) and (`srcip` = ?) and (`itime` >= ?) and (`itime` < ?) limit 10001 [paramArray]: ["2023-07-11 10:00:01","2023-07-11 05:13:23","9.8.7.6","2023-07-11 05:13:23","2023-07-11 09:49:56"]

        // String sqlLog = "SELECT `itime` as pk__itime,`id` as pk__id FROM `db_log_public`.`3_fgt_traffic` WHERE (`itime` <= ? and `itime` >= ?) and ((`srcip` >= ? and `srcip` < ?) and `app` = ? and `action` = ? and (`dstip` >= ? and `dstip` < ?) and `devid` = ? and `user` is not null) and (`itime` >= ?) and (`itime` < ?) limit 4979 [paramArray]: [\"2023-07-26 10:00:01\",\"2023-07-25 10:00:00\",\"172.16.0\",\"172.16.999\",\"FMG_Developers_Ports\",\"accept\",\"172.16.0\",\"172.16.999\",\"FG900D1000000001\",\"2023-07-26 03:54:56\",\"2023-07-26 09:54:56\"]";
        String excutableSQL = convertToSQL(sqlLog);
        System.out.println(excutableSQL);

    }

    private static String convertToSQL(String sqlLog){
        // String[] sql_params = sqlLog.split(" \\[paramArray\\]: ");
        String[] sql_params = sqlLog.split(" \\[Param\\]:");
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
