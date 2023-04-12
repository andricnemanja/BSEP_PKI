package com.example.demo.dto;

import java.util.ArrayList;
import java.util.Date;

public class CertificateParamsDTO {
    public String certificateType;
    public Date notBefore;
    public String issuer;
    public ArrayList<String> keyUsage;
    public ArrayList<String> extendedKeyUsage;

    public String commonName;
    public String surname;
    public String givenName;
    public String organization;
    public String organizationUnit;
    public String country;
    public String email;
    public String password;


    public Date startDate;
    public Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public boolean Istekao(){
        if(this.endDate.before(this.startDate))
            return false;
        else if(this.startDate==null || this.endDate==null || this.email.equals("") || this.email==null)
            return false;
        return true;
    }




    public CertificateParamsDTO(String certificateType, Date notBefore, String issuer, ArrayList<String> keyUsage, ArrayList<String> extendedKeyUsage, String commonName,
                                String surname, String givenName, String organization, String organizationUnit, String country, String email, String password) {
        this.certificateType = certificateType;
        this.notBefore = notBefore;
        this.issuer = issuer;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
        this.commonName = commonName;
        this.surname = surname;
        this.givenName = givenName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.email = email;
        this.password = password;
    }


    public CertificateParamsDTO() {}

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public Date getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Date notBefore) {
        this.notBefore = notBefore;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public ArrayList<String> getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(ArrayList<String> keyUsage) {
        this.keyUsage = keyUsage;
    }

    public ArrayList<String> getExtendedKeyUsage() {
        return extendedKeyUsage;
    }

    public void setExtendedKeyUsage(ArrayList<String> extendedKeyUsage) {
        this.extendedKeyUsage = extendedKeyUsage;
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
}