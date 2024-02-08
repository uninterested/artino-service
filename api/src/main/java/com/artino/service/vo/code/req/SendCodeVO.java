package com.artino.service.vo.code.req;

import com.artino.service.entity.TCode;
import com.artino.service.validator.required.Required;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("发生验证码")
public class SendCodeVO {
    @ApiModelProperty("手机号/邮箱")
    @Required(message = "请输入账号")
    private String account;

    @ApiModelProperty("验证码类型")
    @Required(message = "请输入验证码类型")
    private TCode.EType type;
}
