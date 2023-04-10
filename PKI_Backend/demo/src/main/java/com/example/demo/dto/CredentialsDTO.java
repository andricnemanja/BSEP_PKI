package com.example.demo.dto;

public class CredentialsDTO {
    public String userID;
    public String role;

    public CredentialsDTO(){

    }
    public CredentialsDTO(String userID, String role){
        this.userID = userID;
        this.role = role;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
