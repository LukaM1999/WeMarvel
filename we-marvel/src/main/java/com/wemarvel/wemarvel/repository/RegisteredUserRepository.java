package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.dto.ProfileDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {

    RegisteredUser getByEmail(String email);

    RegisteredUser getByUsername(String username);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ProfileDTO(u.id, u.username, u.imageUrl, " +
            "u.location, u.gender, u.birthday) " +
            "FROM RegisteredUser u")
    List<ProfileDTO> getProfiles();
}
