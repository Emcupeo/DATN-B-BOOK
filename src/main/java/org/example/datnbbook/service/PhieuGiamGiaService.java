package org.example.datnbbook.service;

import jakarta.mail.MessagingException;
import org.example.datnbbook.dto.PhieuGiamGiaDTO;
import org.example.datnbbook.mapper.PhieuGiamGiaMapper;
import org.example.datnbbook.model.PhieuGiamGia;
import org.example.datnbbook.model.PhieuGiamGiaKhachHang;
import org.example.datnbbook.repository.KhachHangRepository;
import org.example.datnbbook.repository.PhieuGiamGiaKhachHangRepository;
import org.example.datnbbook.repository.PhieuGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PhieuGiamGiaService {

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private PhieuGiamGiaKhachHangService phieuGiamGiaKhachHangService;
    @Autowired
    private EmailService emailService;

    public boolean existsById(Long id) {
        return phieuGiamGiaRepository.existsById(id);
    }

    public void deleteById(Long id) {
        PhieuGiamGia phieu = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy!"));
        phieu.setDeleted(true);
        phieuGiamGiaRepository.save(phieu);
    }

    // ✅ Lấy danh sách phiếu giảm giá kèm thông tin khách hàng nếu có
    public Page<PhieuGiamGiaDTO> getAllDTO(Pageable pageable) {
        Page<PhieuGiamGia> pageEntity = phieuGiamGiaRepository.findAllByDeletedFalse(pageable);
        return pageEntity.map(phieu -> {
            List<PhieuGiamGiaKhachHang> danhSachKhach = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(phieu.getId());
            return PhieuGiamGiaMapper.toDTO(phieu, danhSachKhach);
        });
    }

    // ✅ Lấy chi tiết theo ID
    public Optional<PhieuGiamGiaDTO> getDTOById(Long id) {
        return phieuGiamGiaRepository.findById(id)
                .filter(p -> p.getDeleted() == null || !p.getDeleted())
                .map(phieu -> {
                    List<PhieuGiamGiaKhachHang> ds = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(phieu.getId());
                    return PhieuGiamGiaMapper.toDTO(phieu, ds);
                });
    }

    // ✅ Tạo mới và trả về DTO
    public PhieuGiamGiaDTO createPhieuGiamGiaAndReturnDTO(PhieuGiamGiaDTO dto) {
        PhieuGiamGia phieu = new PhieuGiamGia();

        // Sử dụng maPhieuGiamGia được cung cấp nếu có, nếu không thì tạo mới
        if (dto.getMaPhieuGiamGia() != null && !dto.getMaPhieuGiamGia().isEmpty()) {
            phieu.setMaPhieuGiamGia(dto.getMaPhieuGiamGia());
        } else {
            // Tạo mã mới nếu không được cung cấp
            String nextCode = phieuGiamGiaRepository.getNextSequenceValue();
            phieu.setMaPhieuGiamGia(nextCode);
        }

        phieu.setTenPhieuGiamGia(dto.getTenPhieuGiamGia());
        phieu.setSoPhanTramGiam(dto.getSoPhanTramGiam());
        phieu.setGiaTriGiam(dto.getGiaTriGiam());
        phieu.setGiaTriDonHangToiThieu(dto.getGiaTriDonHangToiThieu());
        phieu.setMoTa(dto.getMoTa());
        phieu.setTrangThai(dto.getTrangThai());
        phieu.setNgayBatDau(dto.getNgayBatDau());
        phieu.setNgayKetThuc(dto.getNgayKetThuc());
        phieu.setSoLuong(dto.getSoLuong());
        phieu.setCreatedAt(LocalDateTime.now());
        phieu.setDeleted(false);

        // Lưu phiếu giảm giá
        phieu = phieuGiamGiaRepository.save(phieu);

        // Xử lý logic khách hàng cụ thể nếu cần
        if (dto.getKhachHangId() != null) {
            var khach = khachHangRepository.findById(dto.getKhachHangId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

            PhieuGiamGiaKhachHang link = new PhieuGiamGiaKhachHang();
            link.setPhieuGiamGia(phieu);
            link.setKhachHang(khach);
            link.setCreatedAt(LocalDateTime.now());
            link.setDeleted(false);
            link.setTrangThai(true);
            link.setSoLuong(1);
            phieuGiamGiaKhachHangService.save(link);

            try {
                emailService.sendVoucherEmail(khach.getEmail(), khach.getHoTen(),
                        dto.getSoPhanTramGiam() != null ? "Giảm theo phần trăm" : "Giảm theo giá trị tiền",
                        dto.getGiaTriGiam().doubleValue());
            } catch (MessagingException e) {
                e.printStackTrace();
                throw new RuntimeException("Lỗi khi gửi email: " + e.getMessage());
            }
        }

        List<PhieuGiamGiaKhachHang> danhSach = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(phieu.getId());
        return PhieuGiamGiaMapper.toDTO(phieu, danhSach);
    }

    // ✅ Cập nhật và trả về DTO
    public PhieuGiamGiaDTO updatePhieuGiamGia(PhieuGiamGiaDTO dto) throws MessagingException {
        PhieuGiamGia existingPhieu = phieuGiamGiaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu giảm giá"));

        // If maPhieuGiamGia is provided, use it; otherwise, generate a new one
        if (dto.getMaPhieuGiamGia() != null && !dto.getMaPhieuGiamGia().isEmpty()) {
            existingPhieu.setMaPhieuGiamGia(dto.getMaPhieuGiamGia());
        } else {
            String nextCode = phieuGiamGiaRepository.getNextSequenceValue();
            existingPhieu.setMaPhieuGiamGia(nextCode);
        }

        // Update other fields
        existingPhieu.setTenPhieuGiamGia(dto.getTenPhieuGiamGia());
        existingPhieu.setSoPhanTramGiam(dto.getSoPhanTramGiam());
        existingPhieu.setGiaTriGiam(dto.getGiaTriGiam());
        existingPhieu.setGiaTriDonHangToiThieu(dto.getGiaTriDonHangToiThieu());
        existingPhieu.setMoTa(dto.getMoTa());
        existingPhieu.setTrangThai(dto.getTrangThai());
        existingPhieu.setNgayBatDau(dto.getNgayBatDau());
        existingPhieu.setNgayKetThuc(dto.getNgayKetThuc());
        existingPhieu.setSoLuong(dto.getSoLuong());

        // Kiểm tra và cập nhật liên kết khách hàng
        List<PhieuGiamGiaKhachHang> danhSachCu = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(existingPhieu.getId());
        Long oldCustomerId = danhSachCu.isEmpty() ? null : Long.valueOf(danhSachCu.get(0).getKhachHang().getId());

        if (dto.getKhachHangId() != null && !dto.getKhachHangId().equals(oldCustomerId)) {
            // Cập nhật hoặc tạo mới liên kết khách hàng
            var kh = khachHangRepository.findById(dto.getKhachHangId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

            if (danhSachCu.isEmpty()) {
                PhieuGiamGiaKhachHang link = new PhieuGiamGiaKhachHang();
                link.setPhieuGiamGia(existingPhieu);
                link.setKhachHang(kh);
                link.setCreatedAt(LocalDateTime.now());
                link.setDeleted(false);
                link.setTrangThai(true);
                link.setSoLuong(1);
                phieuGiamGiaKhachHangService.save(link);
            } else {
                PhieuGiamGiaKhachHang link = danhSachCu.get(0);
                link.setKhachHang(kh);
                phieuGiamGiaKhachHangService.save(link);
            }

            // Gửi email cho khách hàng mới
            emailService.sendVoucherEmail(kh.getEmail(), kh.getHoTen(), existingPhieu.getTenPhieuGiamGia(), existingPhieu.getGiaTriGiam().doubleValue());
        } else if (dto.getKhachHangId() == null && !danhSachCu.isEmpty()) {
            // Xóa liên kết khách hàng cũ nếu chuyển sang công khai
            for (PhieuGiamGiaKhachHang link : danhSachCu) {
                phieuGiamGiaKhachHangService.delete(link); // Giả sử có phương thức delete trong service
            }
        }

        // Save the updated voucher
        PhieuGiamGia updated = phieuGiamGiaRepository.save(existingPhieu);

        // Return DTO with new linkages
        List<PhieuGiamGiaKhachHang> danhSachMoi = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(updated.getId());
        return PhieuGiamGiaMapper.toDTO(updated, danhSachMoi);
    }

    public boolean existsByMaPhieuGiamGia(String maPhieuGiamGia) {
        return phieuGiamGiaRepository.existsByMaPhieuGiamGia(maPhieuGiamGia);
    }

    public Optional<PhieuGiamGia> getById(Long id) {
        return phieuGiamGiaRepository.findById(id);
    }

}
