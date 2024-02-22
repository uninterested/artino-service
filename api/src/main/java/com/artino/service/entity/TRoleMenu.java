package com.artino.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TRoleMenu {
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 菜单id
     */
    private Long menuId;
}
