package com.larry.fc.finalproject.core.domain.entity;

import lombok.*;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "noticeboard")
public class NoticeBoard extends BaseEntity{
    private String title;
    private VarcharTypeDescriptor content;
    private boolean good;
    private boolean bad;

    @JoinColumn(name  = "write_id")
    @ManyToOne
    private User user;
    public static NoticeBoard writeNotice(User user, String title, VarcharTypeDescriptor content){
        return NoticeBoard.builder()
                .user(user)
                .title(title)
                .content(content)
                .good(false)
                .bad(false)
                .build();
    }
}
