package com.savelms.core;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
//@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false)
    private LocalDate createdAt;
    private String createId;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate updatedAt;
    private String UpdateId;

}
