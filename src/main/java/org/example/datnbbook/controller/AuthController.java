package org.example.datnbbook.controller;

import org.example.datnbbook.dto.LoginRequest;
import org.example.datnbbook.dto.LoginResponse;
import org.example.datnbbook.dto.NguoiDungDTO;
import org.example.datnbbook.model.NguoiDung;
import org.example.datnbbook.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;
    
    /**
     * API đăng nhập thống nhất cho tất cả loại người dùng
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login request received for user: {}", loginRequest.getTenDangNhap());
        
        if (loginRequest.getTenDangNhap() == null || loginRequest.getTenDangNhap().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(LoginResponse.failure("Tên đăng nhập không được để trống"));
        }
        
        if (loginRequest.getMatKhau() == null || loginRequest.getMatKhau().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(LoginResponse.failure("Mật khẩu không được để trống"));
        }
        
        LoginResponse response = authService.login(loginRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }
    
    /**
     * API tạo người dùng mới
     */
    @PostMapping("/nguoi-dung")
    public ResponseEntity<?> createNguoiDung(@RequestBody NguoiDungDTO nguoiDungDTO) {
        logger.info("Create user request: {}", nguoiDungDTO.getTenDangNhap());
        
        try {
            // Validation cơ bản
            if (nguoiDungDTO.getTenDangNhap() == null || nguoiDungDTO.getTenDangNhap().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Tên đăng nhập không được để trống"));
            }
            
            if (nguoiDungDTO.getMatKhau() == null || nguoiDungDTO.getMatKhau().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Mật khẩu không được để trống"));
            }
            
            if (nguoiDungDTO.getLoaiNguoiDung() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Loại người dùng không được để trống"));
            }
            
            NguoiDungDTO createdUser = authService.createNguoiDung(nguoiDungDTO);
            return ResponseEntity.ok(createdUser);
            
        } catch (RuntimeException e) {
            logger.error("Error creating user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error creating user", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Lỗi hệ thống"));
        }
    }
    
    /**
     * API lấy tất cả người dùng
     */
    @GetMapping("/nguoi-dung")
    public ResponseEntity<List<NguoiDungDTO>> getAllNguoiDung() {
        List<NguoiDungDTO> users = authService.getAllNguoiDung();
        return ResponseEntity.ok(users);
    }
    
    /**
     * API lấy người dùng theo loại
     */
    @GetMapping("/nguoi-dung/loai/{loaiNguoiDung}")
    public ResponseEntity<List<NguoiDungDTO>> getNguoiDungByLoai(@PathVariable NguoiDung.LoaiNguoiDung loaiNguoiDung) {
        List<NguoiDungDTO> users = authService.getNguoiDungByLoai(loaiNguoiDung);
        return ResponseEntity.ok(users);
    }
    
    /**
     * API lấy người dùng theo ID
     */
    @GetMapping("/nguoi-dung/{id}")
    public ResponseEntity<?> getNguoiDungById(@PathVariable Long id) {
        try {
            NguoiDungDTO user = authService.getNguoiDungById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * API cập nhật người dùng
     */
    @PutMapping("/nguoi-dung/{id}")
    public ResponseEntity<?> updateNguoiDung(@PathVariable Long id, @RequestBody NguoiDungDTO nguoiDungDTO) {
        try {
            NguoiDungDTO updatedUser = authService.updateNguoiDung(id, nguoiDungDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * API xóa người dùng (soft delete)
     */
    @DeleteMapping("/nguoi-dung/{id}")
    public ResponseEntity<?> deleteNguoiDung(@PathVariable Long id) {
        try {
            authService.deleteNguoiDung(id);
            return ResponseEntity.ok(Map.of("message", "Xóa người dùng thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * API tìm kiếm người dùng
     */
    @GetMapping("/nguoi-dung/search")
    public ResponseEntity<List<NguoiDungDTO>> searchNguoiDung(@RequestParam String keyword) {
        List<NguoiDungDTO> users = authService.searchNguoiDung(keyword);
        return ResponseEntity.ok(users);
    }
    
    /**
     * API tạo tài khoản admin mặc định (chỉ dùng lần đầu setup)
     */
    @PostMapping("/create-admin")
    public ResponseEntity<?> createDefaultAdmin() {
        try {
            NguoiDungDTO admin = authService.createAdminAccount(
                    "admin", 
                    "admin123", 
                    "Administrator", 
                    "admin@bbook.com"
            );
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * API kiểm tra trạng thái hệ thống
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        return ResponseEntity.ok(Map.of(
                "status", "active",
                "message", "Authentication service is running",
                "timestamp", System.currentTimeMillis()
        ));
    }
}