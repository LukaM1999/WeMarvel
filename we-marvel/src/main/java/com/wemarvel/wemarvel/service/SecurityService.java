package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.auth.Credentials;

import javax.servlet.http.HttpServletRequest;

public interface SecurityService {
    RegisteredUser getUser();
    Credentials getCredentials();
    boolean isPublic();
    String getBearerToken(HttpServletRequest request);
}
