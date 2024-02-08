package com.artino.service.controller;

import com.artino.service.base.R;
import com.artino.service.services.IAdminService;
import com.artino.service.vo.admin.req.CreateAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@Api(value = "AdminController", tags = {"管理员用户模块"})
public class AdminController {
    @Autowired
    @Lazy
    private IAdminService adminService;

    @PostMapping("")
    @ApiOperation("创建用户")
    public R<?> create(@Valid @RequestBody CreateAdminVO vo) {
        return R.success();
    }
}
