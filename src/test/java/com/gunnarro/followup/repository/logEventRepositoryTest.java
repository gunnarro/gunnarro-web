package com.gunnarro.followup.repository;


import com.gunnarro.followup.config.DefaultTestConfig;
import com.gunnarro.followup.config.TestMariDBDataSourceConfiguration;
import com.gunnarro.followup.config.TestRepositoryConfiguration;
import com.gunnarro.followup.domain.log.LogComment;
import com.gunnarro.followup.domain.log.LogEntry;
import com.gunnarro.followup.utility.Utility;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@ContextConfiguration(classes = {TestMariDBDataSourceConfiguration.class, TestRepositoryConfiguration.class})
@Transactional
@Rollback
public class logEventRepositoryTest extends DefaultTestConfig {

    @Autowired
    private LogEventRepository logEventRepository;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void hasPermission_access_denied() {
        Assertions.assertFalse(logEventRepository.hasPermission(200, "per"));
    }

    @Test
    public void hasPermission_access_ok() {
        Assertions.assertTrue(logEventRepository.hasPermission(3, "pepilie"));
    }

    @Test
    public void CRUDEventLog() {
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
        Assertions.assertTrue(newLog.getCreatedTime() == newLog.getLastModifiedTime());
        Assertions.assertEquals("INFO", logEvent.getLevel());
        Assertions.assertEquals("title...", logEvent.getTitle());
        Assertions.assertEquals("content", logEvent.getContent());
        Assertions.assertEquals("<p>content</p>\n", logEvent.getContentHtml());
        Assertions.assertEquals(5, logEvent.getFkUserId().intValue());
        Assertions.assertEquals("pappa", logEvent.getCreatedByUser());
        // Update
        logEvent.setLevel("CONFLICT");
        logEvent.setTitle("title...updated");
        logEvent.setContent("content...updated");
        logEventRepository.updateLogEvent(logEvent);
        Assertions.assertTrue(id > 0);
        logEvent = logEventRepository.getLogEvent(userId, id);
        Assertions.assertEquals(id, logEvent.getId());
        Assertions.assertEquals("CONFLICT", logEvent.getLevel());
        Assertions.assertEquals("title...updated", logEvent.getTitle());
        Assertions.assertEquals("content...updated", logEvent.getContent());
        Assertions.assertEquals(5, logEvent.getFkUserId().intValue());
        // assertTrue(logEvent.getCreatedDate().before(logEvent.getLastModifiedDate()));
        // Delete
        Integer rows = logEventRepository.deleteLogEvent(userId, id);
        Assertions.assertEquals(1, rows.intValue());
    }

    @Test
    public void getLogComments() {
        Assertions.assertEquals(2, logEventRepository.getLogComments(4).size());
        // for ( LogComment comment: logEventRepository.getLogComments(4)) {
        // System.out.println(comment);
        // }
    }

    @Test
    public void createLogComment() {
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
    public void eventLogLogDateNotToday() {
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
        Assertions.assertFalse(newLog.getCreatedTime() == newLog.getLastModifiedTime());
        Assertions.assertEquals("01.02.2016", Utility.formatTime(newLog.getCreatedTime(), "dd.MM.yyyy"));
    }

    @Test
    public void count() {
        Assertions.assertEquals(5, logEventRepository.count("SELECT count(*) FROM event_log"));
    }

    @Disabled
    @Test
    public void getMyLastStatusReport() {
        LogEntry newLog = LogEntry.builder()
                .fkUserId(1)
                .title("title...")
                .content("content")
                .level("INFO")
                .build();
        logEventRepository.createLogEvent(newLog);
        LogEntry myLastStatusReport = logEventRepository.getMyLastStatusReport(1);
        Assertions.assertEquals("report week 47", myLastStatusReport.getTitle());
    }

    @Disabled
    @Test
    public void getRecentEventLog() {
        LogEntry recentLogEvent = logEventRepository.getRecentLogEvent(3, "CONFLICT", 7);
        System.out.println(recentLogEvent);
    }

    @Test
    public void getAllLogEvents() {
        Page<LogEntry> page = logEventRepository.getAllLogEvents(4, 0, 25);
        System.out.println(page.toString());
        Assertions.assertEquals(1, page.getContent().size());
        Assertions.assertNull(page.getContent().get(0).getLogComments());
    }

    // @Test
    // public void followersGetLog() {
    // int followerId = 6; // mamma user id
    // List<LogEntry> allLogEvents =
    // logEventRepository.getAllLogEvents(followerId);
    // assertEquals(1, allLogEvents.size());
    // assertEquals("log event created by mamma",
    // allLogEvents.get(0).getContent());
    // // add so mamma can see log events for pepilie
    // logEventRepository.createFollowerForUser(4, 6);
    // List<Integer> grantedUserIdsForFollower =
    // logEventRepository.getGrantedUserIdsForFollower(followerId);
    // assertEquals("[4]", grantedUserIdsForFollower.toString());
    // allLogEvents = logEventRepository.getAllLogEvents(followerId);
    // System.out.println(allLogEvents);
    // assertEquals(2, allLogEvents.size());
    // assertEquals("log event created by pepilie",
    // allLogEvents.get(0).getContent());
    // assertEquals("log event created by mamma",
    // allLogEvents.get(1).getContent());
    // }

    @AfterEach
    public void tearDown() throws Exception {
    }
}
