package com.gunnarro.followup.domain.user;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Profile {

    private Integer id;
    private Integer userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String emailAddress;
    private char gender;
    private Date dateOfBirth;
}
