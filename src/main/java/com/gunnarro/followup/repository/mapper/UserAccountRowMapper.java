package com.gunnarro.followup.repository.mapper;

import com.gunnarro.followup.domain.user.Privilege;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This call contain RowMapper which is required for converting ResultSet into
 * java domain class
 */
public class UserAccountRowMapper {

    private UserAccountRowMapper() {
    }

    public static RowMapper<Privilege> mapToPrivilegeRM() {
        return (resultSet, rowNum) -> Privilege.builder().id(resultSet.getInt("id")).name(resultSet.getString("name")).build();
    }


    public static SingleColumnRowMapper<Integer> mapCountRM() {
        return new SingleColumnRowMapper<>() {
            @Override
            public Integer mapRow(@NotNull ResultSet resultSet, int rowNum) throws SQLException {
                return resultSet.getInt("count");
            }
        };
    }
}