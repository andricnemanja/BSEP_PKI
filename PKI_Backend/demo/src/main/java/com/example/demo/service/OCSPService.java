package com.example.demo.service;

import com.example.demo.model.OCSPObject;
import com.example.demo.repo.OCSPRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OCSPService {
    @Autowired
    private OCSPRepo ocspRepo;
    @Autowired
    private CertificateService certificateService;

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

        certificateService.revokeCertificate(serialNumber); //TODO testirati

        for(String sn: CACertificate.getSignedCertificates()){
            revokeCertificate(sn);
        }
        return CACertificate.getSignedCertificates();
    }

}
