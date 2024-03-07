package com.artino.service.services;

import com.artino.service.vo.admin.res.AdminMenuListResVO;
import com.artino.service.vo.menu.req.NewMenuVO;
import com.artino.service.vo.menu.req.UpdateMenuVO;

import java.util.List;

public interface IMenuService {

    /**
     * 新增菜单
     * @param vo vo
     * @return 是否成功
     */
    boolean newMenu(NewMenuVO vo);

    /**
     * 删除菜单
     * @param id 菜单id
     * @return 是否成功
     */
    boolean deleteMenu(Long id);

    /**
     * 更新菜单信息
     * @param vo vo
     * @return 是否成功
     */
    boolean updateMenu(UpdateMenuVO vo, Long id);

    /**
     * 获取当前登录用户可访问的菜单列表
     * @return list
     */
    List<AdminMenuListResVO> userMenuLists();

    /**
     * 获取全部的菜单列表
     * @return list
     */
    List<AdminMenuListResVO> systemMenuLists();
}
