package com.gunnarro.web.service;

import com.gunnarro.web.domain.activity.ActivityLog;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * @author admin
 */
public interface ActivityService {

    /**
     * @param userId
     * @return
     */
    @PreAuthorize("hasAuthority('BLOGG_READ_PRIVILEGE')")
    List<ActivityLog> getActivityLogs(Integer userId);

    /**
     * @param userId
     * @param activityId
     * @return
     */
    @PreAuthorize("hasAuthority('BLOGG_READ_PRIVILEGE')")
    ActivityLog getActivityLog(Integer userId, int activityId);

    /**
     * @param activityLog
     * @return
     */
    @PreAuthorize(value = "hasAuthority('BLOGG_WRITE_PRIVILEGE')")
    int saveActivityLog(ActivityLog activityLog);

    /**
     * @param userId
     * @param activityId
     * @return
     */
    @PreAuthorize(value = "hasAuthority('BLOGG_WRITE_PRIVILEGE')")
    int deleteActivityLog(Integer userId, Integer activityId);

}
