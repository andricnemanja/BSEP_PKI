package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "ocspList")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
public class OCSPObject {
    @Id
    @Column(name = "serialNumber")
    private String serialNumber;
    @NotEmpty
    @NotNull
    @Column(name = "revoked", unique = false)
    private Boolean revoked;

    //@OneToMany
    @ElementCollection
    @Column(name = "signedCertificates", unique = false)
    private Set<String> signedCertificates;

    public OCSPObject(String serialNumber, Boolean revoked, Set<String> signedCertificates) {
        this.serialNumber = serialNumber;
        this.revoked = revoked;
        this.signedCertificates = signedCertificates;
    }

    public OCSPObject() {}

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revooked) {
        this.revoked = revooked;
    }

    public Set<String> getSignedCertificates() {
        return signedCertificates;
    }

    public void setSignedCertificates(Set<String> signedCertificates) {
        this.signedCertificates = signedCertificates;
    }
}
