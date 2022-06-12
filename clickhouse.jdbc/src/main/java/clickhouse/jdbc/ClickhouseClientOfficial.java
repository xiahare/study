package clickhouse.jdbc;

import com.clickhouse.client.ClickHouseClient;
import com.clickhouse.client.ClickHouseFormat;
import com.clickhouse.client.ClickHouseNode;
import com.clickhouse.client.ClickHouseProtocol;
import com.clickhouse.client.ClickHouseRecord;
import com.clickhouse.client.ClickHouseResponse;
import com.clickhouse.client.ClickHouseResponseSummary;

public class ClickhouseClientOfficial {

	public static void main(String[] args) {
		String host = "172.30.144.13";
		 Integer port = 8123;
		//String host = "10.106.50.99";
		// Integer port = 8123;
//		String host = "10.106.49.4";
//		Integer port = 18123;
		// only HTTP and gRPC are supported at this point
		ClickHouseProtocol preferredProtocol = ClickHouseProtocol.HTTP;
		// you'll have to parse response manually if use different format
		ClickHouseFormat preferredFormat = ClickHouseFormat.RowBinaryWithNamesAndTypes;

		// connect to localhost, use default port of the preferred protocol
		ClickHouseNode server = ClickHouseNode.builder().host(host).port(preferredProtocol,port).build();

		try (ClickHouseClient client = ClickHouseClient.newInstance(preferredProtocol);
		    ClickHouseResponse response = client.connect(server)
		        .format(preferredFormat)
		        .query("select * from numbers(:limit)")
		        .params(1000).executeAndWait()) {
		    // or resp.stream() if you prefer stream API
			int i = 0;
		    for (ClickHouseRecord r : response.records()) {
		        //int num = r.getValue(0).asInteger();
		        String str = r.getValue(0).asString();
		        System.out.println((i++)  + " | str - " + str);
		    }

		    ClickHouseResponseSummary summary = response.getSummary();
		    long totalRows = summary.getTotalRowsToRead();
		    System.out.println("totalRows:"+totalRows);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
