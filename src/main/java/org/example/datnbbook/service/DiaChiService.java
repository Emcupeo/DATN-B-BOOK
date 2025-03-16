package org.example.datnbbook.service;

import org.example.datnbbook.dto.DiaChiDTO;
import org.example.datnbbook.model.DiaChi;
import org.example.datnbbook.model.KhachHang;
import org.example.datnbbook.repository.DiaChiRepository;
import org.example.datnbbook.repository.KhachHangRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaChiService {

    @Autowired
    private DiaChiRepository diaChiRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<DiaChiDTO> getAllByKhachHangId(Long khachHangId) {
        return diaChiRepository.findByKhachHangIdAndDeletedFalse(khachHangId)
                .stream()
                .map(diaChi -> modelMapper.map(diaChi, DiaChiDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public DiaChiDTO create(Long khachHangId, DiaChiDTO diaChiDTO) {
        KhachHang khachHang = khachHangRepository.findById(khachHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        DiaChi diaChi = modelMapper.map(diaChiDTO, DiaChi.class);
        diaChi.setKhachHang(khachHang);

        // Nếu là địa chỉ mặc định, cập nhật các địa chỉ khác
        if (Boolean.TRUE.equals(diaChiDTO.getMacDinh())) {
            khachHang.getDanhSachDiaChi().forEach(d -> d.setMacDinh(false));
        }

        DiaChi savedDiaChi = diaChiRepository.save(diaChi);
        return modelMapper.map(savedDiaChi, DiaChiDTO.class);
    }

    @Transactional
    public DiaChiDTO update(Long id, DiaChiDTO diaChiDTO) {
        DiaChi diaChi = diaChiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));

        // Cập nhật thông tin
        diaChi.setDiaChiChiTiet(diaChiDTO.getDiaChiChiTiet());
        diaChi.setTinhThanh(diaChiDTO.getTinhThanh());
        diaChi.setQuanHuyen(diaChiDTO.getQuanHuyen());
        diaChi.setXaPhuong(diaChiDTO.getXaPhuong());

        // Xử lý địa chỉ mặc định
        if (Boolean.TRUE.equals(diaChiDTO.getMacDinh()) && !Boolean.TRUE.equals(diaChi.getMacDinh())) {
            KhachHang khachHang = diaChi.getKhachHang();
            khachHang.getDanhSachDiaChi().forEach(d -> d.setMacDinh(false));
            diaChi.setMacDinh(true);
        }

        DiaChi updatedDiaChi = diaChiRepository.save(diaChi);
        return modelMapper.map(updatedDiaChi, DiaChiDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        DiaChi diaChi = diaChiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
        diaChi.setDeleted(true);
        diaChiRepository.save(diaChi);
    }

    public DiaChiDTO getDefaultAddress(Long khachHangId) {
        DiaChi diaChi = diaChiRepository.findDefaultAddress(khachHangId);
        return diaChi != null ? modelMapper.map(diaChi, DiaChiDTO.class) : null;
    }
} 