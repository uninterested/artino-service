package com.artino.service.services.impl;

import com.artino.service.base.BusinessException;
import com.artino.service.context.RequestContext;
import com.artino.service.dto.admin.*;
import com.artino.service.entity.*;
import com.artino.service.services.IAdminService;
import com.artino.service.services.base.*;
import com.artino.service.utils.*;
import com.artino.service.vo.admin.res.AdminLoginResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    @Lazy
    private RoleServiceBase roleServiceBase;

    @Autowired
    @Lazy
    private ConfigServiceBase configServiceBase;

    @Override
    @Transactional
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
                .nickName(StringUtils.isEmpty(dto.getNickName()) ? RandomUtils.getRandomName(0) : dto.getNickName())
                .openId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                .unionId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                .createdAt(DateUtils.getTime())
                .createdBy(Objects.nonNull(RequestContext.get()) ? RequestContext.get().getUid() : 0L)
                .build();
        boolean isEmail = RegexUtils.isEmail(dto.getAccount());
        if (isEmail) admin.setEmail(dto.getAccount());
        else admin.setPhone(dto.getAccount());
        admin.setPassword(CryptoUtils.encryptPassword(dto.getPassword(), admin.getId()));
        adminServiceBase.saveAdminAndRoleAndMini(admin);
        return true;
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
    @Transactional
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
                    .nickName(RandomUtils.getRandomName(0))
                    .openId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                    .unionId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                    .createdAt(DateUtils.getTime())
                    .createdBy(0L)
                    .build();
            boolean isEmail = RegexUtils.isEmail(dto.getAccount());
            if (isEmail) admin.setEmail(dto.getAccount());
            else admin.setPhone(dto.getAccount());
            admin.setPassword(CryptoUtils.encryptPassword(RandomUtils.randomStr(10), admin.getId()));
            adminServiceBase.saveAdminAndRoleAndMini(admin);
            admin = adminServiceBase.getAdminById(admin.getId());
        }
        if (admin.getStatus() == TAdmin.EStatus.FREEZE)
            throw BusinessException.build(110002, "帐户已被冻结");
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

    @Override
    @Transactional
    public boolean setRole(SetRoleDTO dto) {
        if (!dto.isAdmin() && dto.getRoleIds().isEmpty())
            throw BusinessException.build(110001, "请检查入参");
        Long userId = RequestContext.get().getUid();
        if (userId.equals(dto.getUserId()))
            throw BusinessException.build(110002, "不能为自己设置角色");
        TAdmin user = adminServiceBase.getAdminById(dto.getUserId());
        if (Objects.isNull(user))
            throw BusinessException.build(110003, "指定用户不存在");
        if (!dto.isAdmin()) {
            if (dto.getRoleIds().isEmpty())
                throw BusinessException.build(110003, "请检查入参");
            List<TRole> roles = roleServiceBase.findLists(dto.getRoleIds());
            if (roles.size() != dto.getRoleIds().size())
                throw BusinessException.build(110003, "存在无效的角色");
        }
        adminServiceBase.deleteListByUserId(dto.getUserId());
        if (dto.isAdmin()) {
            Long adminId = configServiceBase.findAdmin().getValue();
            TAdminRole item = TAdminRole.builder()
                    .adminId(dto.getUserId())
                    .adminId(adminId).build();
            return adminServiceBase.batchInsertAdminRole(List.of(item));
        } else {
            List<TAdminRole> list = dto.getRoleIds().stream().map(e ->
                    TAdminRole.builder().adminId(dto.getUserId()).roleId(e).build()
            ).toList();
            return adminServiceBase.batchInsertAdminRole(list);
        }
    }

    @Override
    public String newQrcode(QRCodeDTO.EType type) {
        int minute = configServiceBase.findConfig(
                TConfig.builder()
                        .label(ConfigServiceBase.KQRCODEEXIPRED)
                        .type(TConfig.EType.QRCODE)
                        .build()
        ).getValue().intValue();
        Long expiredAt = DateUtils.after(minute).getTime();
        QRCodeDTO dto = QRCodeDTO.builder()
                .expiredAt(expiredAt)
                .token(RandomUtils.uuid())
                .type(type)
                .build();
        Environment env = SpringUtils.getBean(Environment.class);
        String key = env.getProperty("constant.verify.key", "");
        RedisUtils.set(KeyUtils.getCodeKey(dto.getToken()), dto, (minute + 1), TimeUnit.MINUTES);
        return CryptoUtils.desEncode(JSON.stringify(dto), key);
    }

    @Override
    public Object scanInfo(String token) {
        String key = KeyUtils.getCodeKey(token);
        QRCodeDTO codeInfo = RedisUtils.get(key, QRCodeDTO.class);
        if (Objects.isNull(codeInfo))
            throw BusinessException.build(110001, "二维码信息不存在");
        if (codeInfo.getExpiredAt() < DateUtils.timeSpan())
            throw BusinessException.build(110002, "二维码已过期");
        if (Objects.isNull(codeInfo.getData())) return null;
        if (codeInfo.getType() == QRCodeDTO.EType.SWITCHADMIN) {
            Long adminId = Long.parseLong((String) codeInfo.getData());
            TAdmin admin = adminServiceBase.getAdminById(adminId);
            if (admin.getStatus() == TAdmin.EStatus.FREEZE)
                throw BusinessException.build(110003, "帐户已被冻结");
            return adminServiceBase.processResult(admin, null);
        }
        throw BusinessException.build(110009, "无法处理此数据");
    }
}
