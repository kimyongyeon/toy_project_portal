package com.simple.portal.biz.v1.board.dto;

import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "게시글삭제")
public class BoardIdDTO {

    @NotNull(message = "Please enter id")
    @ApiModelProperty(value = "기본키", required = true, example = "기본키이에요")
    private Long id;
}
