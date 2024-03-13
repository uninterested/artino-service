package com.artino.service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TUserAdmin {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 管理员id
     */
    private Long adminId;
}
