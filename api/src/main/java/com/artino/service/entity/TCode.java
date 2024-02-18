package com.artino.service.entity;

import com.artino.service.base.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TCode {
    /**
     * id
     */
    private Long id;
    /**
     * 账号 (手机号/邮箱)
     */
    private String account;
    /**
     * 验证码
     */
    private String code;
    /**
     * 类别
     */
    private EType type;
    /**
     * 验证码使用的次数
     */
    private Integer verified;
    /**
     * 创建时间
     */
    private String createdAt;
    /**
     * 过期时间
     */
    private String expiredAt;


    public static enum EType implements R.IBaseEnum  {
        /**
         * 帐户注册
         */
        REGISTER(0, "REGISTER"),
        /**
         * 重置密码
         */
        RESETPASSWORD(1, "RESETPASSWORD"),
        /**
         * 快速登录
         */
        FASTLOGIN(2, "FASTLOGIN");

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
