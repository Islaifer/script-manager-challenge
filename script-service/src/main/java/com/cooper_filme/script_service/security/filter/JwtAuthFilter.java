package com.cooper_filme.script_service.security.filter;

import com.cooper_filme.script_service.security.model.JwtSimpleUser;
import com.cooper_filme.script_service.security.service.JWTTokenService;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JWTTokenService service;

    @Autowired
    public JwtAuthFilter(JWTTokenService service){
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")){
            String token = header.substring(7);

            try{
                JWTClaimsSet claims = service.validateToken(token);

                Long userId = Long.valueOf(claims.getSubject());
                List<String> roles = null;

                if(claims.getClaim("roles") instanceof List<?> roleList)
                    roles = roleList.stream().map(String::valueOf).toList();
                else
                    throw new Exception("Invalid JWT");

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();

                Authentication auth = new UsernamePasswordAuthenticationToken(new JwtSimpleUser(userId, roles), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
