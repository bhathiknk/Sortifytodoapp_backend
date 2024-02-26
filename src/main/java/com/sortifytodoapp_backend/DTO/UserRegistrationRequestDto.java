package com.sortifytodoapp_backend.DTO;

public class UserRegistrationRequestDto {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public UserRegistrationRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}