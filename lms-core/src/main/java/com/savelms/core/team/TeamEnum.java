package com.savelms.core.team;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.savelms.core.user.role.RoleEnum;
import java.util.HashMap;
import java.util.Map;
import jdk.jshell.Snippet.Status;

public enum TeamEnum {
    NONE, RED, BLUE;
//    private final String value;
//
//    private static final Map<String, TeamEnum> map = new HashMap<>();
//
//    static{
//        for (TeamEnum teamEnum : values()) {
//            map.put(teamEnum.value, teamEnum);
//        }
//    }
//    TeamEnum(String value) {
//        this.value = value;
//    }
//
//    public static TeamEnum findBy(String value) {
//        return map.get(value);
//    }
    @JsonCreator
    public static TeamEnum from(String s) {
        return TeamEnum.valueOf(s.toUpperCase());
    }

}
