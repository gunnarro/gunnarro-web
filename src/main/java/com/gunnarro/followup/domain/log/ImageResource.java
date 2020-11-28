package com.gunnarro.followup.domain.log;

public class ImageResource {

    private String id;
    private String name;
    private String description;
    private String path;
    private String type;
    private long createdTimeMs;

    public ImageResource(String id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatedTimeMs() {
        return createdTimeMs;
    }

    public void setCreatedTimeMs(long createdTimeMs) {
        this.createdTimeMs = createdTimeMs;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ImageResource{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", path='").append(path).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", createdTimeMs=").append(createdTimeMs);
        sb.append('}');
        return sb.toString();
    }
}
