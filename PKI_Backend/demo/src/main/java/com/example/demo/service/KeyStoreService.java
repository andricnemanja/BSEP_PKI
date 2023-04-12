package com.example.demo.service;

import com.example.demo.keystore.KeyStoreReader;
import com.example.demo.keystore.KeyStoreWriter;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.PrivateKey;
import java.security.cert.Certificate;

@Service
public class KeyStoreService {
    private final String ROOT_KEYSTORE_PATH = "src/main/resources/static/RootKeyStore.jks";
    private final String ROOT_KEYSTORE_PASSWORD;
    private final String NON_ROOT_KEYSTORE_PATH = "src/main/resources/static/KeyStore.jks";
    private final String NON_ROOT_KEYSTORE_PASSWORD;
    private KeyStoreReader keyStoreReader;
    private KeyStoreWriter keyStoreWriter;

    @Autowired
    public KeyStoreService(KeyStoreWriter keyStoreWriter, KeyStoreReader keyStoreReader){
        this.keyStoreReader = keyStoreReader;
        this.keyStoreWriter = keyStoreWriter;

        Dotenv dotenv = null;
        dotenv = Dotenv.configure().directory("./").load();
        ROOT_KEYSTORE_PASSWORD = dotenv.get("ROOT_KEY_STORE_PASSWORD");
        NON_ROOT_KEYSTORE_PASSWORD = dotenv.get("NON_ROOT_KEY_STORE_PASSWORD");
    }
    public void saveRootCertificate(String alias, PrivateKey privateKey, Certificate certificate){
        keyStoreWriter.loadKeyStore(ROOT_KEYSTORE_PATH, ROOT_KEYSTORE_PASSWORD.toCharArray());
        keyStoreWriter.write(alias, privateKey, ROOT_KEYSTORE_PASSWORD.toCharArray(), new Certificate[] { certificate } );
        keyStoreWriter.saveKeyStore(ROOT_KEYSTORE_PATH, ROOT_KEYSTORE_PASSWORD.toCharArray());
    }

    public void saveCertificate(String alias, PrivateKey privateKey, Certificate certificate, String issuerAlias){
        Certificate[] certificatesChain = keyStoreReader.getCertificateChain(NON_ROOT_KEYSTORE_PATH, NON_ROOT_KEYSTORE_PASSWORD, issuerAlias);
        Certificate[] newCertificateChain = new Certificate[certificatesChain.length + 1];

        if(certificatesChain.length == 0){
            Certificate rootCertificate = keyStoreReader.readCertificate(ROOT_KEYSTORE_PATH, ROOT_KEYSTORE_PASSWORD, issuerAlias);
            newCertificateChain = new Certificate[2];
            newCertificateChain[0] = rootCertificate;
            newCertificateChain[1] = certificate;
        }
        else{
            System.arraycopy(certificatesChain, 0, newCertificateChain, 0, certificatesChain.length);
            newCertificateChain[newCertificateChain.length - 1] = certificate;
        }

        keyStoreWriter.loadKeyStore(NON_ROOT_KEYSTORE_PATH, NON_ROOT_KEYSTORE_PASSWORD.toCharArray());
        keyStoreWriter.write(alias, privateKey, NON_ROOT_KEYSTORE_PASSWORD.toCharArray(), newCertificateChain );
        keyStoreWriter.saveKeyStore(NON_ROOT_KEYSTORE_PATH, NON_ROOT_KEYSTORE_PASSWORD.toCharArray());
    }

    public Certificate getCertificate(String alias){
        Certificate certificate = keyStoreReader.readCertificate(NON_ROOT_KEYSTORE_PATH, NON_ROOT_KEYSTORE_PASSWORD, alias);
        if(certificate == null){
            return keyStoreReader.readCertificate(ROOT_KEYSTORE_PATH, ROOT_KEYSTORE_PASSWORD, alias);
        }
        return certificate;
    }

    public PrivateKey getPrivateKey(String alias){
        PrivateKey privateKey = keyStoreReader.readPrivateKey(NON_ROOT_KEYSTORE_PATH, NON_ROOT_KEYSTORE_PASSWORD, alias, NON_ROOT_KEYSTORE_PASSWORD);
        if(privateKey == null){
            return keyStoreReader.readPrivateKey(ROOT_KEYSTORE_PATH, ROOT_KEYSTORE_PASSWORD, alias, ROOT_KEYSTORE_PASSWORD);
        }
        return privateKey;
    }
}
