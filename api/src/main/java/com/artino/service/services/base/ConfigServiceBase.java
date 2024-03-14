package com.artino.service.services.base;

import com.artino.service.entity.TConfig;
import com.artino.service.mapper.ConfigMapper;
import com.artino.service.utils.KeyUtils;
import com.artino.service.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ConfigServiceBase {
    /**
     * 用户角色 - 超级管理员
     */
    public static final String KAdminString = "超级管理员";
    /**
     * 用户角色 - 开发者
     */
    public static final String KDevelopString = "开发者";

    /**
     * 小程序二维码的有效期
     */
    public final static String KQRCODEEXIPRED = "生成二维码的有效期";

    @Autowired
    @Lazy
    private ConfigMapper configMapper;

    public List<TConfig> findList(List<Long> roleIds) {
        return configMapper.findListWith(roleIds);
    }

    public TConfig findConfig(TConfig entity) {
        return configMapper.findOne(entity);
    }

    public TConfig findDevelop() {
        return configMapper.findOne(
                TConfig.builder()
                        .type(TConfig.EType.ROLE)
                        .label(KDevelopString)
                        .build()
        );
    }

    public TConfig findAdmin() {
        return configMapper.findOne(
                TConfig.builder()
                        .type(TConfig.EType.ROLE)
                        .label(KAdminString)
                        .build()
        );
    }

    /**
     * 获取超级管理员的id
     * @param list config list
     * @return id
     */
    public Long getAdminId(List<TConfig> list) {
        if (Objects.isNull(list) || list.isEmpty()) return null;
        for (TConfig config : list) {
            if (config.getLabel().equals(KAdminString))
                return config.getValue();
        }
        return null;
    }

    /**
     * 获取系统内置的角色，这些角色不能被删除和修改
     * @return list
     */
    public List<TConfig> findSystemRole() {
        return configMapper.findList(
          TConfig.builder()
                  .type(TConfig.EType.ROLE)
                  .build()
        );
    }
}
