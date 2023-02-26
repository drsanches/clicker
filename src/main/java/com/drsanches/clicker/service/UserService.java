package com.drsanches.clicker.service;

import com.drsanches.clicker.data.user.User;
import com.drsanches.clicker.data.user.UserRepository;
import com.drsanches.clicker.data.dto.UserAuthDTO;
import com.drsanches.clicker.data.dto.UserDTO;
import com.drsanches.clicker.exception.NoUserException;
import com.drsanches.clicker.exception.UserExistsException;
import com.drsanches.clicker.token.CredentialsHelper;
import com.drsanches.clicker.token.Token;
import com.drsanches.clicker.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CredentialsHelper credentialsHelper;

    private final Function<User, UserDTO> userToDto = it -> UserDTO.builder()
            .id(it.getId())
            .username(it.getUsername())
            .build();

    public Mono<Object> registration(UserAuthDTO userAuthDTO) {
        String salt = UUID.randomUUID().toString();
        return userRepository.findByUsername(userAuthDTO.getUsername())
                .flatMap(it -> Mono.error(new UserExistsException("User with username '" + userAuthDTO.getUsername() + "' already exists")))
                .switchIfEmpty(userRepository.save(User.builder()
                        .username(userAuthDTO.getUsername())
                        .password(credentialsHelper.encodePassword(userAuthDTO.getPassword(), salt))
                        .salt(salt)
                        .build()));
    }

    public Mono<Token> login(UserAuthDTO userAuthDTO) {
        return userRepository.findByUsername(userAuthDTO.getUsername())
                .switchIfEmpty(Mono.error(new NoUserException("No user with username '" + userAuthDTO.getUsername() + "'")))
                .map(it -> {
                    credentialsHelper.checkPassword(userAuthDTO.getPassword(), it.getPassword(), it.getSalt());
                    return it;
                })
                .flatMap(it -> tokenService.createToken(it.getId(), userAuthDTO.getDeviceId()));
    }

    public Mono<UserDTO> getCurrentUser(String accessToken) {
        String userId = tokenService.validate(accessToken).getUserId();
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new NoUserException("No user with id '" + userId + "'")))
                .map(userToDto);
    }

    public Mono<UserDTO> getUser(String token, String userId) {
        tokenService.validate(token);
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new NoUserException("No user with id '" + userId + "'")))
                .map(userToDto);
    }

    public Mono<Token> refreshToken(String refreshToken) {
        return tokenService.refreshToken(refreshToken);
    }
}
