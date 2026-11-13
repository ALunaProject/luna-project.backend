package com.luna.lunaproject.domain.enums;

public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private String role;
    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role; //saber oq o zé é
    }
}
