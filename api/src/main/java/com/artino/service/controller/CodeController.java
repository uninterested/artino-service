package com.artino.service.controller;


import com.artino.service.annotation.Frequency;
import com.artino.service.base.R;
import com.artino.service.dto.code.SendCodeDTO;
import com.artino.service.services.ICodeService;
import com.artino.service.vo.code.req.SendCodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/code")
@Api(value = "CodeController", tags = {"验证码模块"})
public class CodeController {

    @Autowired
    @Lazy
    private ICodeService codeService;

    @PostMapping("/send")
    @ApiOperation("发送验证码")
    @Frequency(type = Frequency.Type.FP, time = 60, count = 5)
    public R<?> sendCode(@Valid @RequestBody SendCodeVO vo) {
        boolean ok = codeService.sendCode(
                new SendCodeDTO(vo.getAccount(), vo.getType())
        );
        if (ok) return R.success();
        return R.error(100001, "发送验证码失败");
    }
}
