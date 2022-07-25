package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.auth.Credentials;
import com.wemarvel.wemarvel.model.auth.SecurityProperties;
import com.wemarvel.wemarvel.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;


@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    SecurityProperties securityProps;

    public RegisteredUser getUser() {
        RegisteredUser userPrincipal = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object principal = securityContext.getAuthentication().getPrincipal();
        if (principal instanceof RegisteredUser) {
            userPrincipal = ((RegisteredUser) principal);
        }
        return userPrincipal;
    }

    public Credentials getCredentials() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return (Credentials) securityContext.getAuthentication().getCredentials();
    }

    public boolean isPublic() {
        return securityProps.getAllowedPublicApis().contains(httpServletRequest.getRequestURI());
    }

    public String getBearerToken(HttpServletRequest request) {
        String bearerToken = null;
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            bearerToken = authorization.substring(7);
        }
        return bearerToken;
    }
}