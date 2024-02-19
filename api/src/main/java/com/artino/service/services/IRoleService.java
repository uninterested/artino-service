package com.artino.service.services;

import com.artino.service.dto.role.NewRoleDTO;

public interface IRoleService {
    /**
     * 新增角色
     * @param dto dto
     * @return 是否新增成功
     */
    boolean newRole(NewRoleDTO dto);

    /**
     * 更新角色
     * @param dto dto
     * @return yes / no
     */
    boolean updateRole(Long id, NewRoleDTO dto);
}
