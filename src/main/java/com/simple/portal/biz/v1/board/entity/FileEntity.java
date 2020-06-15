package com.simple.portal.biz.v1.board.entity;

import com.simple.portal.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="TB_FILE")
public class FileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본키

    @Column
    private String orgFileName; // 원본 파일 이름

    @Column
    private String fileName; // 파일 이름

    @Column
    private Long fileSize; // 파일 사이즈

    @Column
    private String fileExtension; // 파일 확장자

    @NotEmpty
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    BoardEntity boardEntity;

}
