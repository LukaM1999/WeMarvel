package com.wemarvel.wemarvel.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private boolean deleted;
    @Getter
    @Setter
    private String role;
}
