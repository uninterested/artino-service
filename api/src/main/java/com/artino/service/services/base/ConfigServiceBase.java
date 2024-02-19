package com.artino.service.services.base;

import com.artino.service.entity.TConfig;
import com.artino.service.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigServiceBase {

    @Autowired
    @Lazy
    private ConfigMapper configMapper;

    public TConfig findAdmin() {
        return configMapper.findOne(
                TConfig.builder()
                        .label("超级管理员")
                        .build()
        );
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
