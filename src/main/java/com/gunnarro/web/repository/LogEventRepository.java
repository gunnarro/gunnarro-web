package com.gunnarro.web.repository;

import com.gunnarro.web.domain.log.LogComment;
import com.gunnarro.web.domain.log.LogEntry;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LogEventRepository {

    LogEntry getRecentLogEvent(Integer userId, String type, Integer forLastDays);

    int updateLogEvent(LogEntry logEntry);

    LogEntry getMyLastStatusReport(Integer userId);

    LogEntry getLogEvent(Integer userId, Integer logEntryId);

    List<LogEntry> getLogEvents(Integer userId);

    Page<LogEntry> getAllLogEvents(Integer userId, int pageNumber, int pageSize);

    int createLogEvent(LogEntry logEntry);

    List<LogEntry> getLogEventsFilteredByType(Integer userId, String level);

    List<LogEntry> getLogEventsFilteredByTitle(Integer userId, String title);

    List<LogEntry> searchLogEventsContent(Integer userId, String text);

    int deleteLogEvent(Integer userId, Integer id);

    int createLogComment(LogComment logComment);

    int deleteLogComment(Integer userId, Integer id);

    List<LogComment> getLogComments(Integer logEntryId);

    boolean hasPermission(Integer logEventId, String username);

    int updateLogEventLastModifiedDate(Integer logEntryId);

    int count(String query);

}
