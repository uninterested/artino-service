package com.artino.service.vo.menu.req;

import com.artino.service.validator.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("更新菜单")
public class UpdateMenuVO {
    @ApiModelProperty("父级菜单ID")
    private Long parentId = 0L;

    @ApiModelProperty("菜单名称")
    @Xss
    private String name;

    @ApiModelProperty("菜单值")
    @Xss
    private String value;

    @ApiModelProperty("图标")
    @Xss
    private String icon;

    @ApiModelProperty("路由")
    @Xss
    private String url;
}
