package com.rc.rcapi.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class FirebaseJwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = request.getHeader("TOKEN");
        log.info("FirebaseJwtFilter ||| TOKEN FROM HEADER: " + token);

        log.info("FirebaseJwtFilter ||| request.getRequestURI(): " + request.getRequestURI());


        if (request.getHeader("TOKEN") == null && request.getRequestURI().contains("/api/v1/auth")) {
            log.info("FirebaseJwtFilter ||| INSIDE IF: " + request.getRequestURI().contains("/api/v1/auth"));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Authentication Token");
            return;
        }

        filterChain.doFilter(request, response);

    }
}
