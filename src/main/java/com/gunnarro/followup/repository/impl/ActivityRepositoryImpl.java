package com.gunnarro.followup.repository.impl;

import com.gunnarro.followup.domain.activity.ActivityLog;
import com.gunnarro.followup.repository.ActivityRepository;
import com.gunnarro.followup.repository.BaseJdbcRepository;
import com.gunnarro.followup.repository.mapper.ActivityRowMapper;
import com.gunnarro.followup.repository.table.activity.ActivityLogTable;
import com.gunnarro.followup.service.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ActivityRepositoryImpl extends BaseJdbcRepository implements ActivityRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityRepositoryImpl.class);

    @Autowired
    public ActivityRepositoryImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int createActivityLog(ActivityLog activityLog) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(activityLog.toString());
        }
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            getJdbcTemplate().update(ActivityLogTable.createInsertPreparedStatement(activityLog), keyHolder);
            return Objects.requireNonNull(keyHolder.getKey()).intValue();
        } catch (Exception e) {
            LOG.error(null, e);
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteActivityLog(Integer userId, Integer id) {
        int rows = getJdbcTemplate().update("DELETE FROM activity_log WHERE id = ? AND fk_user_id = ?",
                id, userId);
        if (LOG.isDebugEnabled()) {
            LOG.debug("deleted activity log with id={}, deleted rows = {}", id, rows);
        }
        return rows;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityLog getActivityLog(Integer userId, Integer activityLogId) {
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT l.*, u.username");
            query.append(" FROM activity_log l, users u");
            query.append(" WHERE l.id = ?");
            // query.append(" AND l.fk_user_id = ?");
            query.append(" AND l.fk_user_id = u.id");
            return getJdbcTemplate().queryForObject(query.toString(), new Object[]{activityLogId},
                    ActivityRowMapper.mapToActivityLogRM());
        } catch (org.springframework.dao.EmptyResultDataAccessException erae) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error: {}", erae.toString());
            }
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ActivityLog> getActivityLogs(Integer userId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT l.*, a.name AS activity_name, u.username");
        query.append(" FROM activity_log l, activities a, users u");
        query.append(" WHERE l.fk_user_id = ?");
        query.append(" AND l.fk_user_id = u.id");
        query.append(" AND l.fk_activity_id = a.id");
        query.append(" ORDER BY l.last_modified_date_time DESC");
        return getJdbcTemplate().query(query.toString(), new Object[]{userId},
                ActivityRowMapper.mapToActivityLogRM());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateActivityLog(ActivityLog activityLog) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(activityLog.toString());
        }
        return getJdbcTemplate().update(ActivityLogTable.createUpdateQuery(),
                ActivityLogTable.createUpdateParam(activityLog));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(Integer logEventId, String username) {
        StringBuilder sqlQuery = new StringBuilder();
        // sqlQuery.append("SELECT u.username");
        // sqlQuery.append(" FROM activity_log l, users u");
        // sqlQuery.append(" WHERE l.id = ?");
        // sqlQuery.append(" AND l.fk_user_id = u.id");
        sqlQuery.append("SELECT * FROM users WHERE username = ?");
        try {
            String name = getJdbcTemplate().queryForObject(sqlQuery.toString(), new Object[]{logEventId},
                    String.class);
            assert name != null;
            return name.equals(username);
        } catch (org.springframework.dao.EmptyResultDataAccessException erae) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error: {}", erae.toString());
            }
            // ignore this
            return false;
        }
    }
}
