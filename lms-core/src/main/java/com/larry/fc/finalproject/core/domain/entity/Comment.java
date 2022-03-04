package com.larry.fc.finalproject.core.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
@Setter
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String commentLine;
    @ManyToOne
    @JoinColumn(name = "noticeboard_id")
    private NoticeBoard noticeBoard;

    public static Comment writeComment(NoticeBoard noticeBoard, String commentLine){
        return Comment.builder()
                .noticeBoard(noticeBoard)
                .commentLine(commentLine)
                .build();
    }

}
