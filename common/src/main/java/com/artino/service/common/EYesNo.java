package com.artino.service.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum EYesNo {
    YES("YES", 0),
    NO("NO", 1);

    @JsonValue
    private int value;
    private String label;

    EYesNo(String label, int value) {
        this.value = value;
        this.label = label;
    }

    @JsonCreator
    public static EYesNo getItem (int val){
        for (EYesNo value : values())
            if (value.value == val)
                return value;
        return null;
    }
}
