package com.artino.service.mapper;

import com.artino.service.entity.TUser;
import com.artino.service.entity.TUserAdmin;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface UserMapper {
    /**
     * 新增
     * @param entity entity
     * @return 影响的行数
     */
    int insert(TUser entity);

    /**
     * 修改
     * @param entity entity
     * @return 影响的行数
     */
    int update(TUser entity);

    /**
     * 批量插入用户-角色数据
     * @param list data
     * @return 影响的行数
     */
    int batchInsertUserAdmin(List<TUserAdmin> list);

    /**
     * 查询列表
     * @param entity entity
     * @return list
     */
    List<TUser> findList(TUser entity);

    /**
     * 查找指定的
     * @param entity entity
     * @return model
     */
    TUser findOne(TUser entity);

    /**
     * 查询指定id 的用户
     * @param ids id list
     * @return entity
     */
    @Deprecated(since = "Deprecated")
    List<TUser> findListWith(List<Long> ids);

    /**
     * 查询指定id 的用户的集合
     * @param list
     * @return
     */
    default List<TUser> findLists(List<Long> list) {
        if (Objects.isNull(list) || list.isEmpty()) return null;
        return findListWith(list);
    }

    /**
     * 查找指定的id
     * @param id
     * @return
     */
    default TUser findById(Long id) {
        if (Objects.isNull(id) || id <= 0L) return null;
        return findOne(TUser.builder().id(id).build());
    }

    default TUser findByPhone(String phone) {
        if (Objects.isNull(phone) || phone.isEmpty()) return null;
        return findOne(TUser.builder().phone(phone).build());
    }

    default TUser findByEmail(String email) {
        if (Objects.isNull(email) || email.isEmpty()) return null;
        return findOne(TUser.builder().email(email).build());
    }
}
