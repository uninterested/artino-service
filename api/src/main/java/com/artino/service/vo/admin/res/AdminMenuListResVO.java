package com.artino.service.vo.admin.res;

import com.artino.service.entity.TMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户登录返回信息")
public class AdminMenuListResVO {
    @ApiModelProperty("菜单id")
    private Long id;
    @ApiModelProperty("父级菜单id")
    private Long parentId;
    @ApiModelProperty("菜单名称")
    private String name;
    @ApiModelProperty("权限值")
    private String value;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("类别")
    private TMenu.EType type;
    @ApiModelProperty("路径")
    private String url;
    @ApiModelProperty("子菜单")
    private List<AdminMenuListResVO> children;
}
