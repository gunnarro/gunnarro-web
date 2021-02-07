package com.gunnarro.followup.domain.user;


import lombok.Builder;
import lombok.Data;

/**
 * Privilege represents a low-level, granular privilege/authority in the system.
 * Example:
 * READ_PRIVILEGE : read permission only
 * WRITE_PRIVILEGE: write permission
 * ALL_PRIVILEGE  : both read and write permission
 */
@Data
@Builder
public class Privilege {
    private Integer id;
    private String name;
}
