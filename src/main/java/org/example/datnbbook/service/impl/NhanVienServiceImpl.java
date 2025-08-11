package org.example.datnbbook.service.impl;

import org.example.datnbbook.dto.NhanVienDTO;
import org.example.datnbbook.dto.NguoiDungDTO;
import org.example.datnbbook.model.NhanVien;
import org.example.datnbbook.model.NguoiDung;
import org.example.datnbbook.repository.NhanVienRepository;
import org.example.datnbbook.service.NhanVienService;
import org.example.datnbbook.service.EmailService;
import org.example.datnbbook.service.AuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class NhanVienServiceImpl implements NhanVienService {
    private final NhanVienRepository nhanVienRepository;
    private final EmailService emailService;
    private final Random random = new Random();
    private final EntityManager entityManager;

    private final AuthService authService;

    public NhanVienServiceImpl(NhanVienRepository nhanVienRepository, EmailService emailService, 
                              EntityManager entityManager, AuthService authService) {
        this.nhanVienRepository = nhanVienRepository;
        this.emailService = emailService;
        this.entityManager = entityManager;
        this.authService = authService;
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    @Override
    public List<NhanVienDTO> getAll() {
        return nhanVienRepository.findByDeletedFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NhanVienDTO getById(Integer id) {
        return nhanVienRepository.findById(id)
                .filter(nv -> !nv.getDeleted())
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại với id: " + id));
    }

    @Override
    public NhanVienDTO create(NhanVienDTO nhanVienDTO) {
        // Xóa dữ liệu NV00000 nếu có (để tránh xung đột)
        cleanupInvalidData();
        
        // Tạo mã nhân viên an toàn
        String maNhanVien = generateSafeMaNhanVien();
        
        // Kiểm tra xem tên đăng nhập đã tồn tại chưa
        if (authService.existsByTenDangNhap(maNhanVien)) {
            throw new RuntimeException("Tên đăng nhập " + maNhanVien + " đã tồn tại");
        }
        
        NhanVien nhanVien = new NhanVien();
        String randomPassword = generateRandomPassword();
        nhanVien.setMatKhau(randomPassword);
        nhanVien.setCreatedAt(Instant.now());
        nhanVien.setUpdatedAt(Instant.now());
        nhanVien.setDeleted(false);
        nhanVien.setTrangThai(true);
        
        nhanVien.setMaNhanVien(maNhanVien);
        nhanVien.setTenTaiKhoan(maNhanVien);
        
        BeanUtils.copyProperties(nhanVienDTO, nhanVien, "id", "maNhanVien", "tenTaiKhoan", "matKhau", "createdAt", "updatedAt", "deleted", "trangThai", "createdBy", "updatedBy");
        NhanVien savedNhanVien = nhanVienRepository.save(nhanVien);
        
        // Tạo tài khoản NguoiDung tương ứng
        try {
            NguoiDungDTO nguoiDungDTO = new NguoiDungDTO();
            nguoiDungDTO.setTenDangNhap(maNhanVien);
            nguoiDungDTO.setMatKhau(randomPassword);
            nguoiDungDTO.setEmail(savedNhanVien.getEmail());
            nguoiDungDTO.setHoTen(savedNhanVien.getHoTen());
            nguoiDungDTO.setSoDienThoai(savedNhanVien.getSoDienThoai());
            nguoiDungDTO.setLoaiNguoiDung(NguoiDung.LoaiNguoiDung.NHAN_VIEN);
            nguoiDungDTO.setTrangThai(true);
            nguoiDungDTO.setNhanVienId(savedNhanVien.getId());
            
            // Tạo NguoiDung mà không để nó tự tạo NhanVien
            authService.createNguoiDungWithoutAssociatedRecord(nguoiDungDTO);
        } catch (Exception e) {
            // Nếu tạo NguoiDung thất bại, xóa NhanVien đã tạo và ném exception
            nhanVienRepository.delete(savedNhanVien);
            throw new RuntimeException("Không thể tạo tài khoản đăng nhập cho NV " + maNhanVien + ": " + e.getMessage());
        }
        
        try {
            emailService.sendEmployeeCredentials(
                    savedNhanVien.getEmail(),
                    maNhanVien,
                    randomPassword
            );
        } catch (Exception e) {
            System.err.println("Không thể gửi email thông tin đăng nhập cho NV " + maNhanVien + ": " + e.getMessage());
        }
        return convertToDTO(savedNhanVien);
    }

    @Override
    public NhanVienDTO update(Integer id, NhanVienDTO nhanVienDTO) {
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .filter(nv -> !nv.getDeleted())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại với id: " + id));
        String tenTaiKhoan = nhanVien.getTenTaiKhoan();
        String matKhau = nhanVien.getMatKhau();
        String maNhanVien = nhanVien.getMaNhanVien();
        BeanUtils.copyProperties(nhanVienDTO, nhanVien, "id", "createdAt", "createdBy", "deleted", "tenTaiKhoan", "matKhau", "maNhanVien");
        nhanVien.setUpdatedAt(Instant.now());
        nhanVien.setTenTaiKhoan(tenTaiKhoan);
        nhanVien.setMatKhau(matKhau);
        nhanVien.setMaNhanVien(maNhanVien);
        nhanVien = nhanVienRepository.save(nhanVien);
        return convertToDTO(nhanVien);
    }

    @Override
    public void delete(Integer id) {
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .filter(nv -> !nv.getDeleted())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại với id: " + id));
        nhanVien.setDeleted(true);
        nhanVienRepository.save(nhanVien);
    }

    @Override
    public List<NhanVienDTO> search(String keyword) {
        return nhanVienRepository.search(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NhanVienDTO updateStatus(Integer id) {
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .filter(nv -> !nv.getDeleted())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại với id: " + id));
        nhanVien.setTrangThai(!nhanVien.getTrangThai());
        nhanVien.setUpdatedAt(Instant.now());
        nhanVien = nhanVienRepository.save(nhanVien);
        return convertToDTO(nhanVien);
    }

    private NhanVienDTO convertToDTO(NhanVien nhanVien) {
        NhanVienDTO nhanVienDTO = new NhanVienDTO();
        BeanUtils.copyProperties(nhanVien, nhanVienDTO);
        if (nhanVien.getIdChucVu() != null) {
            nhanVienDTO.setIdChucVu(nhanVien.getIdChucVu().getId());
        }
        return nhanVienDTO;
    }
    
    private String generateSafeMaNhanVien() {
        String maxMaNhanVien = nhanVienRepository.getMaxMaNhanVien();
        int nextNumber = 1;
        
        if (maxMaNhanVien != null && maxMaNhanVien.startsWith("NV")) {
            try {
                String numberStr = maxMaNhanVien.substring(2);
                int currentNumber = Integer.parseInt(numberStr);
                // Đảm bảo không bao giờ trả về 0, luôn bắt đầu từ 1
                nextNumber = Math.max(currentNumber + 1, 1);
            } catch (NumberFormatException e) {
                // Nếu không parse được, bắt đầu từ 1
                nextNumber = 1;
            }
        }
        
        // Đảm bảo không bao giờ trả về NV00000
        if (nextNumber == 0) {
            nextNumber = 1;
        }
        
        // Tạo mã và kiểm tra xem đã tồn tại chưa
        String candidateMaNhanVien = String.format("NV%05d", nextNumber);
        
        // Kiểm tra xem mã này đã tồn tại chưa (cả trong nhan_vien và nguoi_dung)
        while (nhanVienRepository.existsByMaNhanVien(candidateMaNhanVien) || 
               authService.existsByTenDangNhap(candidateMaNhanVien)) {
            nextNumber++;
            candidateMaNhanVien = String.format("NV%05d", nextNumber);
            
            // Đảm bảo không vượt quá giới hạn hợp lý
            if (nextNumber > 99999) {
                throw new RuntimeException("Đã đạt giới hạn số lượng nhân viên (99999)");
            }
        }
        
        return candidateMaNhanVien;
    }
    
    private void cleanupInvalidData() {
        try {
            // Xóa dữ liệu NV00000 nếu có
            List<NhanVien> invalidNhanVien = nhanVienRepository.findByMaNhanVien("NV00000");
            if (!invalidNhanVien.isEmpty()) {
                nhanVienRepository.deleteAll(invalidNhanVien);
                System.out.println("Đã xóa dữ liệu NV00000 không hợp lệ");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi dọn dẹp dữ liệu không hợp lệ: " + e.getMessage());
        }
    }
}