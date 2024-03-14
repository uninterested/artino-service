package com.artino.service.dto.user;

import com.artino.service.entity.TAdmin;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminMiniDTO extends TAdmin {
    private String miniName;
    private String miniIcon;
}
