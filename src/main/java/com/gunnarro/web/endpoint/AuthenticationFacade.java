package com.gunnarro.web.endpoint;

import com.gunnarro.web.domain.user.LocalUser;
import com.gunnarro.web.service.exception.NotLoggedInException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class AuthenticationFacade implements AuthenticationFacadeInterface {

    @Override
    public Authentication getAuthentication() {
        log.debug(".....getAuth...");
        Authentication authentication = null;
        if (SecurityContextHolder.getContext() != null) {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        }
        log.debug("authentication: {}", authentication);
        return authentication;
    }

    /**
     * Method get logged in user credentials will fail, when called for users who
     * are not logged in.
     */
    @Override
    public LocalUser getLoggedInUser() {
        LocalUser user = null;
        if (getAuthentication() != null && getAuthentication().getPrincipal() instanceof LocalUser) {
                user = (LocalUser) getAuthentication().getPrincipal();
        }
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new NotLoggedInException();
        }
        return user;
    }

}