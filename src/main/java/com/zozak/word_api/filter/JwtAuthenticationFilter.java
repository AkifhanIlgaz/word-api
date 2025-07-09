package com.zozak.word_api.filter;

import com.zozak.word_api.exception.BaseException;
import com.zozak.word_api.exception.ErrorMessage;
import com.zozak.word_api.exception.MessageType;
import com.zozak.word_api.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public final String AUTHORIZATION_HEADER  ="Authorization";
    public final String BEARER_TOKEN_PREFIX = "Bearer ";

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/auth/") || request.getRequestURI().startsWith("/api/word/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        final String accessToken;
        final String email;

        if (authHeader == null || !authHeader.startsWith(BEARER_TOKEN_PREFIX) || authHeader.trim().isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        accessToken = authHeader.trim().substring(BEARER_TOKEN_PREFIX.length());
        email = jwtService.getEmailFromToken(accessToken);

        try {
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (userDetails != null && !jwtService.isExpired(accessToken)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(userDetails);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException e) {
            throw new BaseException(new ErrorMessage(e.getMessage(), MessageType.EXPIRED_TOKEN));
        } catch (Exception e) {
            throw new BaseException(new ErrorMessage(e.getMessage(), MessageType.INTERNAL_ERROR));
        }

        filterChain.doFilter(request, response);
    }
}
