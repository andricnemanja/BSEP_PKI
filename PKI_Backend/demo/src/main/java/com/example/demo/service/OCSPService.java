package com.example.demo.service;

import com.example.demo.dto.CertificateParamsDTO;
import com.example.demo.model.Issuer;
import com.example.demo.model.OCSPObject;
import com.example.demo.repo.OCSPRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Set;

@Service
public class OCSPService {
    @Autowired
    private OCSPRepo ocspRepo;


    private CertificateParamsDTO certificateParamsDTO;
    private Issuer issuer;


    public OCSPObject findBySerialNumber(String serialNumber) {
        return ocspRepo.findBySerialNumber(serialNumber);
    }

    public Boolean isRevoked(String serialNumber) {
        OCSPObject ocspObject = ocspRepo.findBySerialNumber(serialNumber);
        if(ocspObject.getRevoked())
        {
            return true;
        }
        return false;
    }

    public void addSignedCertificate(String CASerialNumber, String certificateSertificateNUmber) {
        OCSPObject CACertificate = ocspRepo.findBySerialNumber(CASerialNumber);
        Set<String> signedCertificates = CACertificate.getSignedCertificates();
        signedCertificates.add(certificateSertificateNUmber);
        CACertificate.setSignedCertificates(signedCertificates);
        ocspRepo.save(CACertificate);
    }

    public Set<String> revokeCertificate(String serialNumber) {
        OCSPObject CACertificate = ocspRepo.findBySerialNumber(serialNumber);
        CACertificate.setRevoked(true);
        ocspRepo.save(CACertificate);

        return CACertificate.getSignedCertificates();
    }


    public boolean isCertificateValid(String serialNumber) {

        OCSPObject CACertificate = ocspRepo.findBySerialNumber(serialNumber);

        if( serialNumber==null) return false;

       else if(isRevoked(serialNumber) || certificateParamsDTO.Istekao()) return false;

       else  if(Objects.equals(CACertificate.getSerialNumber(), issuer.getPrivateKey())) {
            return true;
        }
        else {
            return isCertificateValid(CACertificate.getSerialNumber());
        }
    }



}
