package com.example.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
	
	private static final String AUTH_HEADER = "Authorization";
	
	private final UserDetailsService userService;
	private final TokenUtils tokenUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = request.getHeader(AUTH_HEADER);
		if (token != null && !token.trim().equals("")) {
			UserDetails user = this.userService.loadUserByUsername(this.tokenUtils.getEmail(token));
			if (user != null && this.tokenUtils.validateToken(user, token)) {
				AuthToken authToken = new AuthToken(user, token);
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);		
	}
	
}
