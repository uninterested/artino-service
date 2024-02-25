package com.artino.service.vo.role.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("角色列表")
public class RoleListResVO {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("角色名称")
    private String name;
    @ApiModelProperty("角色备注")
    private String description;
    @ApiModelProperty("系统初始化的，不可编辑/修改")
    private boolean system;
    @ApiModelProperty("拥有的权限")
    private String permissions;
    @ApiModelProperty("创建时间")
    private String createdAt;
}
