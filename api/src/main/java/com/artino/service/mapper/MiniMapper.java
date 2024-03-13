package com.artino.service.mapper;

import com.artino.service.entity.TMini;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface MiniMapper {
    /**
     * 新增
     * @param entity entity
     * @return count
     */
    int insert(TMini entity);

    /**
     * 修改
     * @param entity entity
     * @return count
     */
    int update(TMini entity);

    /**
     * 查询列表
     * @param entity entity
     * @return list
     */
    List<TMini> findList(TMini entity);

    /**
     * 查找指定的
     * @param entity entity
     * @return model
     */
    TMini findOne(TMini entity);

    /**
     * 查询指定id 的用户
     * @param ids id list
     * @return entity
     */
    @Deprecated(since = "Deprecated")
    List<TMini> findListWith(List<Long> ids);

    /**
     * 查询指定id 的用户的集合
     * @param list
     * @return
     */
    default List<TMini> findLists(List<Long> list) {
        if (Objects.isNull(list) || list.isEmpty()) return null;
        return findListWith(list);
    }

    /**
     * 查找指定的id
     * @param id
     * @return
     */
    default TMini findById(Long id) {
        if (Objects.isNull(id) || id <= 0L) return null;
        return findOne(TMini.builder().id(id).build());
    }
}
