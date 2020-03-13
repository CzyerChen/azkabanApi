package com.azkabanconn.config;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * HTTPS 请求证书
 * @author claire
 * @date 2019-11-06 - 18:55
 **/
public class TrustCerts implements X509TrustManager {
    /**
     * checkClientTrusted
     *
     * @param chain
     * @param authType
     */
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {}

    /**
     * checkServerTrusted
     *
     * @param chain
     * @param authType
     */
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {}

    /**
     * getAcceptedIssuers
     *
     * @return
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}

}

