
package com.gunnarro.web.endpoint;

import com.gunnarro.web.service.ActivityService;
import com.gunnarro.web.service.FileUploadService;
import com.gunnarro.web.service.LogEventService;
import com.gunnarro.web.service.exception.ApplicationException;
import com.gunnarro.web.service.exception.UploadFileException;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import java.net.ConnectException;
import java.sql.SQLException;

/**
 * URL security is supplied in the spring security config.
 */
@Slf4j
public class BaseController {

    @Autowired
    protected AuthenticationFacade authenticationFacade;

    @Autowired
    protected LogEventService logEventService;

    @Autowired
    protected ActivityService activityService;

    @Autowired
    protected FileUploadService fileUploadService;

    public AuthenticationFacade getAuthenticationFacade() {
        return authenticationFacade;
    }

    /**
     * for unit testing only
     */
    public void setAuthenticationFacade(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(HttpServletRequest request, RuntimeException ex) {
        return handleException(request.getRequestURI(), request.getRequestURI(), ex, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllException(HttpServletRequest request, Exception ex) {
        return handleException(request.getRequestURI(), request.getRequestURI(), ex, ex.getMessage());
    }

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView handleApplicationException(HttpServletRequest request, Exception ex) {
        return handleException(request.getRequestURI(), request.getRequestURI(), ex, ex.getMessage());
    }

    @ExceptionHandler(UploadFileException.class)
    public ModelAndView handleUploadFileException(HttpServletRequest request, Exception ex) {
        return handleException(request.getRequestURI(), request.getRequestURI(), ex, ex.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    public ModelAndView handleMultipartException(HttpServletRequest request, Exception ex) {
        return handleException(request.getRequestURI(), request.getRequestURI(), ex, "Upload failed!");
    }

    @ExceptionHandler(CommunicationsException.class)
    public ModelAndView handleCommunicationsException(HttpServletRequest request, Exception ex) {
        return handleException(request.getRequestURI(), request.getRequestURI(), ex,
                "Database communication problems!");
    }

    @ExceptionHandler(ConnectException.class)
    public ModelAndView handleConnectException(HttpServletRequest request, Exception ex) {
        return handleException(request.getRequestURI(), request.getRequestURI(), ex, "Database Connect problems!");
    }

    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    public ModelAndView handleJDBCConnectionException(HttpServletRequest request, Exception ex) {
        return handleException(request.getRequestURI(), request.getRequestURI(), ex, "Technical Database problems!");
    }

    @ExceptionHandler(SecurityException.class)
    public ModelAndView handleSecurityException(HttpServletRequest request, Exception ex) {
        return handleException(request.getRequestURI(), "/home", ex, "Access Denied. " + ex.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ModelAndView handleSQLException(HttpServletRequest request, Exception ex) {
        return handleException(request.getRequestURI(), request.getRequestURI(), ex, "Technical Database problems!");
    }

    @ExceptionHandler(org.thymeleaf.exceptions.TemplateInputException.class)
    public ModelAndView handlePgeNotFound(HttpServletRequest request, Exception ex) {
        return handleException(request.getRequestURI(), request.getRequestURI(), ex, "Page not found!");
    }

    /**
     * Prevent user form binding the id-field dataBinder.setDisallowedFields("id");
     */
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
    }

    private ModelAndView handleException(String requestUrl, String backUrl, Exception e, String errorMsg) {
        log.error("Requested URL=" + requestUrl);
        log.error("Exception Raised", e);
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.getModel().put("errorMsg", errorMsg);
        modelAndView.getModel().put("requestUrl", requestUrl);
        modelAndView.getModel().put("backUrl", backUrl);
        return modelAndView;
    }

    /**
     * For unit test only, inject mock
     *
     * @param logEventService use mock
     */
    public void setLogEventService(LogEventService logEventService) {
        this.logEventService = logEventService;
    }
}
