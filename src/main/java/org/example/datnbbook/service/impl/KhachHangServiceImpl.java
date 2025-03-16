package org.example.datnbbook.service.impl;

import org.example.datnbbook.dto.KhachHangDTO;
import org.example.datnbbook.model.DiaChi;
import org.example.datnbbook.model.KhachHang;
import org.example.datnbbook.repository.KhachHangRepository;
import org.example.datnbbook.service.KhachHangService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class KhachHangServiceImpl implements KhachHangService {
    
    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<KhachHangDTO> getAll() {
        return khachHangRepository.findByDeletedFalse()
                .stream()
                .map(khachHang -> modelMapper.map(khachHang, KhachHangDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public KhachHangDTO getById(Long id) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        return modelMapper.map(khachHang, KhachHangDTO.class);
    }

    @Override
    @Transactional
    public KhachHangDTO create(KhachHangDTO khachHangDTO) {
        KhachHang khachHang = modelMapper.map(khachHangDTO, KhachHang.class);
        
        // Xử lý địa chỉ mặc định
        if (khachHang.getDanhSachDiaChi() != null && !khachHang.getDanhSachDiaChi().isEmpty()) {
            boolean hasDefault = false;
            for (DiaChi diaChi : khachHang.getDanhSachDiaChi()) {
                if (Boolean.TRUE.equals(diaChi.getMacDinh())) {
                    if (hasDefault) {
                        diaChi.setMacDinh(false);
                    } else {
                        hasDefault = true;
                    }
                }
                diaChi.setKhachHang(khachHang);
            }
            // Nếu không có địa chỉ mặc định, set địa chỉ đầu tiên làm mặc định
            if (!hasDefault && !khachHang.getDanhSachDiaChi().isEmpty()) {
                khachHang.getDanhSachDiaChi().get(0).setMacDinh(true);
            }
        }

        KhachHang savedKhachHang = khachHangRepository.save(khachHang);
        return modelMapper.map(savedKhachHang, KhachHangDTO.class);
    }

    @Override
    @Transactional
    public KhachHangDTO update(Long id, KhachHangDTO khachHangDTO) {
        KhachHang existingKhachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        // Cập nhật thông tin cơ bản
        existingKhachHang.setHoTen(khachHangDTO.getHoTen());
        existingKhachHang.setEmail(khachHangDTO.getEmail());
        existingKhachHang.setSoDienThoai(khachHangDTO.getSoDienThoai());
        existingKhachHang.setNgaySinh(khachHangDTO.getNgaySinh());
        existingKhachHang.setGioiTinh(khachHangDTO.getGioiTinh());
        existingKhachHang.setTrangThai(khachHangDTO.getTrangThai());

        // Xử lý danh sách địa chỉ
        if (khachHangDTO.getDanhSachDiaChi() != null) {
            existingKhachHang.getDanhSachDiaChi().clear();
            khachHangDTO.getDanhSachDiaChi().forEach(diaChiDTO -> {
                DiaChi diaChi = modelMapper.map(diaChiDTO, DiaChi.class);
                diaChi.setKhachHang(existingKhachHang);
                existingKhachHang.getDanhSachDiaChi().add(diaChi);
            });
        }

        KhachHang updatedKhachHang = khachHangRepository.save(existingKhachHang);
        return modelMapper.map(updatedKhachHang, KhachHangDTO.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        khachHang.setDeleted(true);
        khachHangRepository.save(khachHang);
    }

    @Override
    public List<KhachHangDTO> search(String keyword) {
        return khachHangRepository.search(keyword)
                .stream()
                .map(khachHang -> modelMapper.map(khachHang, KhachHangDTO.class))
                .collect(Collectors.toList());
    }
} 