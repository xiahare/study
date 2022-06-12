package xl.examples.redis;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisTest extends AbstractBaseTest
{
	Logger logger = LogManager.getLogger(RedisTest.class);
	
	@Autowired
    private RedisService redisService;
	
    @Test
    public void getAdomStorageAllMapTest()
    {
    	Map<String, String> adomInfoMap = redisService.getAdomStorageAllMap("adomInfoMap");
    	logger.info(adomInfoMap);
    	for(String key: adomInfoMap.keySet() ) {
    		logger.info(key + ":" + adomInfoMap.get(key) );
    	}
    }
    
    @Test
    public void getAdomIdsTest()
    {
    	Map<String, String> res = redisService.getByPrefix("max_log_time");
    	for(String key: res.keySet() ) {
    		logger.info(key +":"+ res.get(key) );
    		
    	}
    }
    
    @Test
    public void getAllAdomsTest()
    {
    	List<Map<String, Object>> res = redisService.getAllAdoms();
    	for(Map<String, Object> row: res ) {
    		logger.info( row );
    		
    	}
    }
    
}
