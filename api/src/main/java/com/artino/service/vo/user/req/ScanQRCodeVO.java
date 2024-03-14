package com.artino.service.vo.user.req;

import com.artino.service.validator.required.Required;
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
@ApiModel("用户端扫描二维码")
public class ScanQRCodeVO {
    @Required(message = "data不能为空")
    @ApiModelProperty("需要更新的数据")
    private Object data;

    @ApiModelProperty("token")
    @Required(message = "token不能为空")
    private String token;
}
