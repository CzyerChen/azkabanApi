package com.fibodt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * http相关的一些常量参数：
 * 1.线程池参数
 * 2.keepalive参数
 *
 * @author claire
 * @date 2019-11-06 - 19:04
 **/
@Component
@ConfigurationProperties(prefix = "http")
@Data
public class HttpProperties {
    /**
     * 最大连接数
     */
    private int connectMaxIdle;
    /**
     * keep alive的时长
     */
    private long connectKeepAliveDuration;
    /**
     * 连接超时时间
     */
    private long clientConnectTimeout;
    /**
     * 读取内容超时时间
     */
    private long clientReadTimeout;
    /**
     * 写入内容超时时间
     */
    private long clientWriteTimeout;
    /**
     * 是否开启重试机制
     */
    private boolean enableRetry;
}
