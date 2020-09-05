package com.simple.portal.biz.v1.template;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {
    @GetMapping("/websocket-client")
    public String webSocketClient() {
        return "websocket-client";
    }
    @GetMapping("/upload-form")
    public String uploadForm() {
        return "uploadForm";
    }
    @GetMapping("/sockjs")
    public String sockjs() {
        return "sockjs";
    }
    @GetMapping("/stomp")
    public String stomp() {
        return "stomp";
    }
}
