package com.dvare.rules.exceptions;


public class IllegalRuleException extends Exception {
    public IllegalRuleException() {
    }

    public IllegalRuleException(String message) {
        super(message);
    }

    public IllegalRuleException(Throwable cause) {
        super(cause);
    }

    public IllegalRuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalRuleException(String message, Throwable cause,
                                boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
