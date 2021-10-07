package com.printer.app.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class Result {
    private String unit;
    private String remainder;

    public Result() {
    }

    public Result(String unit, String remainder) {
        this.unit = unit;
        this.remainder = remainder;
    }
}
