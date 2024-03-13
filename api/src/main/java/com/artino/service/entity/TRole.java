package com.artino.service.entity;

import com.artino.service.common.EDeleted;
import com.artino.service.common.EYesNo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TRole {
    /**
     * ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色备注
     */
    private String description;

    /**
     * 排序值
     */
    private Long sort;

    /**
     * 创建时间
     */
    private String createdAt;

    /**
     * 是否已删除
     */
    private EDeleted deleted;
}
