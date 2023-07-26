package com.genai.tcoop.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.genai.tcoop.model.constant.Gender;
import com.genai.tcoop.model.constant.UserRole;
import com.genai.tcoop.model.entity.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private UserRole userRole;
    private String name;
    private String nickname;
    private String birthday;
    private Gender gender;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    public static User fromEntity(UserAccount entity) {
        return User.builder()
                .id(entity.getId())
                .username(entity.getUserAccountId())
                .password(entity.getPassword())
                .userRole(entity.getRole())
                .name(entity.getName())
                .nickname(entity.getNickname())
                .birthday(entity.getBirthday())
                .gender(entity.getGender())
                .address(entity.getAddress())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .isDeleted(entity.getIsDeleted())
                .build();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return !this.isDeleted;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !this.isDeleted;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return !this.isDeleted;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return !this.isDeleted;
    }
}
