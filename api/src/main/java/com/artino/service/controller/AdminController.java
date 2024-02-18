package com.artino.service.controller;

import com.artino.service.annotation.LoginRequired;
import com.artino.service.base.R;
import com.artino.service.dto.admin.AdminCodeLoginDTO;
import com.artino.service.dto.admin.AdminCreateDTO;
import com.artino.service.dto.admin.AdminLoginDTO;
import com.artino.service.services.IAdminService;
import com.artino.service.validator.required.Required;
import com.artino.service.vo.admin.req.AdminCreateVO;
import com.artino.service.vo.admin.req.AdminLoginVO;
import com.artino.service.vo.admin.req.CodeLoginVO;
import com.artino.service.vo.admin.res.AdminLoginResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

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
    public R<?> create(@Valid @RequestBody AdminCreateVO vo) {
        AdminCreateDTO dto = AdminCreateDTO.builder()
                .account(vo.getAccount())
                .code(vo.getCode())
                .password(vo.getPassword())
                .nickName(vo.getNickName())
                .build();
        boolean isOk = adminService.adminCreate(dto);
        if (isOk) return R.success();
        return R.error(120002, "创建用户失败");
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public R<AdminLoginResVO> login(@Valid @RequestBody AdminLoginVO vo) {
        AdminLoginDTO dto = AdminLoginDTO.builder()
                .account(vo.getAccount())
                .password(vo.getPassword())
                .build();
        AdminLoginResVO resVo = adminService.adminLogin(dto);
        return R.success(resVo);
    }

    @GetMapping("sync")
    @ApiOperation("同步用户信息")
    @LoginRequired(type = LoginRequired.UserType.ADMIN)
    public R<AdminLoginResVO> sync() {
        AdminLoginResVO resVo = adminService.sync();
        return R.success(resVo);
    }

    @PostMapping("/code-login")
    @ApiOperation("验证码登录")
    public R<AdminLoginResVO> codeLogin(@Valid @RequestBody CodeLoginVO vo) {
        AdminCodeLoginDTO dto = AdminCodeLoginDTO.builder()
                .account(vo.getAccount())
                .code(vo.getCode())
                .build();
        AdminLoginResVO resVo = adminService.codeLogin(dto);
        return R.success(resVo);
    }

    @GetMapping("/out")
    @ApiOperation("账号退出")
    @LoginRequired(type = LoginRequired.UserType.ADMIN)
    public R<?> out() {
        boolean isOK = adminService.out();
        if (isOK) return R.success();
        return R.error(120002, "账号退出失败");
    }
}
