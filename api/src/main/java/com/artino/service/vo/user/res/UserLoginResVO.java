package com.artino.service.vo.user.res;

import com.artino.service.common.ESex;
import com.artino.service.desensitization.Desensitization;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户登录返回信息")
public class UserLoginResVO {
    @ApiModelProperty("用户ID")
    private Long id;
    @ApiModelProperty("昵称")
    private String nickName;
    @Desensitization(type = Desensitization.DesensitizationType.EMAIL)
    @ApiModelProperty("邮箱")
    private String email;
    @Desensitization(type = Desensitization.DesensitizationType.PHONE)
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("性别")
    private ESex sex;
    @Desensitization(type = Desensitization.DesensitizationType.FILE)
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("余额")
    private Long balance;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("创建时间")
    private String createdAt;
}
