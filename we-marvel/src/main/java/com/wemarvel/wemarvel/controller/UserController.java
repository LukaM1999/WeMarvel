package com.wemarvel.wemarvel.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.wemarvel.wemarvel.model.NotificationSettings;
import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.dto.ProfileDTO;
import com.wemarvel.wemarvel.model.dto.RegistrationDTO;
import com.wemarvel.wemarvel.service.NotificationSettingsService;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUser;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RegisteredUserService registeredUserService;

    @PostMapping("")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDTO registration) throws URISyntaxException {
        try {
            FirebaseAuth.getInstance().getUserByEmail(registration.getEmail());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User could not be created");
        }
        RegisteredUser user = registeredUserService.registerUser(registration.getEmail(), registration.getUsername());
        return ResponseEntity.created(new URI("/user/" + user.getUsername())).build();
    }
    @PatchMapping("/image")
    public String uploadImage(@RequestBody ProfileDTO profile) {
        return registeredUserService.saveProfileImage(profile.getImageUrl());
    }

    @GetMapping("/{username}")
    public ResponseEntity<RegisteredUser> getRegisteredUser(@PathVariable String username){
        return ResponseEntity.ok(registeredUserService.getUserByUsername(username));
    }

    @PatchMapping("/username")
    public String updateUsername(@RequestBody ProfileDTO profile) {
        return registeredUserService.updateUsername(profile.getUsername());
    }

    @PatchMapping("/email")
    public String updateEmail(@RequestBody ProfileDTO profile) {
        return registeredUserService.updateEmail(profile.getEmail());
    }

    @PatchMapping("/profile")
    public void updateProfile(@RequestBody ProfileDTO profile) {
        registeredUserService.updateProfile(profile);
    }

    @GetMapping("/profile")
    public List<ProfileDTO> getProfiles(){
        return registeredUserService.getProfiles();
    }

    @PatchMapping("/{userId}/disable")
    public void disableUser(@PathVariable Long userId) {
        registeredUserService.disableUser(userId);
    }

    @PatchMapping("/{userId}/enable")
    public void enableUser(@PathVariable Long userId) {
        registeredUserService.enableUser(userId);
    }
}
