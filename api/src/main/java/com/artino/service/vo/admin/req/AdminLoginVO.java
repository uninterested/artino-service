package com.artino.service.vo.admin.req;

import com.artino.service.validator.required.Required;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class AdminLoginVO {
    @ApiModelProperty("账号")
    @Required(message = "请输入账号")
    private String account;

    @ApiModelProperty("密码")
    @Required(message = "密码不能为空")
    @Size(min = 32, max = 32, message = "密码格式错误")
    private String password;
}
