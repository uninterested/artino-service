package com.artino.service.vo.admin.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel("设置角色")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetRoleVO {
    @ApiModelProperty("是不是管理员")
    private boolean isAdmin;
    @ApiModelProperty("角色id")
    private List<Long> roleIds;
}
