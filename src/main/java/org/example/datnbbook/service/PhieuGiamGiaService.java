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

        // Sinh mã phiếu giảm giá
        String nextCode = phieuGiamGiaRepository.getNextSequenceValue();
        phieu.setMaPhieuGiamGia(nextCode);

        // Lưu phiếu
        phieu = phieuGiamGiaRepository.save(phieu);

        // Nếu có khách hàng cụ thể
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
            System.out.println("Sending email to: " + khach.getEmail());
            System.out.println("Voucher type: " + (dto.getSoPhanTramGiam() != null ? "Giảm theo phần trăm" : "Giảm theo giá trị tiền"));
            System.out.println("Voucher value: " + dto.getGiaTriGiam());

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
    public PhieuGiamGiaDTO updatePhieuGiamGia(PhieuGiamGiaDTO dto) {
        PhieuGiamGia phieu = phieuGiamGiaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu giảm giá"));

        // Cập nhật các trường phiếu giảm giá
        phieu.setSoPhanTramGiam(dto.getSoPhanTramGiam());
        phieu.setGiaTriGiam(dto.getGiaTriGiam());
        phieu.setGiaTriDonHangToiThieu(dto.getGiaTriDonHangToiThieu());
        phieu.setMoTa(dto.getMoTa());
        phieu.setTrangThai(dto.getTrangThai());
        phieu.setNgayBatDau(dto.getNgayBatDau());
        phieu.setNgayKetThuc(dto.getNgayKetThuc());
        phieu.setSoLuong(dto.getSoLuong());

        // Xử lý phần liên kết khách hàng
        List<PhieuGiamGiaKhachHang> danhSachCu = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(phieu.getId());

        if (dto.getKhachHangId() != null) {
            var kh = khachHangRepository.findById(dto.getKhachHangId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

            if (danhSachCu.isEmpty()) {
                // Nếu chưa có liên kết: tạo mới
                PhieuGiamGiaKhachHang link = new PhieuGiamGiaKhachHang();
                link.setPhieuGiamGia(phieu);
                link.setKhachHang(kh);
                link.setCreatedAt(LocalDateTime.now());
                link.setDeleted(false);
                link.setTrangThai(true);
                link.setSoLuong(1);
                phieuGiamGiaKhachHangService.save(link);
                System.out.println("Tạo mới liên kết với khách hàng: " + kh.getId());
            } else {
                // Nếu đã có liên kết: cập nhật khách hàng mới vào bản ghi đầu tiên
                PhieuGiamGiaKhachHang link = danhSachCu.get(0); // Giả sử chỉ có 1 liên kết
                link.setKhachHang(kh);
                phieuGiamGiaKhachHangService.save(link);
                System.out.println("Cập nhật liên kết từ khách hàng cũ sang khách hàng mới: " + kh.getId());
            }
        } else {
            // Nếu là công khai: xóa hết liên kết cũ
            if (!danhSachCu.isEmpty()) {
                System.out.println("Chuyển sang công khai, xóa liên kết cũ cho phiếu: " + phieu.getId());
                phieuGiamGiaKhachHangService.deleteByPhieuGiamGiaId(phieu.getId());
            }
        }

        // Lưu phiếu giảm giá đã cập nhật
        PhieuGiamGia updated = phieuGiamGiaRepository.save(phieu);

        // Trả về DTO với danh sách liên kết mới
        List<PhieuGiamGiaKhachHang> danhSachMoi = phieuGiamGiaKhachHangService.findByPhieuGiamGiaId(phieu.getId());
        return PhieuGiamGiaMapper.toDTO(updated, danhSachMoi);
    }

}
