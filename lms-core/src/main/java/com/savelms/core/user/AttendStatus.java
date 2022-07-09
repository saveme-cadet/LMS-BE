package com.savelms.core.user;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AttendStatus {

    PARTICIPATED, NOT_PARTICIPATED;

    @JsonCreator
    public static AttendStatus from(String s) {
        return AttendStatus.valueOf(s.toUpperCase());
    }
}
