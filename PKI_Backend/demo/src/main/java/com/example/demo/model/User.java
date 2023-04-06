package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Entity
@Table(name = "subjects")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="u_type", discriminatorType = DiscriminatorType.INTEGER)
public class User {

    @Id
    @Column(name = "id")
    private Integer id;
    @NotEmpty
    @NotNull
    @Column(name = "commonName", unique = false)
    private String commonName;
    @NotEmpty
    @NotNull
    @Column(name = "surname", unique = false)
    private String surname;
    @NotEmpty
    @NotNull
    @Column(name = "givenName", unique = false)
    private String givenName;
    @NotEmpty
    @NotNull
    @Column(name = "organization", unique = false)
    private String organization;
    @NotEmpty
    @NotNull
    @Column(name = "organizationUnit", unique = false)
    private String organizationUnit;
    @NotEmpty
    @NotNull
    @Column(name = "country", unique = false)
    private String country;
    @NotEmpty
    @NotNull
    @Column(name = "email", unique = true)
    private String email;
    @NotEmpty
    @NotNull
    @Column(name = "password", unique = true)
    private String password;
    @Column(name = "certificatesSerialNumbers", unique = false)
    private ArrayList<String> certificatesSerialNumbers;

    public User(Integer id, String commonName, String surname, String givenName, String organization, String organizationUnit,
                   String country, String email, String password, ArrayList<String> certificatesSerialNumbers) {
        this.id = id;
        this.commonName = commonName;
        this.surname = surname;
        this.givenName = givenName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.email = email;
        this.password = password;
        this.certificatesSerialNumbers = certificatesSerialNumbers;
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getCertificatesSerialNumbers() {
        return certificatesSerialNumbers;
    }

    public void setCertificatesSerialNumbers(ArrayList<String> certificatesSerialNumbers) {
        this.certificatesSerialNumbers = certificatesSerialNumbers;
    }
}
