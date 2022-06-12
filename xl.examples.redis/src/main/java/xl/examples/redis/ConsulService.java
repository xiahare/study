package xl.examples.redis;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConsulService {
	
	@Autowired
    private ConsulClient consulClient;
	

	public String get(String key) {
		
		Response<GetValue> response = consulClient.getKVValue(key);
		GetValue getValue = response.getValue();
		if( getValue==null ) {
			return null;
		}
		return getValue.getDecodedValue();
	}
	
	public void put(String key, String value) {
		
		consulClient.setKVValue(key, value);
	}

	public Map<String,String> getByPrefix(String keyPrefix) {
		Response<List<GetValue>> responseList = consulClient.getKVValues(keyPrefix);
		Map<String,String> ret = new HashMap<String,String>();
		for(  GetValue getValue: responseList.getValue()) {
			if( getValue!=null ) {
				ret.put( getValue.getKey(), getValue.getDecodedValue() );
			}
		}
		
		return ret;
	}

	public void remove(Collection<String> keys) {
		for( String key: keys) {
			consulClient.deleteKVValue(key);
		}
		
	}

	public String getHashItem(String key, String hashKey) {
		if(key==null||hashKey==null) {
			return null;
		}
		return get(buildPathKey(key, hashKey)).toString();
	}

	public void putHashItem(String key, String hashKey, String value) {
		if(key==null||hashKey==null) {
			return ;
		}
		put(buildPathKey(key, hashKey), value);
	}
	
	private String buildPathKey(String key, String hashKey) {
		return key + "/" + hashKey;
	}
}
