package com.artino.service.services.impl;

import com.artino.service.base.BusinessException;
import com.artino.service.common.EDeleted;
import com.artino.service.common.PageRes;
import com.artino.service.context.RequestContext;
import com.artino.service.dto.role.NewRoleDTO;
import com.artino.service.dto.role.RoleListDTO;
import com.artino.service.entity.TConfig;
import com.artino.service.entity.TMenu;
import com.artino.service.entity.TRole;
import com.artino.service.entity.TRoleMenu;
import com.artino.service.services.IRoleService;
import com.artino.service.services.base.ConfigServiceBase;
import com.artino.service.services.base.MenuServiceBase;
import com.artino.service.services.base.RoleServiceBase;
import com.artino.service.utils.CopyUtils;
import com.artino.service.utils.DateUtils;
import com.artino.service.utils.IDUtils;
import com.artino.service.utils.StringUtils;
import com.artino.service.vo.role.res.RoleListResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    @Lazy
    private RoleServiceBase roleServiceBase;

    @Autowired
    @Lazy
    private ConfigServiceBase configServiceBase;

    @Autowired
    @Lazy
    private MenuServiceBase menuServiceBase;

    @Override
    public boolean newRole(NewRoleDTO dto) {
        Long uid = RequestContext.get().getUid();
        roleServiceBase.ensureIsAdmin(uid);
        TRole role = TRole.builder()
                .id(IDUtils.shared().nextId())
                .name(dto.getName())
                .description(dto.getDescription())
                .sort(roleServiceBase.findMaxSort())
                .createdAt(DateUtils.getTime())
                .build();
        return roleServiceBase.insert(role);
    }

    @Override
    public boolean updateRole(Long id, NewRoleDTO dto) {
        if (StringUtils.isEmpty(dto.getName()) && StringUtils.isEmpty(dto.getDescription()))
            throw BusinessException.build(110000, "请检查入参");
        Long uid = RequestContext.get().getUid();
        roleServiceBase.ensureIsAdmin(uid);
        TRole role = roleServiceBase.findById(id);
        if (Objects.isNull(role)) throw BusinessException.build(110001, "角色不存在或已删除");
        List<TConfig> systemRoles = configServiceBase.findSystemRole();
        if (systemRoles.stream().anyMatch(e -> e.getId().equals(id)))
            throw BusinessException.build(110002, "此角色不可修改");
        TRole upt = TRole.builder().id(id).build();
        if (Objects.nonNull(dto.getName())) upt.setName(dto.getName());
        if (Objects.nonNull(dto.getDescription())) upt.setDescription(dto.getDescription());
        return roleServiceBase.update(upt);
    }

    @Override
    public boolean deleteRole(Long id) {
        Long uid = RequestContext.get().getUid();
        roleServiceBase.ensureIsAdmin(uid);
        TRole role = roleServiceBase.findById(id);
        if (Objects.isNull(role)) throw BusinessException.build(110001, "角色不存在或已删除");
        List<TConfig> systemRoles = configServiceBase.findSystemRole();
        if (systemRoles.stream().anyMatch(e -> e.getId().equals(id)))
            throw BusinessException.build(110002, "此角色不可删除");
        return roleServiceBase.update(
                TRole.builder()
                        .id(id)
                        .deleted(EDeleted.YES)
                        .build()
        );
    }

    @Override
    public List<RoleListResVO> roleList(RoleListDTO dto) {
        List<TRole> roles = roleServiceBase.findWithPage(dto);
        List<TConfig> systemRoles = configServiceBase.findSystemRole();
        List<Long> systemRoleIds = systemRoles.stream().map(TConfig::getValue).toList();
        boolean permission = Objects.nonNull(dto) && dto.isShowPermission();
        Long adminId = configServiceBase.getAdminId(systemRoles);
        HashMap<Long, List<String>> hashMap;
        if (permission) {
            boolean hasAdminId = roles.stream().anyMatch(e -> e.getId().equals(adminId));
            List<Long> roleListIds = roles.stream().map(TRole::getId).toList();
            List<TRoleMenu> roleMenuList = menuServiceBase.findRoleMenuWith(roleListIds);
            List<TMenu> list;
            if (hasAdminId) {
                list = menuServiceBase.findMenuListByAdmin(TMenu.EType.MENU);
            } else {
                List<Long> menuIds = roleMenuList.stream().map(TRoleMenu::getMenuId).toList();
                list = menuServiceBase.findListBy(menuIds, TMenu.EType.MENU);
            }
            Map<Long, List<TMenu>> map = list.stream().collect(Collectors.groupingBy(TMenu::getId));
            hashMap = new HashMap<>();
            for (TRoleMenu item : roleMenuList) {
                Long key = item.getRoleId();
                if (!hashMap.containsKey(key)) hashMap.put(key, new ArrayList<String>());
                String name = map.get(item.getMenuId()).get(0).getName();
                hashMap.get(key).add(name);
            }
            if (hasAdminId) hashMap.put(adminId, list.stream().map(TMenu::getName).toList());
        } else {
            hashMap = null;
        }
        return roles.stream().map(e -> {
            RoleListResVO vo = CopyUtils.copy(e, RoleListResVO.class);
            if (Objects.isNull(vo)) return null;
            vo.setSystem(systemRoleIds.contains(e.getId()));
            if (dto.isShowPermission() && Objects.nonNull(hashMap)) {
                if (hashMap.containsKey(vo.getId()))
                    vo.setPermissions(String.join(",", hashMap.get(vo.getId())));
            }
            return vo;
        }).toList();
    }

    @Override
    public PageRes<RoleListResVO> roleListPage(RoleListDTO dto) {
        List<RoleListResVO> data = roleList(dto);
        Long total = roleServiceBase.findTotal(dto);
        return PageRes.build(data, total, dto);
    }
}
