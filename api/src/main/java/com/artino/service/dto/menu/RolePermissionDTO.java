package com.artino.service.dto.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionDTO {
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 是不是管理员
     */
    private boolean admin;
    /**
     * 菜单id集合
     */
    private List<Long> menuIds;
}
