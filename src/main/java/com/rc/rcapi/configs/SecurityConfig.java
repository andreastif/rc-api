package com.rc.rcapi.configs;

import com.rc.rcapi.filters.CustomHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Value("${api.user}")
    public String apiName;
    @Value("${api.pw}")
    public String apiPw;
    private final CustomHeaderFilter customHeaderFilter;

    @Autowired
    public SecurityConfig(CustomHeaderFilter customHeaderFilter) {
        this.customHeaderFilter = customHeaderFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);
        //must be authenticated jwt
        http
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("api/v1/auth/**")
                                .authenticated()
                                .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2rs ->
                        oauth2rs.jwt(Customizer.withDefaults()));
        //global filters
        http
                .addFilterBefore(customHeaderFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
