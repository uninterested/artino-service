package com.artino.service.controller;

import com.artino.service.annotation.LoginRequired;
import com.artino.service.base.R;
import com.artino.service.dto.role.NewRoleDTO;
import com.artino.service.services.IRoleService;
import com.artino.service.services.impl.RoleServiceImpl;
import com.artino.service.vo.role.req.EditRoleVO;
import com.artino.service.vo.role.req.NewRoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/role")
@Api(value = "RoleController", tags = {"角色模块"})
public class RoleController {

    @Autowired
    @Lazy
    private IRoleService roleService;

    @PostMapping("")
    @ApiOperation("创建角色")
    @LoginRequired(type = LoginRequired.UserType.ADMIN)
    public R<?> newRole(@Valid @RequestBody NewRoleVO vo) {
        NewRoleDTO dto = NewRoleDTO.builder()
                .name(vo.getName())
                .description(vo.getDescription())
                .build();
        boolean isOK = roleService.newRole(dto);
        if (isOK) return R.success();
        return R.error(120001, "创建角色失败");
    }

    @PutMapping("/{id}")
    @ApiOperation("修改角色信息")
    @LoginRequired(type = LoginRequired.UserType.ADMIN)
    public R<?> editRole(@Valid @RequestBody EditRoleVO vo, @PathVariable Long id) {
        NewRoleDTO dto = NewRoleDTO.builder()
                .name(vo.getName())
                .description(vo.getDescription())
                .build();
        boolean isOK = roleService.updateRole(id, dto);
        if (isOK) return R.success();
        return R.error(120001, "修改角色失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除角色信息")
    @LoginRequired(type = LoginRequired.UserType.ADMIN)
    public R<?> deleteRole(@PathVariable Long id) {
        boolean isOK = roleService.deleteRole(id);
        if (isOK) return R.success();
        return R.error(120001, "删除角色失败");
    }

    @GetMapping("/list")
    @ApiOperation("获取所有的角色的列表")
    public R<?> roleList() {
        return null;
    }
}
