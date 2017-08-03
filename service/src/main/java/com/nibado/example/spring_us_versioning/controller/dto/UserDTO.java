package com.nibado.example.spring_us_versioning.controller.dto;

import com.nibado.example.spring_us_versioning.versioning.Versioned;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class UserDTO implements Versioned {
    private final String firstName;
    private final String lastName;
    private final String eMail;

    @Override
    public Versioned toVersion(int version) {

        log.info ("To Version : {}", version);

        if (version <= 2) {
            return new UserDTOv2(firstName, lastName)
                    .toVersion(version);
        } else {
            return this;
        }
    }
}
