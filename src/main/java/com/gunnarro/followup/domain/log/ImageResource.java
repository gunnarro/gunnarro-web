package com.gunnarro.followup.domain.log;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResource {

    private String id;
    private String name;
    private String description;
    private String path;
    private String type;
    private long createdTimeMs;
}
