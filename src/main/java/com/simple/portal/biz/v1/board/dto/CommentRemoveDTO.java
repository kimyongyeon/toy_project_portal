package com.simple.portal.biz.v1.board.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRemoveDTO {
    @NotNull(message = "Please enter id")
    @ApiModelProperty(value = "기본키", required = true, example = "기본키이에요")
    private Long id; // 아이디
}