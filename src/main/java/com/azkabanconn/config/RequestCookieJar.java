package com.azkabanconn.config;

import com.azkabanconn.entity.dto.CookieDTO;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 实现request cookie的保持
 *
 * @author claire
 * @date 2019-11-06 - 18:54
 **/
@Component
public class RequestCookieJar implements CookieJar {

    private static ConcurrentHashMap<String, List<CookieDTO>> cookieListMap = new ConcurrentHashMap<>();

    private String loginFullUrl = "http://121.36.18.212:8081/";

    /**
     * 从请求中把cookie拿出来存储，比如auth
     *
     * @param url
     * @param cookies
     */
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (!cookieListMap.containsKey(url.toString())) {
            if (cookies.size() != 0) {
                List<CookieDTO> cookieDtos = cookies.stream().map(this::transCookie2DTO).collect(Collectors.toList());
                if (cookieDtos.size() != 0) {
                    cookieListMap.put(url.toString(), cookieDtos);
                }
            }
        }
    }

    /**
     * 针对需要访问的url，出去auth的接口，其余把cookie的信息带上
     *
     * @param url
     * @return
     */
    @NotNull
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if (!url.toString().contains(loginFullUrl)) {
            if (cookieListMap.containsKey(loginFullUrl)) {
                List<CookieDTO> cookies = cookieListMap.get(loginFullUrl);
                if(cookies != null && cookies.size() !=0) {
                    List<Cookie> cookieList = cookies.stream().map(this::transToCookie).collect(Collectors.toList());
                    if(cookieList.size() != 0) {
                        return cookieList;
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    /**
     * cookie对象序列化到redis有redisException,所以考虑用一个DTO来承接和转换
     *
     * @param cookie
     * @return
     */
    private CookieDTO transCookie2DTO(Cookie cookie) {
        CookieDTO dto = new CookieDTO();
        if (StringUtils.isNotBlank(cookie.domain())) {
            dto.setDomain(cookie.domain());
        }
        if (StringUtils.isNotBlank(cookie.name())) {
            dto.setName(cookie.name());
        }

        if (StringUtils.isNotBlank(cookie.path())) {
            dto.setPath(cookie.path());
        }

        if (StringUtils.isNotBlank(cookie.value())) {
            dto.setValue(cookie.value());
        }

        dto.setExpires(cookie.expiresAt());
        dto.setHttpOnly(cookie.httpOnly());
        dto.setSecure(cookie.secure());
        return dto;

    }

    /**
     * dto转换成cookie
     *
     * @param dto
     * @return
     */
    private Cookie transToCookie(CookieDTO dto) {
        Cookie.Builder builder = new Cookie.Builder();
        //secure = true
        //httponly = true
        //hostholy = false
        builder.domain(dto.getDomain());
        builder.expiresAt(dto.getExpires());
        builder.name(dto.getName());
        builder.path(dto.getPath());
        builder.value(dto.getValue());
        return builder.build();
    }
}

