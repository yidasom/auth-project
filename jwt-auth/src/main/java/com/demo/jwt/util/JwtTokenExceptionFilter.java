package com.demo.jwt.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * com.demo.jwt.util
 *
 * @author : idasom
 */
public class JwtTokenExceptionFilter extends OncePerRequestFilter {
    public JwtTokenExceptionFilter(JwtProvider jwtProvider) {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            response.sendError(e.getStackTrace().getoc ,e.getMessage());
        }
    }
}
