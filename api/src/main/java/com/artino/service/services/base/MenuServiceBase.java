package com.artino.service.services.base;

import com.artino.service.common.EYesNo;
import com.artino.service.entity.TMenu;
import com.artino.service.entity.TRoleMenu;
import com.artino.service.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MenuServiceBase {

    @Autowired
    @Lazy
    private MenuMapper menuMapper;

    public List<TMenu> findMenuListByAdmin(TMenu.EType type) {
        TMenu dto = TMenu.builder()
                .status(EYesNo.YES)
                .build();
        if (Objects.nonNull(type)) dto.setType(type);
        return findList(dto);
    }

    /**
     * 获取角色拥有的菜单列表
     * @param roleIds 角色列表
     * @return 菜单列表
     */
    public  List<TRoleMenu> findRoleMenuWith(List<Long> roleIds) {
        return menuMapper.findRoleMenuWith(roleIds);
    }

    public List<TMenu> findListBy(List<Long> ids, TMenu.EType type) {
        List<TMenu> list = menuMapper.findListWith(ids);
        if (Objects.isNull(type)) return list;
        return list.stream().filter(e -> e.getType().equals(type)).toList();
    }

    public List<TMenu> findList(TMenu entity) {
        return menuMapper.findList(entity);
    }
}
