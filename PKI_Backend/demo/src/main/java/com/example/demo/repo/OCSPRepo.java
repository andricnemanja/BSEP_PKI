package com.example.demo.repo;

import com.example.demo.model.OCSPObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OCSPRepo extends JpaRepository<OCSPObject, Integer> {

    OCSPObject findBySerialNumber(String serialNumber);
}
