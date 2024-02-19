package com.artino.service.services.impl;

import com.artino.service.base.BusinessException;
import com.artino.service.dto.code.SendCodeDTO;
import com.artino.service.entity.TCode;
import com.artino.service.services.ICodeService;
import com.artino.service.services.base.AdminServiceBase;
import com.artino.service.services.base.CodeServiceBase;
import com.artino.service.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.TimerTask;

@Service
public class CodeServiceImpl implements ICodeService {


    @Value("${constant.code.verify}")
    private int verify;
    @Value("${constant.code.expired}")
    private int expired;

    @Autowired
    @Lazy
    private AdminServiceBase adminServiceBase;

    @Autowired
    @Lazy
    private CodeServiceBase codeServiceBase;

    @Override
    public boolean sendCode(SendCodeDTO dto) {
        boolean isPhone = RegexUtils.isPhone(dto.getAccount());
        boolean isEmail = RegexUtils.isEmail(dto.getAccount());
        if (!isPhone && !isEmail)
            throw BusinessException.build(110002, "请输入邮箱或者手机号");
        TCode entity = codeServiceBase.findCodeBy(dto.getAccount(), dto.getType());
        if (Objects.nonNull(entity)) {
            long now = DateUtils.timeSpan();
            long expiredAt = DateUtils.parseDate(entity.getExpiredAt()).getTime();
            if (expiredAt > now && entity.getVerified() <= expired)
                throw BusinessException.build(110003, "验证码发送频繁，稍后再试");
        }
        if (dto.getType() == TCode.EType.REGISTER) { // 注册
            if (adminServiceBase.checkAdminExists(dto.getAccount()))
                throw BusinessException.build(110004, "帐户已被注册");
        } else if (dto.getType() == TCode.EType.RESETPASSWORD) { // 重置密码
            if (!adminServiceBase.checkAdminExists(dto.getAccount()))
                throw BusinessException.build(110005, "帐户还未注册，请先注册");
        }
        String now = DateUtils.getTime();
        TCode code = TCode.builder()
                .id(IDUtils.shared().nextId())
                .account(dto.getAccount())
                .type(dto.getType())
                .code(RandomUtils.randomStr(6, true).toUpperCase())
                .createdAt(now)
                .expiredAt(DateUtils.parseDateToStr(null, DateUtils.after(now, expired)))
                .build();
        boolean isOK = codeServiceBase.newCode(code);
        TaskUtils.beginTask(100).start(new TimerTask() {
            @Override
            public void run() {
                MessageUtils.sendMessage(dto.getAccount(), TempleteUtils.codeTemplete, code.getCode());
            }
        });
        return isOK;
    }
}
