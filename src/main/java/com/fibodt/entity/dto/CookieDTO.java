package com.fibodt.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * cookie DTO 对象
 * @author claire
 * @date 2019-11-07 - 09:22
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CookieDTO implements Serializable {

    private  String name;
    private  String value;
    private  String domain;
    private  String path;
    private Long expires;
    private Boolean httpOnly;
    private Boolean secure;
}
