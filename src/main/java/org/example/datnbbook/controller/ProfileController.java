package org.example.datnbbook.controller;

import org.example.datnbbook.dto.NguoiDungDTO;
import org.example.datnbbook.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private AuthService authService;

    @GetMapping("/{tenDangNhap}")
    public ResponseEntity<?> getProfile(@PathVariable String tenDangNhap) {
        try {
            logger.info("Getting profile for user: {}", tenDangNhap);
            
            NguoiDungDTO nguoiDung = authService.getNguoiDungByUsername(tenDangNhap);
            
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "data", nguoiDung,
                "message", "Lấy thông tin profile thành công"
            ));
            
        } catch (Exception e) {
            logger.error("Error getting profile for user: {}", tenDangNhap, e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        try {
            String tenDangNhap = request.get("tenDangNhap");
            String currentPassword = request.get("currentPassword");
            String newPassword = request.get("newPassword");

            logger.info("Change password request for user: {}", tenDangNhap);

            // Validation
            if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Tên đăng nhập không được để trống"
                ));
            }

            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Mật khẩu hiện tại không được để trống"
                ));
            }

            if (newPassword == null || newPassword.length() < 6) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Mật khẩu mới phải có ít nhất 6 ký tự"
                ));
            }

            NguoiDungDTO result = authService.changePassword(tenDangNhap, currentPassword, newPassword);

            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "data", result,
                "message", "Đổi mật khẩu thành công"
            ));

        } catch (Exception e) {
            logger.error("Error changing password", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/{tenDangNhap}")
    public ResponseEntity<?> updateProfile(@PathVariable String tenDangNhap, @RequestBody NguoiDungDTO nguoiDungDTO) {
        try {
            logger.info("Updating profile for user: {}", tenDangNhap);
            
            // Set the username to ensure consistency
            nguoiDungDTO.setTenDangNhap(tenDangNhap);
            
            // Get current user ID
            NguoiDungDTO currentUser = authService.getNguoiDungByUsername(tenDangNhap);
            Long userId = currentUser.getId();
            
            NguoiDungDTO result = authService.updateNguoiDung(userId, nguoiDungDTO);
            
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "data", result,
                "message", "Cập nhật profile thành công"
            ));
            
        } catch (Exception e) {
            logger.error("Error updating profile for user: {}", tenDangNhap, e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}