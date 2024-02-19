package com.artino.service.vo.role.req;

import com.artino.service.validator.required.Required;
import com.artino.service.validator.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel("修改角色")
public class EditRoleVO {
    @ApiModelProperty("角色名称")
    @Size(max = 20)
    @Xss(message = "角色名称不能包含脚本字符")
    private String name;

    @ApiModelProperty("角色描述")
    @Size(max = 100)
    @Xss(message = "角色描述不能包含脚本字符")
    private String description;
}
