package com.drsanches.clicker.controller;

import com.drsanches.clicker.data.dto.ClickDTO;
import com.drsanches.clicker.data.dto.ClickRequestDTO;
import com.drsanches.clicker.service.ClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class ClickerWSController {

    @Autowired
    private ClickService clickService;

    @MessageMapping("/click")
    @SendTo("/topic/clicks")
    public Mono<ClickDTO> click(@Header("Authorization") String accessToken,
                                @Payload ClickRequestDTO clickRequest) {
        return clickService.click(accessToken, clickRequest);
    }
}
