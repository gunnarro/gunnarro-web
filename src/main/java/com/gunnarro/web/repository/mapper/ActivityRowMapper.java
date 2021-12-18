package com.gunnarro.web.repository.mapper;

import com.gunnarro.web.domain.activity.Activity;
import com.gunnarro.web.domain.activity.ActivityLog;
import org.springframework.jdbc.core.RowMapper;

/**
 * This call contain RowMapper which is required for converting ResultSet into
 * java domain class
 */
public class ActivityRowMapper {

    /**
     * In order to hide public constructor
     */
    private ActivityRowMapper() {
    }

    public static RowMapper<Integer> mapCountRM() {
        return (resultSet, rowNum) -> resultSet.getInt("count");
    }

    public static RowMapper<ActivityLog> mapToActivityLogRM() {
        return (resultSet, rowNum) -> {

            Activity activity = Activity.builder()
                    .id(resultSet.getInt("fk_activity_id"))
                    .name(resultSet.getString("activity_name"))
                    .build();

            return ActivityLog.builder()
                    .activity(activity)
                    .id(resultSet.getInt("id"))
                    .fkUserId(resultSet.getInt("fk_user_id"))
                    .createdTime(resultSet.getTimestamp("created_date_time").getTime())
                    .lastModifiedTime(resultSet.getTimestamp("last_modified_date_time").getTime())
                    .intensity(resultSet.getInt("rating_intensivity"))
                    .emotions(resultSet.getInt("rating_emotions"))
                    .fromTime(resultSet.getTime("from_hour").toLocalTime())
                    .toTime(resultSet.getTime("to_hour").toLocalTime())
                    .build();
        };
    }

    public static RowMapper<Activity> mapToActivityRM() {
        return (resultSet, rowNum) -> Activity.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .category(resultSet.getString("category"))
                .description(resultSet.getString("description"))
                .build();
    }

}