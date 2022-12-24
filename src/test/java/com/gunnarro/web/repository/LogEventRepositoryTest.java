package com.gunnarro.web.repository;


import com.gunnarro.web.config.DefaultTestConfig;
import com.gunnarro.web.config.TestMariDBDataSourceConfiguration;
import com.gunnarro.web.config.TestRepositoryConfiguration;
import com.gunnarro.web.domain.log.LogComment;
import com.gunnarro.web.domain.log.LogEntry;
import com.gunnarro.web.utility.Utility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@ContextConfiguration(classes = {TestMariDBDataSourceConfiguration.class, TestRepositoryConfiguration.class})
@Transactional
@Rollback
class LogEventRepositoryTest extends DefaultTestConfig {

    @Autowired
    private LogEventRepository logEventRepository;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    void hasPermission_access_denied() {
        Assertions.assertFalse(logEventRepository.hasPermission(200, "per"));
    }

    @Test
    void hasPermission_access_ok() {
        Assertions.assertTrue(logEventRepository.hasPermission(3, "pepilie"));
    }

    @Test
    void CRUDEventLog() {
        int userId = 5;
        LogEntry newLog = LogEntry.builder()
                .fkUserId(userId)
                .title("title...")
                .content("content")
                .level("INFO")
                .build();
        // Create
        Integer id = logEventRepository.createLogEvent(newLog);
        Assertions.assertTrue(id > 0);
        LogEntry logEvent = logEventRepository.getLogEvent(userId, id);
        Assertions.assertEquals(id, logEvent.getId());
        Assertions.assertTrue(newLog.getCreatedTime() >= 0);
        Assertions.assertTrue(newLog.getLastModifiedTime() >= 0);
        Assertions.assertEquals(newLog.getCreatedTime(), newLog.getLastModifiedTime());
        Assertions.assertEquals("INFO", logEvent.getLevel());
        Assertions.assertEquals("title...", logEvent.getTitle());
        Assertions.assertEquals("content", logEvent.getContent());
        Assertions.assertEquals("content", logEvent.getContentHtml());
        Assertions.assertEquals(5, logEvent.getFkUserId().intValue());
        Assertions.assertEquals("pappa", logEvent.getCreatedByUser());
        // Update
        logEvent.setLevel("CONFLICT");
        logEvent.setTitle("title...updated");
        logEvent.setContent("content...updated");
        logEventRepository.updateLogEvent(logEvent);
        Assertions.assertTrue(true);
        logEvent = logEventRepository.getLogEvent(userId, id);
        Assertions.assertEquals(id, logEvent.getId());
        Assertions.assertEquals("CONFLICT", logEvent.getLevel());
        Assertions.assertEquals("title...updated", logEvent.getTitle());
        Assertions.assertEquals("content...updated", logEvent.getContent());
        Assertions.assertEquals(5, logEvent.getFkUserId().intValue());
        // Delete
        int rows = logEventRepository.deleteLogEvent(userId, id);
        Assertions.assertEquals(1, (int) rows);
    }

    @Test
    void getLogComments() {
        Assertions.assertEquals(2, logEventRepository.getLogComments(4).size());
    }

    @Test
    void createLogComment() {
        int userId = 5;
        LogEntry newLog = LogEntry.builder()
                .fkUserId(userId)
                .title("title...")
                .content("content")
                .level("INFO")
                .createdTime(System.currentTimeMillis())
                .lastModifiedTime(System.currentTimeMillis())
                .build();
        // Create log event
        Integer id = logEventRepository.createLogEvent(newLog);

        LogComment comment = LogComment.builder()
                .fkLogId(id)
                .content("comment 1")
                .fkUserId(3)
                .build();

        // create log comment
        logEventRepository.createLogComment(comment);
        // read log event with comment
        LogEntry logEntry = logEventRepository.getLogEvent(userId, id);
        Assertions.assertEquals(id, logEntry.getId());
        Assertions.assertEquals(1, logEntry.getLogComments().size());
        Assertions.assertEquals("guest", logEntry.getLogComments().get(0).getCreatedByUser());
        Assertions.assertEquals(3, logEntry.getLogComments().get(0).getFkUserId().intValue());
        Assertions.assertTrue(logEntry.getLogComments().get(0).getCreatedTime() > 0);
        Assertions.assertEquals("comment 1", logEntry.getLogComments().get(0).getContent());
    }

    @Test
    void eventLogLogDateNotToday() {
        int userId = 5;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2016);
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        LogEntry newLog = LogEntry.builder()
                .fkUserId(userId)
                .title("title...")
                .content("content")
                .level("INFO")
                .createdTime(cal.getTimeInMillis())
                .build();

        Integer id = logEventRepository.createLogEvent(newLog);
        Assertions.assertTrue(id > 0);
        LogEntry logEvent = logEventRepository.getLogEvent(userId, id);
        Assertions.assertEquals(id, logEvent.getId());
        Assertions.assertTrue(newLog.getCreatedTime() >= 0);
        Assertions.assertTrue(newLog.getLastModifiedTime() >= 0);
        Assertions.assertNotEquals(newLog.getCreatedTime(), newLog.getLastModifiedTime());
        Assertions.assertEquals("01.02.2016", Utility.formatTime(newLog.getCreatedTime(), "dd.MM.yyyy"));
    }

    @Test
    void count() {
        Assertions.assertEquals(5, logEventRepository.count("SELECT count(*) FROM event_log"));
    }

    @Test
    void getMyLastStatusReport() {
        LogEntry newLog = LogEntry.builder()
                .fkUserId(1)
                .title("title...")
                .content("content")
                .level("INFO")
                .build();
        logEventRepository.createLogEvent(newLog);
        LogEntry myLastStatusReport = logEventRepository.getMyLastStatusReport(1);
        Assertions.assertNull(myLastStatusReport);
    }

    @Test
    void getAllLogEvents() {
        Page<LogEntry> page = logEventRepository.getAllLogEvents(4, 0, 25);
        System.out.println(page.toString());
        Assertions.assertEquals(1, page.getContent().size());
        Assertions.assertNull(page.getContent().get(0).getLogComments());
    }
}
