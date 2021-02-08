package com.gunnarro.followup.domain.log;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ImageResource implements Serializable {

    private String id;
    private String name;
    private String description;
    private String path;
    private String type;
    private long createdTimeMs;
}
