package com.gunnarro.followup.domain.log;

import com.gunnarro.followup.domain.BaseDomain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

public class LogEntry extends BaseDomain {

    public enum logTypeEnum {
        ACTIVITY, CONFLICT, INFO, REPORT
    }

    private static final long serialVersionUID = 3799683509174086447L;

    private String content;
    private String contentHtml;
    private String createdByUser;
    private String lastModifiedByUser;
    private String level;
    private String title;
    private boolean isPrivate = false;
    private List<LogComment> logComments;
    private int numberOfComments = 0;
    private List<ImageResource> resources;

    /**
     * default constructor
     */
    public LogEntry() {
        // for unit tests
    }

    /**
     * default constructor
     */
    public LogEntry(Integer id) {
        super.setId(id);
    }

    /**
     * @param title
     * @param content
     * @param level
     */
    public LogEntry(String title, String content, String level) {
    }

    public List<LogComment> getLogComments() {
        return logComments;
    }

    public void setLogComments(List<LogComment> logComments) {
        this.logComments = logComments;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public String getLastModifiedByUser() {
        return lastModifiedByUser;
    }

    public String getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(@NotNull @Size(min = 5, max = 4096) String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    @Override
    public void setCreatedDate(Date createdDate) {
        super.setCreatedTime(createdDate.getTime());
    }

    public void setLastModifiedByUser(String lastModifiedByUser) {
        this.lastModifiedByUser = lastModifiedByUser;
    }

    public void setLevel(@NotNull String level) {
        this.level = level;
    }

    public void setTitle(@NotNull @Size(min = 5, max = 30) String title) {
        this.title = title;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public int getNumberOfComments() {
        if (this.logComments != null) {
            this.numberOfComments = logComments.size();
        }
        return this.numberOfComments;
    }

    public List<ImageResource> getResources() {
        return resources;
    }

    public void setResources(List<ImageResource> resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LogEntry{");
        sb.append("content='").append(content).append('\'');
        sb.append(", contentHtml='").append(contentHtml).append('\'');
        sb.append(", createdByUser='").append(createdByUser).append('\'');
        sb.append(", lastModifiedByUser='").append(lastModifiedByUser).append('\'');
        sb.append(", level='").append(level).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", isPrivate=").append(isPrivate);
        sb.append(", logComments=").append(logComments);
        sb.append(", numberOfComments=").append(numberOfComments);
        sb.append(", resources=").append(resources);
        sb.append('}');
        return sb.toString();
    }

    /**
     *
     */
    public static final class LogEntryBuilder {
        protected String description;
        protected String name;
        private String content;
        private String contentHtml;
        private String createdByUser;
        private String lastModifiedByUser;
        private String level;
        private String title;
        private boolean isPrivate = false;
        private List<LogComment> logComments;
        private int numberOfComments = 0;
        private List<ImageResource> resources;
        private long createdTime = System.currentTimeMillis();
        private long lastModifiedTime = System.currentTimeMillis();
        // All foreign keys used in the DB model
        private Integer id;
        private Integer fkUserId;
        private Integer fkLogId;
        private String sortByValue;
        private boolean enabled;

        private LogEntryBuilder() {
        }

        public static LogEntryBuilder aLogEntry() {
            return new LogEntryBuilder();
        }

        public LogEntryBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public LogEntryBuilder withContentHtml(String contentHtml) {
            this.contentHtml = contentHtml;
            return this;
        }

        public LogEntryBuilder withCreatedByUser(String createdByUser) {
            this.createdByUser = createdByUser;
            return this;
        }

        public LogEntryBuilder withLastModifiedByUser(String lastModifiedByUser) {
            this.lastModifiedByUser = lastModifiedByUser;
            return this;
        }

        public LogEntryBuilder withLevel(String level) {
            this.level = level;
            return this;
        }

        public LogEntryBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public LogEntryBuilder withIsPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public LogEntryBuilder withLogComments(List<LogComment> logComments) {
            this.logComments = logComments;
            return this;
        }

        public LogEntryBuilder withNumberOfComments(int numberOfComments) {
            this.numberOfComments = numberOfComments;
            return this;
        }

        public LogEntryBuilder withResources(List<ImageResource> resources) {
            this.resources = resources;
            return this;
        }

        public LogEntryBuilder withCreatedTime(long createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public LogEntryBuilder withLastModifiedTime(long lastModifiedTime) {
            this.lastModifiedTime = lastModifiedTime;
            return this;
        }

        public LogEntryBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public LogEntryBuilder withFkUserId(Integer fkUserId) {
            this.fkUserId = fkUserId;
            return this;
        }

        public LogEntryBuilder withFkLogId(Integer fkLogId) {
            this.fkLogId = fkLogId;
            return this;
        }

        public LogEntryBuilder withSortByValue(String sortByValue) {
            this.sortByValue = sortByValue;
            return this;
        }

        public LogEntryBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public LogEntryBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public LogEntryBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public LogEntry build() {
            LogEntry logEntry = new LogEntry();
            logEntry.setContent(content);
            logEntry.setContentHtml(contentHtml);
            logEntry.setCreatedByUser(createdByUser);
            logEntry.setLastModifiedByUser(lastModifiedByUser);
            logEntry.setLevel(level);
            logEntry.setTitle(title);
            logEntry.setLogComments(logComments);
            logEntry.setNumberOfComments(numberOfComments);
            logEntry.setResources(resources);
            logEntry.setCreatedTime(createdTime);
            logEntry.setLastModifiedTime(lastModifiedTime);
            logEntry.setId(id);
            logEntry.setFkUserId(fkUserId);
            logEntry.setFkLogId(fkLogId);
            logEntry.setSortByValue(sortByValue);
            logEntry.setDescription(description);
            logEntry.setName(name);
            logEntry.setEnabled(enabled);
            logEntry.isPrivate = this.isPrivate;
            return logEntry;
        }
    }
}
