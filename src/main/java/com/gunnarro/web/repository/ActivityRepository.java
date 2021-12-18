package com.gunnarro.web.repository;

import com.gunnarro.web.domain.activity.ActivityLog;

import java.util.List;

public interface ActivityRepository {

    int updateActivityLog(ActivityLog activityLog);

    int createActivityLog(ActivityLog activityLog);

    ActivityLog getActivityLog(Integer userId, Integer activityLogId);

    List<ActivityLog> getActivityLogs(Integer userId);

    int deleteActivityLog(Integer userId, Integer ActivityLogId);

    boolean hasPermission(Integer activityLogId, String username);

}
