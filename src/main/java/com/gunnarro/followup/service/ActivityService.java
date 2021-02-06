package com.gunnarro.followup.service;

import com.gunnarro.followup.domain.activity.ActivityLog;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * @author admin
 */
public interface ActivityService {

    /**
     * @param logEntryId
     * @return
     */
    @PreAuthorize("hasAuthority('BLOGG_READ_PRIVILEGE')")
    List<ActivityLog> getActivityLogs(Integer userId);

    /**
     * @param logEntryId
     * @return
     */
    @PreAuthorize("hasAuthority('BLOGG_READ_PRIVILEGE')")
    ActivityLog getActivityLog(Integer userId, int activityId);

    /**
     * @param log
     * @return
     */
    @PreAuthorize(value = "hasAuthority('BLOGG_WRITE_PRIVILEGE')")
    int saveActivityLog(ActivityLog activityLog);

    /**
     * @param id
     * @return
     */
    @PreAuthorize(value = "hasAuthority('BLOGG_WRITE_PRIVILEGE')")
    int deleteActivityLog(Integer userId, Integer activityId);

}
