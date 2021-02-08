package com.gunnarro.followup.repository.mapper;

import com.gunnarro.followup.domain.log.LogComment;
import com.gunnarro.followup.domain.log.LogEntry;
import com.gunnarro.followup.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;

/**
 * This call contain RowMapper which is required for converting ResultSet into
 * java domain class
 */
public class LogEventRowMapper {

    private static final Logger LOG = LoggerFactory.getLogger(LogEventRowMapper.class);

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
            LogEntry log = LogEntry.builder()
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
                log.setNumberOfComments(resultSet.getInt("number_of_comments"));
            } catch (SQLException sqle) {
                // ignore, the column didn't exist
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Error: {}", sqle.getMessage());
                }
            }
            try {
                log.setCreatedByUser(resultSet.getString("username"));
            } catch (SQLException sqle) {
                // ignore, the column diden't exist
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Error: {}", sqle.getMessage());
                }
            }
            return log;
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
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Error: {}", sqle.getMessage());
                }
            }
            return comment;
        };
    }

}