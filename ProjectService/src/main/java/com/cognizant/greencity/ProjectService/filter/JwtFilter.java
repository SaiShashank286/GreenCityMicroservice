package com.cognizant.greencity.ProjectService.filter;

import com.cognizant.greencity.ProjectService.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	private final HandlerExceptionResolver resolver;

	public JwtFilter(JwtUtil jwtUtil, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.jwtUtil = jwtUtil;
		this.resolver = resolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String header = request.getHeader("Authorization");
			if (header == null || !header.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}

			String token = header.substring(7);
			String email = jwtUtil.extractUsername(token);
			String role = jwtUtil.extractRole(token);

			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null && jwtUtil.validateToken(token)) {
				List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(email, null, authorities);
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
			logger.info("Authenticated user: " + email + " with role: " + role+ SecurityContextHolder.getContext().getAuthentication());
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException ex) {
			resolver.resolveException(request, response, null, ex);
		}
	}
}
