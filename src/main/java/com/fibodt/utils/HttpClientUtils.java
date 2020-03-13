package com.fibodt.utils;

import com.fibodt.entity.dto.OkResponseDTO;
import com.fibodt.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * HTTP 请求帮助类
 *
 * @author claire
 * @date 2019-11-07 - 08:32
 **/
@Slf4j
public class HttpClientUtils {
    /**
     * 获取查询字段
     * @param url
     * @param queries
     * @return
     */
    private static StringBuffer getQueryString(String url, Map<String,String> queries){
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        return sb;
    }

    /**
     * GET请求
     * @param url
     * @param queries
     * @return
     */
    public static String get(String url, Map<String, String> queries) throws Exception {
        StringBuffer sb = getQueryString(url,queries);
        Request request = new Request.Builder()
                .url(sb.toString())
                .build();
        return execNewCall(request);
    }

    /**
     * POST表单请求
     * @param url
     * @param params
     * @param headerParamsMap
     * @return
     */
    public static OkResponseDTO postFormParams(String url, Map<String, String> params, Map<String, String> headerParamsMap) throws Exception {
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request.Builder builder1 = new Request.Builder();
        if(headerParamsMap != null && headerParamsMap.keySet().size()>0){
            Headers headers = Headers.of(headerParamsMap);
            builder1.headers(headers);
        }
        Request request = builder1
                .url(url)
                .post(builder.build())
                .build();
        return execNewCallResponse(request);
    }

    /**
     * POST JSON的请求，带自定义header参数
     * @param url
     * @param jsonParams
     * @return
     */
    public static String postJsonParams(String url, String jsonParams) throws Exception {
        RequestBody requestBody = RequestBody.create( jsonParams,MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE));
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody);

        return execNewCall(builder.build());
    }

    /**
     * POST JSON请求，无自定义header参数
     * @param url
     * @param jsonParams
     * @param headerParamsMap
     * @return
     */
    public static OkResponseDTO postJsonParams(String url, String jsonParams, Map<String, String> headerParamsMap) {
        RequestBody requestBody = RequestBody.create(jsonParams, MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE));
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);

        if(headerParamsMap != null && !headerParamsMap.isEmpty()) {
            Headers headers = Headers.of(headerParamsMap);
            requestBuilder.headers(headers);
        }
        return execNewCallResponse(requestBuilder.build());
    }

    /**
     * POST XML请求
     * @param url
     * @param xml
     * @return
     */
    public static OkResponseDTO postXmlParams(String url, String xml) throws Exception {
        RequestBody requestBody = RequestBody.create( xml,MediaType.parse(org.springframework.http.MediaType.APPLICATION_XML_VALUE));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execNewCallResponse(request);
    }

    /**
     * 执行查询操作
     * @param request
     * @return
     */
    public static String execNewCall(Request request) throws Exception {
        Response response = null;
        try {
            OkHttpClient okHttpClient = SpringContextUtil.getBean(OkHttpClient.class);
            response = okHttpClient.newCall(request).execute();
            if(okHttpClient.cookieJar() != CookieJar.NO_COOKIES){
                List<Cookie> cookies = Cookie.parseAll(request.url(), response.headers());
                if(!cookies.isEmpty()){
                    okHttpClient.cookieJar().saveFromResponse(request.url(),cookies);
                }
            }

            int status = response.code();
            if (status == HttpStatus.OK.value() && response.isSuccessful()) {
                return Objects.requireNonNull(response.body()).string();
            }else{
                log.error(Objects.requireNonNull(response.body()).string());
            }
        } catch (Exception e) {
            log.error("[execNewCall] okhttp3 put error >> ex = {}"+ ExceptionUtils.getStackTrace(e));
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * 执行查询操作
     *
     * @param request
     * @return
     */
    public static OkResponseDTO execNewCallResponse(Request request) {
        Response response = null;
        try {
            OkHttpClient okHttpClient = SpringContextUtil.getBean(OkHttpClient.class);
            response = okHttpClient.newCall(request).execute();
            if (okHttpClient.cookieJar() != CookieJar.NO_COOKIES) {
                List<Cookie> cookies = Cookie.parseAll(request.url(), response.headers());
                if (!cookies.isEmpty()) {
                    okHttpClient.cookieJar().saveFromResponse(request.url(), cookies);
                }
            }
            OkResponseDTO okResponseDTO = new OkResponseDTO();
            int status = response.code();
            if (status == HttpStatus.OK.value() && response.isSuccessful()) {
                okResponseDTO.setCode(HttpStatus.OK.value());
                okResponseDTO.setStatusCode(HttpStatus.OK.value());
                okResponseDTO.setBodyString(Objects.requireNonNull(response.body()).string());
                okResponseDTO.setBody(okResponseDTO.getBodyString());
                return okResponseDTO;
            } else if (status == HttpStatus.NOT_FOUND.value()) {
                okResponseDTO.setCode(HttpStatus.NOT_FOUND.value());
                return okResponseDTO;
            } else {
               log.error(Objects.requireNonNull(response.body()).string());
            }
        } catch (Exception e) {
           log.error("[execNewCallResponse] okhttp3 put error >> ex = {}"+ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

}
