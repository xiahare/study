package study.kudu;
//Licensed to the Apache Software Foundation (ASF) under one

import org.apache.kudu.client.HostAndPort;
import org.apache.kudu.client.KuduClient;

/*
* A simple example of using the synchronous Kudu Java client to
* - get Kude Leader
*/
public class KuduLeaderSample {
	private static final String KUDU_MASTERS = System.getProperty("kuduMasters", "10.106.50.99:7051");

	public static void main(String[] args) {
		System.out.println("-----------------------------------------------");
		System.out.println("Will try to connect to Kudu master(s) at " + KUDU_MASTERS);
		System.out.println("-----------------------------------------------");
		KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTERS).build();
		
		try {
			
			HostAndPort leader = client.findLeaderMasterServer();
			
			String leaderHost = leader.getHost();
			
			System.out.println( "========== Leader Host :" + leaderHost);
			
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