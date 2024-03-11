package com.artino.service.dto.menu;

import com.artino.service.common.PageReq;
import com.artino.service.entity.TMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuListDTO extends PageReq {
    /**
     * 关键词
     */
    private String keyword;
    /**
     * 类别
     */
    private TMenu.EType type;

    /**
     * 创建时间
     */
    private List<String> createdAt;
}
