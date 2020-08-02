package com.simple.portal.biz.v1.note.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "쪽지함")
public class NoteSaveDTO {
    @ApiModelProperty(value = "제목", required = true, example = "쪽지 제목은 소심하게 적어 봅니다.")
    private String title; // 제목
    @ApiModelProperty(value = "내용", required = true, example = "쪽지 내용은 별다른게 없어요...")
    private String contents; // 내용
    @ApiModelProperty(value = "받는아이디", required = true, example = "revId01")
    private String revId; // 받는이 아이디
    @ApiModelProperty(value = "보낸아이디", required = true, example = "sendId01")
    private String sendId; // 보내는이 아이디
}
