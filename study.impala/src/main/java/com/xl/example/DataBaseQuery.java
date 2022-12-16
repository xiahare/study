package com.xl.example;

import org.apache.commons.lang3.time.StopWatch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class DataBaseQuery {
    public int query(ParamJdbc param, String sqlStatement) {
        Connection con = null;
        int size = 0;
        ResultSet rs = null;
        try {
            Class.forName(param.getJdbcDriverName());

            //DriverManager.setLoginTimeout(3600000);
            con = DriverManager.getConnection(param.getConnectionUrl());



            Statement stmt = con.createStatement();

            //stmt.setQueryTimeout(1800);



            StopWatch sw = new StopWatch();

            System.out.println("\n== Begin Query   ======================");
            System.out.println(param);

//            String setFetchTimeout="set FETCH_ROWS_TIMEOUT_MS=100000;";
//
//            stmt.executeQuery(setFetchTimeout);

            sw.start();
            rs = stmt.executeQuery(sqlStatement);
            sw.stop();
            System.out.printf("Send query took [%d]s", sw.getTime()/1000);

            sw.reset();
            sw.start();
            System.out.printf("Next A [%d]s \n", sw.getTime()/1000);
            // print the results to the console
            while (rs.next()) {
                System.out.printf("Next B [%d]s \n", sw.getTime()/1000);
                System.out.println(String.format("****[%d]**** column value:[%s]",rs.getRow(),rs.getString(1)));

                size ++;

            }
            System.out.printf("Next C [%d]s \n", sw.getTime()/1000);
            sw.stop();
            System.out.printf("Fetch took [%d]s\n", sw.getTime()/1000);

            //System.out.println(String.format("Size() took [%d]s", sw.getTime()/1000));
            System.out.printf("Results Size: [%d] \n",size);
            System.out.println("== End Query Results =======================\n\n");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs!=null){
                    rs.close();
                }
                con.close();
            } catch (Exception e) {
                // swallow


            }
        }
        return size;
    }
}
