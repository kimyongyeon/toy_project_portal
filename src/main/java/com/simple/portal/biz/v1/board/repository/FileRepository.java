package com.simple.portal.biz.v1.board.repository;

import com.simple.portal.biz.v1.board.entity.FileEntity;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<FileEntity, Double> {
}
