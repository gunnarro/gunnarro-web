package com.gunnarro.followup.domain.user;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;

@Value
@Builder
public class UserLog implements Serializable {

    private static final long serialVersionUID = -3112437958212912495L;

    private Integer id;
    private Integer userId;
    private Date createdDate;
    private Date lastModifiedDate;
    private Date loggedInDate;
    private String loggedInFromIpAddress;
    private String loggedInFromDevice;
    private int numberOfLoginAttemptSuccess;
    private int numberOfLoginAttemptFailures;
    private boolean isUserBlocked;

}
