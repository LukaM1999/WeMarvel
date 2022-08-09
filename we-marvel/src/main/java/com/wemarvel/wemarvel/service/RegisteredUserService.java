package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.RegisteredUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface RegisteredUserService extends UserDetailsService {

    RegisteredUser registerUser(String email, String username);

    RegisteredUser getUserByEmail(String email);

    RegisteredUser getUserByUsername(String username);

    RegisteredUser updateUser(RegisteredUser user);

}
