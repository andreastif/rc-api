package com.rc.rcapi.configs;

import com.rc.rcapi.filters.FirebaseJwtFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Value("${api.user}")
    public String apiUsername;

    @Value("${api.pw}")
    public String apiPw;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(conf -> {
                    conf.authenticationEntryPoint(doNotShowLoginWindow());
                })
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req.anyRequest().authenticated());

        http.addFilterBefore(new FirebaseJwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint doNotShowLoginWindow()  {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

                if (!response.isCommitted()) {
                    response.addHeader("WWW-Authenticate", "Basic realm=\"YourApp\"");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                } else {
//                    response.sendError(authException.getCause()., authException.getMessage());
                }
            }
        };
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails apiUser = User.builder()
                .username(apiUsername)
                .password(passwordEncoder().encode(apiPw))
                .build();

        return new InMemoryUserDetailsManager(apiUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
