package com.common.common.postgressql.common.convert;

import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayTypeHandlerPg implements TypeHandler<List<?>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<?> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            try {
                ps.setNull(i, JdbcType.ARRAY.TYPE_CODE);
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . "
                        + "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. "
                        + "Cause: " + e, e);
            }
        } else {
            try {
                ps.setArray(i, ps.getConnection().createArrayOf(jdbcType.name(), parameter.toArray()));
            } catch (Exception e) {
                throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType
                        + " . "
                        + "Try setting a different JdbcType for this parameter or a different configuration property. "
                        + "Cause: " + e, e);
            }
        }
    }

    @Override
    public List<?> getResult(ResultSet rs, String columnName) throws SQLException {
        List<?> result;
        try {
            Array array = rs.getArray(columnName);
            result = array == null ? null : new ArrayList<>(Arrays.asList((Object[]) array.getArray()));
        } catch (Exception e) {
            throw new ResultMapException(
                    "Error attempting to get column '" + columnName + "' from result list.  Cause: " + e, e);
        }
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public List<?> getResult(ResultSet rs, int columnIndex) throws SQLException {
        List<?> result;
        try {
            Array array = rs.getArray(columnIndex);
            result = array == null ? null : new ArrayList<>(Arrays.asList((Object[]) array.getArray()));
        } catch (Exception e) {
            throw new ResultMapException(
                    "Error attempting to get column #" + columnIndex + " from result list.  Cause: " + e, e);
        }
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public List<?> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        List<?> result;
        try {
            Array array = cs.getArray(columnIndex);
            result = array == null ? null : new ArrayList<>(Arrays.asList((Object[]) array.getArray()));
        } catch (Exception e) {
            throw new ResultMapException(
                    "Error attempting to get column #" + columnIndex + " from callable statement.  Cause: " + e, e);
        }
        if (cs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }
}
