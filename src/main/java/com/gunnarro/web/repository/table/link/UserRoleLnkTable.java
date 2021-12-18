package com.gunnarro.web.repository.table.link;

import com.gunnarro.web.repository.table.TableHelper;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class UserRoleLnkTable {

    // Database table
    public static final String TABLE_NAME = "user_role_lnk";

    /**
     * In order to hide public constructor
     */
    private UserRoleLnkTable() {
    }

    public static PreparedStatementCreator createInsertPreparedStatement(
            final Integer userId, final Integer roleId) {
        return connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    createInsertQuery(), new String[]{"id"});
            ps.setTimestamp(1, new Timestamp(TableHelper.getToDay()));
            ps.setTimestamp(2, new Timestamp(TableHelper.getToDay()));
            ps.setInt(3, userId);
            ps.setInt(4, roleId);
            return ps;
        };
    }

    public static String createInsertQuery() {
        return TableHelper.createInsertQuery(TABLE_NAME,
                TableHelper.getColumnNames(ColumnsEnum.values()));
    }

    private enum ColumnsEnum {
        fk_user_id, fk_role_id
    }

}
