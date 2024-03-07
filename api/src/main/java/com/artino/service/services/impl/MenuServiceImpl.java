package com.artino.service.services.impl;

import com.artino.service.base.BusinessException;
import com.artino.service.common.EYesNo;
import com.artino.service.context.RequestContext;
import com.artino.service.entity.TMenu;
import com.artino.service.services.IMenuService;
import com.artino.service.services.base.MenuServiceBase;
import com.artino.service.services.base.RoleServiceBase;
import com.artino.service.utils.DateUtils;
import com.artino.service.utils.IDUtils;
import com.artino.service.utils.StringUtils;
import com.artino.service.vo.admin.res.AdminMenuListResVO;
import com.artino.service.vo.menu.req.NewMenuVO;
import com.artino.service.vo.menu.req.UpdateMenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MenuServiceImpl implements IMenuService {
    @Autowired
    @Lazy
    private RoleServiceBase roleServiceBase;

    @Autowired
    @Lazy
    private MenuServiceBase menuServiceBase;

    @Override
    public boolean newMenu(NewMenuVO vo) {
        Long userId = RequestContext.get().getUid();
        roleServiceBase.ensureIsAdmin(userId);
        if (vo.getType() == TMenu.EType.MENU) {
            vo.setValue(null);
            if (StringUtils.isEmpty(vo.getIcon()))
                throw BusinessException.build(100001, "请检查入参");
        } else if (vo.getType() == TMenu.EType.ROUTE) {
            vo.setIcon(null);
            if (StringUtils.isEmpty(vo.getUrl()) || StringUtils.isEmpty(vo.getValue()))
                throw BusinessException.build(100001, "请检查入参");
        } else if (vo.getType() == TMenu.EType.BUTTON) {
            vo.setIcon(null);
            vo.setUrl(null);
            if (StringUtils.isEmpty(vo.getValue()))
                throw BusinessException.build(100001, "请检查入参");
        }
        if (vo.getParentId() != 0L) {
            TMenu parent = menuServiceBase.findMenuBy(vo.getParentId());
            if (Objects.isNull(parent))
                throw BusinessException.build(100002, "指定菜单不存在");
        }
        Long max = menuServiceBase.findMaxSort();
        TMenu dto = TMenu.builder()
                .id(IDUtils.shared().nextId())
                .parentId(vo.getParentId())
                .name(vo.getName())
                .value(vo.getName())
                .icon(vo.getIcon())
                .type(vo.getType())
                .url(vo.getUrl())
                .createdAt(DateUtils.getTime())
                .sort(max.intValue() + 1)
                .build();
        return menuServiceBase.insert(dto);
    }

    @Override
    public boolean deleteMenu(Long id) {
        Long userId = RequestContext.get().getUid();
        roleServiceBase.ensureIsAdmin(userId);
        TMenu menu = menuServiceBase.findMenuBy(id);
        if (Objects.isNull(menu))
            throw BusinessException.build(100002, "指定菜单不存在或已被删除");
        return menuServiceBase.update(
                TMenu.builder()
                        .id(menu.getId())
                        .status(EYesNo.NO)
                        .build()
        );
    }

    @Override
    public boolean updateMenu(UpdateMenuVO vo, Long id) {
        // TODO 功能需要实现
        return false;
    }

    @Override
    public List<AdminMenuListResVO> userMenuLists() {
        Long userId = RequestContext.get().getUid();
        return menuServiceBase.findUserMenuTree(userId, null);
    }

    @Override
    public List<AdminMenuListResVO> systemMenuLists() {
        return menuServiceBase.findUserMenuTree(null, null);
    }
}
