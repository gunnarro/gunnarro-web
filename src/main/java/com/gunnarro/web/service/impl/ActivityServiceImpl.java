package com.gunnarro.web.service.impl;

import com.gunnarro.web.domain.activity.ActivityLog;
import com.gunnarro.web.endpoint.AuthenticationFacade;
import com.gunnarro.web.repository.ActivityRepository;
import com.gunnarro.web.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mentos
 */
@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {

   // @Autowired
    protected AuthenticationFacade authenticationFacade;

   // @Autowired
    private ActivityRepository activityRepository;

    private void checkPermission(Integer logEventId) {
        boolean hasPermission = activityRepository.hasPermission(logEventId, authenticationFacade.getAuthentication().getName());
        if (!hasPermission) {
            throw new SecurityException("Access denied");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ActivityLog> getActivityLogs(Integer userId) {
        return activityRepository.getActivityLogs(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityLog getActivityLog(Integer userId, int activityId) {
        log.debug("userId = {}, activityId = {}", +userId, activityId);
        return activityRepository.getActivityLog(userId, activityId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteActivityLog(Integer userId, Integer activityId) {
        // check if user have permission to delete
        checkPermission(activityId);
        return activityRepository.deleteActivityLog(userId, activityId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int saveActivityLog(ActivityLog activityLog) {
        if (activityLog.isNew()) {
            return activityRepository.createActivityLog(activityLog);
        } else {
            // check if user have permission to update
            checkPermission(activityLog.getId());
            return activityRepository.updateActivityLog(activityLog);
        }
    }

    /**
     * For unit testing only, inject mock
     */
    public void setActivityRepository(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * For unit testing only, inject mock
     */
    public void setAuthenticationFacade(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

}
