package com.jygoh.whoever.global.exception;

public class RedirectException extends RuntimeException {

    private final String redirectUrl;

    public RedirectException(String redirectUrl) {
        super("Redirect to URL: " + redirectUrl);
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

}
