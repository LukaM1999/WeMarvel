package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.dto.ProfileDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RegisteredUserService extends UserDetailsService {

    RegisteredUser registerUser(String email, String username);

    RegisteredUser getUserById(Long id);

    RegisteredUser getUserByEmail(String email);

    RegisteredUser getUserByUsername(String username);

    RegisteredUser updateUser(RegisteredUser user);

    String saveProfileImage(String imageUrl);

    String updateUsername(String username);

    void updateProfile(ProfileDTO profile);

    List<ProfileDTO> getProfiles();
}
