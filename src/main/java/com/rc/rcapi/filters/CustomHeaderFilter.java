package com.rc.rcapi.filters;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@Component
public class CustomHeaderFilter extends OncePerRequestFilter {

    @Value("${api.pw}")
    private String apiPw;
    @Value("${api.user}")
    private String apiName;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String apiCredentials = request.getHeader("RC-R-API");

        //validate
        if (apiCredentials == null || apiCredentials.isBlank() || apiCredentials.isEmpty()) {
            log.debug("ApiCredentials might be null, empty or blank");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request");
            return;
        }

        //decode
        String decodedCredentials = new String(Base64.getDecoder().decode(apiCredentials));
        String username = decodedCredentials.substring(0, decodedCredentials.indexOf(":"));
        String password = decodedCredentials.substring(decodedCredentials.indexOf(":") + 1);

        //validate
        if (!password.equals(apiPw) || !username.equals(apiName)) {
            log.debug("ApiPw and ApiUser might not match input");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request");
            return;
        }

        //proceed
        filterChain.doFilter(request, response);

    }
}
