package com.gunnarro.followup.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.gunnarro.followup.domain.user.Privilege;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

/**
 * This call contain RowMapper which is required for converting ResultSet into
 * java domain class
 * 
 */
public class UserAccountRowMapper {

    private UserAccountRowMapper() {
    }

   
    public static RowMapper<Privilege> mapToPrivilegeRM() {
        return new RowMapper<Privilege>() {
            @Override
            public Privilege mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                return new Privilege(resultSet.getInt("id"), resultSet.getString("name"));
            }
        };
    }


    public static SingleColumnRowMapper<Integer> mapCountRM() {
        return new SingleColumnRowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                return resultSet.getInt("count");
            }
        };
    }
}