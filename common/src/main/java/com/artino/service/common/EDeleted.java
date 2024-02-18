package com.artino.service.common;

import com.artino.service.base.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EDeleted implements R.IBaseEnum {
    NO("NO", 0),
    YES("NO", 1);

    @JsonValue
    private final Integer value;
    private final String label;

    EDeleted(String label, Integer value) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }

    @JsonCreator
    public static EDeleted getItem (Integer val){
        for (EDeleted value : values())
            if (value.value.equals(val))
                return value;
        return null;
    }

    @Override
    public Integer getSelfValue() {
        return value;
    }
}
