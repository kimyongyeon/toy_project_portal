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
@ApiModel(description = "게시글좋아요싫어요")
public class BoardLikeDTO {

    @NotNull(message = "Please enter id")
    @ApiModelProperty(value = "기본키", required = true, example = "기본키이에요")
    private Long id;
    
    @NotBlank(message="아이템구분은 필수 입력값입니다.")
    @ApiModelProperty(value = "아이템구분", required = true, example = "L:좋아요,D:싫어요")
    private String itemGb; // 아이템 구분 L:D

    @NotBlank(message="클릭한 유저ID는 필수 입력값입니다.")
    @ApiModelProperty(value = "아이템구분", required = true, example = "user01, user02")
    private String clickUserId;


}
