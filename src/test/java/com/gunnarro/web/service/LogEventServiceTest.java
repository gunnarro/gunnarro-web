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

    @Autowired
    protected LogEventServiceImpl logEventService;

    @Mock
    private static AuthenticationFacade authenticationFacadeMock;

    @AfterAll
    public static void terminate() {
        SecurityContextHolder.clearContext();
    }

    @BeforeEach
    public void setUp()  {
        // Because of security we have to set user and pwd before every unittest
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("admin", "uiL2oo3");
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(ctx);
        ctx.setAuthentication(authRequest);

        // create mock
        LocalUser user = new LocalUser();
        user.setId(1);
        user.setUsername("admin");
        when(authenticationFacadeMock.getLoggedInUser()).thenReturn(user);
    }

    @Test
    void deleteLogEventAccessDenied() {
        Assertions.assertThrows(SecurityException.class, () -> logEventService.deleteLogEvent(34, 23));
    }

    @Test
    void getLogEvents() {
        Assertions.assertEquals(1, logEventService.getLogEvents(5).size());
    }

    @Test
    void getLogEventWithComments() {
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

}
