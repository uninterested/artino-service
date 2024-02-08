package com.artino.service.mapper;

import com.artino.service.entity.TAdmin;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface AdminMapper {
    /**
     * 新增
     * @param entity
     * @return
     */
    int insert(TAdmin entity);

    /**
     * 修改
     * @param entity
     * @return
     */
    int update(TAdmin entity);

    /**
     * 查询列表
     * @param entity
     * @return
     */
    List<TAdmin> findList(TAdmin entity);

    /**
     * 查找指定的
     * @param entity
     * @return
     */
    TAdmin findOne(TAdmin entity);

    /**
     * 查询指定id 的用户
     * @param ids
     * @return
     */
    @Deprecated(since = "Deprecated")
    List<TAdmin> findListWith(List<Long> ids);

    /**
     * 查询指定id 的用户的集合
     * @param list
     * @return
     */
    default List<TAdmin> findLists(List<Long> list) {
        if (Objects.isNull(list) || list.isEmpty()) return null;
        return findListWith(list);
    }

    /**
     * 查找指定的id
     * @param id
     * @return
     */
    default TAdmin findById(Long id) {
        if (Objects.isNull(id) || id.equals(0L)) return null;
        return findOne(TAdmin.builder().id(id).build());
    }

    default TAdmin findByPhone(String phone) {
        if (Objects.isNull(phone) || phone.isEmpty()) return null;
        return findOne(TAdmin.builder().phone(phone).build());
    }

    default TAdmin findByEmail(String email) {
        if (Objects.isNull(email) || email.isEmpty()) return null;
        return findOne(TAdmin.builder().email(email).build());
    }
}
