package com.xl.example.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 核心线程池大小
        executor.setMaxPoolSize(10); // 最大线程池大小
        executor.setQueueCapacity(100); // 等待队列容量
        executor.setThreadNamePrefix("MyThread-"); // 线程名称前缀
        executor.initialize();
        return executor;
    }

}