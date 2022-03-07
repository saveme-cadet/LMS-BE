package com.larry.fc.finalproject.core.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "announcement")
public class Announcement extends BaseEntity{
//    @JoinColumn(name = "writer_Id")
//    @ManyToOne
//    private User user;
    private String notice;

    public static Announcement getAnnouncement(User user, String notice){
        return Announcement.builder()
          //      .user(user)
                .notice(notice)
                .build();
    }
}
