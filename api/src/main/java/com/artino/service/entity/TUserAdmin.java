package com.artino.service.entity;

import com.artino.service.base.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TUserAdmin {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 管理员id
     */
    private Long adminId;
    /**
     * 拥有的权限
     */
    private EPermission permission;

    public static enum EPermission implements R.IBaseEnum {
        /**
         * 拥有着 (正常)
         */
        OWNER(1, "OWNER"),
        /**
         * 运营这
         */
        OPERATE(2, "OPERATE"),
        /**
         * 开发者
         */
        DEVELOPER(3, "DEVELOPER"),
        /**
         * 数据分析者
         */
        DATAVIEWER(8, "DATAVIEWER");
        @JsonValue
        private final Integer value;
        private final String label;

        EPermission(int value, String label) {
            this.value = value;
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }

        @JsonCreator
        public static EPermission getItem(Integer value) {
            for (EPermission status: EPermission.values())
                if (status.value.equals(value)) return status;
            return null;
        }

        @Override
        public Integer getSelfValue() {
            return value;
        }
    }
}
