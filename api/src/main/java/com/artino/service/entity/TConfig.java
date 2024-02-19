package com.artino.service.entity;

import com.artino.service.base.R;
import com.artino.service.common.EYesNo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TConfig {
    /**
     * id
     */
    private Long id;
    /**
     * 类别
     */
    private EType type;

    /**
     * 标签
     */
    private String label;

    /**
     * 值
     */
    private Long value;

    /**
     * 启用状态
     */
    private EYesNo status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createdAt;

    public static enum EType implements R.IBaseEnum {
        /**
         * 帐户注册
         */
        ROLE(0, "ROLE");

        @JsonValue
        public final Integer value;
        public final String label;

        EType(Integer value, String label) {
            this.value = value;
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }

        @JsonCreator
        public static TConfig.EType getItem(Integer value) {
            for (TConfig.EType type: TConfig.EType.values())
                if (type.value.equals(value)) return type;
            return null;
        }

        @Override
        public Integer getSelfValue() {
            return this.value;
        }
    }
}
