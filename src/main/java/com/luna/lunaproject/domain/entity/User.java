package com.luna.lunaproject.domain.entity;


import lombok.Data;

import java.util.UUID;

@Data
public class User {

    private UUID id;
    private String username;
    private String email;
    private String password;
    private String profilePicUrl;
    private String bannerUrl;
}
