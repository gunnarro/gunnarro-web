package com.gunnarro.followup.domain.user;

import java.util.List;

/**
 * Role represents the high-level roles of the user in the system; each role will have a set of low-level privileges.
 * Example:
 * ROLE_ADMIN
 * ROLE_USER
 * ROLE_GUEST
 * ROLE_ANONYMOUS
 */
public class Role {
    private Integer id;
    private String name;
    private List<Privilege> privileges;

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Role{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", privileges=").append(privileges);
        sb.append('}');
        return sb.toString();
    }
}
