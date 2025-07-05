package org.example.datnbbook.service;

import org.example.datnbbook.dto.DanhMucDTO;
import org.example.datnbbook.model.DanhMuc;
import org.example.datnbbook.repository.DanhMucRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanhMucService {
    @Autowired
    private DanhMucRepository danhMucRepository;

    public List<DanhMuc> getAll() {
        return danhMucRepository.findAllByDeletedFalseOrderByIdDesc();
    }

    public DanhMuc getById(Integer id) {
        return danhMucRepository.findById(id)
                .filter(dm -> !dm.getDeleted())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại với id: " + id));
    }

    public DanhMuc create(DanhMuc danhMuc) {
        danhMuc.setDeleted(false);
        return danhMucRepository.save(danhMuc);
    }

    public DanhMucDTO update(Integer id, DanhMucDTO danhMucDTO) {
        DanhMuc danhMuc = danhMucRepository.findById(id)
                .filter(dm -> !dm.getDeleted())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại với id: " + id));

        BeanUtils.copyProperties(danhMucDTO, danhMuc, "maDanhMuc", "id", "deleted");
        danhMuc = danhMucRepository.save(danhMuc);
        return convertToDTO(danhMuc);
    }

    public void delete(Integer id) {
        DanhMuc danhMuc = danhMucRepository.findById(id)
                .filter(dm -> !dm.getDeleted())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại với id: " + id));
        danhMuc.setDeleted(true);
        danhMucRepository.save(danhMuc);
    }

    public List<DanhMuc> search(String keyword) {
        return danhMucRepository.search(keyword);
    }

    private DanhMucDTO convertToDTO(DanhMuc danhMuc) {
        DanhMucDTO danhMucDTO = new DanhMucDTO();
        BeanUtils.copyProperties(danhMuc, danhMucDTO);
        return danhMucDTO;
    }
} 