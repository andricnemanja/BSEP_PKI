package com.example.demo.service;

import com.example.demo.dto.CertificateParamsDTO;
import com.example.demo.model.Certificate;
import com.example.demo.model.Issuer;
import com.example.demo.model.Subject;
import com.example.demo.model.User;
import com.example.demo.repo.CertificateRepository;
import com.example.demo.utils.Utils;
import org.apache.commons.lang3.time.DateUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class CertificateService {

    @Autowired
    private Utils utils;

    @Autowired
    private OCSPService ocspService;

    @Autowired
    private UserService userService;
    @Autowired
    private CertificateRepository certificateRepository;

    public CertificateService() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public X509Certificate selfSignedCertificate(CertificateParamsDTO certificateParamsDTO){
        // GENERISANJE KORISNIKA U BAZI
        User subjectUser = userService.findByEmail(certificateParamsDTO.email);
        if(subjectUser == null){
            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString();

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(certificateParamsDTO.password);

            subjectUser = new User(uuidString, certificateParamsDTO.commonName, certificateParamsDTO.surname, certificateParamsDTO.givenName, certificateParamsDTO.organization,
                    certificateParamsDTO.organizationUnit, certificateParamsDTO.country, certificateParamsDTO.email, hashedPassword, new ArrayList<>());
            userService.save(subjectUser);
        }

        // LOGIKA KLUCEVA I CUVANJE TREBA DA SE IMPLEMENTIRA OVO JE PRIVREMENO
        KeyPair keyPairSubject = utils.generateKeyPair();
        PublicKey publicKeySubject = keyPairSubject.getPublic();

        X500Name x500NameSubject;

        X500NameBuilder subjectBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        subjectBuilder.addRDN(BCStyle.CN, subjectUser.getCommonName());
        subjectBuilder.addRDN(BCStyle.SURNAME, subjectUser.getSurname());
        subjectBuilder.addRDN(BCStyle.GIVENNAME, subjectUser.getGivenName());
        subjectBuilder.addRDN(BCStyle.O, subjectUser.getOrganization());
        subjectBuilder.addRDN(BCStyle.OU, subjectUser.getOrganizationUnit());
        subjectBuilder.addRDN(BCStyle.C, subjectUser.getCountry());
        subjectBuilder.addRDN(BCStyle.E, subjectUser.getEmail());
        x500NameSubject = subjectBuilder.build();

        // LOGIKA KLUCEVA I CUVANJE TREBA DA SE IMPLEMENTIRA OVO JE PRIVREMENO
        KeyPair keyPairIssuer = utils.generateKeyPair();
        PrivateKey privateKeyIssuer = keyPairIssuer.getPrivate();
        PublicKey publicKeyIssuer = keyPairIssuer.getPublic();

        X500Name x500NameIssuer;

        x500NameIssuer = x500NameSubject;

        // KREIRANJE SERTIFIKATA
        X509Certificate certificate;

        Subject subject = new Subject(publicKeySubject, x500NameSubject);
        Issuer issuer = new Issuer(privateKeyIssuer, publicKeyIssuer, x500NameIssuer);
        Date startDate = certificateParamsDTO.notBefore;
        Date endDate = DateUtils.addYears(startDate, 10);

        certificate = generateCertificate(subject, issuer, startDate, endDate);
        subjectUser.getCertificatesSerialNumbers().add(
                certificate.getSerialNumber().toString()
        );

        // CUVANJE U BAZI
        userService.save(subjectUser);
        certificateRepository.save(new Certificate(
                certificate.getSerialNumber(),
                subjectUser.getEmail(),
                subjectUser.getEmail(),
                startDate,
                endDate,
                certificateParamsDTO.keyUsage,
                certificateParamsDTO.extendedKeyUsage
        ));

        return certificate;
    }

    public X509Certificate intermediaryCertificate(CertificateParamsDTO certificateParamsDTO){
        // GENERISANJE KORISNIKA U BAZI
        User subjectUser = userService.findByEmail(certificateParamsDTO.email);
        if(subjectUser == null){
            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString();

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(certificateParamsDTO.password);

            subjectUser = new User(uuidString, certificateParamsDTO.commonName, certificateParamsDTO.surname, certificateParamsDTO.givenName, certificateParamsDTO.organization,
                    certificateParamsDTO.organizationUnit, certificateParamsDTO.country, certificateParamsDTO.email, hashedPassword, new ArrayList<>());
            userService.save(subjectUser);
        }

        // LOGIKA KLUCEVA I CUVANJE TREBA DA SE IMPLEMENTIRA OVO JE PRIVREMENO
        KeyPair keyPairSubject = utils.generateKeyPair();
        PublicKey publicKeySubject = keyPairSubject.getPublic();

        X500Name x500NameSubject;

        X500NameBuilder subjectBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        subjectBuilder.addRDN(BCStyle.CN, subjectUser.getCommonName());
        subjectBuilder.addRDN(BCStyle.SURNAME, subjectUser.getSurname());
        subjectBuilder.addRDN(BCStyle.GIVENNAME, subjectUser.getGivenName());
        subjectBuilder.addRDN(BCStyle.O, subjectUser.getOrganization());
        subjectBuilder.addRDN(BCStyle.OU, subjectUser.getOrganizationUnit());
        subjectBuilder.addRDN(BCStyle.C, subjectUser.getCountry());
        subjectBuilder.addRDN(BCStyle.E, subjectUser.getEmail());
        x500NameSubject = subjectBuilder.build();

        // LOGIKA KLUCEVA I CUVANJE TREBA DA SE IMPLEMENTIRA OVO JE PRIVREMENO
        KeyPair keyPairIssuer = utils.generateKeyPair();
        PrivateKey privateKeyIssuer = keyPairIssuer.getPrivate();
        PublicKey publicKeyIssuer = keyPairIssuer.getPublic();

        X500Name x500NameIssuer;
        System.out.print("\n\n\n" + certificateParamsDTO.issuer + "\n\n\n");
        Certificate c = certificateRepository.getBySerialNumber(new BigInteger(certificateParamsDTO.issuer));
        User issuerUser = userService.findByEmail(c.getSubjectEmail());

        X500NameBuilder issuerBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        issuerBuilder.addRDN(BCStyle.CN, issuerUser.getCommonName());
        issuerBuilder.addRDN(BCStyle.SURNAME, issuerUser.getSurname());
        issuerBuilder.addRDN(BCStyle.GIVENNAME, issuerUser.getGivenName());
        issuerBuilder.addRDN(BCStyle.O, issuerUser.getOrganization());
        issuerBuilder.addRDN(BCStyle.OU, issuerUser.getOrganizationUnit());
        issuerBuilder.addRDN(BCStyle.C, issuerUser.getCountry());
        issuerBuilder.addRDN(BCStyle.E, issuerUser.getEmail());
        x500NameIssuer = issuerBuilder.build();


        // KREIRANJE SERTIFIKATA
        X509Certificate certificate;

        Subject subject = new Subject(publicKeySubject, x500NameSubject);
        Issuer issuer = new Issuer(privateKeyIssuer, publicKeyIssuer, x500NameIssuer);
        Date startDate = certificateParamsDTO.notBefore;
        Date endDate = DateUtils.addYears(startDate, 5);

        certificate = generateCertificate(subject, issuer, startDate, endDate);
        subjectUser.getCertificatesSerialNumbers().add(
                certificate.getSerialNumber().toString()
        );

        // CUVANJE U BAZI
        userService.save(subjectUser);
        certificateRepository.save(new Certificate(
                certificate.getSerialNumber(),
                subjectUser.getEmail(),
                issuerUser.getEmail(),
                startDate,
                endDate,
                certificateParamsDTO.keyUsage,
                certificateParamsDTO.extendedKeyUsage
        ));

        return certificate;
    }

    public X509Certificate endEntityCertificate(CertificateParamsDTO certificateParamsDTO){
        // LOGIKA KLUCEVA I CUVANJE TREBA DA SE IMPLEMENTIRA OVO JE PRIVREMENO
        KeyPair keyPairSubject = utils.generateKeyPair();
        PublicKey publicKeySubject = keyPairSubject.getPublic();

        X500Name x500NameSubject;
        User subjectUser = userService.findById(certificateParamsDTO.email);

        X500NameBuilder subjectBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        subjectBuilder.addRDN(BCStyle.CN, subjectUser.getCommonName());
        subjectBuilder.addRDN(BCStyle.SURNAME, subjectUser.getSurname());
        subjectBuilder.addRDN(BCStyle.GIVENNAME, subjectUser.getGivenName());
        subjectBuilder.addRDN(BCStyle.O, subjectUser.getOrganization());
        subjectBuilder.addRDN(BCStyle.OU, subjectUser.getOrganizationUnit());
        subjectBuilder.addRDN(BCStyle.C, subjectUser.getCountry());
        subjectBuilder.addRDN(BCStyle.E, subjectUser.getEmail());
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
        x500NameIssuer = issuerBuilder.build();

        // KREIRANJE SERTIFIKATA
        Subject subject = new Subject(publicKeySubject, x500NameSubject);
        Issuer issuer = new Issuer(privateKeyIssuer, publicKeyIssuer, x500NameIssuer);
        Date startDate = certificateParamsDTO.notBefore;
        Date endDate = DateUtils.addYears(startDate, 1);

        return generateCertificate(subject, issuer, startDate, endDate);
    }

    public X509Certificate generateCertificate(Subject subject, Issuer issuer, Date startDate, Date endDate){

        BigInteger serialNumber = utils.getRandomBigInteger();
        while(ocspService.findBySerialNumber(serialNumber.toString()) != null){
            serialNumber = utils.getRandomBigInteger();
        }

        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(issuer.getPrivateKey());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuer.getX500Name(),
                    serialNumber,
                    startDate,
                    endDate,
                    subject.getX500Name(),
                    subject.getPublicKey());
            X509CertificateHolder certHolder = certGen.build(contentSigner);

            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            return certConverter.getCertificate(certHolder);

        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Certificate> getBySubjectEmail(String email){
        return certificateRepository.findBySubjectEmail(email);
    }

    public ArrayList<Certificate> getAll(){
        return certificateRepository.getAll();
    }

}
