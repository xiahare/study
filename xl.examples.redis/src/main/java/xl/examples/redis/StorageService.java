package xl.examples.redis;

import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class StorageService {
	
	@Autowired
    private ConsulService consulService;
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Value("${data.catalog.default.storage.id:root}")
	private String DEFAULT_STORAGE_ID ;

	@Value("${consul.key.adoms.mapping:adoms/mapping}")
	private String CONSUL_KEY_ADOMS_MAPPING ;

	public String getDefaultStorageId() {
		return DEFAULT_STORAGE_ID;
	}
	
	public String getStorageIdByAdom(String adomId) {
		if(adomId==null){
			return getDefaultStorageId();
		}
		Object entryValue = redisTemplate.opsForHash().get("adomInfoMap",adomId);
		if(entryValue!=null){
			JSONObject adomJsonObject = JSONObject.parseObject(entryValue.toString());
			return (String) adomJsonObject.get("storageId");
		}
		return null;
	}
	
	public Map<String,String> getAdomStorageMapping() {
		String strAdomsMapping = consulService.get(CONSUL_KEY_ADOMS_MAPPING);
		Map<String,String> mapAdomsMapping = JSONObject.parseObject(strAdomsMapping,new TypeReference<Map<String, String>>(){}); // "3":"__root"
		// convert storage: "__root" -> "root"

		for ( String key: mapAdomsMapping.keySet()) {
			String value = mapAdomsMapping.get(key);

			if(value!=null){
				if( value.startsWith("__") ){
					mapAdomsMapping.put(key,value.substring(2));
				}

			}
		}
		return mapAdomsMapping;
	}
	
	public List<String> getAllStorageIds(){
		Map<String, String> mapAdomsMapping = getAdomStorageMapping();
		Set<String> set = new HashSet<String>(mapAdomsMapping.values());

		return new ArrayList<String>(set);
	}

	public List<String> getAllAdomids(){
		String strAdomsMapping = consulService.get(CONSUL_KEY_ADOMS_MAPPING);
		Map<String,String> mapAdomsMapping = JSONObject.parseObject(strAdomsMapping,new TypeReference<Map<String, String>>(){}); // "3":"__root"

		return new ArrayList<String>(mapAdomsMapping.keySet());
	}

	public List<Map<String, Object>> getAllAdoms(){
		Map<Object, Object> map = redisTemplate.opsForHash().entries("adomInfoMap");
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		for(Map.Entry<Object, Object> entry: map.entrySet()) {
			String entryKey = entry.getKey().toString();
			String entryValue = entry.getValue().toString();
			JSONObject adomJsonObject = JSONObject.parseObject(entryValue);
			if( "ready".equals(adomJsonObject.getString("status")) ) {
				//"status":"ready"
				//{"createdTimestamp":1642126142306,"updatedTimestamp":1642126142306,"status":"ready","code":"","message":"","tenantId":"db_log_public","adomId":"141","storageId":"root","adomName":"FortiDDoS","adomType":"leader","leaderAdomId":"141","startTime":null,"endTime":null,"deviceType":"fdd","description":"","properties":{"alertUsage":"90.0","maxAllowed":"0.0","percentageDB":"50.0","retentionDB":"60.0","retentionArchive":"365.0"},"devices":null,"percentageDB":50.0,"maxAllowed":0.0,"alertUsage":90.0,"retentionArchive":365.0,"retentionDB":60.0}
				Map<String, Object> adomIdName = new HashMap<String, Object>();
				adomIdName.put("adom_id", adomJsonObject.getString("adomId"));
				adomIdName.put("adom_name", adomJsonObject.getString("adomName"));
				res.add(adomIdName);
			}
		}
		return res;
	}
}
