package com.xl.example;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogviewBaseIDQuery {

    Logger logger = LogManager.getLogger();

    public List<TimeID> queryID(ParamJdbc param, String sqlStatement) {
        Connection con = null;
        List<TimeID> results = new ArrayList<TimeID>();
        int size = 0;
        ResultSet rs = null;
        try {
            Class.forName(param.getJdbcDriverName());

            con = DriverManager.getConnection(param.getConnectionUrl());

            Statement stmt = con.createStatement();

            StopWatch sw = new StopWatch();

            sw.start();
            rs = stmt.executeQuery(sqlStatement);
            sw.stop();
            logger.debug(String.format("executeQuery [%d]s \n", sw.getTime()/1000));

            sw.reset();
            sw.start();
            // print the results to the console
            while (rs.next()) {
                TimeID timeID = new TimeID();
                timeID.setTime(rs.getTimestamp(2));
                timeID.setId(rs.getLong(1));
                results.add(timeID);
                size ++;
            }
            sw.stop();
            logger.debug(String.format("Fetch result took [%d]s\n", sw.getTime()/1000));

            //System.out.println(String.format("Size() took [%d]s", sw.getTime()/1000));
            logger.debug(String.format("Results Size: [%d] \n",size));

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
        return results;
    }
}
