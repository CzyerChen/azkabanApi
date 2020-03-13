package com.fibodt.config;

import okhttp3.ConnectionPool;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * http请求配置，包含超时、自动打印日志、连接池的设置等
 *
 * @author claire
 * @date 2019-11-06 - 18:50
 **/
@Configuration
public class OkHttpClientConfig {
    @Autowired
    private HttpProperties httpProperties;


    /**
     * https证书配置，目前暂不需要https的支持
     * @return
     */
    private SSLSocketFactory createSslSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return ssfFactory;
    }

    /**
     * http连接池的定义
     * @return
     */
    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(httpProperties.getConnectMaxIdle(), httpProperties.getConnectKeepAliveDuration(), TimeUnit.MINUTES);
    }

    /**
     * okhttp client的定义
     * 配置连接超时、读取超时、写入超时、重试机制、连接池、https、cookie保持机制、
     * @return
     */
    @Bean
    public OkHttpClient okHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(httpProperties.getClientConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(httpProperties.getClientReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(httpProperties.getClientWriteTimeout(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(httpProperties.isEnableRetry())
                .connectionPool(pool())
                .sslSocketFactory(Objects.requireNonNull(createSslSocketFactory()),new TrustCerts())
                .cookieJar(CookieJar.NO_COOKIES)
                .hostnameVerifier((hostname, session) -> true)
                .addNetworkInterceptor(loggingInterceptor);

        return builder.build();
    }
}

