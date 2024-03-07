package com.artino.service.mapper;

import com.artino.service.entity.TMenu;
import com.artino.service.entity.TRoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface MenuMapper {
    /**
     * 新增
     * @param entity entity
     * @return yes / no
     */
    int insert(TMenu entity);

    /**
     * 修改
     * @param entity entity
     * @return yes / no
     */
    int update(TMenu entity);

    /**
     * 获取列表
     * @param entity entity
     * @return menu list
     */
    List<TMenu> findList(TMenu entity);

    /**
     * 获取角色拥有的权限
     * @param list
     * @return
     */
    List<TRoleMenu> findRoleMenuWith(List<Long> list);

    /**
     * 获取指定改的数据
     * @param entity entity
     * @return model
     */
    TMenu findOne(TMenu entity);

    /**
     查询指定id 的用户的菜单
     * @param ids id list
     * @return entity list
     */
    List<TMenu> findListWith(List<Long> ids);

    /**
     * 查询指定角色的菜单
     * @param roleIds 角色id
     * @return entity list
     */
    List<TMenu> findListWithRoleId(List<Long> roleIds);

    /**
     * 查询指定id 的用户的集合
     * @param list id list
     * @return entity list
     */
    default List<TMenu> findLists(List<Long> list) {
        if (Objects.isNull(list) || list.isEmpty()) return null;
        return findListWith(list);
    }

    /**
     * 获取最大的sort值
     * @return 最大值
     */
    Long findMaxSort();
}
