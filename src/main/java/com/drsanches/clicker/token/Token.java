package com.drsanches.clicker.token;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
public class Token {

    @ToString.Exclude
    private String accessToken;

    @ToString.Exclude
    private String refreshToken;
}
