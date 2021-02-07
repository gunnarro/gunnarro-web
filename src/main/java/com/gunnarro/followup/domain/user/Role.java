package com.gunnarro.followup.domain.user;

import lombok.Builder;
import lombok.Data;

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
public class Role {
    private Integer id;
    private String name;
    private List<Privilege> privileges;
}
