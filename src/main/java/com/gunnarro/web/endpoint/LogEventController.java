package com.gunnarro.web.endpoint;

import com.gunnarro.web.domain.log.ImageResource;
import com.gunnarro.web.domain.log.LogComment;
import com.gunnarro.web.domain.log.LogEntry;
import com.gunnarro.web.domain.user.LocalUser;
import com.gunnarro.web.service.exception.ApplicationException;
import com.gunnarro.web.utility.Utility;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * http://microbuilder.io/blog/2016/01/10/plotting-json-data-with-chart-js.html
 * https://github.com/chartjs/Chart.js/tree/master/dist
 */
@Slf4j
@Controller
@Scope("session")
public class LogEventController extends BaseController {

    private static final String REDIRECT = "redirect";
    private static final String URI_LOG_EVENTS = "/log/events";
    private static final String URI_LOG_EVENT = "/log/event";
    private static final String URI_LOG_EVENT_VIEW = "/log/event/view";

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat(Utility.DATE_TIME_PATTERN);
        sdf.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, false));
    }

    @GetMapping(URI_LOG_EVENTS)
    @ResponseBody
    public ModelAndView getLogEvents(@RequestParam(value = "page", required = false) Integer pageNumber, @RequestParam(value = "size", required = false) Integer pageSize) {
        LocalUser loggedInUser = authenticationFacade.getLoggedInUser();
        Page<LogEntry> logsPage = logEventService.getAllLogEvents(loggedInUser.getId(), pageNumber != null ? pageNumber : 0, pageSize != null ? pageSize : 25);
        log.debug("number = {}, logs = {}, total pages = {}", logsPage.getNumber(), logsPage.getNumberOfElements(), logsPage.getTotalPages());
        PageWrapper<LogEntry> page = new PageWrapper<>(logsPage, URI_LOG_EVENTS);
        ModelAndView modelView = new ModelAndView("log/view-event-logs");
        modelView.getModel().put("page", page);
        //  modelView.getModel().put("logsFromDate", !page.getContent().isEmpty() ? page.getContent().get(page.getContent().size() - 1).getCreateTime() : new Date());
        //  modelView.getModel().put("logsToDate", !page.getContent().isEmpty() ? page.getContent().get(0).getCreatedTime() : new Date());
        return modelView;
    }

    @GetMapping(URI_LOG_EVENT_VIEW + "/{logId}")
    public ModelAndView logEventView(@PathVariable("logId") int logId) {
        LocalUser loggedInUser = authenticationFacade.getLoggedInUser();
        if (loggedInUser == null) {
            throw new ApplicationException("Not logged in!");
        }
        LogEntry logEvent = logEventService.getLogEvent(loggedInUser.getId(), logId);
        log.debug("{}", logEvent);
        ModelAndView modelView = new ModelAndView("log/view-log-event");
        modelView.getModel().put("log", logEvent);
        return modelView;
    }

    @GetMapping("/log/events/txt")
    @ResponseBody
    public ModelAndView viewLogEventsAsPlainText() {
        LocalUser loggedInUser = authenticationFacade.getLoggedInUser();
        Page<LogEntry> page = logEventService.getAllLogEvents(loggedInUser.getId(), 1, 25);
        log.debug("number of log entries: {}", page.getNumber());
        ModelAndView modelView = new ModelAndView("log/view-event-logs-txt");
        modelView.getModel().put("page", page);
        return modelView;
    }

    // ---------------------------------------------
    // New and update log event
    // ---------------------------------------------

    @GetMapping(URI_LOG_EVENT + "/new")
    public String initNewLogEventForm(Map<String, Object> model) {
        LocalUser loggedInUser = authenticationFacade.getLoggedInUser();
        if (loggedInUser == null) {
            throw new ApplicationException("Not logged in!");
        }
        LogEntry log = LogEntry.builder()
                .level("INFO")
                .createdTime(System.currentTimeMillis())
                .lastModifiedTime(System.currentTimeMillis())
                .createdByUser(loggedInUser.getUsername())
                .build();
        model.put("log", log);
        return "log/edit-event-log";
    }

    /**
     * User POST for new
     */
    @PostMapping(URI_LOG_EVENT + "/new")
    public String processNewLogEventForm(@Valid @ModelAttribute("log") LogEntry logEntry, BindingResult result, SessionStatus status) {
        log.debug("{}", logEntry);
        if (result.hasErrors()) {
            log.debug("{}", result);
            return "log/edit-event-log";
        } else {
            // set created by user id
            logEntry.setFkUserId(authenticationFacade.getLoggedInUser().getId());
            this.logEventService.saveLogEvent(logEntry);
            status.setComplete();
            return String.format("%s:%s", REDIRECT, URI_LOG_EVENTS);
        }
    }

    @GetMapping(URI_LOG_EVENT + "/edit/{logEventId}")
    public String initUpdateLogEventForm(@PathVariable("logEventId") int logEventId, Model model) {
        LocalUser loggedInUser = authenticationFacade.getLoggedInUser();
        LogEntry logEntry = logEventService.getLogEvent(loggedInUser.getId(), logEventId);
        if (logEntry == null) {
            throw new ApplicationException(String.format("Object Not Found, logEventId=%s", logEventId));
        }
        log.debug("{}", logEntry);
        model.addAttribute("log", log);
        return "log/edit-event-log";
    }

    /**
     * Use PUT for updates
     */
    @PostMapping(URI_LOG_EVENT + "/edit")
    public String processUpdateLogEventForm(@Valid @ModelAttribute("log") LogEntry logEntry, BindingResult result, SessionStatus status) {
        log.debug("{}", logEntry);
        if (result.hasErrors()) {
            log.debug("{}", result);
            return "log/edit-event-log";
        } else {
            logEventService.saveLogEvent(logEntry);
            status.setComplete();
            return String.format("%s:%s", REDIRECT, URI_LOG_EVENTS);
        }
    }

    /**
     * Use PUT for updates
     */
    @PostMapping(URI_LOG_EVENT + "/edit/{logEventId}")
    public String processUpdateLogEventIdForm(@Valid @ModelAttribute("log") LogEntry logEntry, BindingResult result, SessionStatus status) {
        log.debug("{}", logEntry);
        if (result.hasErrors()) {
            log.debug("{}", result);
            return "log/edit-event-log";
        } else {
            logEventService.saveLogEvent(logEntry);
            status.setComplete();
            return String.format("%s:%s", REDIRECT, URI_LOG_EVENTS);
        }
    }

    /**
     *
     */
    @GetMapping(URI_LOG_EVENT + "/delete/{logEntryId}")
    public String deletelogEvent(@PathVariable("logEntryId") int logEntryId) {
        LocalUser loggedInUser = authenticationFacade.getLoggedInUser();
        LogEntry log = logEventService.getLogEvent(loggedInUser.getId(), logEntryId);
        if (log == null) {
            throw new ApplicationException(String.format("Object Not Found, logEntryId=%s", logEntryId));
        }
        logEventService.deleteLogEvent(loggedInUser.getId(), logEntryId);
        return String.format("%s:%s", REDIRECT, URI_LOG_EVENTS);
    }

    // ---------------------------------------------
    // Add/delete image to log event
    // ---------------------------------------------

    /**
     *
     */
    @PostMapping(URI_LOG_EVENT + "/img/upload/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("description") String description, @RequestParam("id") String id, RedirectAttributes redirectAttributes) {
        if (file == null) {
            // return error
            return String.format("redirect:/upload/%s", id);
        }
        fileUploadService.store(file, id, description);
        log.debug("Successfully uploaded: {}/{}", id, file.getName());
        // Add parameters to be viewed on the redirect page
        redirectAttributes.addFlashAttribute("message", String.format("You successfully uploaded %s", file.getOriginalFilename()));
        return String.format("redirect:/upload/%s", id);
    }


    /**
     * Use PUT for updates
     */
    @PostMapping(URI_LOG_EVENT + "/img/delete")
    public String processDeleteLogImageForm(@Valid @ModelAttribute("resource") ImageResource resource, BindingResult result, SessionStatus status) {
        log.debug("{}", resource);
        if (result.hasErrors()) {
            log.debug("{}", result);
        } else {
            fileUploadService.deleteImage(resource.getId(), resource.getName());
            logEventService.deleteLogEventImage(Integer.parseInt(resource.getId()), resource.getName());
        }
        return String.format("%s:%s/%s", REDIRECT, URI_LOG_EVENT_VIEW, resource.getId());
    }

    /**
     *
     */
    @GetMapping(URI_LOG_EVENT + "/img/delete/{logEntryId}/{filename}")
    public String deletelogEventImg(@PathVariable("logEntryId") int logEntryId, @PathVariable("filename") String filename) {
        fileUploadService.deleteImage(Integer.toString(logEntryId), filename);
        logEventService.deleteLogEventImage(logEntryId, filename);
        return String.format("%s:%s/%s", REDIRECT, URI_LOG_EVENT_VIEW, logEntryId);
    }

    // ---------------------------------------------
    // Add comments to log event
    // ---------------------------------------------

    @PostMapping(value = URI_LOG_EVENT + "/comment/add", params = {"logId", "comment"})
    public String newCommentLogEventForm(@RequestParam("logId") Integer logId, @RequestParam("comment") String comment, Model map) {
        LocalUser loggedInUser = authenticationFacade.getLoggedInUser();
        LogComment logComment = LogComment.builder()
                .fkUserId(loggedInUser.getId())
                .createdTime(System.currentTimeMillis())
                .fkLogId(logId)
                .content(comment)
                .build();
        logEventService.saveLogEventComment(logComment);
        return String.format("%s:%s/%s", REDIRECT, URI_LOG_EVENT_VIEW, logId);
    }

    @GetMapping(value = URI_LOG_EVENT + "/comment/new")
    public String initNewCommentLogEventForm(Map<String, Object> model) {
        LogComment comment = LogComment.builder()
                .createdTime(System.currentTimeMillis())
                .build();
        model.put("logComment", comment);
        return "log/edit-comment-event-log";
    }

    /**
     * User POST for new
     */
    @PostMapping(value = URI_LOG_EVENT + "/comment/new")
    public String processNewCommentLogEventForm(@Valid @ModelAttribute("logComment") LogComment logComment, BindingResult result, SessionStatus status) {
        log.debug("{}", logComment);
        if (result.hasErrors()) {
            log.debug("{}", result);
            return "log/edit-comment-event-log";
        } else {
            // set created by user id
            logComment.setFkUserId(authenticationFacade.getLoggedInUser().getId());
            this.logEventService.saveLogEventComment(logComment);
            status.setComplete();
            return String.format("%s:%s", REDIRECT, URI_LOG_EVENTS);
        }
    }
}
