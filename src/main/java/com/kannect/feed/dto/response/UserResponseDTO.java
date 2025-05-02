package com.kannect.feed.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String email;
    private String userName;
    private String firstName;
    private String lastName;
    private String department;
    private String techStack;
    private String profilePhotoUrl;
    private Set<String> roleNames;
    private Boolean activatedByHr;
    private Integer walletBalance;
    private LocalDateTime lastLogin;
}
