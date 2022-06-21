package com.savelms;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "TODO")
@Entity
@SequenceGenerator(
    name = "TODO_GENERATOR",
    sequenceName = "MEMBER_SEQ",
    initialValue = 1,
    allocationSize = 50)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Todo extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TODO_GENERATOR")
    private Long seq;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="ATTENDANCE_ID")
    private Attendance attendance;

    private String title;
    private boolean titleCheck;


}
