package com.gunnarro.web.domain.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Role represents the high-level roles of the user in the system; each role will have a set of low-level privileges.
 * Example:
 * ROLE_ADMIN
 * ROLE_USER
 * ROLE_GUEST
 * ROLE_ANONYMOUS
 */
@Data
@Builder
public class Role implements Serializable {
    private Integer id;
    private String name;
    private List<Privilege> privileges;
}
