package com.artino.service.entity;

import com.artino.service.base.R;
import com.artino.service.common.EDeleted;
import com.artino.service.common.ESex;
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
public class TUser {
    /**
     * id
     */
    private Long id;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 性别
     */
    private ESex sex;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 密码
     */
    private String password;
    /**
     * 余额
     */
    private Long balance;
    /**
     * openId
     */
    private String openId;
    /**
     * unionId
     */
    private String unionId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private String createdAt;
    /**
     * 状态
     */
    private TAdmin.EStatus status;
    /**
     * 是否已删除
     */
    private EDeleted deleted;

    public static enum EStatus implements R.IBaseEnum {
        /**
         * 默认 (正常)
         */
        DEFAULT(0, "NORMAL"),
        /**
         * 被冻结
         */
        FREEZE(1, "FREEZE");
        @JsonValue
        private final Integer value;
        private final String label;

        EStatus(int value, String label) {
            this.value = value;
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }

        @JsonCreator
        public static EStatus getItem(Integer value) {
            for (EStatus status: EStatus.values())
                if (status.value.equals(value)) return status;
            return null;
        }

        @Override
        public Integer getSelfValue() {
            return value;
        }
    }
}
