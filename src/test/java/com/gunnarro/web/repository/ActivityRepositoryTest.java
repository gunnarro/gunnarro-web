package com.gunnarro.web.repository;

import com.gunnarro.web.config.DefaultTestConfig;
import com.gunnarro.web.config.TestMariDBDataSourceConfiguration;
import com.gunnarro.web.config.TestRepositoryConfiguration;
import com.gunnarro.web.domain.activity.Activity;
import com.gunnarro.web.domain.activity.ActivityLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@ContextConfiguration(classes = {TestMariDBDataSourceConfiguration.class, TestRepositoryConfiguration.class})
@Transactional
@Rollback
class ActivityRepositoryTest extends DefaultTestConfig {

    @Autowired
    private ActivityRepository activityRepository;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void getActivityLogs() {
        List<ActivityLog> activityLogs = activityRepository.getActivityLogs(1);
        System.out.println(activityLogs);
        Assertions.assertNotNull(activityLogs);
    }

    @Test
    void createActivityLog() {
        Activity a = Activity.builder()
                .id(1)
                .build();
        ActivityLog activityLog = ActivityLog.builder()
                .fkUserId(1)
                .emotions(2)
                .intensity(4)
                .fromTime(LocalTime.now())
                .toTime(LocalTime.now().plusHours(1))
                .activity(a)
                .build();

        activityLog.setActivity(a);
        Assertions.assertTrue(activityRepository.createActivityLog(activityLog) > 1);
    }

    @Test
    void deleteActivityLog() {
        Assertions.assertEquals(1, activityRepository.deleteActivityLog(1, 1));
    }
}
