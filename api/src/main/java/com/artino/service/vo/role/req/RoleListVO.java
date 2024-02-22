package com.artino.service.vo.role.req;

import com.artino.service.common.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@ApiModel("角色列表")
public class RoleListVO extends PageReq {
    @ApiModelProperty("是否需要显示权限值")
    private boolean showPermission;
    @ApiModelProperty("关键字")
    private String keyword;
    @ApiModelProperty("创建时间")
    private List<String> createdAt;
}
