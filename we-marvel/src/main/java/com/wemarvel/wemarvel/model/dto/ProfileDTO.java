package com.wemarvel.wemarvel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wemarvel.wemarvel.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private boolean deleted;
    @Getter
    @Setter
    private String role;
    @Getter
    @Setter
    private String imageUrl;
    @Getter
    @Setter
    private boolean enabled;
    @Getter
    @Setter
    private String location;
    @Getter
    @Setter
    private Gender gender;
    @Getter
    @Setter
    @JsonFormat(pattern = "dd.MM.yyyy.")
    private LocalDate birthday;

    public ProfileDTO(Long id, String username, String imageUrl, boolean enabled, String location, Gender gender, LocalDate birthday) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.enabled = enabled;
        this.location = location;
        this.gender = gender;
        this.birthday = birthday;
    }
}
