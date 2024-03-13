package com.artino.service.vo.user.req;

import com.artino.service.validator.required.Required;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户登录")
public class UserLoginVO {
    @ApiModelProperty("账号")
    @Required(message = "请输入账号")
    private String account;

    @ApiModelProperty("密码")
    @Required(message = "密码不能为空")
    @Size(min = 32, max = 32, message = "密码格式错误")
    private String password;
}
