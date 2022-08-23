package com.wemarvel.wemarvel.service.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.dto.ProfileDTO;
import com.wemarvel.wemarvel.repository.RegisteredUserRepository;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.UUID;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUser;
import static java.nio.charset.StandardCharsets.UTF_8;

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
    public RegisteredUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
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
    public String saveProfileImage(String imageUrl) {
        RegisteredUser user = userRepository.findById(getSignedInUser().getId()).orElse(null);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        user.setImageUrl(imageUrl);
        userRepository.save(user);
        return imageUrl;
    }

    @Override
    public String updateUsername(String username) {
        RegisteredUser user = userRepository.findById(getSignedInUser().getId()).orElse(null);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        user.setUsername(username);
        userRepository.save(user);
        return username;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByUsername(username);
    }

    @Override
    public void updateProfile(ProfileDTO profile){
        RegisteredUser user = getSignedInUser();
        if(user == null) throw new UsernameNotFoundException("User not found");
        user.setBirthday(profile.getBirthday());
        user.setGender(profile.getGender());
        user.setLocation(profile.getLocation());
        userRepository.save(user);
    }
}
