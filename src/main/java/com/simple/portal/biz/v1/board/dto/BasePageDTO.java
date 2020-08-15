package com.simple.portal.biz.v1.board.dto;

import lombok.Data;

@Data
public class BasePageDTO {
    int currentPage = 1;
    int size = 20;
    private String sort;
}
