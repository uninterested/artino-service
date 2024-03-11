package com.artino.service.vo.role.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel("为角色赋权")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionVO {
    @ApiModelProperty("是不是管理员")
    private boolean admin;
    @ApiModelProperty("菜单id集合")
    private List<Long> menuIds;
}
