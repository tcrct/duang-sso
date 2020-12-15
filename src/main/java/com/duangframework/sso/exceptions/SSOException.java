package com.duangframework.sso.exceptions;

public class SSOException extends RuntimeException {

    public SSOException() {
        super();
    }

    public SSOException(String msg) {
        super(msg);
    }

    public SSOException(String msg , Throwable cause) {
        super(msg, cause);
    }


}
