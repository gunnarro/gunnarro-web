package com.gunnarro.web.repository.impl;

import com.gunnarro.web.domain.log.LogComment;
import com.gunnarro.web.domain.log.LogEntry;
import com.gunnarro.web.repository.BaseJdbcRepository;
import com.gunnarro.web.repository.LogEventRepository;
import com.gunnarro.web.repository.mapper.LogEventRowMapper;
import com.gunnarro.web.repository.table.log.EventLogTable;
import com.gunnarro.web.repository.table.log.LogCommentTable;
import com.gunnarro.web.service.exception.ApplicationException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Objects;

@Slf4j
//@Repository
public class LogEventRepositoryImpl extends BaseJdbcRepository implements LogEventRepository {

    @Autowired
    public LogEventRepositoryImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int createLogEvent(LogEntry logEntry) {
        log.debug("{}", logEntry);
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            getJdbcTemplate().update(EventLogTable.createInsertPreparedStatement(logEntry), keyHolder);
            return Objects.requireNonNull(keyHolder.getKey()).intValue();
        } catch (Exception e) {
            log.error(null, e);
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int createLogComment(LogComment logComment) {
        log.debug("{}", logComment);
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            getJdbcTemplate().update(LogCommentTable.createInsertPreparedStatement(logComment), keyHolder);
            return Objects.requireNonNull(keyHolder.getKey()).intValue();
        } catch (Exception e) {
            log.error(null, e);
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLogComment(Integer userId, Integer id) {
        int rows = getJdbcTemplate().update("DELETE FROM event_log_comment WHERE id = ? AND fk_user_id = ?",
                id, userId);
        log.debug("deleted log comment with id={}, deleted rows={}", id, rows);
        return rows;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLogEvent(Integer userId, Integer id) {
        int rows = getJdbcTemplate().update("DELETE FROM event_log WHERE id = ? AND fk_user_id = ?",
                id, userId);
        log.debug("deleted log event with id={}, deleted rows={}", id, rows);
        return rows;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<LogEntry> getAllLogEvents(Integer userId, int pageNumber, int pageSize) {
        Pageable pageSpecification = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        log.debug("pageSpecification: {}, offset: {}", pageSpecification, pageSpecification.getOffset());
        StringBuilder inQuery = new StringBuilder();
        inQuery.append("IN(?");
        List<Integer> grantedUserIdsForFollower = getGrantedUserIdsForFollower(userId);
        for (int i = 0; i < grantedUserIdsForFollower.size(); i++) {
            inQuery.append(",?");
        }
        inQuery.append(")");
        grantedUserIdsForFollower.add(userId);
        StringBuilder query = new StringBuilder();
        query.append("SELECT l.*");
        query.append(", u.username");
        query.append(
                ", (SELECT count(c.id) FROM event_log_comment c WHERE c.fk_event_log_id = l.id) AS number_of_comments");
        query.append(" FROM event_log l, users u");
        query.append(" WHERE l.fk_user_id ").append(inQuery);
        query.append(" AND l.fk_user_id = u.id");
        // query.append(" AND l.id >= " + pageNumber*pageSize);
        // query.append(" AND l.last_modified_date_time >= (SELECT
        // last_modified_date_time FROM event_log WHERE = )");
        query.append(" ORDER BY l.last_modified_date_time DESC");
        query.append(" LIMIT ").append(pageSpecification.getPageSize()).append(" OFFSET ").append(pageSpecification.getOffset());
        int totalCount = count("SELECT count(*) FROM event_log");
        List<LogEntry> list = getJdbcTemplate().query(query.toString(), LogEventRowMapper.mapToLogEntryRM(), grantedUserIdsForFollower.toArray());
        log.debug("list size: {}", list.size());
        return new PageImpl<>(list, pageSpecification, totalCount);
    }

    @Override
    public int count(String query) {
        Integer count = getJdbcTemplate().queryForObject(query, Integer.class);
        return count != null ? count : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogEntry getLogEvent(Integer userId, Integer logEntryId) {
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT l.*, u.username");
            query.append(" FROM event_log l, users u");
            query.append(" WHERE l.id = ?");
            // query.append(" AND l.fk_user_id = ?");
            query.append(" AND l.fk_user_id = u.id");
            LogEntry log = getJdbcTemplate().queryForObject(query.toString(),
                    LogEventRowMapper.mapToLogEntryRM(), logEntryId);
            List<LogComment> logComments = getLogComments(Objects.requireNonNull(log).getId());
            log.setLogComments(logComments);
            return log;
        } catch (Exception erae) {
            log.debug("Error: {}", erae.toString());
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LogComment> getLogComments(Integer logEntryId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT l.*, u.username");
        query.append(" FROM event_log_comment l, users u");
        query.append(" WHERE l.fk_event_log_id = ?");
        query.append(" AND l.fk_user_id = u.id");
        query.append(" ORDER BY l.created_date_time ASC");
        return getJdbcTemplate().query(query.toString(),
                LogEventRowMapper.mapToLogCommentRM(), logEntryId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LogEntry> getLogEventsFilteredByType(Integer userId, String type) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT l.*, u.username");
        query.append(" FROM event_log l, users u");
        query.append(" WHERE l.fk_user_id = ?");
        query.append(" AND l.fk_user_id = u.id");
        query.append(" AND l.level = ?");
        query.append(" ORDER BY l.last_modified_date_time DESC");
        return getJdbcTemplate().query(query.toString(),
                LogEventRowMapper.mapToLogEntryRM(), userId, type.replace("*", "%"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LogEntry> getLogEventsFilteredByTitle(Integer userId, String title) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT l.*, u.username");
        query.append(" FROM event_log l, users u");
        query.append(" WHERE l.fk_user_id = ?");
        query.append(" AND l.fk_user_id = u.id");
        query.append(" AND l.title = ?");
        query.append(" ORDER BY l.last_modified_date_time DESC");
        return getJdbcTemplate().query(query.toString(),
                LogEventRowMapper.mapToLogEntryRM(), userId, title.replace("*", "%"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LogEntry> searchLogEventsContent(@NotNull Integer userId, @NotNull String text) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT l.*, u.username");
        query.append(" FROM event_log l, users u");
        query.append(" WHERE l.fk_user_id = ?");
        query.append(" AND l.fk_user_id = u.id");
        query.append(" AND l.content = ?");
        query.append(" ORDER BY l.last_modified_date_time DESC");
        return getJdbcTemplate().query(query.toString(),
                LogEventRowMapper.mapToLogEntryRM(), userId, text.replace("*", "%"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LogEntry> getLogEvents(Integer userId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT l.*");
        query.append(", u.username");
        query.append(
                ", (SELECT count(c.id) FROM event_log_comment c WHERE c.fk_event_log_id = l.id) AS number_of_comments");
        query.append(" FROM event_log l, users u");
        query.append(" WHERE l.fk_user_id = ?");
        query.append(" AND l.fk_user_id = u.id");
        query.append(" ORDER BY l.last_modified_date_time DESC");
        return getJdbcTemplate().query(query.toString(),
                LogEventRowMapper.mapToLogEntryRM(), userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogEntry getMyLastStatusReport(Integer userId) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT *");
        sqlQuery.append(" FROM event_log");
        sqlQuery.append(" WHERE YEARWEEK(created_date_time) = YEARWEEK(NOW())-1");
        sqlQuery.append(" AND level = ?");
        sqlQuery.append(" ORDER BY last_modified_date_time DESC");
        sqlQuery.append(" LIMIT 1");
        try {
            return getJdbcTemplate().queryForObject(sqlQuery.toString(),
                    LogEventRowMapper.mapToLogEntryRM(), "REPORT");
        } catch (Exception erae) {
            log.debug("Error: {}", erae.toString());
            // ignore this
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogEntry getRecentLogEvent(Integer userId, String type, Integer forLastDays) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT *");
        sqlQuery.append(" FROM event_log");
        sqlQuery.append(" WHERE created_date_time > CURRENT_DATE - INTERVAL ? DAY");
        sqlQuery.append(" AND level = ?");
        sqlQuery.append(" ORDER BY last_modified_date_time DESC");
        sqlQuery.append(" LIMIT 1");
        try {
            return getJdbcTemplate().queryForObject(sqlQuery.toString(),
                    LogEventRowMapper.mapToLogEntryRM(), forLastDays, type);
        } catch (Exception erae) {
            log.debug("Error: {}", erae.getMessage());
            // ignore this
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateLogEvent(LogEntry logEntry) {
        log.debug("{}", logEntry);
        return getJdbcTemplate().update(EventLogTable.createUpdateQuery(), EventLogTable.createUpdateParam(logEntry));
    }

    /**
     * If comment is added to a log event, we updates the last modified date for the
     * log event in order to signal the the conversation thread has been updated.
     * {@inheritDoc}
     */
    @Override
    public int updateLogEventLastModifiedDate(Integer logEntryId) {
        return getJdbcTemplate().update(EventLogTable.createUpdateLastModifiedQuery(),
                EventLogTable.createUpdateLastModifieParam(logEntryId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(Integer logEventId, String username) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT u.username");
        sqlQuery.append(" FROM event_log l, users u");
        sqlQuery.append(" WHERE l.id = ?");
        sqlQuery.append(" AND l.fk_user_id = u.id");
        try {
            String name = getJdbcTemplate().queryForObject(sqlQuery.toString(),
                    String.class, logEventId);
            assert name != null;
            return name.equals(username);
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            // ignore this
            return false;
        }
    }
}
