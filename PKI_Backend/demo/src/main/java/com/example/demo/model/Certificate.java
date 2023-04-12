package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.bouncycastle.asn1.x500.X500Name;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "certificate")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
public class Certificate {

    @Id
    @Column(name = "serialNumber")
    private BigInteger serialNumber;
    @NotEmpty
    @NotNull
    @Column(name = "issuerSerialNumber", unique = false)
    private String issuerSerialNumber;
    @NotEmpty
    @NotNull
    @Column(name = "subjectEmail", unique = false)
    private String subjectEmail;
    @NotEmpty
    @NotNull
    @Column(name = "issuerEmail", unique = false)
    private String issuerEmail;
    @NotEmpty
    @NotNull
    @Column(name = "startDate", unique = false)
    private Date startDate;
    @NotEmpty
    @NotNull
    @Column(name = "endDate", unique = false)
    private Date endDate;
    @Column(name = "keyUsage", unique = false)
    private ArrayList<String> keyUsage;
    @Column(name = "extendedKeyUsage", unique = false)
    private ArrayList<String> extendedKeyUsage;

    @Column(name = "revoked", unique = false)
    private Boolean revoked;

    public Certificate() {}

    public Certificate(BigInteger serialNumber, String issuerSerialNumber, String subjectEmail, String issuerEmail, Date startDate, Date endDate, ArrayList<String> keyUsage, ArrayList<String> extendedKeyUsage, Boolean revoked) {
        this.serialNumber = serialNumber;
        this.issuerSerialNumber = issuerSerialNumber;
        this.subjectEmail = subjectEmail;
        this.issuerEmail = issuerEmail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
        this.revoked = revoked;
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

    public String getIssuerEmail() {
        return issuerEmail;
    }

    public void setIssuerEmail(String issuerEmail) {
        this.issuerEmail = issuerEmail;
    }

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

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public String getIssuerSerialNumber() {
        return issuerSerialNumber;
    }

    public void setIssuerSerialNumber(String issuerSerialNumber) {
        this.issuerSerialNumber = issuerSerialNumber;
    }
}
