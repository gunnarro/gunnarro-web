package com.gunnarro.web.endpoint;

import com.gunnarro.web.domain.activity.ActivityLog;
import com.gunnarro.web.domain.log.LogEntry;
import com.gunnarro.web.domain.user.LocalUser;
import com.gunnarro.web.service.exception.ApplicationException;
import com.gunnarro.web.utility.Utility;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * http://microbuilder.io/blog/2016/01/10/plotting-json-data-with-chart-js.html
 * https://github.com/chartjs/Chart.js/tree/master/dist
 *
 * @author admin
 */
@Slf4j
@Controller
@Scope("session")
public class ActivityController extends BaseController {

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat(Utility.DATE_TIME_PATTERN);
        sdf.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, false));
    }

    @GetMapping("/activity/logs")
    @ResponseBody
    public ModelAndView getActivityLogs() {
        LocalUser loggedInUser = authenticationFacade.getLoggedInUser();
        List<ActivityLog> logs = activityService.getActivityLogs(loggedInUser.getId());
        log.debug("number of log entries: " + logs.size());
        ModelAndView modelView = new ModelAndView("activity/view-activity-logs");
        modelView.getModel().put("logs", logs);
        modelView.getModel().put("numberOfLogs", logs.size());
        //   modelView.getModel().put("logsFromDate", !logs.isEmpty() ? logs.get(logs.size() - 1).getCreatedDate() : new Date());
        //   modelView.getModel().put("logsToDate", !logs.isEmpty() ? logs.get(0).getCreatedDate() : new Date());
        return modelView;
    }

    @GetMapping("/activity/log/view/{logId}")
    public ModelAndView activitylogView(@PathVariable("logId") int logId) {
        LocalUser loggedInUser = authenticationFacade.getLoggedInUser();
        if (loggedInUser == null) {
            throw new ApplicationException("Not logged in!");
        }
        ActivityLog activityLog = activityService.getActivityLog(loggedInUser.getId(), logId);
        log.debug("{}", activityLog);
        ModelAndView modelView = new ModelAndView("activity/view-activity-log");
        modelView.getModel().put("log", activityLog);
        return modelView;
    }

    // ---------------------------------------------
    // New and update log event
    // ---------------------------------------------

    @GetMapping("/activity/log/new")
    public String initNewActivityLogForm(Map<String, Object> model) {
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
        return "activity/edit-activity-log";
    }

    /**
     * User POST for new
     */
    @PostMapping("/activity/log/new")
    public String processNewActivityLogForm(@Valid @ModelAttribute("log") ActivityLog activityLog, BindingResult result, SessionStatus status) {
        log.debug("{}", activityLog);
        if (result.hasErrors()) {
            log.debug("{}", result);
            return "activity/edit-activity-log";
        } else {
            // set created by user id
            activityLog.setFkUserId(authenticationFacade.getLoggedInUser().getId());
            this.activityService.saveActivityLog(activityLog);
            status.setComplete();
            return "redirect:/activity/logs";
        }
    }

    @GetMapping("/activity/log/edit/{activityId}")
    public String initUpdateActivityForm(@PathVariable("activityId") int activityId, Model model) {
        LocalUser loggedInUser = authenticationFacade.getLoggedInUser();
        ActivityLog activityLog = activityService.getActivityLog(loggedInUser.getId(), activityId);
        if (log == null) {
            throw new ApplicationException("Object Not Found, activityId=" + activityId);
        }

        log.debug("{}", activityLog);
        model.addAttribute("log", log);
        return "activity/edit-activity-log";
    }

    /**
     * Use PUT for updates
     */
    @PostMapping("/activity/log/edit")
    public String processUpdateActivityLogForm(@Valid @ModelAttribute("log") ActivityLog activityLog, BindingResult result, SessionStatus status) {
        log.debug("{}", activityLog);
        if (result.hasErrors()) {
            log.debug("{}", result);
            return "activity/edit-activity-log";
        } else {
            activityService.saveActivityLog(activityLog);
            status.setComplete();
            return "redirect:/activity/logs";
        }
    }

    /**
     * Use PUT for updates
     */
    @PostMapping("/activity/log/edit/{activityId}")
    public String processUpdateActivityLogIdForm(@Valid @ModelAttribute("log") ActivityLog activityLog, BindingResult result, SessionStatus status) {
        log.debug("{}", activityLog);
        if (result.hasErrors()) {
            log.debug("{}", result);
            return "activity/edit-activity-log";
        } else {
            activityService.saveActivityLog(activityLog);
            status.setComplete();
            return "redirect:/activity/logs";
        }
    }

    /**
     *
     */
    @GetMapping(value = "/activity/log/delete/{activityId}")
    public String deleteActivityLog(@PathVariable("activityId") int activityId) {
        LocalUser loggedInUser = authenticationFacade.getLoggedInUser();
        ActivityLog log = activityService.getActivityLog(loggedInUser.getId(), activityId);
        if (log == null) {
            throw new ApplicationException("Object Not Found, activityId=" + activityId);
        }
        activityService.deleteActivityLog(loggedInUser.getId(), activityId);
        return "redirect:/activity/logs";
    }
}
