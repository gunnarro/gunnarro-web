package com.gunnarro.web.repository.mapper;

import com.gunnarro.web.domain.log.LogComment;
import com.gunnarro.web.domain.log.LogEntry;
import com.gunnarro.web.utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;

/**
 * This call contain RowMapper which is required for converting ResultSet into
 * java domain class
 */
@Slf4j
public class LogEventRowMapper {

    /**
     * In order to hide public constructor
     */
    private LogEventRowMapper() {
    }

    public static RowMapper<Integer> mapCountRM() {
        return (resultSet, rowNum) -> resultSet.getInt("count");
    }

    public static RowMapper<LogEntry> mapToLogEntryRM() {
        return (resultSet, rowNum) -> {
            LogEntry logEntry = LogEntry.builder()
                    .id(resultSet.getInt("id"))
                    .fkUserId(resultSet.getInt("fk_user_id"))
                    .createdTime(resultSet.getTimestamp("created_date_time").getTime())
                    .lastModifiedTime(resultSet.getTimestamp("last_modified_date_time").getTime())
                    .content(resultSet.getString("content"))
                    .contentHtml(Utility.convertMarkdownToHtml(resultSet.getString("content")))
                    .level(resultSet.getString("level"))
                    .title(resultSet.getString("title"))
                    .build();
            try {
                logEntry.setNumberOfComments(resultSet.getInt("number_of_comments"));
            } catch (SQLException sqle) {
                // ignore, the column didn't exist
                log.debug("Error: {}", sqle.getMessage());
            }
            try {
                logEntry.setCreatedByUser(resultSet.getString("username"));
            } catch (SQLException sqle) {
                // ignore, the column diden't exist
                log.debug("Error: {}", sqle.getMessage());
            }
            return logEntry;
        };
    }

    public static RowMapper<LogComment> mapToLogCommentRM() {
        return (resultSet, rowNum) -> {
            LogComment comment = LogComment.builder()
                    .id(resultSet.getInt("id"))
                    .fkUserId(resultSet.getInt("fk_user_id"))
                    .fkLogId(resultSet.getInt("fk_event_log_id"))
                    .createdTime(resultSet.getTimestamp("created_date_time").getTime())
                    .lastModifiedTime(resultSet.getTimestamp("last_modified_date_time").getTime())
                    .content(resultSet.getString("content"))
                    .contentHtml(Utility.convertMarkdownToHtml(resultSet.getString("content")))
                    .build();
            try {
                comment.setCreatedByUser(resultSet.getString("username"));
            } catch (SQLException sqle) {
                // ignore, the column diden't exist
                log.debug("Error: {}", sqle.getMessage());
            }
            return comment;
        };
    }

}