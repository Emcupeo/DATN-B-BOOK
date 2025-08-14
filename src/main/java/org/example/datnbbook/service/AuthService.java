package org.example.datnbbook.service;

import org.example.datnbbook.dto.LoginRequest;
import org.example.datnbbook.dto.LoginResponse;
import org.example.datnbbook.dto.NguoiDungDTO;
import org.example.datnbbook.model.NguoiDung;
import org.example.datnbbook.model.KhachHang;
import org.example.datnbbook.model.NhanVien;
import org.example.datnbbook.model.ChucVu;
import org.example.datnbbook.repository.NguoiDungRepository;
import org.example.datnbbook.repository.KhachHangRepository;
import org.example.datnbbook.repository.NhanVienRepository;
import org.example.datnbbook.repository.ChucVuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    
    @Autowired
    private KhachHangRepository khachHangRepository;
    
    @Autowired
    private NhanVienRepository nhanVienRepository;
    
    @Autowired
    private ChucVuRepository chucVuRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public LoginResponse login(LoginRequest loginRequest) {
        logger.info("Attempting login for user: {}", loginRequest.getTenDangNhap());
        
        try {
            // Tìm người dùng theo tên đăng nhập và trạng thái active
            Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository
                    .findByTenDangNhapAndTrangThaiTrueAndDeletedFalse(loginRequest.getTenDangNhap());
            
            if (nguoiDungOpt.isEmpty()) {
                logger.warn("User not found or inactive: {}", loginRequest.getTenDangNhap());
                return LoginResponse.failure("Tên đăng nhập không tồn tại hoặc tài khoản đã bị khóa");
            }
            
            NguoiDung nguoiDung = nguoiDungOpt.get();
            
            // Kiểm tra mật khẩu với BCrypt
            if (!passwordEncoder.matches(loginRequest.getMatKhau(), nguoiDung.getMatKhau())) {
                logger.warn("Invalid password for user: {}", loginRequest.getTenDangNhap());
                return LoginResponse.failure("Mật khẩu không chính xác");
            }
            
            // Tạo token đơn giản (trong thực tế nên dùng JWT)
            String token = generateToken(nguoiDung);
            
            logger.info("Login successful for user: {} - Type: {}", 
                    loginRequest.getTenDangNhap(), nguoiDung.getLoaiNguoiDung());
            
            return LoginResponse.success(token, nguoiDung);
            
        } catch (Exception e) {
            logger.error("Login error for user: {}", loginRequest.getTenDangNhap(), e);
            return LoginResponse.failure("Lỗi hệ thống. Vui lòng thử lại sau");
        }
    }
    
    public NguoiDungDTO createNguoiDung(NguoiDungDTO nguoiDungDTO) {
        logger.info("Creating new user: {}", nguoiDungDTO.getTenDangNhap());
        
        // Kiểm tra trùng lặp
        if (nguoiDungRepository.existsByTenDangNhapAndDeletedFalse(nguoiDungDTO.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        
        if (nguoiDungDTO.getEmail() != null && 
            nguoiDungRepository.existsByEmailAndDeletedFalse(nguoiDungDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        
        NguoiDung nguoiDung = new NguoiDung();
        BeanUtils.copyProperties(nguoiDungDTO, nguoiDung, "id", "maNguoiDung", "createdAt", "updatedAt", "matKhau");
        
        // Mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(nguoiDungDTO.getMatKhau());
        nguoiDung.setMatKhau(encodedPassword);
        
        // Generate mã người dùng
        if (nguoiDung.getMaNguoiDung() == null || nguoiDung.getMaNguoiDung().isEmpty()) {
            String nextMaNguoiDung = nguoiDungRepository.getNextSequenceValue();
            nguoiDung.setMaNguoiDung(nextMaNguoiDung);
        }
        
        nguoiDung.setDeleted(false);
        nguoiDung.setTrangThai(true);
        nguoiDung.setCreatedAt(LocalDateTime.now());
        
        NguoiDung savedNguoiDung = nguoiDungRepository.save(nguoiDung);
        
        // Tự động tạo bản ghi KhachHang hoặc NhanVien tương ứng
        try {
            if (savedNguoiDung.getLoaiNguoiDung() == NguoiDung.LoaiNguoiDung.KHACH_HANG) {
                createKhachHangRecord(savedNguoiDung);
            } else if (savedNguoiDung.getLoaiNguoiDung() == NguoiDung.LoaiNguoiDung.NHAN_VIEN) {
                createNhanVienRecord(savedNguoiDung);
            }
        } catch (Exception e) {
            logger.warn("Failed to create corresponding record for user: {}", savedNguoiDung.getMaNguoiDung(), e);
        }
        
        NguoiDungDTO result = new NguoiDungDTO();
        BeanUtils.copyProperties(savedNguoiDung, result);
        
        logger.info("User created successfully: {}", savedNguoiDung.getMaNguoiDung());
        return result;
    }
    
    public List<NguoiDungDTO> getAllNguoiDung() {
        List<NguoiDung> nguoiDungs = nguoiDungRepository.findAllByDeletedFalseOrderByCreatedAtDesc();
        return nguoiDungs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<NguoiDungDTO> getNguoiDungByLoai(NguoiDung.LoaiNguoiDung loaiNguoiDung) {
        List<NguoiDung> nguoiDungs = nguoiDungRepository
                .findAllByLoaiNguoiDungAndDeletedFalseOrderByCreatedAtDesc(loaiNguoiDung);
        return nguoiDungs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public NguoiDungDTO getNguoiDungById(Long id) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .filter(nd -> !nd.getDeleted())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));
        return convertToDTO(nguoiDung);
    }
    
    public NguoiDungDTO updateNguoiDung(Long id, NguoiDungDTO nguoiDungDTO) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .filter(nd -> !nd.getDeleted())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));
        
        // Kiểm tra trùng lặp (trừ chính nó)
        if (!nguoiDung.getTenDangNhap().equals(nguoiDungDTO.getTenDangNhap()) &&
            nguoiDungRepository.existsByTenDangNhapAndDeletedFalse(nguoiDungDTO.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        
        BeanUtils.copyProperties(nguoiDungDTO, nguoiDung, 
                "id", "maNguoiDung", "createdAt", "createdBy", "deleted", "matKhau");
        
        // Chỉ cập nhật mật khẩu nếu có mật khẩu mới
        if (nguoiDungDTO.getMatKhau() != null && !nguoiDungDTO.getMatKhau().trim().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(nguoiDungDTO.getMatKhau());
            nguoiDung.setMatKhau(encodedPassword);
        }
        
        nguoiDung.setUpdatedAt(LocalDateTime.now());
        
        NguoiDung savedNguoiDung = nguoiDungRepository.save(nguoiDung);
        return convertToDTO(savedNguoiDung);
    }
    
    public void deleteNguoiDung(Long id) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .filter(nd -> !nd.getDeleted())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));
        
        nguoiDung.setDeleted(true);
        nguoiDung.setUpdatedAt(LocalDateTime.now());
        nguoiDungRepository.save(nguoiDung);
    }
    
    public List<NguoiDungDTO> searchNguoiDung(String keyword) {
        List<NguoiDung> nguoiDungs = nguoiDungRepository.search(keyword);
        return nguoiDungs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public NguoiDungDTO createAdminAccount(String tenDangNhap, String matKhau, String hoTen, String email) {
        logger.info("Creating admin account: {}", tenDangNhap);
        
        NguoiDungDTO adminDTO = new NguoiDungDTO();
        adminDTO.setTenDangNhap(tenDangNhap);
        adminDTO.setMatKhau(matKhau); // Sẽ được mã hóa trong createNguoiDung
        adminDTO.setHoTen(hoTen);
        adminDTO.setEmail(email);
        adminDTO.setLoaiNguoiDung(NguoiDung.LoaiNguoiDung.ADMIN);
        adminDTO.setTrangThai(true);
        
        return createNguoiDung(adminDTO);
    }
    
    public NguoiDungDTO createNguoiDungWithoutAssociatedRecord(NguoiDungDTO nguoiDungDTO) {
        logger.info("Creating new user without associated record: {}", nguoiDungDTO.getTenDangNhap());
        
        // Kiểm tra trùng lặp
        if (nguoiDungRepository.existsByTenDangNhapAndDeletedFalse(nguoiDungDTO.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        
        if (nguoiDungDTO.getEmail() != null && 
            nguoiDungRepository.existsByEmailAndDeletedFalse(nguoiDungDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        
        NguoiDung nguoiDung = new NguoiDung();
        BeanUtils.copyProperties(nguoiDungDTO, nguoiDung, "id", "maNguoiDung", "createdAt", "updatedAt", "matKhau");
        
        // Mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(nguoiDungDTO.getMatKhau());
        nguoiDung.setMatKhau(encodedPassword);
        
        // Generate mã người dùng
        if (nguoiDung.getMaNguoiDung() == null || nguoiDung.getMaNguoiDung().isEmpty()) {
            String nextMaNguoiDung = nguoiDungRepository.getNextSequenceValue();
            nguoiDung.setMaNguoiDung(nextMaNguoiDung);
        }
        
        nguoiDung.setDeleted(false);
        nguoiDung.setTrangThai(true);
        nguoiDung.setCreatedAt(LocalDateTime.now());
        
        NguoiDung savedNguoiDung = nguoiDungRepository.save(nguoiDung);
        
        // KHÔNG tự động tạo bản ghi KhachHang hoặc NhanVien tương ứng
        
        NguoiDungDTO result = new NguoiDungDTO();
        BeanUtils.copyProperties(savedNguoiDung, result);
        
        logger.info("User created successfully (without associated record): {}", savedNguoiDung.getMaNguoiDung());
        return result;
    }
    
    public NguoiDungDTO getNguoiDungByUsername(String tenDangNhap) {
        Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository
                .findByTenDangNhapAndTrangThaiTrueAndDeletedFalse(tenDangNhap);
        
        if (nguoiDungOpt.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại hoặc đã bị khóa");
        }
        
        return convertToDTO(nguoiDungOpt.get());
    }
    
    public boolean existsByTenDangNhap(String tenDangNhap) {
        return nguoiDungRepository.existsByTenDangNhapAndDeletedFalse(tenDangNhap);
    }

    public NguoiDungDTO changePassword(String tenDangNhap, String currentPassword, String newPassword) {
        logger.info("Changing password for user: {}", tenDangNhap);
        
        // Tìm người dùng
        Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository
                .findByTenDangNhapAndTrangThaiTrueAndDeletedFalse(tenDangNhap);
        
        if (nguoiDungOpt.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại hoặc đã bị khóa");
        }
        
        NguoiDung nguoiDung = nguoiDungOpt.get();
        
        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(currentPassword, nguoiDung.getMatKhau())) {
            throw new RuntimeException("Mật khẩu hiện tại không chính xác");
        }
        
        // Kiểm tra mật khẩu mới không trùng với mật khẩu cũ
        if (passwordEncoder.matches(newPassword, nguoiDung.getMatKhau())) {
            throw new RuntimeException("Mật khẩu mới phải khác mật khẩu hiện tại");
        }
        
        // Cập nhật mật khẩu mới
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        nguoiDung.setMatKhau(encodedNewPassword);
        nguoiDung.setUpdatedAt(LocalDateTime.now());
        
        NguoiDung savedNguoiDung = nguoiDungRepository.save(nguoiDung);
        
        NguoiDungDTO result = new NguoiDungDTO();
        BeanUtils.copyProperties(savedNguoiDung, result);
        // Không trả về mật khẩu
        result.setMatKhau(null);
        
        logger.info("Password changed successfully for user: {}", tenDangNhap);
        return result;
    }
    
    private String generateToken(NguoiDung nguoiDung) {
        // Đơn giản cho demo - trong thực tế nên dùng JWT
        return UUID.randomUUID().toString() + "_" + nguoiDung.getId() + "_" + nguoiDung.getLoaiNguoiDung();
    }
    
    private void createKhachHangRecord(NguoiDung nguoiDung) {
        logger.info("Creating KhachHang record for user: {}", nguoiDung.getMaNguoiDung());
        
        KhachHang khachHang = new KhachHang();
        
        // Tạo mã khách hàng tự động
        String maKhachHang = khachHangRepository.getNextMaKhachHang();
        khachHang.setMaKhachHang(maKhachHang);
        
        khachHang.setHoTen(nguoiDung.getHoTen());
        khachHang.setEmail(nguoiDung.getEmail());
        khachHang.setSoDienThoai(nguoiDung.getSoDienThoai());
        khachHang.setTrangThai(1); // 1 = active, 0 = inactive
        khachHang.setDeleted(false);
        khachHang.setCreatedAt(LocalDateTime.now());
        khachHang.setCreatedBy("System");
        
        KhachHang savedKhachHang = khachHangRepository.save(khachHang);
        
        // Cập nhật lại NguoiDung với khachHangId
        nguoiDung.setKhachHangId(savedKhachHang.getId());
        nguoiDungRepository.save(nguoiDung);
        
        logger.info("KhachHang record created successfully: {}", savedKhachHang.getMaKhachHang());
    }
    
    private void createNhanVienRecord(NguoiDung nguoiDung) {
        logger.info("Creating NhanVien record for user: {}", nguoiDung.getMaNguoiDung());
        
        NhanVien nhanVien = new NhanVien();
        
        // Tạo mã nhân viên tự động
        String maNhanVien = nhanVienRepository.getNextMaNhanVien();
        nhanVien.setMaNhanVien(maNhanVien);
        
        nhanVien.setHoTen(nguoiDung.getHoTen());
        nhanVien.setEmail(nguoiDung.getEmail());
        nhanVien.setSoDienThoai(nguoiDung.getSoDienThoai());
        nhanVien.setTrangThai(true);
        nhanVien.setDeleted(false);
        nhanVien.setCreatedAt(java.time.Instant.now());
        nhanVien.setCreatedBy("System");
        
        // Tìm chức vụ mặc định
        Optional<ChucVu> chucVuOpt = chucVuRepository.findByTenChucVuContainingIgnoreCase("Nhân viên");
        if (chucVuOpt.isEmpty()) {
            // Nếu không tìm thấy, lấy chức vụ đầu tiên
            chucVuOpt = chucVuRepository.findFirstByOrderByIdAsc();
        }
        
        if (chucVuOpt.isPresent()) {
            nhanVien.setIdChucVu(chucVuOpt.get());
        }
        
        NhanVien savedNhanVien = nhanVienRepository.save(nhanVien);
        
        // Cập nhật lại NguoiDung với nhanVienId  
        nguoiDung.setNhanVienId(savedNhanVien.getId());
        nguoiDungRepository.save(nguoiDung);
        
        logger.info("NhanVien record created successfully: {}", savedNhanVien.getMaNhanVien());
    }
    
    private NguoiDungDTO convertToDTO(NguoiDung nguoiDung) {
        NguoiDungDTO dto = new NguoiDungDTO();
        BeanUtils.copyProperties(nguoiDung, dto);
        // Không trả về mật khẩu
        dto.setMatKhau(null);
        return dto;
    }
}