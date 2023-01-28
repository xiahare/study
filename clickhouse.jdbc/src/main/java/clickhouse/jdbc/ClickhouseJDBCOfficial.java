package clickhouse.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ClickhouseJDBCOfficial {

    static void dropAndCreateTable(String url, String user, String password, String table) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement()) {
            stmt.execute(
                    String.format(
                            "drop table if exists %1$s; create table %1$s(a String, b Nullable(String)) engine=Memory",
                            table));
        }
    }

    static void batchInsert(String url, String user, String password, String table) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // not that fast as it's based on string substitution and large sql statement
            String sql = String.format("insert into %1$s values(?, ?)", table);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "a");
                ps.setString(2, "b");
                ps.addBatch();
                ps.setString(1, "c");
                ps.setString(2, null);
                ps.addBatch();
                ps.executeBatch();
            }

            // faster when inserting massive data
            sql = String.format("insert into %1$s", table);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "a");
                ps.setString(2, "b");
                ps.addBatch();
                ps.setString(1, "c");
                ps.setString(2, null);
                ps.addBatch();
                ps.executeBatch();
            }

            // fastest approach as it does not need to issue additional query for metadata
            sql = String.format("insert into %1$s select a, b from input('a String, b Nullable(String)')", table);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "a");
                ps.setString(2, "b");
                ps.addBatch();
                ps.setString(1, "c");
                ps.setString(2, null);
                ps.addBatch();
                ps.executeBatch();
            }
        }
    }

    static int query(String url, String user, String password, String table) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from " + table)) {
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println( "A=" + rs.getString(1) + " | B=" + rs.getString(2) );
            }
            return count;
        }
    }

    public static void main(String[] args) {
		//String host = "10.106.50.99";
    	//String port= "8123";
    	
//		String host = "10.106.49.4";
//		String port= "18123";
		
//      String user = System.getProperty("chUser", "admin");
//      String password = System.getProperty("chPassword", "admin");
		
		String host = "10.106.49.4";
		String port = "8123";
		
//		String host = "172.24.172.44";
//		String port = "8123";
		
        String user = System.getProperty("chUser", "admin");
        String password = System.getProperty("chPassword", "admin");
		
    	
        String url = String.format("jdbc:ch://%s:%d/default", System.getProperty("chHost", host),
                Integer.parseInt(System.getProperty("chPort", port)));
        String table = "jdbc_example_basic";

        try {
            dropAndCreateTable(url, user, password, table);

            batchInsert(url, user, password, table);

            int count = query(url, user, password, table);
            System.out.println( count );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
