package com.drsanches.clicker.controller;

import com.drsanches.clicker.data.dto.ClickRequestDTO;
import com.drsanches.clicker.data.dto.WSResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClickerController {

    @Autowired
    private SimpMessagingTemplate template;

    @RequestMapping(path = "/click", method = RequestMethod.POST)
    public void click(@RequestBody ClickRequestDTO clickRequest) {
        template.convertAndSend(
                "/topic/clicks",
                new WSResponseDTO("Clicks: " + clickRequest.getClicks() + " (REST response)")
        );
    }
}
