package com.luna.lunaproject.dto;

import com.luna.lunaproject.entity.User;

public class UserResponseDTO {

    private Long userId;
    private String nickName;
    private String email;

    public UserResponseDTO(User user) {
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
