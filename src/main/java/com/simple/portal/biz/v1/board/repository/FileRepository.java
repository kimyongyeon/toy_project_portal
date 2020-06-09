package com.simple.portal.biz.v1.board.repository;

import com.simple.portal.biz.v1.board.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
