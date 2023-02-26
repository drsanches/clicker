package com.drsanches.clicker.controller;

import com.drsanches.clicker.data.dto.ClickRequestDTO;
import com.drsanches.clicker.service.ClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ClickerController {

    @Autowired
    private ClickService clickService;

    @RequestMapping(path = "/click", method = RequestMethod.POST)
    public Mono<Object> click(@RequestHeader("Authorization") String accessToken,
                              @RequestBody ClickRequestDTO clickRequest) {
        return clickService.clickAndSend(accessToken, clickRequest, "/topic/clicks");
    }
}
