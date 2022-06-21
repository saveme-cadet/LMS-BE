package com.savelms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CommonCode {

    @Id
    @GeneratedValue
    @Column(name = "COMMON_CODE_ID")
    private Long id;

    private String
}
