package com.xl.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    String sqlStatementLong = "SELECT `utmsubtype`,`countav`,`dstcountry`,`utmaction`,`rcvdpkt`,`itime`,`countssl`,`countweb`,`dstip`,`threattyps`,`countdns`,`policyid`,`app`,`action`,`srcport`,`srcip`,`appid`,`type`,`srcmac`,`devtype`,`countwaf`,`srcintf`,`sentpkt`,`countdlp`,`utmref`,`countssh`,`osname`,`dstintf`,`countips`,`vd`,`devid`,`dstport`,`service`,`countemail`,`countapp`,`countff`,`itime` as pk__itime,`devid` as pk__devid,`vd` as pk__vd,`id` as pk__id FROM `db_log_public`.`3_fgt_traffic` WHERE (`dstip` = '197.80.130.66' and `service` <> 'HTTPS') and (`itime` >= '2022-12-10 16:24:59') and (`itime` < '2022-12-11 16:24:59') limit 501";

    String sqlStatementCount = "SELECT count(*) FROM `db_log_public`.`3_fgt_traffic` WHERE (`dstip` = '197.80.130.66' and `service` <> 'HTTPS') and (`itime` >= '2022-12-10 12:24:59') and (`itime` < '2022-12-11 16:24:59') limit 501";

    String sqlStatementShort = "SELECT `utmsubtype`,`countav`,`dstcountry`,`utmaction`,`rcvdpkt`,`itime`,`countssl`,`countweb`,`dstip`,`threattyps`,`countdns`,`policyid`,`app`,`action`,`srcport`,`srcip`,`appid`,`type`,`srcmac`,`devtype`,`countwaf`,`srcintf`,`sentpkt`,`countdlp`,`utmref`,`countssh`,`osname`,`dstintf`,`countips`,`vd`,`devid`,`dstport`,`service`,`countemail`,`countapp`,`countff`,`itime` as pk__itime,`devid` as pk__devid,`vd` as pk__vd,`id` as pk__id FROM `db_log_public`.`3_fgt_traffic` WHERE (`dstip` = '197.80.130.66' and `service` <> 'HTTPS') and (`itime` >= '2022-12-11 03:24:59') and (`itime` < '2022-12-11 16:24:59') limit 501";

    ParamJdbc param = new ParamJdbc("172.18.78.233");
    /**
     * Rigourous Test :-)
     */
    public void testOfficialJdbc()
    {

        DataBaseQuery dq = new DataBaseQuery();
        dq.query(ParamJdbc.getClouderaParam(), sqlStatementLong);
    }

    public void testHiveJdbc()
    {
        DataBaseQuery dq = new DataBaseQuery();
        dq.query(ParamJdbc.getHiveParam(), sqlStatementLong);
    }

    public void testHiveJdbcShort()
    {
        DataBaseQuery dq = new DataBaseQuery();
        dq.query(ParamJdbc.getHiveParam(), sqlStatementShort);
    }

    public void testHiveJdbcCount()
    {
        DataBaseQuery dq = new DataBaseQuery();
        dq.query(ParamJdbc.getHiveParam(), sqlStatementCount);
    }

    public void testOfficialJdbcShortLoop()
    {
        System.out.printf("Test SQL: [%s]%n",sqlStatementShort);
        int total = 20;
        int fail = 0;
        DataBaseQuery dq = new DataBaseQuery();
        for (int i = 0; i < total; i++) {
            int size = dq.query(ParamJdbc.getClouderaParam(), sqlStatementShort);
            if(size==0){
                fail++;
            }
        }
        assertEquals(0, fail );

    }

    public void testHiveJdbcShortLoop()
    {
        System.out.printf("Test SQL: [%s]%n",sqlStatementShort);
        int total = 20;
        int fail = 0;
        DataBaseQuery dq = new DataBaseQuery();
        for (int i = 0; i < total; i++) {
            int size = dq.query(ParamJdbc.getHiveParam(), sqlStatementShort);
            if(size==0){
                fail++;
            }
        }
        assertEquals(0, fail );

    }

    public void testDebugHiveJdbcShort()
    {
        DataBaseQuery dq = new DataBaseQuery();
        ParamJdbc par = new ParamJdbc("jdbc:hive2://172.18.78.233:21050/db_log_public;auth=noSasl;", "org.apache.hive.jdbc.HiveDriver");
        dq.query(par, sqlStatementShort);
    }
}
