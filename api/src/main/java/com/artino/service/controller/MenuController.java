package com.artino.service.controller;

import com.artino.service.annotation.LoginRequired;
import com.artino.service.base.R;
import com.artino.service.common.PageRes;
import com.artino.service.dto.menu.MenuListDTO;
import com.artino.service.dto.menu.NewMenuDTO;
import com.artino.service.dto.menu.UpdateMenuDTO;
import com.artino.service.entity.TMenu;
import com.artino.service.services.IMenuService;
import com.artino.service.utils.CopyUtils;
import com.artino.service.vo.admin.res.AdminMenuListResVO;
import com.artino.service.vo.menu.req.MenuListVO;
import com.artino.service.vo.menu.req.NewMenuVO;
import com.artino.service.vo.menu.req.UpdateMenuVO;
import com.artino.service.vo.menu.res.MenuListResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/menu")
@Api(value = "MenuController", tags = {"菜单模块"})
public class MenuController {
    @Autowired
    @Lazy
    private IMenuService menuService;

    @PostMapping("")
    @ApiOperation("新增菜单")
    @LoginRequired(type = LoginRequired.UserType.ADMIN)
    public R<?> newMenu(@Valid @RequestBody NewMenuVO vo) {
        NewMenuDTO dto = CopyUtils.copy(vo, NewMenuDTO.class);
        boolean isOK = menuService.newMenu(dto);
        if (isOK) return R.success();
        return R.error(120001, "新增菜单失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除菜单")
    @LoginRequired(type = LoginRequired.UserType.ADMIN)
    public R<?> deleteMenu(@PathVariable Long id) {
        boolean isOK = menuService.deleteMenu(id);
        if (isOK) return R.success();
        return R.error(120001, "删除菜单失败");
    }

    @PutMapping("/{id}")
    @ApiOperation("更新菜单")
    @LoginRequired(type = LoginRequired.UserType.ADMIN)
    public R<?> updateMenu(@PathVariable Long id, @Valid @RequestBody UpdateMenuVO vo) {
        UpdateMenuDTO dto = CopyUtils.copy(vo, UpdateMenuDTO.class);
        boolean isOK = menuService.updateMenu(dto, id);
        if (isOK) return R.success();
        return R.error(120001, "更新菜单失败");
    }

    @GetMapping("/tree")
    @ApiOperation("获取当前登录用户可访问的菜单列表")
    @LoginRequired(type = LoginRequired.UserType.ADMIN)
    public R<List<AdminMenuListResVO>> menuTree() {
        List<AdminMenuListResVO> list = menuService.userMenuLists();
        return R.success(list);
    }

    @GetMapping("/system.tree")
    @ApiOperation("获取所有的可访问的菜单列表 - 树型")
    @LoginRequired(type = LoginRequired.UserType.ADMIN)
    public R<List<AdminMenuListResVO>> systemMenuList() {
        List<AdminMenuListResVO> list = menuService.systemMenuLists();
        return R.success(list);
    }

    @GetMapping("/list")
    @ApiOperation("获取所有的可访问的菜单列表")
    @LoginRequired(type = LoginRequired.UserType.ADMIN)
    public R<List<MenuListResVO>> menuList() {
        List<MenuListResVO> list = menuService.systemMenus(null);
        return R.success(list);
    }

    @PostMapping("/system.list")
    @ApiOperation("分页获取所有的可访问的菜单列表")
    @LoginRequired(type = LoginRequired.UserType.ADMIN)
    public R<PageRes<MenuListResVO>> menuPageList(@Valid @RequestBody MenuListVO vo) {
        MenuListDTO dto = CopyUtils.copy(vo, MenuListDTO.class);
        PageRes<MenuListResVO> result = menuService.systemMenusPage(dto);
        return R.success(result);
    }
}
