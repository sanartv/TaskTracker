package com.Polazna.TaskTracker.security;

import com.Polazna.TaskTracker.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtCore jwtCore;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String headerAuth = request.getHeader("Authorization");
            System.out.println("DEBUG: Header Auth: " + headerAuth); // 1. Видим ли заголовок?

            String jwt = parseJwt(request);
            System.out.println("DEBUG: Parsed JWT: " + jwt); // 2. Вытащили ли токен?

            if (jwt != null && jwtCore.validateJwtToken(jwt)) {
                String username = jwtCore.getNameFromJwt(jwt);
                System.out.println("DEBUG: Username from JWT: " + username); // 3. Узнали имя?

                UserDetails userDetails = userService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("DEBUG: Authentication set successfully!"); // 4. Успех?
            } else {
                System.out.println("DEBUG: Token is null or invalid");
            }
        } catch (Exception e) {
            System.err.println("DEBUG ERROR: " + e.getMessage()); // 5. Если упало - почему?
            e.printStackTrace(); // Печатаем полный стек ошибки
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
