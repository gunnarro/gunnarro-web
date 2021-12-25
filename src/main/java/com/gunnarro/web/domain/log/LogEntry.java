package com.gunnarro.web.domain.log;

import com.gunnarro.web.domain.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@ToString
public class LogEntry extends BaseDomain {

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
    public enum logTypeEnum {
        ACTIVITY, CONFLICT, INFO, REPORT
    }

}
