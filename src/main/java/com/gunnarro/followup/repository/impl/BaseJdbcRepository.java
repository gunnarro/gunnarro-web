package com.gunnarro.followup.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 */
public abstract class BaseJdbcRepository {

    private JdbcTemplate jdbcTemplate;

    protected BaseJdbcRepository() {
    }

    /**
     * Creates a new JdbcRepositorySupport for the given JdbcTemplate.
     *
     * @param jdbcTemplate the JDBC template to create the JDBC Repository
     *                     Support for.
     */
    protected BaseJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Returns the JdbcTemplate injected into the class.
     *
     * @return
     */
    public final JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }
}