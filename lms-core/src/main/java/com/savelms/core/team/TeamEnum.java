package com.savelms.core.team;

import java.util.HashMap;
import java.util.Map;
import jdk.jshell.Snippet.Status;

public enum TeamEnum {
    RED("red"), BLUE("blue");
    private final String value;

    private static final Map<String, TeamEnum> map = new HashMap<>();

    static{
        for (TeamEnum teamEnum : values()) {
            map.put(teamEnum.value, teamEnum);
        }
    }
    TeamEnum(String value) {
        this.value = value;
    }

    public static TeamEnum findBy(String value) {
        return map.get(value);
    }

}
