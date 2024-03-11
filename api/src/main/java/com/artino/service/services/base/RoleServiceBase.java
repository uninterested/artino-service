package com.artino.service.services.base;

import com.artino.service.base.BusinessException;
import com.artino.service.dto.role.RoleListDTO;
import com.artino.service.dto.role.UserRoleDTO;
import com.artino.service.entity.TConfig;
import com.artino.service.entity.TRole;
import com.artino.service.entity.TRoleMenu;
import com.artino.service.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RoleServiceBase {
    @Autowired
    @Lazy
    private RoleMapper roleMapper;

    @Autowired
    @Lazy
    private ConfigServiceBase configServiceBase;

    /**
     * 新增角色
     *
     * @param entity entity
     * @return yes / no
     */
    public boolean insert(TRole entity) {
        return roleMapper.insert(entity) > 0;
    }

    public boolean update(TRole entity) {
        return roleMapper.update(entity) > 0;
    }

    public TRole findById(Long id) {
        if (Objects.isNull(id) || id <= 0L) return null;
        return roleMapper.findOne(
                TRole.builder()
                        .id(id)
                        .build()
        );
    }

    /**
     * 获取列表
     * @param dto dto
     * @return list
     */
    public List<TRole> findWithPage(RoleListDTO dto) {
        return roleMapper.findWithPage(dto);
    }

    /**
     * 获取总数
     * @param dto dto
     * @return count
     */
    public Long findTotal(RoleListDTO dto) {
        return roleMapper.findTotal(dto);
    }

    /**
     * 获取下一个sort值
     *
     * @return 新的sort值
     */
    public Long findMaxSort() {
        Long max = roleMapper.findMaxSort();
        return (Objects.isNull(max) ? 0 : max) + 1;
    }

    /**
     * 查询指定id 的用户的集合
     * @param ids ids
     * @return list
     */
    public List<TRole> findLists(List<Long> ids) {
        return roleMapper.findLists(ids);
    }

    /**
     * 查询指定用户拥有的角色
     *
     * @param userId 用户id
     * @return 角色列表
     */
    public List<UserRoleDTO> findUserRole(Long userId) {
        if (Objects.isNull(userId) || userId <= 0) return null;
        return roleMapper.findUserRoles(userId);
    }

    /**
     * 查询用户是不是管理员
     *
     * @param userId 用户id
     * @return yes / no
     */
    public boolean userIsAdmin(Long userId) {
        List<UserRoleDTO> roles = findUserRole(userId);
        if (Objects.isNull(roles) || roles.isEmpty()) return false;
        TConfig config = configServiceBase.findAdmin();
        if (Objects.isNull(config)) return false;
        return roles.stream().anyMatch(e -> e.getSystemRole().equals(config.getId()));
    }

    /**
     * 确保登录的用户是管理员
     * @param userId 用户id
     */
    public void ensureIsAdmin(Long userId) {
        boolean isAdmin = userIsAdmin(userId);
        if (!isAdmin) throw BusinessException.build(100001, "没有操作的权限");
    }

    /**
     * 删除指定的角色id
     * @param roleId roleId
     * @return 是否删除成功
     */
    public boolean deleteListByRoleId(Long roleId) {
        return roleMapper.deleteListByRoleId(roleId) > 0;
    }

    /**
     * 批量插入数据
     * @param list list
     * @return 是否成功
     */
    public boolean batchInsertRoleMenu(List<TRoleMenu> list) {
        if (Objects.isNull(list) || list.isEmpty()) return false;
        return roleMapper.batchInsertRoleMenu(list) > 0;
    }
}
