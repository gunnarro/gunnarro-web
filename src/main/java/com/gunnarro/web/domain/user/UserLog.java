package com.gunnarro.web.domain.user;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

@Value
@Builder
@ToString
public class UserLog implements Serializable {

    private static final long serialVersionUID = -3112437958212912495L;

    Integer id;
    Integer userId;
    Date createdDate;
    Date lastModifiedDate;
    Date loggedInDate;
    String loggedInFromIpAddress;
    String loggedInFromDevice;
    int numberOfLoginAttemptSuccess;
    int numberOfLoginAttemptFailures;
    boolean isUserBlocked;

}
