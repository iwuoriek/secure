package com.example.secure.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

public class JwtFilter extends GenericFilterBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);
    private static final String BEARER = "Bearer";
    private BankingUserDetailsService userDetailsService;

    public JwtFilter(BankingUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        LOGGER.info("Process request to check for token");
        String headerValue = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        getBearerToken(headerValue).ifPresent(token -> {
            userDetailsService.loadUserByJwtToken(token).ifPresent(userDetails -> {
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities()));
            });
        });
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Optional<String> getBearerToken(String headerVal) {
        if (headerVal != null && headerVal.startsWith(BEARER)) {
            LOGGER.info("Found a bearer token");
            return Optional.of(headerVal.replace(BEARER, "").trim());
        }
        LOGGER.warn("No token found");
        return Optional.empty();
    }
}
