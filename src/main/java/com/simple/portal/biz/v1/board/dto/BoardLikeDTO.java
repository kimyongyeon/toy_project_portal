package com.simple.portal.biz.v1.board.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "게시글좋아요싫어요")
public class BoardLikeDTO {

    @NotBlank(message="기본키는 필수 입력값입니다.")
    @ApiModelProperty(value = "기본키", required = true, example = "기본키이에요")
    private String id;
    
    @NotBlank(message="아이템구분은 필수 입력값입니다.")
    @ApiModelProperty(value = "아이템구분", required = true, example = "L:좋아요,D:싫어요")
    private String itemGb; // 아이템 구분 L:D


}
