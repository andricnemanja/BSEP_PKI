package com.example.demo.service;

import com.example.demo.model.Certificate;
import com.example.demo.model.OCSPObject;
import com.example.demo.repo.CertificateRepository;
import com.example.demo.repo.OCSPRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

@Service
public class OCSPService {
    @Autowired
    private OCSPRepo ocspRepo;
    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private KeyStoreService keyStoreService;

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

        BigInteger sN = new BigInteger(serialNumber);
        Certificate certificate = certificateRepository.getBySerialNumber(sN);
        certificate.setRevoked(true);
        certificateRepository.save(certificate);

        for(String sn: CACertificate.getSignedCertificates()){
            revokeCertificate(sn);
        }
        return CACertificate.getSignedCertificates();
    }


    public boolean isValid(String serialNumber){
        try {
            Certificate certModel = certificateRepository.getBySerialNumber(new BigInteger(serialNumber));
            String issuerSerialNumber = certModel.getIssuerSerialNumber();

            do{
                java.security.cert.Certificate certificate = keyStoreService.getCertificate(serialNumber);
                if(!isRevoked(serialNumber)){
                    return false;
                }
                X509Certificate x509Cert = (X509Certificate)certificate;
                x509Cert.checkValidity(new Date());
                java.security.cert.Certificate issuerCertificate = keyStoreService.getCertificate(issuerSerialNumber);
                certificate.verify(issuerCertificate.getPublicKey());

                serialNumber = issuerSerialNumber;
                certModel = certificateRepository.getBySerialNumber(new BigInteger(serialNumber));
                issuerSerialNumber = certModel.getIssuerSerialNumber();
            }while(serialNumber != issuerSerialNumber);
        }
        catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            return false;
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


}
