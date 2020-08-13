package com.simple.portal.common;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    private String createdBy = "admin";

    @CreatedDate
    private LocalDateTime createdDate;

    private String lastModifiedBy = "editer";

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;


}
