package com.gunnarro.web.endpoint;

import com.gunnarro.web.domain.user.LocalUser;
import com.gunnarro.web.service.exception.ApplicationException;
import com.gunnarro.web.service.exception.NotLoggedInException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class LoginController extends BaseController {

    public static final String PUBLIC_PAGE = "/index";
    public static final String HOME_PAGE = "/home";
    public static final String ADMIN_PAGE = "/admin";
    public static final String LOGIN_PAGE = "/login";

    @GetMapping("/home")
    public String home() {
        String redirectUrl;
        try {
            LocalUser user = authenticationFacade.getLoggedInUser();
            log.debug("logged in as user: {}", user);
            if (user == null) {
                // this was an ANONYMOUS user, i.e, not logged in
                redirectUrl = HOME_PAGE;
            } else {
                // direct non activated users to the default start page
                if (!user.isActivated()) {
                    redirectUrl = HOME_PAGE;
                }

                if (user.isAdmin()) {
                    redirectUrl = ADMIN_PAGE;
                } else if (user.isUser()) {
                    redirectUrl = HOME_PAGE;
                } else {
                    // Not authenticated direct back to login page
                    redirectUrl = LOGIN_PAGE;
                }
            }
            log.debug("redirect user to: {}", redirectUrl);
            return "redirect:" + redirectUrl;
        } catch (NotLoggedInException nla) {
            log.debug("not legged in, redirect to public page: {}", PUBLIC_PAGE);
            return "redirect:" + PUBLIC_PAGE;
        } catch (ApplicationException ae) {
            log.error("", ae);
            // Will direct user to the error page
            throw ae;
        }
    }

    /**
     * @return the login page
     */
    @GetMapping("/public/login")
    public String login() {
        return "/public/login";
    }

    /**
     * logout performs Invalidates HTTP Session ,then unbinds any objects bound
     * to it. Removes the Authentication from the SecurityContext to prevent
     * issues with concurrent requests. Explicitly clears the context value from
     * the current thread.
     */
    @GetMapping("/perform-logout-user")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        logoutLocalUser(request, response);
        // logoutFaceBook();
        return "redirect:login?loggedout";
    }

    private void logoutLocalUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = authenticationFacade.getAuthentication();
        log.debug("logout: {}", auth.getPrincipal());
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }
}
