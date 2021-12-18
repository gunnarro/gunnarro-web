package com.gunnarro.web.domain.user;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Privilege represents a low-level, granular privilege/authority in the system.
 * Example:
 * READ_PRIVILEGE : read permission only
 * WRITE_PRIVILEGE: write permission
 * ALL_PRIVILEGE  : both read and write permission
 */
@Data
@Builder
public class Privilege implements Serializable {
    private Integer id;
    private String name;
}
