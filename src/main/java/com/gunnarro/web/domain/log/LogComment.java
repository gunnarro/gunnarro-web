package com.gunnarro.web.domain.log;

import com.gunnarro.web.domain.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class LogComment extends BaseDomain {

    private static final long serialVersionUID = 8461304356156397577L;
    private String content;
    private String contentHtml;
    private String createdByUser;
    private String lastModifiedByUser;
    private boolean isPrivate;

}
