package com.artino.service.common;

import com.artino.service.base.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ESex implements R.IBaseEnum {
    /**
     * 默认
     */
    DEFAULT(0, "UNKNOWN"),
    /**
     * 男
     */
    MALE(1, "MALE"),
    /**
     * 女
     */
    FEMALE(2, "FEMALE"),
    /**
     * 保密
     */
    SECRET(3, "SECRET");

    @JsonValue
    private final Integer value;
    private final String label;

    ESex(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }

    @JsonCreator
    public static ESex getItem(Integer value) {
        for (ESex sex: ESex.values())
            if (sex.value.equals(value)) return sex;
        return null;
    }

    @Override
    public Integer getSelfValue() {
        return value;
    }
}