package com.azkabanconn.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 为API请求添加日志
 *
 * @author claire
 * @date 2019-11-06 - 18:49
 **/
@Slf4j
public class HttpLogger implements HttpLoggingInterceptor.Logger {
    /**
     * 打日志的方法
     * @param message
     */
    @Override
    public void log(String message) {
        log.debug("API请求详情："+message);
    }
}
