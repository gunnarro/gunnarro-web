package com.gunnarro.web.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class PublicController {

    public static final String PUBLIC_URI = "/public";

    @GetMapping("/")
    public String indexPublic() {
        return publicHome();
    }

    @GetMapping(PUBLIC_URI)
    public String publicHome() {
        return createUri("/index");
    }

    @GetMapping(PUBLIC_URI + "/about")
    public String about() {
        return createUri("/about");
    }

    @GetMapping(PUBLIC_URI + "/codeofconduct")
    public String codeOfConduct() {
        return createUri("/codeofconduct");
    }

    @GetMapping(PUBLIC_URI + "/cv")
    public String cv(@RequestParam(name = "id") String id) {
        return createUri(String.format("/cv/%s/cv", id));
    }

    @GetMapping(PUBLIC_URI + "/employees")
    public String employees() {
        return createUri("/employees/list-employees");
    }

    @GetMapping(PUBLIC_URI + "/availability")
    public String availability() {
        return createUri("/availability");
    }

    @GetMapping(PUBLIC_URI + "/cv/projects")
    public String cvProjects(@RequestParam(name = "id") String id) {
        return createUri(String.format("/cv/%s/cv-project", id));
    }

    @GetMapping(PUBLIC_URI + "/cv/pdf")
    public String cvPdf(@RequestParam(name = "id") String id) {
        // check input parameter
        return createUri(String.format("/cv/%s/cv.pdf", id));
    }

    @GetMapping(PUBLIC_URI + "/releasenotes")
    public String releasenotes() {
        return createUri("/release-notes");
    }

    @GetMapping("/error")
    public String error() {
        return "/application-error";
    }

    @GetMapping("/403")
    public String error403() {
        return "error/403";
    }

    @GetMapping("/access-denied")
    public String denied() {
        return "access-denied";
    }


    static String createUri(String pageUri) {
        return PUBLIC_URI + pageUri;
    }

}
