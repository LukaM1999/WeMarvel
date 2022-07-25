package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.RegisteredUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface RegisteredUserService extends UserDetailsService {

    RegisteredUser registerUser(String email);

    RegisteredUser getUserByEmail(String email);

    RegisteredUser updateUser(RegisteredUser user);

}
