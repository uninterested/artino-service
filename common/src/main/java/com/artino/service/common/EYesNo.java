package com.artino.service.common;

import com.artino.service.base.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EYesNo implements R.IBaseEnum {
    YES("YES", 0),
    NO("NO", 1);

    @JsonValue
    private final Integer value;
    private final String label;

    EYesNo(String label, Integer value) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }

    @JsonCreator
    public static EYesNo getItem (Integer val){
        for (EYesNo value : values())
            if (value.value.equals(val))
                return value;
        return null;
    }

    @Override
    public Integer getSelfValue() {
        return value;
    }
}
