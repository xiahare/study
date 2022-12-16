package com.xl.example;

import java.util.HashMap;

public class ParamJdbc {
    public static final String DEFAULT_JDBC_HOST_IP = "172.0.0.1";
    public static final String CLOUDERA_JDBC_PARAM = "cloudera";
    public static final String HIVE_JDBC_PARAM = "hive";

    public ParamJdbc(){}

    public ParamJdbc(String connectionUrl, String jdbcDriverName){
        this.connectionUrl = connectionUrl;
        this.jdbcDriverName = jdbcDriverName;
    }

    public ParamJdbc(String ip){
        update(ip);
    }

    public static void update(String ip){
        factory.put(CLOUDERA_JDBC_PARAM, newClouderaParamJdbc(ip));
        factory.put(HIVE_JDBC_PARAM, newHiveParamJdbc(ip));

    }

    static public HashMap<String,ParamJdbc> factory = new HashMap<String, ParamJdbc>();

    static {
        update(DEFAULT_JDBC_HOST_IP);
     }

    public static ParamJdbc getClouderaParam(){
        return factory.get(CLOUDERA_JDBC_PARAM);

    }

    public static ParamJdbc getHiveParam(){
        return factory.get(HIVE_JDBC_PARAM);
    }

    public static ParamJdbc newClouderaParamJdbc(String ip){
        return new ParamJdbc("jdbc:impala://"+ip+":21050/db_log_puglic","com.cloudera.impala.jdbc41.Driver");
    }

    public static ParamJdbc newHiveParamJdbc(String ip){
        return new ParamJdbc("jdbc:hive2://"+ip+":21050/db_log_public;auth=noSasl;","org.apache.hive.jdbc.HiveDriver");
    }

    private String connectionUrl;
    private String jdbcDriverName;

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getJdbcDriverName() {
        return jdbcDriverName;
    }

    public void setJdbcDriverName(String jdbcDriverName) {
        this.jdbcDriverName = jdbcDriverName;
    }

    @Override
    public String toString() {
        return String.format("ConnectionUrl:[%s]",getConnectionUrl());
    }
}
