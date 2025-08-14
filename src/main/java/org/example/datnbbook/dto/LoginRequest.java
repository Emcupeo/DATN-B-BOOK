package org.example.datnbbook.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginRequest {
    private String tenDangNhap;
    private String matKhau;
}