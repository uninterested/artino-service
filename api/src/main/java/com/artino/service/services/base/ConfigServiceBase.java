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
    private final String adminString = "超级管理员";

    @Autowired
    @Lazy
    private ConfigMapper configMapper;

    public List<TConfig> findList(List<Long> roleIds) {
        return configMapper.findListWith(roleIds);
    }

    public TConfig findAdmin() {
        return configMapper.findOne(
                TConfig.builder()
                        .label(adminString)
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
            if (config.getLabel().equals(adminString))
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
