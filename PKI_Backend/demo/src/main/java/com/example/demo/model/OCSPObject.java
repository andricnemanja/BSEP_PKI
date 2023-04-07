package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

    public OCSPObject(String serialNumber, Boolean revoked) {
        this.serialNumber = serialNumber;
        this.revoked = revoked;
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
}
