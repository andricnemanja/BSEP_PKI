package com.example.demo.dto;

import com.example.demo.model.Certificate;
import com.example.demo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

public class CertificateDTO {

    private BigInteger serialNumber;
    private String subjectEmail;
    private String commonName;
    private String organization;

    @Autowired
    private UserRepo userRepo;

    public CertificateDTO() {

    }
    public CertificateDTO(BigInteger serialNumber, String subjectEmail, String commonName, String organization) {
        this.serialNumber = serialNumber;
        this.subjectEmail = subjectEmail;
        this.commonName = commonName;
        this.organization = organization;
    }
    public CertificateDTO(Certificate certificate){
        this.serialNumber = certificate.getSerialNumber();
        this.subjectEmail = certificate.getSubjectEmail();

        // OVE PARAMETRE IZVLACIMO IZ USER-A
        this.commonName = null;
        this.organization = null;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSubjectEmail() {
        return subjectEmail;
    }

    public void setSubjectEmail(String subjectEmail) {
        this.subjectEmail = subjectEmail;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
