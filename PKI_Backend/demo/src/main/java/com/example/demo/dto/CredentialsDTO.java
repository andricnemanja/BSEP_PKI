package com.example.demo.dto;

public class CredentialsDTO {
    public String userID;
    public String email;

    public CredentialsDTO(){

    }
    public CredentialsDTO(String userID, String email){
        this.userID = userID;
        this.email = email;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
