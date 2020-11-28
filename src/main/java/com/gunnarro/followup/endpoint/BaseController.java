package com.gunnarro.followup.endpoint;

import com.gunnarro.followup.service.ActivityService;
import com.gunnarro.followup.service.FileUploadService;
import com.gunnarro.followup.service.LogEventService;
import com.gunnarro.followup.service.exception.ApplicationException;
import com.gunnarro.followup.service.exception.UploadFileException;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.sql.SQLException;

/**
 * URL security is applied in the spring security config.
 *
 */
public class BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

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
                "Database cummunication problems!");
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
        return handleException(request.getRequestURI(), "/dietmanager/home", ex, "Access Denied. " + ex.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ModelAndView handleSQLException(HttpServletRequest request, Exception ex) {
        return handleException(request.getRequestURI(), request.getRequestURI(), ex, "Technical Database problems!");
    }

    /**
     * Prevent user form binding the id-field dataBinder.setDisallowedFields("id");
     */
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
    }

    private ModelAndView handleException(String requestUrl, String backUrl, Exception e, String errorMsg) {
        LOG.error("Requested URL=" + requestUrl);
        LOG.error("Exception Raised", e);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", new Exception(errorMsg));
        modelAndView.addObject("requestUrl", requestUrl);
        modelAndView.addObject("backUrl", backUrl);
        modelAndView.setViewName("error");
        return modelAndView;
    }

    /**
     * for unit testing only
     */
    public void setAuthenticationFacade(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
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
