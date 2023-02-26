package com.drsanches.clicker.service;

import com.drsanches.clicker.data.click.Click;
import com.drsanches.clicker.data.click.ClickRepository;
import com.drsanches.clicker.data.dto.ClickDTO;
import com.drsanches.clicker.data.dto.ClickRequestDTO;
import com.drsanches.clicker.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ClickService {

    @Autowired
    private ClickRepository clickRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SimpMessagingTemplate template;

    public Mono<ClickDTO> click(String accessToken, ClickRequestDTO clickRequest) {
        String userId = tokenService.validate(accessToken).getUserId();
        return clickRepository.findByUserId(userId)
                .flatMap(it -> {
                    it.setCount(it.getCount() + clickRequest.getClicks());
                    return clickRepository.save(it);
                })
                .switchIfEmpty(clickRepository.save(Click.builder()
                        .userId(userId)
                        .count(clickRequest.getClicks())
                        .build()))
                .map(it -> ClickDTO.builder()
                        .userId(it.getUserId())
                        .count(it.getCount())
                        .build());
    }

    public Mono<Object> clickAndSend(String accessToken, ClickRequestDTO clickRequest, String destination) {
        return click(accessToken, clickRequest)
                .map(it -> {
                    template.convertAndSend(destination, it);
                    return it;
                });
    }
}
