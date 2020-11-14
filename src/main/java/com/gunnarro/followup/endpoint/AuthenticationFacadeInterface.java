package com.gunnarro.followup.endpoint;

import com.gunnarro.followup.domain.user.LocalUser;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacadeInterface {

    /**
     * 
     * @return
     */
    Authentication getAuthentication();

    /**
     * 
     * @return
     */
    LocalUser getLoggedInUser();
}
