package com.xl.example.redis;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class RedisTest extends RedisApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(RedisTest.class);
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Test
    public void testBigkeyForString(){

        int M1 = 1000000;
        for (int i = 0; i < 100; i++) {
            bigkeyForString( i*M1 );
        }


    }

    @Test
    public void testBigkeyForStringMulitThreadsNoreplica() throws InterruptedException {
        int threadCnt = 50;
        CountDownLatch latch = new CountDownLatch(threadCnt);

        final int M1 = 1000000;
        for (int i = 0; i < threadCnt; i++) {
            taskExecutor.execute(()->{
                try{
                    bigkeyForString(10*M1);
                } catch (Throwable th){
                    if(th.getMessage().contains("NOREPLICA")){
                        System.exit(9);
                    }
                    logger.error("Thread errors!"+Thread.currentThread().getName(),th);

                } finally {
                    latch.countDown();
                }

            });
        }

        latch.await();
    }

    @Test
    public void testBigkeyForStringMulitThreadsTimeout() throws InterruptedException {
        int threadCnt = 50;
        CountDownLatch latch = new CountDownLatch(threadCnt);

        final int M1 = 1000000;
        for (int i = 0; i < threadCnt; i++) {
            taskExecutor.execute(()->{
                try{
                    bigkeyForString(100*M1);
                } catch (Throwable th){
                    if(th.getMessage().contains("NOREPLICAS")){
                        System.exit(9);
                    }
                    if(th.getMessage().contains("Command timed out")){
                        System.exit(8);
                    }
                    logger.error("Thread errors!"+Thread.currentThread().getName(),th);
                } finally {
                    latch.countDown();
                }

            });
        }

        latch.await();
    }

    private void bigkeyForString(int stringLength){
        StringBuffer sb = new StringBuffer("hello");
        String key = "xl_test_bigkey";

        for (int i = 0; i < stringLength; i++) {
            sb.append("a");
        }

        logger.info("value lengh is : [{}]K",sb.length()/1000);

        StopWatch sw = new StopWatch();
        sw.start();
        redisUtil.put(key, sb.toString());
        sw.stop();

        logger.info("took [{}]s Put the key of [{}] to redis. Get the value size is : [{}]K", sw.getTotalTimeSeconds() , key, redisUtil.get(key).length()/100);


    }

}
