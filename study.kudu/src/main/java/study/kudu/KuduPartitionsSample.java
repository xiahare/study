package study.kudu;
//Licensed to the Apache Software Foundation (ASF) under one

import java.util.List;

import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduTable;
import org.apache.kudu.client.ListTablesResponse;
import org.apache.kudu.client.ListTablesResponse.TableInfo;

/*
* A simple example of using the synchronous Kudu Java client to
* - Create a table.
* - Insert rows.
* - Alter a table.
* - Scan rows.
* - Delete a table.
*/
public class KuduPartitionsSample {
	private static final Double DEFAULT_DOUBLE = 12.345;
	private static final String KUDU_MASTERS = System.getProperty("kuduMasters", "10.106.50.99:7051");

	public static void main(String[] args) {
		System.out.println("-----------------------------------------------");
		System.out.println("Will try to connect to Kudu master(s) at " + KUDU_MASTERS);
		System.out.println("-----------------------------------------------");
		KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTERS).build();
		
		try {
			ListTablesResponse tableList = client.getTablesList();
			List<TableInfo> tableInfoList = tableList.getTableInfosList();
			for(TableInfo tableInfo:tableInfoList) {
				KuduTable kuduTable = client.openTable(tableInfo.getTableName());
				List<Integer> rangePartitionColumnIds = kuduTable.getPartitionSchema().getRangeSchema().getColumnIds();
				System.out.println(tableInfo.getTableName() + ":" + rangePartitionColumnIds.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {				
			try {
			client.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}}
	}
	
}