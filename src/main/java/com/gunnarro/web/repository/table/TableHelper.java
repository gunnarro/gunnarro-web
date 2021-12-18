package com.gunnarro.web.repository.table;

import com.gunnarro.web.service.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TableHelper {

    public static final String COLUMN_CREATED_DATETIME = "created_date_time";
    public static final String COLUMN_LAST_MODIFIED_DATETIME = "last_modified_date_time";
    private static final Logger LOG = LoggerFactory.getLogger(TableHelper.class);
    // deprecated
    // Common database table columns
    private static final String COLUMN_ID = "id";

    public static <T extends Enum<T>> String[] getColumnNames(T[] values) {
        List<String> list = new ArrayList<>();
        list.add(ColumnsDefaultEnum.CREATED_DATE_TIME.name());
        list.add(ColumnsDefaultEnum.LAST_MODIFIED_DATE_TIME.name());
        for (Enum<T> e : values) {
            list.add(e.name());
        }
        return list.toArray(new String[0]);
    }

    public static void checkInputs(Object[] colNames, Object[] values) {
        checkArray(colNames);
        checkArray(values);
        if (colNames.length != values.length) {
            throw new ApplicationException("Wrong input! columnNames <> values length!");
        }
    }

    private static void checkArray(Object[] arr) {
        if (arr == null) {
            throw new ApplicationException("Array is null!");
        }
        if (arr.length == 0) {
            throw new ApplicationException("Array is empty!");
        }
        for (Object o : arr) {
            if (o == null) {
                throw new ApplicationException("Wrong input! Array contains null values!");
            }
        }
    }

    public static PreparedStatementCreator createInsertPreparedStatement(final String query, final Object[] values) {
        return connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setTimestamp(1, new Timestamp(TableHelper.getToDay()));
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            return ps;
        };
    }

    public static String createInsertQuery(String tableName, String[] columnNames) {
        StringBuilder query = new StringBuilder();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        query.append("INSERT INTO ").append(tableName);
        int n = 0;
        for (String columnName : columnNames) {
            n++;
            columns.append(columnName);
            values.append("?");
            if (n < columnNames.length) {
                columns.append(",");
                values.append(",");
            }
        }
        query.append("(").append(columns).append(") VALUES (").append(values).append(")");
        return query.toString();
    }

    public static String createUpdateQuery(String tableName, String[] columnNames) {
        return createUpdateQuery(COLUMN_ID, tableName, columnNames);
    }

    public static String createUpdateQuery(String keyName, String tableName, String[] columnNames) {
        StringBuilder query = new StringBuilder();
        StringBuilder columnValuePairs = new StringBuilder();
        query.append("UPDATE ").append(tableName);
        int n = 0;
        for (String columnName : columnNames) {
            n++;
            columnValuePairs.append(columnName).append(" = ?");
            if (n < columnNames.length) {
                columnValuePairs.append(",");
            }
        }
        query.append(" SET ").append(columnValuePairs);
        query.append(" WHERE ").append(keyName).append(" = ?");
        if (LOG.isDebugEnabled()) {
            LOG.debug(query.toString());
        }
        return query.toString();
    }

    public static long getToDay() {
        return System.currentTimeMillis();
    }

    // Common database table columns
    public enum ColumnsDefaultEnum {
        ID, CREATED_DATE_TIME, LAST_MODIFIED_DATE_TIME
    }

}
