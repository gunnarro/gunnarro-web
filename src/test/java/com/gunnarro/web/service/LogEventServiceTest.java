package com.gunnarro.web.service;

import com.gunnarro.web.config.DefaultTestConfig;
import com.gunnarro.web.config.TestMariDBDataSourceConfiguration;
import com.gunnarro.web.config.TestRepositoryConfiguration;
import com.gunnarro.web.domain.log.LogEntry;
import com.gunnarro.web.domain.user.LocalUser;
import com.gunnarro.web.endpoint.AuthenticationFacade;
import com.gunnarro.web.service.impl.FileUploadServiceImpl;
import com.gunnarro.web.service.impl.LogEventServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TestMariDBDataSourceConfiguration.class, TestRepositoryConfiguration.class, LogEventServiceImpl.class, AuthenticationFacade.class, FileUploadServiceImpl.class})
@Transactional(timeout = 10)
class LogEventServiceTest extends DefaultTestConfig {

    @Mock
    private static AuthenticationFacade authenticationFacadeMock;
    @Autowired
    protected LogEventServiceImpl logEventService;

    @AfterAll
    public static void terminate() {
        SecurityContextHolder.clearContext();
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Because of security we have to set user and pwd before every unit
        // test
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("admin", "uiL2oo3");
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(ctx);
        ctx.setAuthentication(authRequest);

        // logEventService.setAuthenticationFacade(authenticationFacadeMock);

        // create mock
        LocalUser user = new LocalUser();
        user.setId(1);
        user.setUsername("admin");
        when(authenticationFacadeMock.getLoggedInUser()).thenReturn(user);
    }

    // @Test(expected = SecurityException.class)
    // public void hasPermission_access_denied() {
    // logEventService.checkPermission(3, "guest");
    // Assert.assertTrue(false);
    // }
    //
    // @Test
    // public void hasPermission_access_ok() {
    // Assert.assertTrue(logEventService.checkPermission(3, "pepilie"));
    // }

    @Test
    void getLogEventsWithComments() {
        LogEntry logEvent = logEventService.getLogEvent(5, 4);
        Assertions.assertNotNull(logEvent);
        Assertions.assertEquals(5, logEvent.getFkUserId().intValue());
        Assertions.assertEquals(4, logEvent.getId().intValue());
        Assertions.assertEquals("INFO", logEvent.getLevel());
        Assertions.assertEquals("title pappa", logEvent.getTitle());
        Assertions.assertEquals("log event created by pappa", logEvent.getContent());
        Assertions.assertEquals(2, logEvent.getLogComments().size());
        Assertions.assertEquals(2, logEvent.getLogComments().get(0).getId().intValue());
        Assertions.assertTrue(logEvent.getLogComments().get(0).getCreatedTime() > 0);
        Assertions.assertEquals("added comment 1", logEvent.getLogComments().get(0).getContent());
    }

//    @Test
//    public void logEventCRUD() {
//        int adminUserId = 1;
//        // Read
//        Assert.assertEquals(2, logEventService.getAllLogEvents(adminUserId).size());
//        LogEntry log = new LogEntry();
//        log.setFkUserId(adminUserId);
//        log.setTitle("title");
//        log.setContent("content");
//        // Create
//        int logEventId = logEventService.saveLogEvent(log);
//        Assert.assertEquals(3, logEventService.getAllLogEvents(adminUserId).size());
//        LogEntry logEvent = logEventService.getLogEvent(adminUserId, logEventId);
//        Assert.assertEquals("title", logEvent.getTitle());
//        Assert.assertEquals("content", logEvent.getContent());
//        Assert.assertEquals(1, logEvent.getFkUserId().intValue());
//        // Update
//        logEvent.setContent("updated content");
//        logEventService.saveLogEvent(logEvent);
//        LogEntry updatedLogEvent = logEventService.getLogEvent(adminUserId, logEventId);
//        Assert.assertEquals("updated content", updatedLogEvent.getContent());
//        // Delete
//        int deletedRows = logEventService.deleteLogEvent(adminUserId, logEventId);
//        Assert.assertEquals(1, deletedRows);
//        Assert.assertEquals(2, logEventService.getAllLogEvents(adminUserId).size());
//    }
//
//    @Test
//    public void getLogEventsFiltered() {
//        Assert.assertEquals(0, logEventService.getLogEvents(99, "", "").size());
//        Assert.assertEquals(0, logEventService.getLogEvents(99, null, null).size());
//        Assert.assertEquals(0, logEventService.getLogEvents(99, "title", "*dd*").size());
//        Assert.assertEquals(0, logEventService.getLogEvents(99, "type", "INFO").size());
//        Assert.assertEquals(0, logEventService.getLogEvents(99, "content", "*").size());
//    }
//
//    /**
//     * users must see each other log events
//     */
//    @Disabled
//    @Test
//    public void noLogEventesForUser() {
//        int userId = 3;
//        Assert.assertEquals(0, logEventService.getAllLogEvents(userId).size());
//        Assert.assertNull(logEventService.getLogEvent(userId, 5));
//        Assert.assertNull(logEventService.getLogEvent(userId, 3));
//    }
}
