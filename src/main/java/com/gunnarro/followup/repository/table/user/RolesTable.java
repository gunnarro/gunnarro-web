package com.gunnarro.followup.repository.table.user;

import com.gunnarro.followup.domain.user.Role;
import com.gunnarro.followup.repository.table.TableHelper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;


public class RolesTable {

    // Database table
    public static final String TABLE_NAME = "roles";

    public static String createInsertQuery() {
        return TableHelper.createInsertQuery(TABLE_NAME, TableHelper.getColumnNames(ColumnsEnum.values()));
    }

    public static PreparedStatementCreator createInsertPreparedStatement(final String userName, final String role) {
        return connection -> {
            PreparedStatement ps = connection.prepareStatement(createInsertQuery(), new String[]{"id"});
            ps.setTimestamp(1, new Timestamp(TableHelper.getToDay()));
            ps.setObject(2, userName);
            ps.setString(3, role);
            return ps;
        };
    }

    public static RowMapper<Role> mapToRoleRM() {
        return (resultSet, rowNum) -> Role.builder().id(resultSet.getInt("id")).name(resultSet.getString("name")).build();
    }

    public static RowMapper<String> mapToRoleNameRM() {
        return (resultSet, rowNum) -> resultSet.getString(ColumnsEnum.role.name());
    }

    private enum ColumnsEnum {
        username, role
    }

    public enum RolesEnum {
        ROLE_USER, ROLE_ADMIN, ROLE_GUEST, ROLE_ANONYMOUS
    }
}
