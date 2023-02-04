package com.gunnarro.web.endpoint;

import com.gunnarro.web.domain.log.LogEntry;
import com.gunnarro.web.domain.user.LocalUser;
import com.gunnarro.web.service.LogEventService;
import com.gunnarro.web.service.exception.ApplicationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

//@ContextConfiguration(classes={LogEventServiceImpl.class, FileUploadServiceImpl.class, AuthenticationFacade.class})
class LogEventControllerTest extends SpringTestSetup {

    private static final int ADMIN_USER_ID = 1;

    private LogEventController controller;

    @Mock
    private AuthenticationFacade authenticationFacadeMock;

    @Mock
    private LogEventService logEventServiceMock;

    @BeforeEach
    void init() {
        controller = new LogEventController();
        controller.setLogEventService(logEventServiceMock);
        controller.setAuthenticationFacade(authenticationFacadeMock);
        LocalUser user = new LocalUser();
        user.setId(ADMIN_USER_ID);
        user.setUsername("admin");
        when(authenticationFacadeMock.getLoggedInUser()).thenReturn(user);
    }

    @Test()
    void deleteLogEvent() {
        LogEntry logEntry = LogEntry.builder()
                .fkUserId(ADMIN_USER_ID)
                .build();
            when(logEventServiceMock.getLogEvent(ADMIN_USER_ID, 4)).thenReturn(logEntry);
        String redirectUrl = controller.deletelogEvent(4);
        Assertions.assertEquals("redirect:/log/events", redirectUrl);
    }

    @Test()
    void deleteLogEventNotFound() {
        LogEntry logEntry = LogEntry.builder()
                .fkUserId(ADMIN_USER_ID)
                .build();
        when(logEventServiceMock.getLogEvent(ADMIN_USER_ID, 43)).thenReturn(logEntry);
        Assertions.assertThrows(ApplicationException.class, () -> controller.deletelogEvent(45));
    }

    @Test
    void deleteLogEventNotAllowedToDelete() {
        when(logEventServiceMock.getLogEvent(ADMIN_USER_ID, 4)).thenReturn(null);
        Assertions.assertThrows(ApplicationException.class, () -> controller.deletelogEvent(4));
    }

    @Test
    void editLogEvent()  {
        LogEntry logEntry = LogEntry.builder()
                .fkUserId(ADMIN_USER_ID)
                .build();
        when(logEventServiceMock.getLogEvent(ADMIN_USER_ID, 4)).thenReturn(logEntry);
        String redirectUrl = controller.initUpdateLogEventForm(4, new ExtendedModelMap());
        Assertions.assertEquals("log/edit-event-log", redirectUrl);
    }

    @Test
    void editLogEventNotAllowedToEdit() {
        when(logEventServiceMock.getLogEvent(ADMIN_USER_ID, 4)).thenReturn(null);
        Assertions.assertThrows(ApplicationException.class, () ->
            controller.initUpdateLogEventForm(4, null)
        );
    }

    @Test
    void getLogEvents()  {
        List<LogEntry> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(LogEntry.builder().id(i).build());
        }
        Pageable pageSpecification = PageRequest.of(0, 5, Sort.by("id"));
        Page<LogEntry> page = new PageImpl<>(list, pageSpecification, list.size());
        when(logEventServiceMock.getAllLogEvents(ADMIN_USER_ID, 1, 5)).thenReturn(page);
        ModelAndView modelAndView = controller.getLogEvents(1, 5);
        Assertions.assertEquals("log/view-event-logs", modelAndView.getViewName());
        Assertions.assertNotNull(modelAndView.getModel());
        PageWrapper p = (PageWrapper) modelAndView.getModel().get("page");
        Assertions.assertNotNull(p.getContent());
        Assertions.assertEquals(29, ((LogEntry) p.getLastElement()).getId().intValue());
        Assertions.assertEquals(30, p.getContent().size());
        Assertions.assertEquals(1, p.getNumber());
        Assertions.assertEquals(30, p.getTotalElements());
        Assertions.assertEquals(5, p.getItems().size());
        Assertions.assertEquals(6, p.getTotalPages());

        Assertions.assertAll("page",
                () -> Assertions.assertEquals(30, p.getContent().size()),
                () -> Assertions.assertEquals(1, p.getNumber())
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void viewLogEventsAsPlainText() {
        Pageable pageSpecification = PageRequest.of(1, 25, Sort.by("id"));
        Page<LogEntry> page = new PageImpl<>(new ArrayList<>(), pageSpecification, 100);
        when(logEventServiceMock.getAllLogEvents(ADMIN_USER_ID, 1, 25)).thenReturn(page);
        ModelAndView modelAndView = controller.viewLogEventsAsPlainText();
        Assertions.assertEquals("log/view-event-logs-txt", modelAndView.getViewName());
        Assertions.assertNotNull(modelAndView.getModel());
        Page<LogEntry> p = (Page<LogEntry>) modelAndView.getModel().get("page");
        Assertions.assertNotNull(p.getContent());
        Assertions.assertEquals(1, p.getNumber());
        Assertions.assertEquals(100, p.getTotalElements());
        Assertions.assertEquals(4, p.getTotalPages());
    }

    @Test
    void initNewLogEventForm() {
        String redirectUrl = controller.initNewLogEventForm(new ExtendedModelMap());
        Assertions.assertEquals("log/edit-event-log", redirectUrl);
    }
}