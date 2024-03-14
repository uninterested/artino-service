package com.artino.service.services.base;

import com.artino.service.dto.user.UserAdminMiniDTO;
import com.artino.service.entity.TUser;
import com.artino.service.entity.TUserAdmin;
import com.artino.service.mapper.UserMapper;
import com.artino.service.utils.*;
import com.artino.service.vo.user.res.AdminsResVO;
import com.artino.service.vo.user.res.UserLoginResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceBase {
    @Autowired
    @Lazy
    private UserMapper userMapper;


    /**
     * 新增用户
     *
     * @param entity
     * @return
     */
    public boolean insert(TUser entity) {
        return userMapper.insert(entity) > 0;
    }

    /**
     * 检测帐户是否被注册
     *
     * @param account 账号
     * @return 帐户是否被注册
     */
    public boolean checkUserExists(String account) {
        boolean isEmail = RegexUtils.isEmail(account);
        if (isEmail) return Objects.nonNull(userMapper.findByEmail(account));
        else return Objects.nonNull(userMapper.findByPhone(account));
    }

    /**
     * 获取当前用户关联的管理端
     *
     * @param userId 用户id
     * @return list
     */
    public List<UserAdminMiniDTO> findUserAdminMini(Long userId) {
        return userMapper.findUserAdminMini(userId);
    }

    /**
     * 同步至redis
     *
     * @param user admin model
     */
    public void syncToRedis(TUser user) {
        if (Objects.nonNull(user)) {
            String key = KeyUtils.getUserKey(user.getId());
            RedisUtils.set(key, user);
        }
    }

    /**
     * 根据手机号获取用户的信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    public TUser getUserByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) return null;
        TUser user = userMapper.findByPhone(phone);
        syncToRedis(user);
        return user;
    }

    /**
     * 根据邮箱获取用户的信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    public TUser getUserByEmail(String email) {
        if (StringUtils.isEmpty(email)) return null;
        TUser user = userMapper.findByEmail(email);
        syncToRedis(user);
        return user;
    }

    /**
     * 根据账号查询管理员
     *
     * @param account 账号
     * @return 账号信息
     */
    public TUser getUserInfo(String account) {
        boolean isEmail = RegexUtils.isEmail(account);
        if (isEmail) return getUserByEmail(account);
        return getUserByPhone(account);
    }

    /**
     * 批量插入用户-管理员关系
     * @param list list
     * @return 是否成功
     */
    public boolean batchInsertUserAdmin(List<TUserAdmin> list) {
        return userMapper.batchInsertUserAdmin(list) > 0;
    }

    /**
     * 根据id获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    public TUser getUserById(Long id) {
        if (Objects.isNull(id) || id.equals(0L)) return null;
        String key = KeyUtils.getUserKey(id);
        TUser user = RedisUtils.get(key, TUser.class);
        if (Objects.isNull(user)) {
            user = userMapper.findById(id);
            syncToRedis(user);
        }
        return user;
    }

    // ====== private =======

    /**
     * 格式化返回的数据格式
     *
     * @param user    user model
     * @param lockKey 锁定的key
     * @return 登录信息
     */
    public UserLoginResVO processResult(TUser user, String lockKey) {
        syncToRedis(user);
        if (Objects.nonNull(lockKey)) RedisUtils.del(lockKey);
        loginOutIfNeed(user);
        syncTokenToHeader(user);
        return CopyUtils.copy(user, UserLoginResVO.class);
    }


    // ======= private =======

    /**
     * 设置token至Response的Header
     *
     * @param user admin model
     */
    private void syncTokenToHeader(TUser user) {
        String token = CryptoUtils.base64Encode(RandomUtils.uuid());
        Environment env = SpringUtils.getBean(Environment.class);
        String tokenName = env.getProperty("constant.login.token", "X-Token");
        ServletUtils.currentResponse().setHeader(tokenName, token);
        RedisUtils.set(KeyUtils.getUserTokenKey(token), user.getId(), 180, TimeUnit.DAYS);
    }

    /**
     * 单点登录的时候，退出已经登录的账号
     *
     * @param user user model
     */
    private void loginOutIfNeed(TUser user) {
        Environment env = SpringUtils.getBean(Environment.class);
        String single = env.getProperty("constant.login.single", "false");
        if (!"true".equals(single)) return;
        List<String> keys = RedisUtils.scan("user.token.*");
        if (keys.isEmpty()) return;
        List<Long> ids = RedisUtils.mget(keys, Long.class);
        if (ids.isEmpty()) return;
        List<String> waitRemove = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++)
            if (ids.get(i).equals(user.getId()))
                waitRemove.add(keys.get(i));
        if (!waitRemove.isEmpty()) RedisUtils.del(waitRemove);
    }
}
