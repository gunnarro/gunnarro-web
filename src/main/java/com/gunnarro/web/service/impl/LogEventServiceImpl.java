package com.gunnarro.web.service.impl;

import com.gunnarro.web.domain.log.LogComment;
import com.gunnarro.web.domain.log.LogEntry;
import com.gunnarro.web.endpoint.AuthenticationFacade;
import com.gunnarro.web.repository.LogEventRepository;
import com.gunnarro.web.service.FileUploadService;
import com.gunnarro.web.service.LogEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mentos
 */
@Slf4j
@Service
public class LogEventServiceImpl implements LogEventService {

    @Autowired
    protected AuthenticationFacade authenticationFacade;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private LogEventRepository logEventRepository;

    private void checkPermission(Integer logEventId) {
        boolean hasPermission = logEventRepository.hasPermission(logEventId,
                authenticationFacade.getAuthentication().getName());
        if (!hasPermission) {
            throw new SecurityException("Access denied");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LogEntry> getLogEvents(Integer userId) {
        return logEventRepository.getLogEvents(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogEntry getLogEvent(Integer userId, Integer logEntryId) {
        log.debug("userId = {}, logEntryId = {}", +userId, logEntryId);
        LogEntry logEvent = logEventRepository.getLogEvent(userId, logEntryId);
        logEvent.setResources(fileUploadService.getImages(logEntryId.toString()));
        return logEvent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<LogEntry> getAllLogEvents(Integer userId, int pageNumber, int pageSize) {
        return logEventRepository.getAllLogEvents(userId, pageNumber, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteLogEvent(Integer userId, Integer id) {
        // check if user have permission to delete
        checkPermission(id);
        return logEventRepository.deleteLogEvent(userId, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLogEventImage(Integer logId, String fileName) {
        checkPermission(logId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int saveLogEvent(LogEntry log) {
        if (log.isNew()) {
            return logEventRepository.createLogEvent(log);
        } else {
            // check if user have permission to update
            checkPermission(log.getId());
            return logEventRepository.updateLogEvent(log);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int saveLogEventComment(LogComment logComment) {
        if (logComment.isNew()) {
            logEventRepository.createLogComment(logComment);
            // update last modified date for main log entry
            return logEventRepository.updateLogEventLastModifiedDate(logComment.getFkLogId());
        } else {
            // update is not supported for comments
            return 0;
        }
    }

}
