package com.simple.portal.biz.v1.socket.repository;

import com.simple.portal.biz.v1.socket.entity.SocketNoteEntitiy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocketRepository extends JpaRepository<SocketNoteEntitiy, Long> {

}
