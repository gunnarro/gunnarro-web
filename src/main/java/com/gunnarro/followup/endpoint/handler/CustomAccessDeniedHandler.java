package com.gunnarro.followup.endpoint.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e)
            throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            if (LOG.isInfoEnabled()) {
                LOG.info("User '%s' attempted to access the protected URL: %s", auth.getName(), httpServletRequest.getRequestURI());
            }
        }
        httpServletResponse.sendRedirect(String.format("%s/403?requesturi=%s", httpServletRequest.getContextPath(), httpServletRequest.getRequestURI()));
    }
}
