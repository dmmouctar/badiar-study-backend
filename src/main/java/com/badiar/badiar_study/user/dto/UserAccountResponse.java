package com.badiar.badiar_study.user.dto;

import com.badiar.badiar_study.user.entity.UserAccount.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAccountResponse {

    private Long          id;
    private String        firstName;
    private String        lastName;
    private String        fullName;
    private String        email;
    private UserRole      role;
    private String        photoUrl;
    private Boolean       isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
