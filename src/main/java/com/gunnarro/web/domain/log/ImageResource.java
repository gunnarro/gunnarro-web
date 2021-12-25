package com.gunnarro.web.domain.log;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
public class ImageResource implements Serializable {

    private String id;
    private String name;
    private String description;
    private String path;
    private String type;
    private long createdTimeMs;
}
