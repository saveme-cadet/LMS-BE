package com.savelms.core;

import lombok.Getter;

@Getter
public enum SortTypeEnum {

    ASC("asc"), DESC("desc");
    private String value;

    SortTypeEnum(String value) {
        this.value = value;
    }
}
