package org.example.datnbbook.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class EmailRequest {
    private String to;
    private String customerName;
    private String voucherType;
    private Double voucherValue;

    // Getters and Setters
}
