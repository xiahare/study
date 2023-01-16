package com.xl.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
public class LogviewBaseIDTest
    extends TestCase
{
    Logger logger = LogManager.getLogger();
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public LogviewBaseIDTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( LogviewBaseIDTest.class );
    }

    String selectColumnsTimeID = "SELECT `id`,`itime`";

    String fromTable = "FROM `db_log_public`.`3_fgt_traffic`";

    ParamJdbc param = new ParamJdbc("172.18.78.233");
    /**
     * Rigourous Test :-)
     */

    public void testQueryGroupingIDLike50Limit50()
    {
        String selectColumns50 = "SELECT `id`,`itime`,`adomid`,`devid`,`vd`,`dtime`,`euid`,`srcip`,`epid`,`dsteuid`,`dstepid`,`logflag`,`logver`,`sfsid`,`logid`,`type`,`subtype`,`level`,`action`,`utmaction`,`policyid`,`sessionid`,`dstip`,`tranip`,`transip`,`srcport`,`dstport`,`tranport`,`transport`,`trandisp`,`duration`,`proto`,`vrf`,`slot`,`sentbyte`,`rcvdbyte`,`sentdelta`,`rcvddelta`,`sentpkt`,`rcvdpkt`,`user`,`unauthuser`,`dstunauthuser`,`srcname`,`dstname`,`group`,`service`,`app`,`appcat`,`fctuid`";
        String whereOrigin = "WHERE (`itime` >= '2023-01-04 10:58:11' and `itime` <= '2023-01-04 21:58:11') and `srcip` like '194.163.132.36%%' limit 50";

        queryGroupingID(selectColumns50,whereOrigin);
    }

    public void testQueryGroupingIDLike50()
    {
        String selectColumns50 = "SELECT `id`,`itime`,`adomid`,`devid`,`vd`,`dtime`,`euid`,`srcip`,`epid`,`dsteuid`,`dstepid`,`logflag`,`logver`,`sfsid`,`logid`,`type`,`subtype`,`level`,`action`,`utmaction`,`policyid`,`sessionid`,`dstip`,`tranip`,`transip`,`srcport`,`dstport`,`tranport`,`transport`,`trandisp`,`duration`,`proto`,`vrf`,`slot`,`sentbyte`,`rcvdbyte`,`sentdelta`,`rcvddelta`,`sentpkt`,`rcvdpkt`,`user`,`unauthuser`,`dstunauthuser`,`srcname`,`dstname`,`group`,`service`,`app`,`appcat`,`fctuid`";
        String whereOrigin = "WHERE (`itime` >= '2023-01-04 10:58:11' and `itime` <= '2023-01-04 21:58:11') and `srcip` like '10.245.160.10%' limit 500";

        queryGroupingID(selectColumns50,whereOrigin);
    }

    public void testQueryGroupingIDLike50Limit1000()
    {
        String selectColumns50 = "SELECT `id`,`itime`,`adomid`,`devid`,`vd`,`dtime`,`euid`,`srcip`,`epid`,`dsteuid`,`dstepid`,`logflag`,`logver`,`sfsid`,`logid`,`type`,`subtype`,`level`,`action`,`utmaction`,`policyid`,`sessionid`,`dstip`,`tranip`,`transip`,`srcport`,`dstport`,`tranport`,`transport`,`trandisp`,`duration`,`proto`,`vrf`,`slot`,`sentbyte`,`rcvdbyte`,`sentdelta`,`rcvddelta`,`sentpkt`,`rcvdpkt`,`user`,`unauthuser`,`dstunauthuser`,`srcname`,`dstname`,`group`,`service`,`app`,`appcat`,`fctuid`";
        String whereOrigin = "WHERE (`itime` >= '2023-01-04 10:58:11' and `itime` <= '2023-01-04 21:58:11') and `srcip` like '10.245.160.10%' limit 1000";

        queryGroupingID(selectColumns50,whereOrigin);
    }

    public void testQueryGroupingIDLike50Limit10000()
    {
        String selectColumns50 = "SELECT `id`,`itime`,`adomid`,`devid`,`vd`,`dtime`,`euid`,`srcip`,`epid`,`dsteuid`,`dstepid`,`logflag`,`logver`,`sfsid`,`logid`,`type`,`subtype`,`level`,`action`,`utmaction`,`policyid`,`sessionid`,`dstip`,`tranip`,`transip`,`srcport`,`dstport`,`tranport`,`transport`,`trandisp`,`duration`,`proto`,`vrf`,`slot`,`sentbyte`,`rcvdbyte`,`sentdelta`,`rcvddelta`,`sentpkt`,`rcvdpkt`,`user`,`unauthuser`,`dstunauthuser`,`srcname`,`dstname`,`group`,`service`,`app`,`appcat`,`fctuid`";
        String whereOrigin = "WHERE (`itime` >= '2023-01-04 10:58:11' and `itime` <= '2023-01-04 21:58:11') and `srcip` like '10.245.160.10%' limit 9999";

        queryGroupingID(selectColumns50,whereOrigin);
    }

    public void testQueryGroupingIDEqual50()
    {
        String selectColumns50 = "SELECT `id`,`itime`,`adomid`,`devid`,`vd`,`dtime`,`euid`,`srcip`,`epid`,`dsteuid`,`dstepid`,`logflag`,`logver`,`sfsid`,`logid`,`type`,`subtype`,`level`,`action`,`utmaction`,`policyid`,`sessionid`,`dstip`,`tranip`,`transip`,`srcport`,`dstport`,`tranport`,`transport`,`trandisp`,`duration`,`proto`,`vrf`,`slot`,`sentbyte`,`rcvdbyte`,`sentdelta`,`rcvddelta`,`sentpkt`,`rcvdpkt`,`user`,`unauthuser`,`dstunauthuser`,`srcname`,`dstname`,`group`,`service`,`app`,`appcat`,`fctuid`";
        String whereOrigin = "WHERE (`itime` >= '2023-01-04 10:58:11' and `itime` <= '2023-01-04 21:58:11') and `srcip` = '10.245.160.10' limit 500";

        queryGroupingID(selectColumns50,whereOrigin);
    }

    public void testQueryGroupingIDEqual50Range1h()
    {
        String selectColumns50 = "SELECT `id`,`itime`,`adomid`,`devid`,`vd`,`dtime`,`euid`,`srcip`,`epid`,`dsteuid`,`dstepid`,`logflag`,`logver`,`sfsid`,`logid`,`type`,`subtype`,`level`,`action`,`utmaction`,`policyid`,`sessionid`,`dstip`,`tranip`,`transip`,`srcport`,`dstport`,`tranport`,`transport`,`trandisp`,`duration`,`proto`,`vrf`,`slot`,`sentbyte`,`rcvdbyte`,`sentdelta`,`rcvddelta`,`sentpkt`,`rcvdpkt`,`user`,`unauthuser`,`dstunauthuser`,`srcname`,`dstname`,`group`,`service`,`app`,`appcat`,`fctuid`";
        String whereOrigin = "WHERE (`itime` >= '2023-01-04 10:58:11' and `itime` <= '2023-01-04 11:58:11') and `srcip` = '10.245.160.10' limit 500";

        queryGroupingID(selectColumns50,whereOrigin);
    }

    private void queryGroupingID(String selectColumnsN,String whereOrigin)
    {
        String sqlStatementTimeID = String.format("%s %s %s;", selectColumnsTimeID, fromTable, whereOrigin);
        String sqlStatementN = String.format("%s %s %s;", selectColumnsN, fromTable, whereOrigin);

        LogviewBaseIDQuery dq = new LogviewBaseIDQuery();

        StopWatch sw = new StopWatch();
        sw.start();
        List<TimeID> timeIDs = dq.queryID(param.getHiveParam(), sqlStatementTimeID);
        sw.stop();
        long durationQueryID = sw.getTime();
        logger.info(sqlStatementTimeID);

        timeIDs.stream().forEach( o -> {logger.debug(String.format(" %d , %s ",o.getId(),CalendarUtil.getHandledDateTimeString(o.getTime())));} );

        Collections.sort(timeIDs);

        PriorityQueue<TimeID> pq =  new PriorityQueue<>(new TimeIDGapComparator());

        for (int i=0 ; i<timeIDs.size();i++){

            TimeID timeID = timeIDs.get(i);
            timeID.setIndex(i);
            if(i!=0){
                timeID.setGap(  timeIDs.get(i-1).getTime().getTime() - timeID.getTime().getTime() );
            }
            pq.add(timeID);
        }

        timeIDs.stream().forEach( o -> logger.debug(o.getId() + " | " + CalendarUtil.getHandledDateTimeString(o.getTime()) + " | " + o.getGap()) );
        List<Integer> sepList = new ArrayList<>();
        long quotaGap = 10*60*1000; // 10 min
        long quotaGapCountMax = 4; // max_size=5
        long gapCount = 0;
        while(!pq.isEmpty()){
            TimeID o = pq.poll();
            if ( o.getGap()<quotaGap || gapCount>=quotaGapCountMax){
                break;
            }
            sepList.add(o.getIndex());
            gapCount++;
            logger.debug(String.format(" Index : %d | Gap: %d (m)",o.getIndex(), o.getGap()/60/1000 ));
        }

        Collections.sort(sepList);
        logger.debug(sepList);

        List<List<TimeID>> group = new ArrayList<>();
        if(!sepList.isEmpty()){

            int currentIdxStart = 0;
            int currentIdxEnd = sepList.get(0);
            // first one
            List<TimeID> sublist = timeIDs.subList(currentIdxStart, currentIdxEnd);
            group.add(sublist);

            for (int i = 0; i < sepList.size(); i++) {
                currentIdxStart = sepList.get(i);
                if(i==sepList.size()-1){
                    currentIdxEnd = timeIDs.size();
                }else{
                    currentIdxEnd = sepList.get(i+1);
                }

                sublist = timeIDs.subList(currentIdxStart, currentIdxEnd);
                group.add(sublist);
            }
        } else {
            group.add(timeIDs);
        }
        
        group.forEach( ls -> {
            TimeID max = ls.get(0);
            TimeID min = ls.get(ls.size()-1);
            logger.debug( String.format("%s - %s", CalendarUtil.getHandledDateTimeString(min.getTime()), CalendarUtil.getHandledDateTimeString(max.getTime()) ));
            String sbIN = ls.stream().map(o -> String.valueOf(o.getId())).collect(Collectors.joining(",", "(", ")"));
            logger.debug( String.format("%s",sbIN));
        });
        String sqlQueryDetails = generateSQL(group,selectColumnsN);

        sw.reset();
        sw.start();
        dq.queryID(param.getHiveParam(), sqlQueryDetails);
        sw.stop();
        long durationQueryDetails = sw.getTime();
        logger.info(sqlQueryDetails);

        // test group all
        List<List<TimeID>> groupAll = new ArrayList<>();
        groupAll.add(timeIDs);
        String sqlQueryDetailsAll = generateSQL(groupAll,selectColumnsN);

        logger.info(sqlQueryDetailsAll);
        sw.reset();
        sw.start();
        dq.queryID(param.getHiveParam(), sqlQueryDetailsAll);
        sw.stop();
        long durationQueryDetailsAll = sw.getTime();

        sw.reset();
        sw.start();
        dq.queryID(param.getHiveParam(), sqlStatementN);
        sw.stop();
        long durationQueryOrigin = sw.getTime();
        logger.info(sqlStatementN);

        logger.info(String.format("Query Duration : Origin [%,d]ms, ID+Details [%,d]ms, Query ID [%,d]ms , Query Details [%,d]ms ", durationQueryOrigin, durationQueryID+durationQueryDetails,durationQueryID,durationQueryDetails));

        logger.info(String.format("Query Duration : Query Grouping Details [%,d]ms, Query Non-Grouping Details [%,d]ms ", durationQueryDetails, durationQueryDetailsAll));


    }

    private String generateSQL(List<List<TimeID>> group, String select){

        return group.stream().map( ls -> {
            StringBuffer where = new StringBuffer();
            int maxIdx = 0;
            int minIdx = ls.size()-1;
            TimeID max = ls.get(maxIdx);
            TimeID min = ls.get(minIdx);
            if( maxIdx==minIdx){
                where.append( String.format("%s %s where itime='%s'", select, fromTable,CalendarUtil.getHandledDateTimeString(max.getTime())) );
            }else{
                where.append(String.format("%s %s where itime>='%s' and itime<='%s'", select, fromTable,CalendarUtil.getHandledDateTimeString(min.getTime()), CalendarUtil.getHandledDateTimeString(max.getTime())) );
            }
            String sbIN = ls.stream().map(o -> String.valueOf(o.getId())).collect(Collectors.joining(",", "(", ")"));
            where.append(String.format(" and id in %s", sbIN));
            return where;

        }).collect(Collectors.joining(" union all ")) + ";";

    }

}
