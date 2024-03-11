package com.artino.service.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetRoleDTO {
    /**
     * 是不是管理员
     */
    private boolean isAdmin;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private List<Long> roleIds;
}
