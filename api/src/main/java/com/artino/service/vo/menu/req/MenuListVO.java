package com.artino.service.vo.menu.req;

import com.artino.service.common.PageReq;
import com.artino.service.entity.TMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel("分页获取菜单列表")
@Data
@Builder
public class MenuListVO extends PageReq {
    @ApiModelProperty("关键词")
    private String keyword;
    @ApiModelProperty("类别")
    private TMenu.EType type;
    @ApiModelProperty("创建时间")
    private List<String> createdAt;
}
