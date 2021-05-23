package com.gunnarro.followup.endpoint.rest;

import com.gunnarro.followup.endpoint.ActivityController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogServices {

    private static final Logger LOG = LoggerFactory.getLogger(LogServices.class);

    @GetMapping
    public String log() {
        LOG.trace("This is a TRACE level message");
        LOG.debug("This is a DEBUG level message");
        LOG.info("This is an INFO level message");
        LOG.warn("This is a WARN level message");
        LOG.error("This is an ERROR level message");
        return "See the log for details";
    }
}
