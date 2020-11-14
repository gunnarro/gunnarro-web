package com.gunnarro.followup.domain.activity;

import com.gunnarro.followup.domain.BaseDomain;

import java.time.LocalTime;

public class ActivityLog extends BaseDomain {

    private static final long serialVersionUID = -180659968576477898L;

    private String createdByUser;
    private String lastModifiedByUser;
    private Activity activity;
    // hh:mm
    private LocalTime fromTime;
    // hh:mm
    private LocalTime toTime;
    // HIGH, MEDION, LOW
    private Integer intensitivity;
    // how I feel 1 to 10, where 1 is BAD and 10 is GREATE
    private Integer emotions;

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getLastModifiedByUser() {
        return lastModifiedByUser;
    }

    public void setLastModifiedByUser(String lastModifiedByUser) {
        this.lastModifiedByUser = lastModifiedByUser;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }

    public Integer getIntensitivity() {
        return intensitivity;
    }

    public void setIntensitivity(Integer intensitivity) {
        this.intensitivity = intensitivity;
    }

    public Integer getEmotions() {
        return emotions;
    }

    public void setEmotions(Integer emotions) {
        this.emotions = emotions;
    }

    @Override
    public String toString() {
        return "ActivityLog [createdByUser=" + createdByUser + ", lastModifiedByUser=" + lastModifiedByUser
                + ", activity=" + activity + ", fromTime=" + fromTime + ", toTime=" + toTime + ", intensitivity="
                + intensitivity + ", emotions=" + emotions + "]";
    }


    public static final class ActivityLogBuilder {
        protected String description;
        protected String name;
        private String createdByUser;
        private String lastModifiedByUser;
        private Activity activity;
        // hh:mm
        private LocalTime fromTime;
        // hh:mm
        private LocalTime toTime;
        // HIGH, MEDION, LOW
        private Integer intensitivity;
        // how I feel 1 to 10, where 1 is BAD and 10 is GREATE
        private Integer emotions;
        private long createdTime = System.currentTimeMillis();
        private long lastModifiedTime = System.currentTimeMillis();
        // All foreign keys used in the DB model
        private Integer id;
        private Integer fkUserId;
        private Integer fkLogId;
        private String sortByValue;
        private boolean enabled;

        private ActivityLogBuilder() {
        }

        public static ActivityLogBuilder anActivityLog() {
            return new ActivityLogBuilder();
        }

        public ActivityLogBuilder withCreatedByUser(String createdByUser) {
            this.createdByUser = createdByUser;
            return this;
        }

        public ActivityLogBuilder withLastModifiedByUser(String lastModifiedByUser) {
            this.lastModifiedByUser = lastModifiedByUser;
            return this;
        }

        public ActivityLogBuilder withActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public ActivityLogBuilder withFromTime(LocalTime fromTime) {
            this.fromTime = fromTime;
            return this;
        }

        public ActivityLogBuilder withToTime(LocalTime toTime) {
            this.toTime = toTime;
            return this;
        }

        public ActivityLogBuilder withIntensitivity(Integer intensitivity) {
            this.intensitivity = intensitivity;
            return this;
        }

        public ActivityLogBuilder withEmotions(Integer emotions) {
            this.emotions = emotions;
            return this;
        }

        public ActivityLogBuilder withCreatedTime(long createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public ActivityLogBuilder withLastModifiedTime(long lastModifiedTime) {
            this.lastModifiedTime = lastModifiedTime;
            return this;
        }

        public ActivityLogBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public ActivityLogBuilder withFkUserId(Integer fkUserId) {
            this.fkUserId = fkUserId;
            return this;
        }

        public ActivityLogBuilder withFkLogId(Integer fkLogId) {
            this.fkLogId = fkLogId;
            return this;
        }

        public ActivityLogBuilder withSortByValue(String sortByValue) {
            this.sortByValue = sortByValue;
            return this;
        }

        public ActivityLogBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ActivityLogBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ActivityLogBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public ActivityLog build() {
            ActivityLog activityLog = new ActivityLog();
            activityLog.setCreatedByUser(createdByUser);
            activityLog.setLastModifiedByUser(lastModifiedByUser);
            activityLog.setActivity(activity);
            activityLog.setFromTime(fromTime);
            activityLog.setToTime(toTime);
            activityLog.setIntensitivity(intensitivity);
            activityLog.setEmotions(emotions);
            activityLog.setCreatedTime(createdTime);
            activityLog.setLastModifiedTime(lastModifiedTime);
            activityLog.setId(id);
            activityLog.setFkUserId(fkUserId);
            activityLog.setFkLogId(fkLogId);
            activityLog.setSortByValue(sortByValue);
            activityLog.setDescription(description);
            activityLog.setName(name);
            activityLog.setEnabled(enabled);
            return activityLog;
        }
    }
}
