package com.artino.service.services.base;

import com.artino.service.common.EYesNo;
import com.artino.service.dto.role.UserRoleDTO;
import com.artino.service.entity.TMenu;
import com.artino.service.entity.TRoleMenu;
import com.artino.service.mapper.MenuMapper;
import com.artino.service.utils.CopyUtils;
import com.artino.service.vo.admin.res.AdminMenuListResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.artino.service.utils.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class MenuServiceBase {

    @Autowired
    @Lazy
    private MenuMapper menuMapper;

    @Autowired
    @Lazy
    private RoleServiceBase roleServiceBase;

    /**
     * 新增菜单
     *
     * @param entity entity
     * @return 是否成功
     */
    public boolean insert(TMenu entity) {
        return menuMapper.insert(entity) > 0;
    }

    /**
     * 更新数据
     *
     * @param entity entity
     * @return 是否成功
     */
    public boolean update(TMenu entity) {
        return menuMapper.update(entity) > 0;
    }

    /**
     * 获取角色拥有的菜单列表
     *
     * @param roleIds 角色列表
     * @return 菜单列表
     */
    public List<TRoleMenu> findRoleMenuWith(List<Long> roleIds) {
        return menuMapper.findRoleMenuWith(roleIds);
    }

    /**
     * 获取管理员所拥有的菜单列表
     *
     * @param type 类别
     * @return list
     */
    public List<TMenu> findMenuListByAdmin(TMenu.EType type) {
        TMenu dto = TMenu.builder()
                .status(EYesNo.YES)
                .build();
        if (Objects.nonNull(type)) dto.setType(type);
        return findList(dto);
    }

    /**
     * 根据用户id 获取菜单列表
     *
     * @param userId 用户id
     * @param type   类别
     * @return list
     */
    public List<TMenu> findListByUserId(Long userId, TMenu.EType type) {
        boolean isAdmin = Objects.isNull(userId) || roleServiceBase.userIsAdmin(userId);
        if (isAdmin) return this.findMenuListByAdmin(type);
        List<UserRoleDTO> roleList = roleServiceBase.findUserRole(userId);
        List<Long> roleIds = roleList.stream().map(UserRoleDTO::getId).toList();
        return findListWithRoleId(roleIds, type);
    }

    /**
     * 根据用户id 获取菜单s树
     *
     * @param userId 用户id
     * @param type   类别
     * @return list
     */
    public List<AdminMenuListResVO> findUserMenuTree(Long userId, TMenu.EType type) {
        List<TMenu> list = findListByUserId(userId, type);
        return buildTree(list);
    }

    /**
     * 查询指定角色id集合的列表
     *
     * @param roleIds 菜单id
     * @param type    类别
     * @return list
     */
    public List<TMenu> findListWithRoleId(List<Long> roleIds, TMenu.EType type) {
        List<TMenu> list = menuMapper.findListWithRoleId(roleIds);
        if (Objects.isNull(type)) return list;
        return list.stream().filter(e -> e.getType().equals(type)).toList();
    }

    /**
     * 查询指定菜单id集合的列表
     *
     * @param ids  菜单id
     * @param type 类别
     * @return list
     */
    public List<TMenu> findListBy(List<Long> ids, TMenu.EType type) {
        List<TMenu> list = menuMapper.findListWith(ids);
        if (Objects.isNull(type)) return list;
        return list.stream().filter(e -> e.getType().equals(type)).toList();
    }

    /**
     * 查询当前最大的排序值
     *
     * @return 排序值
     */
    public Long findMaxSort() {
        Long max = menuMapper.findMaxSort();
        return Objects.nonNull(max) ? max : 0L;
    }

    /**
     * 根据id获取菜单
     *
     * @param id 菜单id
     * @return 菜单
     */
    public TMenu findMenuBy(Long id) {
        if (id <= 0L) return null;
        return menuMapper.findOne(
                TMenu.builder()
                        .id(id)
                        .build()
        );
    }

    /**
     * 获取list
     *
     * @param entity entity
     * @return list
     */
    public List<TMenu> findList(TMenu entity) {
        return menuMapper.findList(entity);
    }

    /**
     * 构建树
     *
     * @param list 列表
     * @return 树
     */
    public List<AdminMenuListResVO> buildTree(List<TMenu> list) {
        HashMap<Long, AdminMenuListResVO> cache = new HashMap<>();
        List<AdminMenuListResVO> tree = new ArrayList<>();
        for (TMenu menu : list) {
            AdminMenuListResVO vo = CopyUtils.copy(menu, AdminMenuListResVO.class);
            if (Objects.nonNull(vo)) {
                if (vo.getType() == TMenu.EType.MENU) {
                    vo.setValue(null);
                } else if (vo.getType() == TMenu.EType.ROUTE) {
                    vo.setIcon(null);
                } else if (vo.getType() == TMenu.EType.BUTTON) {
                    vo.setIcon(null);
                    vo.setUrl(null);
                }
                cache.put(vo.getId(), vo);
                if (vo.getParentId().equals(0L)) {
                    tree.add(vo);
                } else {
                    AdminMenuListResVO parent = cache.get(vo.getParentId());
                    if (Objects.isNull(parent.getChildren())) parent.setChildren(new ArrayList<>());
                    parent.getChildren().add(vo);
                }
            }
        }
        cache.clear();
        return tree;
    }
}
