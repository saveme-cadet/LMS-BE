package com.savelms.core.user;

import lombok.Getter;

@Getter
public enum UserSortFieldEnum {
    NICKNAME("nickname"), CREATEDDATE("createdAt");

    private String value;

    UserSortFieldEnum(String value) {
        this.value = value;
    }
}
