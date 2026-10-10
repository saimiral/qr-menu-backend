package com.saimiral.qr_menu_backend.security;

import com.saimiral.qr_menu_backend.service.SseTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final SseTokenService sseTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String email = null;
        String role = null;

        String bearer = extractBearerToken(request);
        if (bearer != null) {
            if (jwtService.isTokenValid(bearer)) {
                email = jwtService.extractEmail(bearer);
                role = jwtService.extractRole(bearer);
            }
        } else if (request.getRequestURI().endsWith("/stream")) {
            // Το native EventSource δεν στέλνει headers: δεχόμαστε ΜΟΝΟ short-lived one-time ticket
            String ticket = request.getParameter("token");
            if (ticket != null) {
                Optional<SseTokenService.SseTicket> found = sseTokenService.consume(ticket);
                if (found.isPresent()) {
                    email = found.get().email();
                    role = found.get().role();
                }
            }
        }

        if (email == null) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                email,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private String extractBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}