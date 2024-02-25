package com.artino.service.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("分页查询结果")
public class PageRes<T> {
    @ApiModelProperty("总数")
    private Long total;
    @ApiModelProperty("当前页数")
    private Long current;
    @ApiModelProperty("当前数量")
    private Long pageSize;
    @ApiModelProperty("数据")
    private List<T> data;

    public static <T> PageRes<T> build(List<T> data, Long total, PageReq req) {
        PageRes<T> res = new PageRes<T>();
        res.setPageSize(req.getPageSize());
        res.setCurrent(req.getCurrent());
        res.setTotal(total);
        res.setData(data);
        return res;
    }
}
