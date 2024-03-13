package com.artino.service.dto.admin;

import com.artino.service.base.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QRCodeDTO {
    /**
     * 签发机构
     */
    private final String publisher = "Artino";
    /**
     * 二维码用途
     */
    private EType type;
    /**
     * 过期时间
     */
    private Long expiredAt;
    /**
     * 票据
     */
    private String token;
    /**
     * type 是登录的时候：管理端的id
     */
    private Object data;

    public static enum EType implements R.IBaseEnum {
        /**
         * 登录二维码
         */
        LOGIN(110000, "LOGIN");

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
        public static EType getItem(Integer value) {
            for (EType type: EType.values())
                if (type.value.equals(value)) return type;
            return null;
        }

        @Override
        public Integer getSelfValue() {
            return this.value;
        }
    }
}
