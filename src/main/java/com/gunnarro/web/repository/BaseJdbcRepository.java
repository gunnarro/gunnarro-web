package com.gunnarro.web.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 *
 */
public abstract class BaseJdbcRepository {

    public static final int PAGE_SIZE = 25;

    /**
     * The gcp datasource is auto configured and autowired as jdbc template
     */
    private JdbcTemplate jdbcTemplate;

    public BaseJdbcRepository() {
    }

    /**
     * Creates a new JdbcRepositorySupport for the given JdbcTemplate.
     *
     * @param jdbcTemplate the JDBC template to create the JDBC Repository Support for.
     */
    public BaseJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected int getPageIndex(int pageNumber) {
        // check if fist page, always start at 0
        if (pageNumber <= 1) {
            return 0;
        } else {
            return pageNumber * PAGE_SIZE;
        }
    }

    /**
     * Returns the JdbcTemplate injected into the class.
     */
    public final JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    /**
     * return all user id's this followers are allowed to read
     */
    public List<Integer> getGrantedUserIdsForFollower(Integer userIdFollowerId) {
        RowMapper<Integer> rm = (resultSet, rowNum) -> resultSet.getInt("fk_user_id");
        return getJdbcTemplate().query(
                "SELECT fk_user_id FROM user_follower_lnk WHERE fk_user_follower_id = ? ORDER BY fk_user_id ASC", rm, userIdFollowerId);
    }
}