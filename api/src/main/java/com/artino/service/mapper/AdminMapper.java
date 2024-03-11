package com.artino.service.mapper;

import com.artino.service.entity.TAdmin;
import com.artino.service.entity.TAdminRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface AdminMapper {
    /**
     * 新增
     * @param entity entity
     * @return 影响的行数
     */
    int insert(TAdmin entity);

    /**
     * 修改
     * @param entity entity
     * @return 影响的行数
     */
    int update(TAdmin entity);

    /**
     * 删除用户的权限
     * @param adminId 用户id
     * @return 影响的行数
     */
    int deleteListByUserId(Long adminId);

    /**
     * 批量插入用户-角色数据
     * @param list data
     * @return 影响的行数
     */
    int batchInsertAdminRole(List<TAdminRole> list);

    /**
     * 查询列表
     * @param entity entity
     * @return list
     */
    List<TAdmin> findList(TAdmin entity);

    /**
     * 查找指定的
     * @param entity entity
     * @return model
     */
    TAdmin findOne(TAdmin entity);

    /**
     * 查询指定id 的用户
     * @param ids id list
     * @return entity
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
        if (Objects.isNull(id) || id <= 0L) return null;
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
