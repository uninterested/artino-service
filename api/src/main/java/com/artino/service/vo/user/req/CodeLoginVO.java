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
@ApiModel("验证码登录")
public class CodeLoginVO {
    @ApiModelProperty("账号")
    @Required(message = "请输入账号")
    private String account;

    @ApiModelProperty("验证码")
    @Required(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码格式错误")
    private String code;
}
