package com.artino.service.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.util.Objects;

@ApiModel("分页数据请求")
@Data
public class PageReq {
    /**
     * 分页查询的页码
     */
    @Min(value = 0, message = "页码不能小于0")
    @ApiModelProperty("分页查询的页码")
    private Long current = 0L;
    /**
     * 分页查询的数量
     */
    @Min(value = 1, message = "数量不能小于1")
    @ApiModelProperty("分页查询的数量")
    private Long pageSize = 0L;

    private Long skip;

    public long getSkip() {
        if (Objects.isNull(current) || Objects.isNull(pageSize))
            return 0L;
        return this.current * this.pageSize;
    }
}
