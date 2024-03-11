package com.artino.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TAdminRole {
    /**
     * 用户id
     */
    private Long adminId;
    /**
     * 角色id
     */
    private Long roleId;
}
