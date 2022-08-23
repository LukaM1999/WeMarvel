package com.wemarvel.wemarvel.util;

import com.wemarvel.wemarvel.model.RegisteredUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityContextUtils {
    public static RegisteredUser getSignedInUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof RegisteredUser) {
            return (RegisteredUser) principal;
        }
        return null;
    }
}
