package com.example.demo.controller;

import com.example.demo.dto.CertificateDTO;
import com.example.demo.dto.CertificateParamsDTO;
import com.example.demo.model.Certificate;
import com.example.demo.model.User;
import com.example.demo.service.CertificateService;
import com.example.demo.service.KeyStoreService;
import com.example.demo.service.OCSPService;
import com.example.demo.service.UserService;
import com.example.demo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/CertificateController")
public class CertificateController {

    @Autowired
    private UserService userService;

    @Autowired
    private KeyStoreService keyStoreService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private OCSPService ocspService;

    @Autowired
    private Utils utils;

    @PostMapping(value = "/generateCertificate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity selfSignedGenerateCertificate(@RequestBody CertificateParamsDTO certificateParamsDTO) {

        X509Certificate certificate;
        switch (certificateParamsDTO.certificateType){

            case "self-signed":
                certificate = certificateService.selfSignedCertificate(certificateParamsDTO);
                break;
            case "intermediary":
                certificate = certificateService.intermediaryCertificate(certificateParamsDTO);
                break;
            case "end-entity":
                certificate = certificateService.endEntityCertificate(certificateParamsDTO);
                break;
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if(certificate != null) {
            System.out.print(certificate + "\n");
            return new ResponseEntity(HttpStatus.CREATED);
        }
        else {
            System.out.print("There was a problem Creating sertificate!\n");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/getBySubjectEmail/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<CertificateDTO>> getAll(@RequestParam(name = "email") String email) {

        User user = userService.findByEmail(email);

        ArrayList<CertificateDTO> certificateDtos = new ArrayList<>();
        for (Certificate certificate : certificateService.getBySubjectEmail(email)) {

            java.security.cert.Certificate check = keyStoreService.getCertificate(certificate.getSerialNumber().toString());
            X509Certificate currCert = (X509Certificate)check;

            if(!certificate.getRevoked() && currCert.getBasicConstraints() > -1 &&
                    certificate.getEndDate().getYear() - certificate.getStartDate().getYear() > 1) {

                CertificateDTO c = new CertificateDTO(certificate);

                c.setCommonName(user.getCommonName());
                c.setOrganization(user.getOrganization());
                User issuer = userService.findByEmail(c.getIssuerEmail());
                c.setIssuerCommonName(issuer.getCommonName());
                c.setIssuerOrganization(issuer.getOrganization());

                certificateDtos.add(c);
            }
        }

        return new ResponseEntity(certificateDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/getBySubjectEmail/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArrayList<CertificateDTO>> getAll() {

        ArrayList<CertificateDTO> certificateDtos = new ArrayList<>();
        for (Certificate certificate : certificateService.getAll()) {

            java.security.cert.Certificate check = keyStoreService.getCertificate(certificate.getSerialNumber().toString());
            X509Certificate currCert = (X509Certificate)check;

            if(!certificate.getRevoked() && currCert.getBasicConstraints() > -1 &&
                    certificate.getEndDate().getYear() - certificate.getStartDate().getYear() > 1) {

                User user = userService.findByEmail(certificate.getSubjectEmail());

                CertificateDTO c = new CertificateDTO(certificate);
                c.setCommonName(user.getCommonName());
                c.setOrganization(user.getOrganization());
                certificateDtos.add(c);
            }
        }

        return new ResponseEntity(certificateDtos, HttpStatus.OK);
    }


    @GetMapping("/export/{alias}")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable String alias) {

        try {
            java.security.cert.Certificate cert = keyStoreService.getCertificate(alias);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(cert.getEncoded());
            outputStream.close();
            byte[] certBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", alias + ".cer");

            return new ResponseEntity<>(certBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error exporting certificate: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/revoke/{serialNumber}")
    public ResponseEntity<?> revokeCertificate(@PathVariable String serialNumber) {
        return (ResponseEntity<?>) ocspService.revokeCertificate(serialNumber);
    }

}
