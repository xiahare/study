package xl.examples.redis;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class ObjectSerializationUtil {

    private static final Logger logger = LoggerFactory.getLogger(ObjectSerializationUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static String convertObjectToStr(Object object) {
        try {
            String objStr = objectMapper.writeValueAsString(object);
            return objStr;
        } catch (JsonProcessingException e) {
            logger.error("fail to serialize the string " , e);
            return null;
        }
    }

    public static Object convertStrToObject(String objectStr, Class<?> targetClass) {
        try {
            Object object = objectMapper.readValue(objectStr, targetClass);
            return object;
        } catch (Exception e) {
            logger.error("fail to deserialize the string " + objectStr, e);
            return null;
        }

    }
}
