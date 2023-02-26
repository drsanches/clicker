package com.drsanches.clicker.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClickDTO {

    private String userId;

    private Integer count;
}
