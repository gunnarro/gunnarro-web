package com.gunnarro.web.endpoint;

import com.gunnarro.web.domain.user.LocalUser;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacadeInterface {

    /**
     *
     */
    Authentication getAuthentication();

    /**
     *
     */
    LocalUser getLoggedInUser();
}
