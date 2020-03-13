package com.fibodt.entity.dto;

import lombok.Data;

@Data
public class OkResponseDTO extends HttpResponse{
    private Integer code;
    private String bodyString;
}
