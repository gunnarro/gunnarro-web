package com.gunnarro.followup.domain.activity;

import com.gunnarro.followup.domain.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
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

}
