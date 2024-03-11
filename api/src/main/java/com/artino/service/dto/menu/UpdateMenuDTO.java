package com.artino.service.dto.menu;

import com.artino.service.validator.xss.Xss;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMenuDTO {
    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单值
     */
    private String value;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路由
     */
    private String url;
}
