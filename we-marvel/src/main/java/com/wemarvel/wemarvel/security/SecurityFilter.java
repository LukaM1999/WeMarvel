package com.wemarvel.wemarvel.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.auth.Credentials;
import com.wemarvel.wemarvel.model.auth.SecurityProperties;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import com.wemarvel.wemarvel.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    SecurityService securityService;

    @Autowired
    RegisteredUserService registeredUserService;

    @Autowired
    SecurityProperties securityProps;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        verifyToken(request);
        filterChain.doFilter(request, response);
    }

    private void verifyToken(HttpServletRequest request) {
        FirebaseToken decodedToken = null;
        boolean strictServerSessionEnabled = securityProps.getFirebaseProps().isEnableStrictServerSession();
        String token = securityService.getBearerToken(request);
        logger.info(token);
        try {
            if (!strictServerSessionEnabled) {
                if (token != null && !token.equalsIgnoreCase("undefined")) {
                    decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                }
            }
        } catch (FirebaseAuthException e) {
            log.warn("Firebase warning: " + e.getLocalizedMessage());
        }
        if(decodedToken == null) return;
        RegisteredUser user = registeredUserService.getUserByEmail(decodedToken.getEmail());
        if (user != null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                    new Credentials(decodedToken, token), user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if(user.getRole().getAuthority().equals("ADMIN")) {
                if(Boolean.TRUE.equals(decodedToken.getClaims().get("admin"))) return;
                Map<String, Object> claims = new HashMap<>(Map.of("admin", true));
                try {
                    FirebaseAuth.getInstance().setCustomUserClaims(decodedToken.getUid(), claims);
                    log.info("Firebase claims set for user: " + decodedToken.getUid());
                } catch (FirebaseAuthException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
