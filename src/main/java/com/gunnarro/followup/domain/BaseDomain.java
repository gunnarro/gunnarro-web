package com.gunnarro.followup.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Common domain object
 *
 * @author admin
 */
@Getter
@SuperBuilder
public abstract class BaseDomain implements Serializable {

    private static final long serialVersionUID = -4340377387275807526L;
    private final long createdTime;
    private final long lastModifiedTime;
    // All foreign keys used in the DB model
    private final Integer id;
    private Integer fkUserId;
    private final Integer fkLogId;
    private final String sortByValue;
    protected final String description;
    protected final String name;
    private final boolean enabled;

    public void setFkUserId(Integer id) {
        this.fkUserId = id;
    }

    public boolean isNew() {
        return this.id != null;
    }
}
