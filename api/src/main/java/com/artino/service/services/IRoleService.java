package com.artino.service.services;

import com.artino.service.common.PageRes;
import com.artino.service.dto.role.NewRoleDTO;
import com.artino.service.dto.role.RoleListDTO;
import com.artino.service.vo.role.res.RoleListResVO;

import java.util.List;

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

    /**
     * 删除角色
     * @param id id
     * @return yes / no
     */
    boolean deleteRole(Long id);

    /**
     * 获取角色列表
     * @return 列表
     */
    List<RoleListResVO> roleList(RoleListDTO dto);

    /**
     * 分页获取角色列表
     * @param dto dto
     * @return 列表
     */
    PageRes<RoleListResVO> roleListPage(RoleListDTO dto);
}
