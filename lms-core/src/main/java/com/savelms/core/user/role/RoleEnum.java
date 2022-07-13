package com.savelms.core.user.role;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RoleEnum {
    ROLE_UNAUTHORIZED, ROLE_USER, ROLE_MANAGER, ROLE_ADMIN;
    private static final String prefix = "ROLE_";

    @JsonCreator
    public static RoleEnum from(String s) {
        String target = prefix + s.toUpperCase();
        return RoleEnum.valueOf(target);
    }
}
