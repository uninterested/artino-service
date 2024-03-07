package com.artino.service.vo.menu.req;


import com.artino.service.entity.TMenu;
import com.artino.service.validator.required.Required;
import com.artino.service.validator.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("新增菜单")
public class NewMenuVO {
    @ApiModelProperty("父级菜单ID")
    @Required(message = "请输入父级菜单ID")
    private Long parentId = 0L;

    @Required(message = "请输入菜单名称")
    @ApiModelProperty("菜单名称")
    @Xss
    private String name;

    @ApiModelProperty("菜单值")
    @Xss
    private String value;

    @ApiModelProperty("图标")
    @Xss
    private String icon;

    @ApiModelProperty("类别")
    @Required(message = "请输入类别")
    private TMenu.EType type;

    @ApiModelProperty("路由")
    @Xss
    private String url;
}
