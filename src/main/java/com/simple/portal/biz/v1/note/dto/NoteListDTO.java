package com.simple.portal.biz.v1.note.dto;

import com.simple.portal.biz.v1.board.dto.BasePageDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteListDTO extends BasePageDTO {
    private String userId;
    private String gb;
}
