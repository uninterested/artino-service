package com.artino.service.services.impl;

import com.artino.service.base.BusinessException;
import com.artino.service.common.EDeleted;
import com.artino.service.context.RequestContext;
import com.artino.service.dto.role.NewRoleDTO;
import com.artino.service.entity.TConfig;
import com.artino.service.entity.TRole;
import com.artino.service.services.IRoleService;
import com.artino.service.services.base.ConfigServiceBase;
import com.artino.service.services.base.RoleServiceBase;
import com.artino.service.utils.DateUtils;
import com.artino.service.utils.IDUtils;
import com.artino.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    @Lazy
    private RoleServiceBase roleServiceBase;

    @Autowired
    @Lazy
    private ConfigServiceBase configServiceBase;

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
}
