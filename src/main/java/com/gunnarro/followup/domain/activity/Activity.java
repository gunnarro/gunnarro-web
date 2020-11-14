package com.gunnarro.followup.domain.activity;

import com.gunnarro.followup.domain.BaseDomain;

public class Activity extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public static final class ActivityBuilder {
        protected String description;
        protected String name;
        private String category;
        private long createdTime = System.currentTimeMillis();
        private long lastModifiedTime = System.currentTimeMillis();
        // All foreign keys used in the DB model
        private Integer id;
        private Integer fkUserId;
        private Integer fkLogId;
        private String sortByValue;
        private boolean enabled;

        private ActivityBuilder() {
        }

        public static ActivityBuilder anActivity() {
            return new ActivityBuilder();
        }

        public ActivityBuilder withCategory(String category) {
            this.category = category;
            return this;
        }

        public ActivityBuilder withCreatedTime(long createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public ActivityBuilder withLastModifiedTime(long lastModifiedTime) {
            this.lastModifiedTime = lastModifiedTime;
            return this;
        }

        public ActivityBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public ActivityBuilder withFkUserId(Integer fkUserId) {
            this.fkUserId = fkUserId;
            return this;
        }

        public ActivityBuilder withFkLogId(Integer fkLogId) {
            this.fkLogId = fkLogId;
            return this;
        }

        public ActivityBuilder withSortByValue(String sortByValue) {
            this.sortByValue = sortByValue;
            return this;
        }

        public ActivityBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ActivityBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ActivityBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Activity build() {
            Activity activity = new Activity();
            activity.setCategory(category);
            activity.setCreatedTime(createdTime);
            activity.setLastModifiedTime(lastModifiedTime);
            activity.setId(id);
            activity.setFkUserId(fkUserId);
            activity.setFkLogId(fkLogId);
            activity.setSortByValue(sortByValue);
            activity.setDescription(description);
            activity.setName(name);
            activity.setEnabled(enabled);
            return activity;
        }
    }
}
