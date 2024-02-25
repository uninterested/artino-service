package com.artino.service.entity;

import com.artino.service.base.R;
import com.artino.service.common.EYesNo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TMenu {
    /**
     * id
     */
    private Long id;
    /**
     * 父级id
     */
    private Long parentId;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 权限值
     */
    private String value;
    /**
     * 图标
     */
    private String icon;
    /**
     * 类别
     */
    private EType type;
    /**
     * 资源路由
     */
    private String url;
    /**
     * 状态
     */
    private EYesNo status;
    /**
     * 创建时间
     */
    private String createdAt;
    /**
     * 排序值
     */
    private Integer sort;

    public static enum EType implements R.IBaseEnum {
        /**
         * 一级路由
         */
        MENU(0, "MENU"),
        /**
         * 二级路由
         */
        ROUTE(1, "ROUTE"),
        /**
         * 按钮
         */
        BUTTON(2, "BUTTON");

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
            for (EType type : EType.values())
                if (type.value.equals(value)) return type;
            return null;
        }

        @Override
        public Integer getSelfValue() {
            return this.value;
        }
    }
}
