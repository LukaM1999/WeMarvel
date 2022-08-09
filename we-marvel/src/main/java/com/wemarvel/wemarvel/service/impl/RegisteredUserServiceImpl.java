package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.repository.RegisteredUserRepository;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RegisteredUserServiceImpl implements RegisteredUserService {

    @Autowired
    private RegisteredUserRepository userRepository;

    @Override
    public RegisteredUser registerUser(String email, String username) {
        if(userRepository.getByEmail(email) != null ||
                userRepository.getByUsername(username) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        RegisteredUser user = new RegisteredUser(email, username);
        return userRepository.save(user);
    }

    @Override
    public RegisteredUser getUserByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public RegisteredUser getUserByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public RegisteredUser updateUser(RegisteredUser user) {
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByUsername(username);
    }
}
