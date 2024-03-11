package com.artino.service.dto.menu;

import com.artino.service.entity.TMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewMenuDTO {
    /**
     * 父级id
     */
    private Long parentId = 0L;

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
     * 类别
     */
    private TMenu.EType type;

    /**
     * 路由
     */
    private String url;
}
