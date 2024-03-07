package com.artino.service.services.impl;

import com.artino.service.base.BusinessException;
import com.artino.service.context.RequestContext;
import com.artino.service.dto.admin.AdminCodeLoginDTO;
import com.artino.service.dto.admin.AdminCreateDTO;
import com.artino.service.dto.admin.AdminLoginDTO;
import com.artino.service.entity.TAdmin;
import com.artino.service.entity.TCode;
import com.artino.service.services.IAdminService;
import com.artino.service.services.base.AdminServiceBase;
import com.artino.service.services.base.CodeServiceBase;
import com.artino.service.utils.*;
import com.artino.service.vo.admin.res.AdminLoginResVO;
import com.artino.service.vo.admin.res.AdminMenuListResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    @Lazy
    private AdminServiceBase adminServiceBase;

    @Autowired
    @Lazy
    private CodeServiceBase codeServiceBase;

    @Override
    public boolean adminCreate(AdminCreateDTO dto) {
        adminServiceBase.accountIsOk(dto.getAccount());
        TCode code = TCode.builder()
                .account(dto.getAccount())
                .type(TCode.EType.REGISTER)
                .code(dto.getCode())
                .build();
        codeServiceBase.verifyCode(code);
        boolean adminExists = adminServiceBase.checkAdminExists(dto.getAccount());
        if (adminExists) throw BusinessException.build(110003, "账号已被注册");
        TAdmin admin = TAdmin.builder()
                .id(IDUtils.shared().nextId())
                .nickName(StringUtils.isEmpty(dto.getNickName()) ? RandomUtils.getChineseName(0) : dto.getNickName())
                .openId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                .unionId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                .createdAt(DateUtils.getTime())
                .createdBy(Objects.nonNull(RequestContext.get()) ? RequestContext.get().getUid() : 0L)
                .build();
        boolean isEmail = RegexUtils.isEmail(dto.getAccount());
        if (isEmail) admin.setEmail(dto.getAccount());
        else admin.setPhone(dto.getAccount());
        admin.setPassword(CryptoUtils.encryptPassword(dto.getPassword(), admin.getId()));
        return adminServiceBase.newAdmin(admin);
    }

    @Override
    public AdminLoginResVO adminLogin(AdminLoginDTO dto) {
        adminServiceBase.accountIsOk(dto.getAccount());
        String lockKey = KeyUtils.getAccountLock(dto.getAccount());
        Long count = RedisUtils.get(lockKey, Long.class);
        int errorTimes = Objects.isNull(count) ? 0 : count.intValue();
        Environment env = SpringUtils.getBean(Environment.class);
        int maxTimes = Integer.parseInt(env.getProperty("constant.login.times", "5"));
        if (errorTimes > maxTimes)
            throw BusinessException.build(110001, "账号已被锁定，请稍后再试");
        TAdmin admin = adminServiceBase.getAdminInfo(dto.getAccount());
        if (null == admin || !admin.getPassword().equals(CryptoUtils.encryptPassword(dto.getPassword(), admin.getId()))) {
            RedisUtils.inc(lockKey, 30, TimeUnit.MINUTES);
            int limit = maxTimes - errorTimes - 1;
            throw BusinessException.build(110002, limit <= 0 ? "账户已被锁定，请30分钟后再试" : String.format("帐号密码不匹配，还剩%d次机会", limit));
        }
        if (admin.getStatus() == TAdmin.EStatus.FREEZE)
            throw BusinessException.build(110003, "帐户已被冻结");
        return adminServiceBase.processResult(admin, lockKey);
    }

    @Override
    public AdminLoginResVO sync() {
        Long uid = RequestContext.get().getUid();
        TAdmin admin = adminServiceBase.getAdminById(uid);
        if (admin.getStatus() == TAdmin.EStatus.FREEZE)
            throw BusinessException.build(110001, "帐户已被冻结");
        return CopyUtils.copy(admin, AdminLoginResVO.class);
    }

    @Override
    public AdminLoginResVO codeLogin(AdminCodeLoginDTO dto) {
        adminServiceBase.accountIsOk(dto.getAccount());
        TCode code = TCode.builder()
                .account(dto.getAccount())
                .type(TCode.EType.FASTLOGIN)
                .code(dto.getCode())
                .build();
        codeServiceBase.verifyCode(code);
        TAdmin admin = adminServiceBase.getAdminInfo(dto.getAccount());
        if (Objects.isNull(admin)) {
            admin = TAdmin.builder()
                    .id(IDUtils.shared().nextId())
                    .nickName(RandomUtils.getChineseName(0))
                    .openId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                    .unionId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                    .createdAt(DateUtils.getTime())
                    .createdBy(0L)
                    .build();
            boolean isEmail = RegexUtils.isEmail(dto.getAccount());
            if (isEmail) admin.setEmail(dto.getAccount());
            else admin.setPhone(dto.getAccount());
            admin.setPassword(CryptoUtils.encryptPassword(RandomUtils.randomStr(10), admin.getId()));
            boolean isOk = adminServiceBase.newAdmin(admin);
            if (!isOk) throw BusinessException.build(110001, "验证码登录失败，请重试");
            admin = adminServiceBase.getAdminById(admin.getId());
        }
        if (admin.getStatus() == TAdmin.EStatus.FREEZE)
            throw BusinessException.build(110001, "帐户已被冻结");
        return adminServiceBase.processResult(admin, null);
    }

    @Override
    public boolean out() {
        Environment env = SpringUtils.getBean(Environment.class);
        String tokenKey = env.getProperty("constant.login.token", "X-Token");
        String token = ServletUtils.currentRequest().getHeader(tokenKey);
        if (StringUtils.isNotEmpty(token)) RedisUtils.del(KeyUtils.getTokenKey(token));
        return true;
    }
}
