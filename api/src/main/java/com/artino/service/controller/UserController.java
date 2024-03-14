package com.artino.service.controller;

import com.artino.service.annotation.LoginRequired;
import com.artino.service.base.R;
import com.artino.service.dto.user.NewUserDTO;
import com.artino.service.dto.user.ScanQRCodeDTO;
import com.artino.service.dto.user.UserCodeLoginDTO;
import com.artino.service.dto.user.UserLoginDTO;
import com.artino.service.services.IUserService;
import com.artino.service.vo.admin.res.AdminLoginResVO;
import com.artino.service.vo.user.req.CodeLoginVO;
import com.artino.service.vo.user.req.NewUserVO;
import com.artino.service.vo.user.req.ScanQRCodeVO;
import com.artino.service.vo.user.req.UserLoginVO;
import com.artino.service.vo.user.res.AdminsResVO;
import com.artino.service.vo.user.res.UserLoginResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(value = "UserController", tags = {"用户模块"})
public class UserController {
    @Autowired
    @Lazy
    private IUserService userService;

    @PostMapping("")
    @ApiOperation("创建用户")
    public R<?> newUser(@Valid @RequestBody NewUserVO vo) {
        NewUserDTO dto = NewUserDTO.builder()
                .nickName(vo.getNickName())
                .password(vo.getPassword())
                .account(vo.getAccount())
                .code(vo.getCode())
                .build();
        boolean isOK = userService.newUser(dto);
        if (isOK) return R.success();
        return R.error(120002, "创建用户失败");
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public R<UserLoginResVO> login(@Valid @RequestBody UserLoginVO vo) {
        UserLoginDTO dto = UserLoginDTO.builder()
                .account(vo.getAccount())
                .password(vo.getPassword())
                .build();
        UserLoginResVO result = userService.userLogin(dto);
        return R.success(result);
    }

    @GetMapping("/sync")
    @ApiOperation("同步用户信息")
    @LoginRequired(type = LoginRequired.UserType.USER)
    public R<UserLoginResVO> sync() {
        UserLoginResVO resVo = userService.sync();
        return R.success(resVo);
    }

    @PostMapping("/code.login")
    @ApiOperation("验证码登录")
    public R<UserLoginResVO> codeLogin(@Valid @RequestBody CodeLoginVO vo) {
        UserCodeLoginDTO dto = UserCodeLoginDTO.builder()
                .account(vo.getAccount())
                .code(vo.getCode())
                .build();
        UserLoginResVO result = userService.codeLogin(dto);
        return R.success(result);
    }

    @GetMapping("/out")
    @ApiOperation("账号退出")
    @LoginRequired(type = LoginRequired.UserType.USER)
    public R<?> out() {
        boolean isOK = userService.out();
        if (isOK) return R.success();
        return R.error(120002, "账号退出失败");
    }

    @GetMapping("/admins")
    @ApiOperation("获取当前用户关联的管理端")
    @LoginRequired(type = LoginRequired.UserType.USER)
    public R<List<AdminsResVO>> admins() {
        List<AdminsResVO> list = userService.admins();
        return R.success(list);
    }

    @PostMapping("/scan.qrcode")
    @ApiOperation("用户端扫描了二维码")
    @LoginRequired(type = LoginRequired.UserType.USER)
    public R<?> scanQRCode(@Valid @RequestBody ScanQRCodeVO vo) {
        ScanQRCodeDTO dto = ScanQRCodeDTO.builder()
                .data(vo.getData())
                .token(vo.getToken())
                .build();
        boolean isOK = userService.scanQRCode(dto);
        if (isOK) return R.success();
        return R.error(120001, "扫描二维码失败");
    }
}
