package org.example.datnbbook.service.impl;

import org.example.datnbbook.dto.NhanVienDTO;
import org.example.datnbbook.model.NhanVien;
import org.example.datnbbook.repository.NhanVienRepository;
import org.example.datnbbook.service.NhanVienService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NhanVienServiceImpl implements NhanVienService {
    private final NhanVienRepository nhanVienRepository;

    public NhanVienServiceImpl(NhanVienRepository nhanVienRepository) {
        this.nhanVienRepository = nhanVienRepository;
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
//        if (nhanVienDTO.getMaNhanVien() == null || nhanVienDTO.getMaNhanVien().isEmpty()) {
////            String nextMaSanPham = nhanVienRepository.getNextSequenceValue();
//            nhanVienDTO.setMaNhanVien(nextMaSanPham);
//        }
        nhanVienDTO.setDeleted(false); // Đảm bảo sản phẩm mới không bị đánh dấu xóa

        NhanVien nhanVien = new NhanVien();
        BeanUtils.copyProperties(nhanVienDTO, nhanVien);
        nhanVien.setCreatedAt(Instant.now());
        nhanVien.setUpdatedAt(Instant.now());
        nhanVien.setDeleted(false);
        nhanVien = nhanVienRepository.save(nhanVien);
        return convertToDTO(nhanVien);
    }

    @Override
    public NhanVienDTO update(Integer id, NhanVienDTO nhanVienDTO) {
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .filter(nv -> !nv.getDeleted())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại với id: " + id));
        
        BeanUtils.copyProperties(nhanVienDTO, nhanVien, "id", "createdAt", "createdBy", "deleted");
        nhanVien.setUpdatedAt(Instant.now());
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

    private NhanVienDTO convertToDTO(NhanVien nhanVien) {
        NhanVienDTO nhanVienDTO = new NhanVienDTO();
        BeanUtils.copyProperties(nhanVien, nhanVienDTO);
        if (nhanVien.getIdChucVu() != null) {
            nhanVienDTO.setIdChucVu(nhanVien.getIdChucVu().getId());
        }
        return nhanVienDTO;
    }
} 