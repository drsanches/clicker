package com.drsanches.clicker.data.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class UserAuthDTO {

    private String username;

    @ToString.Exclude
    private String password;

    private String deviceId;
}
