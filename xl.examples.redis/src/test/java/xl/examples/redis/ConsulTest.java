package xl.examples.redis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class ConsulTest extends AbstractBaseTest
{
	Logger logger = LogManager.getLogger(ConsulTest.class);
	
	@Autowired
    private ConsulService consulService;
	
    @Test
    public void getTest()
    {
        String strAdomsMapping = consulService.get("adoms/mapping");
        Map<String,String> mapAdomsMapping = JSONObject.parseObject(strAdomsMapping,new TypeReference<Map<String, String>>(){});
        logger.info(mapAdomsMapping);
    }

}
