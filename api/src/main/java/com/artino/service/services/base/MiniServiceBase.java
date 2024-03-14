package com.artino.service.services.base;

import com.artino.service.entity.TMini;
import com.artino.service.mapper.MiniMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class MiniServiceBase {
    @Autowired
    @Lazy
    private MiniMapper miniMapper;

    /**
     * 新增小程序
     * @param entity entity
     * @return 是否成功
     */
    boolean insert(TMini entity) {
        return miniMapper.insert(entity) > 0;
    }

    /**
     * 修改小程序
     * @param entity entity
     * @return 是否成功
     */
    boolean update(TMini entity) {
        return miniMapper.update(entity) > 0;
    }
}
