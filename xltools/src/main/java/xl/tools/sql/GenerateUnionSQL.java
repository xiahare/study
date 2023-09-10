package xl.tools.sql;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;

public class GenerateUnionSQL {
    private final static String prefix_path = "src/main/resources/";

    public static void main(String[] args) throws IOException, ParseException {

        String filename = "slice_union_sql.tpl";
        File file = new File(prefix_path+filename);
        String sqlSliceTemplate = FileUtils.readFileToString(file, "UTF-8");


        // select `hash_id`,`adomid` as __adomid ,`__start_time`,`__end_time`, __adom_row_no*10000000+adomid as row_no,cast(__adom_row_no as int) as `slot`, `adomid`, `app_group`, `appcat`, `service`, `bandwidth`, `traffic_in`, `traffic_out`, `sessions` from (select 111 as `hash_id`,'2023-08-23 04:00:00' as __start_time, '2023-08-23 04:29:59' as __end_time, row_number() over( partition by adomid order by `sessions` desc) as __adom_row_no , `adomid`, `app_group`, `appcat`, `service`, `bandwidth`, `traffic_in`, `traffic_out`, `sessions` from(/*HCACHE-RPT-AUTO ({0}.fgt_traffic)*/select adomid as `adomid`,  app_group_name(app) as app_group, appcat, service, sum(coalesce(sentdelta, sentbyte, 0)+coalesce(rcvddelta, rcvdbyte, 0)) as bandwidth, sum(coalesce(rcvddelta, rcvdbyte, 0)) as traffic_in, sum(coalesce(sentdelta, sentbyte, 0)) as traffic_out, count(*) as sessions from (select * from __i4malgiu_fgt_traffic_view where itime>='2023-08-23 04:00:00' and itime<='2023-08-23 04:29:59' )  t1 where (1=1) and (logflag&(1|32)>0) and nullifna(app) is not null group by `adomid`,  app_group, appcat, service order by bandwidth desc) __original_sql  ) __partition_sql where adomid=2693 and __adom_row_no<=10 and app_group='NTP'

        String start = "2023-08-23 04:00:00";
        String end = "2023-08-24 03:59:59";
        int interval = 1800*1000;
        Timestamp startDate = CalendarUtil.parseDateTimeString(start);
        long endTimeMs = CalendarUtil.parseDateTimeString(end).getTime();
        long currentTimeMs = startDate.getTime();
        String sql = null;
        while(currentTimeMs<endTimeMs){
            String subStart = CalendarUtil.getDateTimeString( new Date(currentTimeMs));
            currentTimeMs = currentTimeMs+interval;
            String subEnd = CalendarUtil.getDateTimeString( new Date(currentTimeMs-1));
            System.out.println(subStart+"  ==> "+subEnd);
            String formatted = sqlSliceTemplate.formatted(subStart, subEnd, subStart, subEnd);
            if(sql==null){
                sql = formatted;
            } else {
                sql = sql + " union all " + formatted;
            }
        }
        System.out.println(sql);

    }
}
