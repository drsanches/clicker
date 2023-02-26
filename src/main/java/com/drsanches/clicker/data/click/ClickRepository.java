package com.drsanches.clicker.data.click;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ClickRepository extends ReactiveMongoRepository<Click, String> {

    Mono<Click> findByUserId(String userId);
}
