package com.gunnarro.followup.service.exception;

/**
 * @author mentos
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = -5086299951349235980L;

    public ApplicationException(String message) {
        super(message);
    }

}
