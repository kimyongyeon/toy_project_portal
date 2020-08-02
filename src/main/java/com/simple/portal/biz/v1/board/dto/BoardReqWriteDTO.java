package com.simple.portal.biz.v1.board.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "게시글쓰기")
public class BoardReqWriteDTO {
    @NotNull(message = "Please enter id")
    @ApiModelProperty(value = "제목", required = true, example = "제목이에요.")
    private String title; // 제목

    @NotBlank(message="아이디는 필수 입력값입니다.")
    @ApiModelProperty(value = "글쓴이", required = true, example = "글쓴이에요.")
    private String writer; // 글쓴이

    @NotBlank(message="내용은 필수 입력값입니다.")
    @ApiModelProperty(value = "내용", required = true, example = "내용이에요.")
    private String contents; // 내용
}
