package com.gunnarro.web.domain.user;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@ToString
public class Profile implements Serializable {

    private Integer id;
    private Integer userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String emailAddress;
    private char gender;
    private Date dateOfBirth;
}
