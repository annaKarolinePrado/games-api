package com.games.jwt;

import com.games.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtUtil.getSecretKey().getBytes(StandardCharsets.UTF_8))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                if (username != null) {
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            username, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
