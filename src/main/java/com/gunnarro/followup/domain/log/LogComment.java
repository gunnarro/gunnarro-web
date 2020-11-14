package com.gunnarro.followup.domain.log;

import com.gunnarro.followup.domain.BaseDomain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class LogComment extends BaseDomain {

    private static final long serialVersionUID = 8461304356156397577L;
    private String content;
    private String contentHtml;
    private String createdByUser;
    private String lastModifiedByUser;
    private boolean isPrivate = false;

    /**
     * default constructor
     */
    public LogComment() {
        // for unit tests
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

    public String getCreatedByUser() {
        return createdByUser;
    }

    public String getLastModifiedByUser() {
        return lastModifiedByUser;
    }

    public void setContent(@NotNull @Size(min = 5, max = 4096) String content) {
        this.content = content;
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }


    public static final class LogCommentBuilder {
        protected String description;
        protected String name;
        private String content;
        private String contentHtml;
        private String createdByUser;
        private String lastModifiedByUser;
        private boolean isPrivate = false;
        private long createdTime = System.currentTimeMillis();
        private long lastModifiedTime = System.currentTimeMillis();
        // All foreign keys used in the DB model
        private Integer id;
        private Integer fkUserId;
        private Integer fkLogId;
        private String sortByValue;
        private boolean enabled;

        private LogCommentBuilder() {
        }

        public static LogCommentBuilder aLogComment() {
            return new LogCommentBuilder();
        }

        public LogCommentBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public LogCommentBuilder withContentHtml(String contentHtml) {
            this.contentHtml = contentHtml;
            return this;
        }

        public LogCommentBuilder withCreatedByUser(String createdByUser) {
            this.createdByUser = createdByUser;
            return this;
        }

        public LogCommentBuilder withLastModifiedByUser(String lastModifiedByUser) {
            this.lastModifiedByUser = lastModifiedByUser;
            return this;
        }

        public LogCommentBuilder withIsPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public LogCommentBuilder withCreatedTime(long createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public LogCommentBuilder withLastModifiedTime(long lastModifiedTime) {
            this.lastModifiedTime = lastModifiedTime;
            return this;
        }

        public LogCommentBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public LogCommentBuilder withFkUserId(Integer fkUserId) {
            this.fkUserId = fkUserId;
            return this;
        }

        public LogCommentBuilder withFkLogId(Integer fkLogId) {
            this.fkLogId = fkLogId;
            return this;
        }

        public LogCommentBuilder withSortByValue(String sortByValue) {
            this.sortByValue = sortByValue;
            return this;
        }

        public LogCommentBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public LogCommentBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public LogCommentBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public LogComment build() {
            LogComment logComment = new LogComment();
            logComment.setContent(content);
            logComment.setContentHtml(contentHtml);
            logComment.setCreatedByUser(createdByUser);
            logComment.setLastModifiedByUser(lastModifiedByUser);
            logComment.setCreatedTime(createdTime);
            logComment.setLastModifiedTime(lastModifiedTime);
            logComment.setId(id);
            logComment.setFkUserId(fkUserId);
            logComment.setFkLogId(fkLogId);
            logComment.setSortByValue(sortByValue);
            logComment.setDescription(description);
            logComment.setName(name);
            logComment.setEnabled(enabled);
            logComment.isPrivate = this.isPrivate;
            return logComment;
        }
    }
}
