package com.example.demo.repo;

import com.example.demo.model.Certificate;
import com.example.demo.model.OCSPObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.ArrayList;

public interface CertificateRepository extends JpaRepository<Certificate, Integer>{
    @Query(value="select * from certificate c where c.subject_email = :subjectEmail", nativeQuery = true)
    public ArrayList<Certificate> findBySubjectEmail(String subjectEmail);
    @Query(value="select * from certificate c where c.subject_email = :issuerEmail", nativeQuery = true)
    public ArrayList<Certificate> findByIssuerEmail(String issuerEmail);
    @Query(value="select * from certificate", nativeQuery = true)
    public ArrayList<Certificate> getAll();
    @Query(value = "select * from certificate c where c.serial_number = :serialNumber", nativeQuery = true)
    public Certificate getBySerialNumber(BigInteger serialNumber);

}
