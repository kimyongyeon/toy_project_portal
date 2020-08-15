package com.simple.portal.biz.v1.board.dto;

import lombok.Data;

@Data
public class BasePageDTO {
    int page = 1;
    int offset = 10;
    int size = 20;
    private String sort;
}
