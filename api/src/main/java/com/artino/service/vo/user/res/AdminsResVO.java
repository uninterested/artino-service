package com.artino.service.vo.user.res;

import com.artino.service.vo.admin.res.AdminLoginResVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("当前用户关联的管理端")
public class AdminsResVO extends AdminLoginResVO {
    @ApiModelProperty("小程序名称")
    private String miniName;
    @ApiModelProperty("小程序头像")
    private String miniIcon;
}
