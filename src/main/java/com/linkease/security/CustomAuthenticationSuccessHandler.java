package com.linkease.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Redirect to the original requested URL or default page
        String targetUrl = request.getSession().getAttribute(SecurityConstants.TARGET_URL) != null ?
                (String) request.getSession().getAttribute(SecurityConstants.TARGET_URL) : "/";

        // To handle error page
        if("/error".equals(targetUrl)){
            targetUrl = "/";
        }

        request.getSession().removeAttribute(SecurityConstants.TARGET_URL);
        response.sendRedirect(targetUrl);
    }
}
