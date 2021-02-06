package com.gunnarro.followup.repository.table.user;

import com.gunnarro.followup.domain.user.LocalUser;
import com.gunnarro.followup.repository.table.TableHelper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

public abstract class UsersTable {

    // Database table
    public static final String TABLE_NAME = "users";

    private enum ColumnsEnum {
        username, password, email, enabled;

        public static String[] updateValues() {
            String[] s = new String[3];
            s[0] = TableHelper.ColumnsDefaultEnum.LAST_MODIFIED_DATE_TIME.name();
            s[1] = email.name();
            s[2] = enabled.name();
            return s;
        }
    }

    public static PreparedStatementCreator createInsertPreparedStatement(final LocalUser user) {
        return connection -> {
            PreparedStatement ps = connection.prepareStatement(createInsertQuery(), new String[]{"id"});
            ps.setTimestamp(1, new Timestamp(TableHelper.getToDay()));
            ps.setTimestamp(2, new Timestamp(TableHelper.getToDay()));
            ps.setObject(3, user.getUsername());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getEmail());
            ps.setInt(6, user.isActivated() ? 1 : 0);
            return ps;
        };
    }

    public static String createInsertQuery() {
        return TableHelper.createInsertQuery(TABLE_NAME, TableHelper.getColumnNames(ColumnsEnum.values()));
    }

    public static Object[] createUpdateParam(LocalUser user) {
        return new Object[]{new Timestamp(TableHelper.getToDay()), user.getEmail(), user.isActivated() ? 1 : 0, user.getId()};
    }

    public static String createUpdateQuery() {
        return TableHelper.createUpdateQuery(TABLE_NAME, ColumnsEnum.updateValues());
    }

    public static Object[] createPasswordUpdateParam(Integer userId, String password) {
        return new Object[]{new Timestamp(TableHelper.getToDay()), password, userId};
    }

    public static String createPasswordUpdateQuery() {
        return TableHelper.createUpdateQuery(TABLE_NAME, new String[]{TableHelper.ColumnsDefaultEnum.LAST_MODIFIED_DATE_TIME.name(), ColumnsEnum.password.name()});
    }

    public static RowMapper<LocalUser> mapToUserRM() {
        return (resultSet, rowNum) -> {
            LocalUser user = new LocalUser();
            user.setId(resultSet.getInt(TableHelper.ColumnsDefaultEnum.ID.name()));
            user.setCreatedDate(new Date(resultSet.getTimestamp(TableHelper.ColumnsDefaultEnum.CREATED_DATE_TIME.name()).getTime()));
            user.setLastModifiedDate(new Date(resultSet.getTimestamp(TableHelper.ColumnsDefaultEnum.LAST_MODIFIED_DATE_TIME.name()).getTime()));
            user.setUsername(resultSet.getString(ColumnsEnum.username.name()));
            user.setPassword(resultSet.getString(ColumnsEnum.password.name()));
            user.setEmail(resultSet.getString(ColumnsEnum.email.name()));
            user.setActivated(resultSet.getInt(ColumnsEnum.enabled.name()) == 1);
            return user;
        };
    }
}
