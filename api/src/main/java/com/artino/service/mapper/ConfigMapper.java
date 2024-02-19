package com.artino.service.mapper;

import com.artino.service.entity.TConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigMapper {
    /**
     * 新增
     * @param entity entity
     * @return 影响的行数
     */
    int insert(TConfig entity);

    /**
     * 修改
     * @param entity entity
     * @return 影响的行数
     */
    int update(TConfig entity);

    /**
     * 查询列表
     * @param entity entity
     * @return list
     */
    List<TConfig> findList(TConfig entity);

    /**
     * 查询一个model
     * @param entity entity
     * @return model
     */
    TConfig findOne(TConfig entity);

    /**
     * 查询列表
     * @param ids id list
     * @return list
     */
    List<TConfig> findListWith(List<Long> ids);
}
