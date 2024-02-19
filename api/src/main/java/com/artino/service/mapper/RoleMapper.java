package com.artino.service.mapper;

import com.artino.service.dto.role.UserRoleDTO;
import com.artino.service.entity.TRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface RoleMapper {
    /**
     * 新增
     * @param entity entity
     * @return 影响的行数
     */
    int insert(TRole entity);

    /**
     * 修改
     * @param entity entity
     * @return 影响的行数
     */
    int update(TRole entity);

    /**
     * 查询列表
     * @param entity entity
     * @return 查询的数据
     */
    List<TRole> findList(TRole entity);

    /**
     * 查询一个model
     * @param entity entity
     * @return model
     */
    TRole findOne(TRole entity);

    /**
     * 获取用户所有用的权限
     * @param id user id
     * @return role list
     */
    List<UserRoleDTO> findUserRoles(Long id);

    /**
     * 查询指定id 的用户
     * @param ids id list
     * @return entity list
     */
    @Deprecated(since = "Deprecated")
    List<TRole> findListWith(List<Long> ids);

    /**
     * 查询最大的sort值
     * @return 表中的最大的值
     */
    Long findMaxSort();

    /**
     * 查询指定id 的用户的集合
     * @param list id list
     * @return entity list
     */
    default List<TRole> findLists(List<Long> list) {
        if (Objects.isNull(list) || list.isEmpty()) return null;
        return findListWith(list);
    }

    /**
     * 根据指定改的id查询model
     * @param id id
     * @return model
     */
    default TRole findRoleBy(Long id) {
        if (Objects.isNull(id) || id <= 0L) return null;
        return findOne(
          TRole.builder().id(id).build()
        );
    }
}
