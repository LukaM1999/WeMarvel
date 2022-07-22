package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.RegisteredUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface RegisteredUserService extends UserDetailsService {

    RegisteredUser registerUser(String username, String password);

    RegisteredUser getUserByUsername(String username);

    RegisteredUser updateUser(RegisteredUser user);

}
