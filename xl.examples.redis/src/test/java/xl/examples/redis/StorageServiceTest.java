package xl.examples.redis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class StorageServiceTest extends AbstractBaseTest
{
	Logger logger = LogManager.getLogger(StorageServiceTest.class);
	
	@Autowired
    private StorageService storageService;
	
    @Test
    public void getStorageIdByAdomTest()
    {
        String storageId = storageService.getStorageIdByAdom("3");
        logger.info("-------- adomId[3]:"+storageId);
        Assert.assertEquals(storageId, "root");
    }

    @Test
    public void getAdomStorageMappingTest()
    {
        Map<String, String> map = storageService.getAdomStorageMapping();
        logger.info("-------- adomMapping:" + map);
        Assert.assertEquals( map.get("3"), "root");
    }

    @Test
    public void getAllStorageIdsTest()
    {
        Map<String, String> map = storageService.getAdomStorageMapping();
        logger.info( "-------- storages :"  + storageService.getAllStorageIds());
        Assert.assertNotEquals( storageService.getAllStorageIds().size(), map.size());
    }

    @Test
    public void getAllAdomidsTest()
    {
        List<String> adomids = storageService.getAllAdomids();
        logger.info( "-------- adomids :"  + adomids);
        Assert.assertEquals( adomids.size()>0 , true);
    }

    @Test
    public void getAllAdomsTest()
    {
        List<Map<String, Object>> list = storageService.getAllAdoms();
        logger.info( "-------- Adoms :"  + list);
        Assert.assertEquals( list.size()>0, true);
    }
}
