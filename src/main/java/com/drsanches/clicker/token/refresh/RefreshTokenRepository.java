package com.drsanches.clicker.token.refresh;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RefreshTokenRepository extends ReactiveMongoRepository<RefreshToken, String> {

    Mono<RefreshToken> findByDeviceId(String deviceId);
}
