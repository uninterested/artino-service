package com.artino.service.services;

import com.artino.service.common.PageRes;
import com.artino.service.dto.menu.MenuListDTO;
import com.artino.service.dto.menu.NewMenuDTO;
import com.artino.service.dto.menu.UpdateMenuDTO;
import com.artino.service.vo.admin.res.AdminMenuListResVO;
import com.artino.service.vo.menu.req.MenuListVO;
import com.artino.service.vo.menu.res.MenuListResVO;

import java.util.List;

public interface IMenuService {

    /**
     * 新增菜单
     * @param dto dto
     * @return 是否成功
     */
    boolean newMenu(NewMenuDTO dto);

    /**
     * 删除菜单
     * @param id 菜单id
     * @return 是否成功
     */
    boolean deleteMenu(Long id);

    /**
     * 更新菜单信息
     * @param dto dto
     * @return 是否成功
     */
    boolean updateMenu(UpdateMenuDTO dto, Long id);

    /**
     * 获取当前登录用户可访问的菜单列表 - 树型
     * @return list
     */
    List<AdminMenuListResVO> userMenuLists();

    /**
     * 获取全部的菜单列表 - 树型
     * @return list
     */
    List<AdminMenuListResVO> systemMenuLists();

    /**
     * 获取全部的菜单列表
     * @param query 查询条件
     * @return list
     */
    List<MenuListResVO> systemMenus(MenuListDTO query);

    /**
     * 分页获取全部的菜单列表
     * @param query 查询条件
     * @return 查询结果
     */
    PageRes<MenuListResVO> systemMenusPage(MenuListDTO query);
}
