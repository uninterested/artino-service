package com.artino.service.mapper;

import com.artino.service.entity.TCode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeMapper {
    /**
     * 新增
     * @param entity
     * @return
     */
    int insert(TCode entity);
    /**
     * 修改
     * @param entity
     * @return
     */
    int update(TCode entity);
    /**
     * 查询列表
     * @param entity
     * @return
     */
    List<TCode> findList(TCode entity);
    /**
     * 查询一个modal
     * @param entity
     * @return
     */
    TCode findOne(TCode entity);
    /**
     * 获取指定的集合
     * @param ids
     * @return
     */
    List<TCode> findListWith(List<Long> ids);
    /**
     * 查找最新的一个验证码
     * @param entity
     * @return
     */
    TCode findNewestOne(TCode entity);
}
