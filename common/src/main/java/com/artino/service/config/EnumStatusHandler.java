package com.artino.service.config;

import com.artino.service.base.R;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class EnumStatusHandler<E extends R.IBaseEnum> extends BaseTypeHandler<E> {
    private Class<E> type = null;
    private E[] enums = null;

    public EnumStatusHandler(Class<E> type) {
        this.type = type;
        this.enums = type.getEnumConstants();
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int i) throws SQLException {
        if (Objects.isNull(resultSet)) return null;
        int value = resultSet.getInt(i);
        return locateEnumStatus(value);
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String s) throws SQLException {
        if (Objects.isNull(resultSet)) return null;
        int value = resultSet.getInt(s);
        return locateEnumStatus(value);
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        if (Objects.isNull(callableStatement)) return null;
        int value = callableStatement.getInt(i);
        return locateEnumStatus(value);
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, E e, JdbcType jdbcType) throws SQLException {
        if (Objects.isNull(preparedStatement)) return;
        preparedStatement.setInt(i, e.getSelfValue());
    }

    private E locateEnumStatus(int code) {
        for (E status: enums) {
            if (status.getSelfValue() == code) return status;
        }
        throw new IllegalArgumentException("位置的枚举类型：" + code + "，请核对" + type.getSimpleName());
    }
}