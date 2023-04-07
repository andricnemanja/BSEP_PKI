package com.example.demo.service;

import com.example.demo.model.OCSPObject;
import com.example.demo.repo.OCSPRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OCSPService {
    @Autowired
    private OCSPRepo ocspRepo;

    public Boolean findBySerialNumber(String serialNumber) {
        OCSPObject ocspObject = ocspRepo.findBySerialNumber(serialNumber);
        if(ocspObject.getRevoked())
        {
            return true;
        }
        return false;
    }
}
