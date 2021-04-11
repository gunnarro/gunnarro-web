package com.gunnarro.followup.service;

import com.gunnarro.followup.domain.log.LogComment;
import com.gunnarro.followup.domain.log.LogEntry;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;


/**
 * ref http://websystique.com/spring-security/spring-security-4-method-security-
 * using-preauthorize-postauthorize-secured-el/
 *
 * @author admin
 */
public interface LogEventService {

    /**
     *
     * @param userId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @PreAuthorize("hasAuthority('BLOGG_READ_PRIVILEGE')")
    Page<LogEntry> getAllLogEvents(Integer userId, int pageNumber, int pageSize);

    /**
     * @param logEntryId
     * @return
     */
    @PreAuthorize("hasAuthority('BLOGG_READ_PRIVILEGE')")
    LogEntry getLogEvent(Integer userId, Integer logEntryId);

    /**
     * @param userId
     * @return
     */
    @PreAuthorize("hasAuthority('BLOGG_READ_PRIVILEGE')")
    List<LogEntry> getLogEvents(Integer userId);

    /**
     *
     * @param logEntry
     * @return
     */
    // @PreAuthorize("hasAuthority('BLOGG_WRITE_PRIVILEGE') and #log.fkUserId ==
    // authentication.name")
    @PreAuthorize(value = "hasAuthority('BLOGG_WRITE_PRIVILEGE')")
    int saveLogEvent(LogEntry logEntry);

    /**
     * @param id
     * @return
     */
    // Bug, bean not found @PreAuthorize(value =
    // "hasAuthority('BLOGG_WRITE_PRIVILEGE') and
    // @logEventService.hasAccess(#log.id, authentication.name)")
    @PreAuthorize(value = "hasAuthority('BLOGG_WRITE_PRIVILEGE')")
    int deleteLogEvent(Integer userId, Integer id);

    /**
     * @param logComment
     * @return
     */
    @PreAuthorize("hasAuthority('BLOGG_WRITE_PRIVILEGE')")
    int saveLogEventComment(LogComment logComment);

    void deleteLogEventImage(Integer logId, String fileName);

}
