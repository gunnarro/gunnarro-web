package com.gunnarro.followup.service.exception;

/**
 * @author mentos
 */
public class NotLoggedInException extends RuntimeException {

    private static final long serialVersionUID = -5086299951349230L;

    private static final String NOT_LOGGED_IN = "Not logged in!";

    public NotLoggedInException() {
        super(NOT_LOGGED_IN);
    }

}
