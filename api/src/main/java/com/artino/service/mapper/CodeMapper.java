package com.artino.service.mapper;

import com.artino.service.entity.TCode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeMapper {
    /**
     * 新增
     * @param entity entity
     * @return 影响的行数
     */
    int insert(TCode entity);
    /**
     * 修改
     * @param entity entity
     * @return 影响的行数
     */
    int update(TCode entity);
    /**
     * 查询列表
     * @param entity entity
     * @return 查询的数据
     */
    List<TCode> findList(TCode entity);
    /**
     * 查询一个model
     * @param entity entity
     * @return model
     */
    TCode findOne(TCode entity);
    /**
     * 获取指定的集合
     * @param ids id list
     * @return entity list
     */
    List<TCode> findListWith(List<Long> ids);
    /**
     * 查找最新的一个验证码
     * @param entity entity
     * @return model
     */
    TCode findNewestOne(TCode entity);
}
