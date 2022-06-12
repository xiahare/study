package xl.examples.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class RedisService {
	
	@Autowired
    private StringRedisTemplate redisTemplate;
	

    public List<String> getAdomIds() {
        Map<Object, Object> map = redisTemplate.opsForHash().entries("adomInfoMap");
        List<String> res = new ArrayList<String>();
        for(Map.Entry<Object, Object> entry: map.entrySet()) {
            String entryKey = entry.getKey().toString();
            String entryValue = entry.getValue().toString();
            JSONObject adomJsonObject = JSONObject.parseObject(entryValue);
            if( "ready".equals(adomJsonObject.getString("status")) ) {
            	//"status":"ready"
            	res.add(entryKey);
            }
        }
        return res;
    }
    
    public Map<String, String> getAdomStorageAllMap(String key) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        Map<String, String> res = new HashMap<String, String>();
        for(Map.Entry<Object, Object> entry: map.entrySet()) {
            String entryKey = entry.getKey().toString();
            String entryValue = entry.getValue().toString();
            res.put(entryKey, entryValue);
        }
        return res;
    }
    
	public Map<String,String> getByPrefix(String keyPrefix) {
		Map<String,String> ret = new HashMap<String,String>();
		Set<String> keys = redisTemplate.keys(keyPrefix + "*");
		List<String> list = redisTemplate.opsForValue().multiGet(keys);
		Iterator<String> it = keys.iterator();
		for(String obj: list) {
			ret.put(it.next(),obj);
		}
		return ret;
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
            	Map<String, Object> adomIdName = new HashMap<String, Object>();
            	adomIdName.put("adom_id", adomJsonObject.getString("adomId"));
            	adomIdName.put("adom_name", adomJsonObject.getString("adomName"));
            	res.add(adomIdName);
            }
        }
        return res;
	}
    
}
