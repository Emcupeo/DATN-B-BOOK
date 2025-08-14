package org.example.datnbbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.datnbbook.model.NguoiDung;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private UserInfo userInfo;

    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String maNguoiDung;
        private String tenDangNhap;
        private String email;
        private String hoTen;
        private String soDienThoai;
        private NguoiDung.LoaiNguoiDung loaiNguoiDung;
        private Long khachHangId;
        private Integer nhanVienId;
    }

    public static LoginResponse success(String token, NguoiDung nguoiDung) {
        UserInfo userInfo = new UserInfo(
                nguoiDung.getId(),
                nguoiDung.getMaNguoiDung(),
                nguoiDung.getTenDangNhap(),
                nguoiDung.getEmail(),
                nguoiDung.getHoTen(),
                nguoiDung.getSoDienThoai(),
                nguoiDung.getLoaiNguoiDung(),
                nguoiDung.getKhachHangId(),
                nguoiDung.getNhanVienId()
        );
        return new LoginResponse(true, "Đăng nhập thành công", token, userInfo);
    }

    public static LoginResponse failure(String message) {
        return new LoginResponse(false, message, null, null);
    }
}