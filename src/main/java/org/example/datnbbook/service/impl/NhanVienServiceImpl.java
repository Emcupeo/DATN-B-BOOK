package org.example.datnbbook.service.impl;

import org.example.datnbbook.dto.NhanVienDTO;
import org.example.datnbbook.model.NhanVien;
import org.example.datnbbook.repository.NhanVienRepository;
import org.example.datnbbook.service.NhanVienService;
import org.example.datnbbook.service.EmailService;
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

    public NhanVienServiceImpl(NhanVienRepository nhanVienRepository, EmailService emailService, EntityManager entityManager) {
        this.nhanVienRepository = nhanVienRepository;
        this.emailService = emailService;
        this.entityManager = entityManager;
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
        NhanVien nhanVien = new NhanVien();
        String randomPassword = generateRandomPassword();
        nhanVien.setMatKhau(randomPassword);
        nhanVien.setCreatedAt(Instant.now());
        nhanVien.setUpdatedAt(Instant.now());
        nhanVien.setDeleted(false);
        nhanVien.setTrangThai(true);
        BeanUtils.copyProperties(nhanVienDTO, nhanVien, "id", "maNhanVien", "tenTaiKhoan", "matKhau", "createdAt", "updatedAt", "deleted", "trangThai", "createdBy", "updatedBy");
        NhanVien savedNhanVien = nhanVienRepository.save(nhanVien);
        entityManager.flush();
        entityManager.refresh(savedNhanVien);
        String maNhanVien = savedNhanVien.getMaNhanVien();
        if (maNhanVien == null || maNhanVien.isEmpty()) {
            throw new RuntimeException("Trigger không tạo được mã nhân viên hoặc không cập nhật được entity.");
        }
        savedNhanVien.setTenTaiKhoan(maNhanVien);
        savedNhanVien = nhanVienRepository.save(savedNhanVien);
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
}