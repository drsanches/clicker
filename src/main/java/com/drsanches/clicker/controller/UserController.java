package com.drsanches.clicker.controller;

import com.drsanches.clicker.data.dto.UserAuthDTO;
import com.drsanches.clicker.data.dto.UserDTO;
import com.drsanches.clicker.service.UserService;
import com.drsanches.clicker.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public Mono<Object> registration(@RequestBody UserAuthDTO userAuthDTO) {
        return userService.registration(userAuthDTO);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public Mono<Token> login(@RequestBody UserAuthDTO userAuthDTO) {
        return userService.login(userAuthDTO);
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public Mono<UserDTO> getCurrentUser(@RequestHeader("Authorization") String accessToken) {
        return userService.getCurrentUser(accessToken);
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public Mono<UserDTO> getUser(@RequestHeader("Authorization") String accessToken,
                                 @PathVariable("userId") String userId) {
        return userService.getUser(accessToken, userId);
    }

    @RequestMapping(path = "/refreshToken", method = RequestMethod.GET)
    public Mono<Token> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        return userService.refreshToken(refreshToken);
    }
}
