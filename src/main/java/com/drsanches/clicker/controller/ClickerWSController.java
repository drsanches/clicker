package com.drsanches.clicker.controller;

import com.drsanches.clicker.data.dto.ClickRequestDTO;
import com.drsanches.clicker.data.dto.WSResponseDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ClickerWSController {

    @MessageMapping("/click")
    @SendTo("/topic/clicks")
    public WSResponseDTO click(ClickRequestDTO clickRequest) {
        return new WSResponseDTO("Clicks: " + clickRequest.getClicks() + " (WS response)");
    }
}
