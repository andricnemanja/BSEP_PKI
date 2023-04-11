package com.example.demo.controller;

import com.example.demo.dto.CertificateParamsDTO;
import com.example.demo.model.Issuer;
import com.example.demo.model.Subject;
import com.example.demo.model.User;
import com.example.demo.service.CertificateService;
import com.example.demo.service.KeyStoreService;
import com.example.demo.service.UserService;
import com.example.demo.utils.Utils;
import org.apache.commons.lang3.time.DateUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

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
    private Utils utils;

    @PostMapping(value = "/generateCertificate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity generateCertificate(@RequestBody CertificateParamsDTO certificateParamsDTO) {
        User user = userService.findByEmail(certificateParamsDTO.email);
        if (user == null && !certificateParamsDTO.email.equals("")) {
            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString();

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(certificateParamsDTO.password);

            user = new User(uuidString, certificateParamsDTO.commonName, certificateParamsDTO.surname, certificateParamsDTO.givenName, certificateParamsDTO.organization,
                    certificateParamsDTO.organizationUnit, certificateParamsDTO.country, certificateParamsDTO.email, hashedPassword, new ArrayList<>(), certificateParamsDTO.role);
            userService.save(user);
            return new ResponseEntity(HttpStatus.CREATED);
        }

        // LOGIKA KLUCEVA I CUVANJE TREBA DA SE IMPLEMENTIRA OVO JE PRIVREMENO
        KeyPair keyPairSubject = utils.generateKeyPair();
        PublicKey publicKeySubject = keyPairSubject.getPublic();

        X500Name x500NameSubject;

        X500NameBuilder subjectBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        subjectBuilder.addRDN(BCStyle.CN, user.getCommonName());
        subjectBuilder.addRDN(BCStyle.SURNAME, user.getSurname());
        subjectBuilder.addRDN(BCStyle.GIVENNAME, user.getGivenName());
        subjectBuilder.addRDN(BCStyle.O, user.getOrganization());
        subjectBuilder.addRDN(BCStyle.OU, user.getOrganizationUnit());
        subjectBuilder.addRDN(BCStyle.C, user.getCountry());
        subjectBuilder.addRDN(BCStyle.E, user.getEmail());
        subjectBuilder.addRDN(BCStyle.UID, user.getId());
        x500NameSubject = subjectBuilder.build();

        // LOGIKA KLUCEVA I CUVANJE TREBA DA SE IMPLEMENTIRA OVO JE PRIVREMENO
        KeyPair keyPairIssuer = utils.generateKeyPair();
        PrivateKey privateKeyIssuer = keyPairIssuer.getPrivate();
        PublicKey publicKeyIssuer = keyPairIssuer.getPublic();

        X500Name x500NameIssuer;
        User issuerUser = userService.findById(certificateParamsDTO.issuer);

        X500NameBuilder issuerBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        issuerBuilder.addRDN(BCStyle.CN, issuerUser.getCommonName());
        issuerBuilder.addRDN(BCStyle.SURNAME, issuerUser.getSurname());
        issuerBuilder.addRDN(BCStyle.GIVENNAME, issuerUser.getGivenName());
        issuerBuilder.addRDN(BCStyle.O, issuerUser.getOrganization());
        issuerBuilder.addRDN(BCStyle.OU, issuerUser.getOrganizationUnit());
        issuerBuilder.addRDN(BCStyle.C, issuerUser.getCountry());
        issuerBuilder.addRDN(BCStyle.E, issuerUser.getEmail());
        issuerBuilder.addRDN(BCStyle.UID, issuerUser.getId());
        x500NameIssuer = issuerBuilder.build();

        Subject subject = new Subject(publicKeySubject, x500NameSubject);
        Issuer issuer = new Issuer(privateKeyIssuer, publicKeyIssuer, x500NameIssuer);
        Date startDate = certificateParamsDTO.notBefore;
        Date endDate;

        X509Certificate certificate;
        switch (certificateParamsDTO.certificateType){
            case "self-signed":
                endDate = DateUtils.addYears(startDate, 5);
                subject.setX500Name(issuer.getX500Name());

                certificate = certificateService.generateCertificate(subject, issuer, startDate, endDate);

                System.out.print("\nSertifikat za self-signed je kreiran uspesno!\n\n");
                System.out.print(certificate + "\n");
                break;

            case "intermediary":
                endDate = DateUtils.addYears(startDate, 2);

                certificate = certificateService.generateCertificate(subject, issuer, startDate, endDate);

                System.out.print("\nSertifikat za intermediary je kreiran uspesno!\n\n");
                System.out.print(certificate + "\n");
                break;

            case "end-entity":
                endDate = DateUtils.addYears(startDate, 1);

                certificate = certificateService.generateCertificate(subject, issuer, startDate, endDate);

                System.out.print("\nSertifikat za end-entity je kreiran uspesno!\n\n");
                System.out.print(certificate + "\n");
                break;
            default:
                certificate = null;
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        // TREBA DA SE SACUVA SERTIFIKAT AKO JE DOSLO DO GRESKE OVDE NECE STICI ^default okida^
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
