package com.gunnarro.followup.repository.table.user;

import com.gunnarro.followup.domain.user.UserLog;
import com.gunnarro.followup.repository.table.TableHelper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;

public abstract class UsersLogTable {

    // Database table
    public static final String TABLE_NAME = "user_details_log";

    public static PreparedStatementCreator createInsertPreparedStatement(final UserLog userLog) {
        return connection -> {
            PreparedStatement ps = connection.prepareStatement(createInsertQuery(), new String[]{"id"});
            ps.setTimestamp(1, new Timestamp(TableHelper.getToDay()));
            ps.setTimestamp(2, new Timestamp(TableHelper.getToDay()));
            ps.setObject(3, userLog.getUserId());
            ps.setTimestamp(4, new Timestamp(userLog.getLoggedInDate().getTime()));
            ps.setString(5, userLog.getLoggedInFromIpAddress());
            ps.setString(6, userLog.getLoggedInFromDevice());
            ps.setInt(7, userLog.getNumberOfLoginAttemptFailures());
            ps.setInt(8, userLog.getNumberOfLoginAttemptSuccess());
            return ps;
        };
    }

    public static String createInsertQuery() {
        return TableHelper.createInsertQuery(TABLE_NAME, TableHelper.getColumnNames(ColumnsEnum.values()));
    }

    public static Object[] createUpdateParam(UserLog userLog) {
        return new Object[]{new Timestamp(TableHelper.getToDay()), new Timestamp(TableHelper.getToDay()), userLog.getLoggedInFromIpAddress(), userLog.getLoggedInFromDevice(),
                userLog.getUserId()};
    }

    public static String createUpdateQuery() {
        return TableHelper.createUpdateQuery(TABLE_NAME, TableHelper.getColumnNames(ColumnsEnum.updateValues()));
    }

    public static String createUpdateQueryLoginAttemptFailures() {
        return TableHelper.createUpdateQuery(ColumnsEnum.fk_user_id.name(), TABLE_NAME, new String[]{TableHelper.ColumnsDefaultEnum.LAST_MODIFIED_DATE_TIME.name(),
                ColumnsEnum.login_attempt_failures.name()});
    }

    public static Object[] createUpdateParamLoginAttemptFailures(Integer numberOfAttemptFailures, Integer userId) {
        return new Object[]{new Timestamp(TableHelper.getToDay()), numberOfAttemptFailures, userId};
    }

    public static String createUpdateQueryLoginAttemptSuccess() {
        return TableHelper.createUpdateQuery(ColumnsEnum.fk_user_id.name(), TABLE_NAME, new String[]{TableHelper.ColumnsDefaultEnum.LAST_MODIFIED_DATE_TIME.name(),
                ColumnsEnum.last_logged_in_date_time.name(), ColumnsEnum.login_attempt_success.name()});
    }

    public static Object[] createUpdateParamLoginAttemptSuccess(Integer numberOfAttemptSuccess, Integer userId) {
        return new Object[]{new Timestamp(TableHelper.getToDay()), new Timestamp(TableHelper.getToDay()), numberOfAttemptSuccess, userId};
    }

    public static RowMapper<UserLog> mapToUserLogRM() {
        return (resultSet, rowNum) -> UserLog.builder()
                .id(resultSet.getInt(TableHelper.ColumnsDefaultEnum.ID.name()))
                .loggedInDate(new Date(resultSet.getTimestamp(ColumnsEnum.last_logged_in_date_time.name()).getTime()))
                .userId(resultSet.getInt(ColumnsEnum.fk_user_id.name()))
                .loggedInFromIpAddress(resultSet.getString(ColumnsEnum.last_logged_in_from_ip_address.name()))
                .loggedInFromDevice(resultSet.getString(ColumnsEnum.last_logged_in_from_device.name()))
                .numberOfLoginAttemptFailures(resultSet.getInt(ColumnsEnum.login_attempt_failures.name()))
                .numberOfLoginAttemptSuccess(resultSet.getInt(ColumnsEnum.login_attempt_success.name()))
                .build();
    }

    private enum ColumnsEnum {
        fk_user_id, last_logged_in_date_time, last_logged_in_from_ip_address, last_logged_in_from_device, login_attempt_failures, login_attempt_success;

        public static ColumnsEnum[] updateValues() {
            ColumnsEnum[] c = new ColumnsEnum[5];
            c[0] = last_logged_in_date_time;
            c[1] = last_logged_in_from_ip_address;
            c[2] = last_logged_in_from_device;
            c[3] = login_attempt_failures;
            c[4] = login_attempt_success;
            return c;
        }
    }
}
