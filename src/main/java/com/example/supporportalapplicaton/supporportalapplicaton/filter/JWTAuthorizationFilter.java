package com.example.supporportalapplicaton.supporportalapplicaton.filter;

import static com.example.supporportalapplicaton.supporportalapplicaton.constant.SecurityConstant.OPTIONS_HTTP_METHOD;
import static com.example.supporportalapplicaton.supporportalapplicaton.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.example.supporportalapplicaton.supporportalapplicaton.utility.JWTTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private JWTTokenProvider jwtTokenProvider;

    public JWTAuthorizationFilter(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Option is sent to gather information about the server

        if(request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)){
            response.setStatus(HttpStatus.OK.value());
        }else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(null == authorizationHeader  || !authorizationHeader.startsWith(TOKEN_PREFIX)){
                filterChain.doFilter(request, response);
                return ;
            }
            String token = authorizationHeader.substring (TOKEN_PREFIX.length());
            String username = jwtTokenProvider.getSubject(token);
            if(StringUtils.hasText( username) && jwtTokenProvider.isTokenValid(username, token) && null == SecurityContextHolder.getContext().getAuthentication()){
                List<GrantedAuthority> authorities = jwtTokenProvider.getGrantedAuthorities(token);
                Authentication authentication = jwtTokenProvider.getAuthentication(username,authorities,request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
