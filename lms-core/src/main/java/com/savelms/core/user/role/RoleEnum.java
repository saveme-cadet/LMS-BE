package com.savelms.core.user.role;

import java.util.HashMap;
import java.util.Map;

public enum RoleEnum {
    ROLE_UNAUTHORIZED("unAuthorized"), ROLE_USER("user"), ROLE_MANAGER("manager"), ROLE_ADMIN("admin");

    private final String value;

    private static final Map<String, RoleEnum> map = new HashMap<>();

    static{
        for (RoleEnum roleEnum : values()) {
            map.put(roleEnum.value, roleEnum);
        }
    }
    RoleEnum(String value) {
        this.value = value;
    }

    public static RoleEnum findBy(String value) {
        return map.get(value);
    }
}
