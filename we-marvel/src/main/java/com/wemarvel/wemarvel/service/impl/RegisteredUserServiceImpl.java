package com.wemarvel.wemarvel.service.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.StorageClient;
import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.Role;
import com.wemarvel.wemarvel.model.dto.ProfileDTO;
import com.wemarvel.wemarvel.repository.RegisteredUserRepository;
import com.wemarvel.wemarvel.repository.RoleRepository;
import com.wemarvel.wemarvel.service.NotificationSettingsService;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.UUID;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUser;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class RegisteredUserServiceImpl implements RegisteredUserService {

    @Autowired
    private RegisteredUserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private NotificationSettingsService notificationSettingsService;

    @Override
    @Transactional
    public RegisteredUser registerUser(String email, String username) {
        if(userRepository.getByEmail(email) != null ||
                userRepository.getByUsername(username) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        RegisteredUser user = new RegisteredUser(email, username);
        Role role = roleRepository.findByRoleName("USER");
        System.out.println(role.getAuthority());
        user.setRole(role);
        RegisteredUser newUser = userRepository.save(user);
        notificationSettingsService.createNotificationSettings(newUser.getId());
        return newUser;
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

    @Override
    public List<ProfileDTO> getProfiles() {
        return userRepository.getProfiles();
    }

    @Override
    public void disableUser(Long userId) {
        RegisteredUser signedInUser = getSignedInUser();
        if(signedInUser == null) throw new UsernameNotFoundException("You are not logged in");
        RegisteredUser user = userRepository.findById(userId).orElse(null);
        if(user == null) throw new UsernameNotFoundException("User not found");
        if(!signedInUser.getId().equals(userId) &&
                !signedInUser.getRole().getAuthority().equals("ADMIN"))
            throw new IllegalArgumentException("You are not authorized to disable this user");
        user.setEnabled(false);
        userRepository.save(user);
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(user.getEmail());
            if(userRecord == null) return;
            FirebaseAuth.getInstance().updateUser(new UserRecord.UpdateRequest(userRecord.getUid())
                    .setDisabled(true));
            FirebaseAuth.getInstance().revokeRefreshTokens(userRecord.getUid());
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void enableUser(Long userId) {
        RegisteredUser signedInUser = getSignedInUser();
        if(signedInUser == null) throw new UsernameNotFoundException("You are not logged in");
        RegisteredUser user = userRepository.findById(userId).orElse(null);
        if(user == null) throw new UsernameNotFoundException("User not found");
        if(!signedInUser.getId().equals(userId) &&
                !signedInUser.getRole().getAuthority().equals("ADMIN"))
            throw new IllegalArgumentException("You are not authorized to enable this user");
        user.setEnabled(true);
        userRepository.save(user);
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(user.getEmail());
            if(userRecord == null) return;
            FirebaseAuth.getInstance().updateUser(new UserRecord.UpdateRequest(userRecord.getUid())
                    .setDisabled(false));
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String updateEmail(String email) {
        RegisteredUser user = getSignedInUser();
        if(user == null) throw new UsernameNotFoundException("You are not logged in");
        if(userRepository.getByEmail(email) != null) throw new IllegalArgumentException("Email already exists");
        String oldEmail = user.getEmail();
        user.setEmail(email);
        userRepository.save(user);
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(oldEmail);
            if(userRecord == null) return email;
            FirebaseAuth.getInstance().updateUser(new UserRecord.UpdateRequest(userRecord.getUid())
                    .setEmail(email));
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
        return email;
    }
}
