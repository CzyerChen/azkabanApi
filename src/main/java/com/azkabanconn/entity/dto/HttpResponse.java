package com.azkabanconn.entity.dto;

import lombok.Data;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述: <br/>
 * 〈http 响应对象〉
 *
 * @author claire
 * @date 2020-02-06 - 18:07
 */
@Data
public class HttpResponse {
    /**
     * Get the status code of the response.
     */
    private int statusCode;

    /**
     * Get the status message of the response.
     */
    private String statusMessage;

    /**
     * Get the character set name of the response, derived from the content-type header.
     */
    private String charset;

    /**
     * Get the response content type (e.g. "text/html");
     */
    private String contentType;

    /**
     * Get the body of the response as an array of bytes.
     */
    private byte[] bodyAsBytes;
    /**
     * Get the body of the response as a plain string.
     */
    private String body;
    /**
     * Get the body of the response as a (buffered) InputStream. You should close the input stream when you're done with it.
     * Other body methods (like bufferUp, body, parse, etc) will not work in conjunction with this method.
     * <p>This method is useful for writing large responses to disk, without buffering them completely into memory first.</p>
     */
    private InputStream bodyStream;

    private Map<String, List<String>> headers;

    /**
     * Retrieve all of the request/response cookies as a map
     *
     * @return cookies
     */
    private Map<String, String> cookies;

    public HttpResponse() {
    }

    public HttpResponse(int statusCode, String statusMessage, String contentType) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.contentType = contentType;
    }


    public Map<String, String> getHeaders() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(headers.size());
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String header = entry.getKey();
            List<String> values = entry.getValue();
            if (values.size() > 0) {
                map.put(header, values.get(0));
            }
        }
        return map;
    }

}
