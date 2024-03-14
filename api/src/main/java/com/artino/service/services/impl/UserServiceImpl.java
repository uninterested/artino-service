package com.artino.service.services.impl;

import com.artino.service.base.BusinessException;
import com.artino.service.context.RequestContext;
import com.artino.service.dto.admin.QRCodeDTO;
import com.artino.service.dto.user.*;
import com.artino.service.entity.TAdmin;
import com.artino.service.entity.TCode;
import com.artino.service.entity.TUser;
import com.artino.service.entity.TUserAdmin;
import com.artino.service.services.IUserService;
import com.artino.service.services.base.AdminServiceBase;
import com.artino.service.services.base.CodeServiceBase;
import com.artino.service.services.base.UserServiceBase;
import com.artino.service.utils.*;
import com.artino.service.vo.admin.res.AdminLoginResVO;
import com.artino.service.vo.user.res.AdminsResVO;
import com.artino.service.vo.user.res.UserLoginResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    @Lazy
    private AdminServiceBase adminServiceBase;

    @Autowired
    @Lazy
    private CodeServiceBase codeServiceBase;

    @Autowired
    @Lazy
    private UserServiceBase userServiceBase;

    @Override
    public boolean newUser(NewUserDTO dto) {
        adminServiceBase.accountIsOk(dto.getAccount());
        TCode code = TCode.builder()
                .account(dto.getAccount())
                .type(TCode.EType.REGISTER)
                .code(dto.getCode())
                .build();
        codeServiceBase.verifyCode(code);
        boolean userExists = userServiceBase.checkUserExists(dto.getAccount());
        if (userExists) throw BusinessException.build(110003, "账号已被注册");
        TUser user = TUser.builder()
                .id(IDUtils.shared().nextId())
                .nickName(StringUtils.isEmpty(dto.getNickName()) ? RandomUtils.getRandomName(0) : dto.getNickName())
                .openId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                .unionId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                .createdAt(DateUtils.getTime())
                .build();
        boolean isEmail = RegexUtils.isEmail(dto.getAccount());
        if (isEmail) user.setEmail(dto.getAccount());
        else user.setPhone(dto.getAccount());
        user.setPassword(CryptoUtils.encryptPassword(dto.getPassword(), user.getId()));
        return userServiceBase.insert(user);
    }

    @Override
    public UserLoginResVO userLogin(UserLoginDTO dto) {
        adminServiceBase.accountIsOk(dto.getAccount());
        String lockKey = KeyUtils.getAccountLock(dto.getAccount());
        Long count = RedisUtils.get(lockKey, Long.class);
        int errorTimes = Objects.isNull(count) ? 0 : count.intValue();
        Environment env = SpringUtils.getBean(Environment.class);
        int maxTimes = Integer.parseInt(env.getProperty("constant.login.times", "5"));
        if (errorTimes > maxTimes)
            throw BusinessException.build(110001, "账号已被锁定，请稍后再试");
        TUser user = userServiceBase.getUserInfo(dto.getAccount());
        if (Objects.isNull(user) || !user.getPassword().equals(CryptoUtils.encryptPassword(dto.getPassword(), user.getId()))) {
            RedisUtils.inc(lockKey, 30, TimeUnit.MINUTES);
            int limit = maxTimes - errorTimes - 1;
            throw BusinessException.build(110002, limit <= 0 ? "账户已被锁定，请30分钟后再试" : String.format("帐号密码不匹配，还剩%d次机会", limit));
        }
        if (user.getStatus() == TAdmin.EStatus.FREEZE)
            throw BusinessException.build(110003, "帐户已被冻结");
        return userServiceBase.processResult(user, lockKey);
    }

    @Override
    public UserLoginResVO sync() {
        Long uid = RequestContext.get().getUid();
        TUser admin = userServiceBase.getUserById(uid);
        if (admin.getStatus() == TAdmin.EStatus.FREEZE)
            throw BusinessException.build(110001, "帐户已被冻结");
        return CopyUtils.copy(admin, UserLoginResVO.class);
    }

    @Override
    public UserLoginResVO codeLogin(UserCodeLoginDTO dto) {
        adminServiceBase.accountIsOk(dto.getAccount());
        TCode code = TCode.builder()
                .account(dto.getAccount())
                .type(TCode.EType.FASTLOGIN)
                .code(dto.getCode())
                .build();
        codeServiceBase.verifyCode(code);
        TUser user = userServiceBase.getUserInfo(dto.getAccount());
        if (Objects.isNull(user)) {
            user = TUser.builder()
                    .id(IDUtils.shared().nextId())
                    .nickName(RandomUtils.getRandomName(0))
                    .openId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                    .unionId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                    .createdAt(DateUtils.getTime())
                    .build();
            boolean isEmail = RegexUtils.isEmail(dto.getAccount());
            if (isEmail) user.setEmail(dto.getAccount());
            else user.setPhone(dto.getAccount());
            user.setPassword(CryptoUtils.encryptPassword(RandomUtils.randomStr(10), user.getId()));
            boolean isOk = userServiceBase.insert(user);
            if (!isOk) throw BusinessException.build(110001, "验证码登录失败，请重试");
            user = userServiceBase.getUserById(user.getId());
        }
        if (user.getStatus() == TAdmin.EStatus.FREEZE)
            throw BusinessException.build(110002, "帐户已被冻结");
        return userServiceBase.processResult(user, null);
    }

    @Override
    public boolean out() {
        Environment env = SpringUtils.getBean(Environment.class);
        String tokenKey = env.getProperty("constant.login.token", "X-Token");
        String token = ServletUtils.currentRequest().getHeader(tokenKey);
        if (StringUtils.isNotEmpty(token)) RedisUtils.del(KeyUtils.getUserTokenKey(token));
        return true;
    }

    @Override
    @Transactional
    public List<AdminsResVO> admins() {
        Long userId = RequestContext.get().getUid();
        TUser user = userServiceBase.getUserById(userId);
        if (Objects.isNull(user) || user.getStatus() == TAdmin.EStatus.FREEZE)
            throw BusinessException.build(110001, "用户不存在或已冻结");
        List<UserAdminMiniDTO> list = userServiceBase.findUserAdminMini(userId);
        if (Objects.isNull(list) || list.isEmpty()) {
            TAdmin admin = TAdmin.builder()
                    .id(IDUtils.shared().nextId())
                    .nickName(user.getNickName())
                    .avatar(user.getAvatar())
                    .sex(user.getSex())
                    .openId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                    .unionId(CryptoUtils.base64Encode(RandomUtils.uuid()))
                    .createdAt(DateUtils.getTime())
                    .createdBy(0L)
                    .build();
            if (StringUtils.isNotEmpty(user.getPhone())) {
                TAdmin find = adminServiceBase.getAdminByPhone(user.getPhone());
                if (Objects.isNull(find)) admin.setPhone(user.getPhone());
            }
            if (StringUtils.isNotEmpty(user.getEmail())) {
                TAdmin find = adminServiceBase.getAdminByEmail(user.getEmail());
                if (Objects.isNull(find)) admin.setEmail(user.getEmail());
            }
            adminServiceBase.saveAdminAndRoleAndMini(admin);
            List<TUserAdmin> relationship = List.of(
                    TUserAdmin.builder()
                            .userId(userId)
                            .adminId(admin.getId())
                            .build()
            );
            userServiceBase.batchInsertUserAdmin(relationship);
            list = userServiceBase.findUserAdminMini(userId);
        }
        return list.stream().map(e -> CopyUtils.copy(e, AdminsResVO.class)).toList();
    }

    @Override
    public boolean scanQRCode(ScanQRCodeDTO dto) {
        String key = KeyUtils.getCodeKey(dto.getToken());
        QRCodeDTO qrcode = RedisUtils.get(key, QRCodeDTO.class);
        if (Objects.isNull(qrcode) || qrcode.getExpiredAt() < DateUtils.timeSpan())
            throw BusinessException.build(110001, "验证码不存在或者已过期");
        qrcode.setData(dto.getData());
        RedisUtils.set(key, qrcode);
        return true;
    }
}
