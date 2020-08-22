package com.simple.portal.biz.v1.note.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(description = "쪽지함 삭제")
public class NoteRemoveDTO {
    @ApiModelProperty(value = "쪽지함ID", required = true, example = "[1,2,3,4,5]")
    private List<Long> id;
    @ApiModelProperty(value = "구분", required = true, example = "구분(R:받는편지함,S:보낸편지함)")
    private String gb;
}
