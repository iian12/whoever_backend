package com.jygoh.whoever.global.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class HttpSessionService {

    private final HttpSession session;

    public HttpSessionService(HttpSession session) {
        this.session = session;
    }

    public void setAttribute(String key, Object value) {
        session.setAttribute(key, value);
    }

    public Object getAttribute(String key) {
        return session.getAttribute(key);
    }
}
