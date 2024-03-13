package com.artino.service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
