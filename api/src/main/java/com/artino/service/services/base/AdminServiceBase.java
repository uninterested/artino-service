package com.artino.service.services.base;


import com.artino.service.base.BusinessException;
import com.artino.service.entity.TAdmin;
import com.artino.service.entity.TAdminRole;
import com.artino.service.mapper.AdminMapper;
import com.artino.service.utils.*;
import com.artino.service.vo.admin.res.AdminLoginResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class AdminServiceBase {
    @Autowired
    @Lazy
    private AdminMapper adminMapper;

    /**
     * 新增管理员
     * @param admin model
     * @return 是否新增成功
     */
    public boolean newAdmin(TAdmin admin) {
        return adminMapper.insert(admin) > 0;
    }

    /**
     * 检测帐户是否被注册
     *
     * @param account 账号
     * @return 帐户是否被注册
     */
    public boolean checkAdminExists(String account) {
        boolean isEmail = RegexUtils.isEmail(account);
        if (isEmail) return Objects.nonNull(adminMapper.findByEmail(account));
        else return Objects.nonNull(adminMapper.findByPhone(account));
    }

    /**
     * 删除用户的权限
     * @param userId 用户id
     * @return is ok
     */
    public boolean deleteListByUserId(Long userId) {
        if (Objects.isNull(userId) || userId <=0) return false;
        return adminMapper.deleteListByUserId(userId) > 0;
    }

    /**
     * 批量插入用户-角色数
     * @param list list
     * @return is ok
     */
    public boolean batchInsertAdminRole(List<TAdminRole> list) {
        if (Objects.isNull(list) || list.isEmpty()) return false;
        return adminMapper.batchInsertAdminRole(list) > 0;
    }

    /**
     * 根据账号查询管理员
     *
     * @param account 账号
     * @return 账号信息
     */
    public TAdmin getAdminInfo(String account) {
        boolean isEmail = RegexUtils.isEmail(account);
        if (isEmail) return getAdminByEmail(account);
        return getAdminByPhone(account);
    }

    /**
     * 根据id获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    public TAdmin getAdminById(Long id) {
        if (Objects.isNull(id) || id.equals(0L)) return null;
        String key = KeyUtils.getUserKey(id);
        TAdmin admin = RedisUtils.get(key, TAdmin.class);
        if (Objects.isNull(admin)) {
            admin = adminMapper.findById(id);
            syncToRedis(admin);
        }
        return admin;
    }

    /**
     * 根据手机号获取用户的信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    public TAdmin getAdminByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) return null;
        TAdmin admin = adminMapper.findByPhone(phone);
        syncToRedis(admin);
        return admin;
    }

    /**
     * 根据邮箱获取用户的信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    public TAdmin getAdminByEmail(String email) {
        if (StringUtils.isEmpty(email)) return null;
        TAdmin admin = adminMapper.findByEmail(email);
        syncToRedis(admin);
        return admin;
    }

    /**
     * 同步至redis
     *
     * @param admin admin model
     */
    public void syncToRedis(TAdmin admin) {
        if (Objects.nonNull(admin)) {
            String key = KeyUtils.getUserKey(admin.getId());
            RedisUtils.set(key, admin);
        }
    }


    /**
     * 检测账号是不是合法的
     *
     * @param account 账号
     */
    public void accountIsOk(String account) {
        boolean isEmail = RegexUtils.isEmail(account);
        boolean isPhone = RegexUtils.isPhone(account);
        if (!isEmail && !isPhone)
            throw BusinessException.build(100002, "请输入邮箱或者手机号");
    }


    /**
     * 格式化返回的数据格式
     *
     * @param admin   admin model
     * @param lockKey 锁定的key
     * @return 登录信息
     */
    public AdminLoginResVO processResult(TAdmin admin, String lockKey) {
        syncToRedis(admin);
        if (Objects.nonNull(lockKey)) RedisUtils.del(lockKey);
        loginOutIfNeed(admin);
        syncTokenToHeader(admin);
        return CopyUtils.copy(admin, AdminLoginResVO.class);
    }

    // ======= private =======

    /**
     * 设置token至Response的Header
     *
     * @param admin admin model
     */
    private void syncTokenToHeader(TAdmin admin) {
        String token = CryptoUtils.base64Encode(RandomUtils.uuid());
        Environment env = SpringUtils.getBean(Environment.class);
        String tokenName = env.getProperty("constant.login.token", "X-Token");
        ServletUtils.currentResponse().setHeader(tokenName, token);
        RedisUtils.set(KeyUtils.getTokenKey(token), admin.getId(), 180, TimeUnit.DAYS);
    }

    /**
     * 单点登录的时候，退出已经登录的账号
     *
     * @param admin admin model
     */
    private void loginOutIfNeed(TAdmin admin) {
        Environment env = SpringUtils.getBean(Environment.class);
        String single = env.getProperty("constant.login.single", "false");
        if (!"true".equals(single)) return;
        List<String> keys = RedisUtils.scan("token.*");
        if (keys.isEmpty()) return;
        List<Long> ids = RedisUtils.mget(keys, Long.class);
        if (ids.isEmpty()) return;
        List<String> waitRemove = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++)
            if (ids.get(i).equals(admin.getId()))
                waitRemove.add(keys.get(i));
        if (!waitRemove.isEmpty()) RedisUtils.del(waitRemove);
    }
}
