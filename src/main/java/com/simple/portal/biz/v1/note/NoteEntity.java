package com.simple.portal.biz.v1.note;

import com.simple.portal.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Table(name="TB_NOTE")
public class NoteEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTE_ID")
    private Long id;
    @Column
    private String title; // 제목
    @Column
    private String contents; // 내용
    @Column
    private String writer; // 글쓴이
}
