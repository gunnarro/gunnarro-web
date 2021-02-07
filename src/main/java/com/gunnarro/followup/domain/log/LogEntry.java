package com.gunnarro.followup.domain.log;

import com.gunnarro.followup.domain.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
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
    private boolean isPrivate;
    private List<LogComment> logComments;
    private int numberOfComments;
    private List<ImageResource> resources;

}
