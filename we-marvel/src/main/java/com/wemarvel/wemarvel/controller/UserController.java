package com.wemarvel.wemarvel.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.dto.RegistrationDTO;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RegisteredUserService registeredUserService;

    @PostMapping("")
    public ResponseEntity registerUser(@RequestBody RegistrationDTO registration) throws URISyntaxException {
        try {
            FirebaseAuth.getInstance().getUserByEmail(registration.getEmail());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User could not be created");
        }
        RegisteredUser user = registeredUserService.registerUser(registration.getEmail(), registration.getUsername());
        return ResponseEntity.created(new URI("/user/" + user.getEmail())).build();
    }
}
