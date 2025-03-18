package org.example.datnbbook.service;

import org.example.datnbbook.dto.NgonNguDTO;
import org.example.datnbbook.model.NgonNgu;
import org.example.datnbbook.repository.NgonNguRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NgonNguService {
    @Autowired
    private NgonNguRepository ngonNguRepository;


    public List<NgonNgu> getAll() {
        return ngonNguRepository.findAllByDeletedFalseOrderByIdDesc();
    }

    public NgonNgu getById(Integer id) {
        return ngonNguRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Ngôn ngữ không tồn tại với id: " + id));
    }

    public NgonNgu create(NgonNgu sp) {
        if (sp.getMaNgonNgu() == null || sp.getMaNgonNgu().isEmpty()) {
            String nextMaNgonNgu = ngonNguRepository.getNextSequenceValue();
            sp.setMaNgonNgu(nextMaNgonNgu);
        }
        sp.setDeleted(false); // Đảm bảo Ngôn ngữ mới không bị đánh dấu xóa
        return ngonNguRepository.save(sp);
    }

    public NgonNguDTO update(Integer id, NgonNguDTO ngonNguDTO) {
        NgonNgu ngonNgu = ngonNguRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Ngôn ngữ không tồn tại với id: " + id));

        BeanUtils.copyProperties(ngonNguDTO, ngonNgu ,"maNgonNgu","id", "deleted");
        ngonNgu = ngonNguRepository.save(ngonNgu);
        return convertToDTO(ngonNgu);
    }

    public void delete(Integer id) {
        NgonNgu ngonNgu = ngonNguRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Ngôn ngữ không tồn tại với id: " + id));
        ngonNgu.setDeleted(true);
        ngonNguRepository.save(ngonNgu);
    }

    public List<NgonNgu> search(String keyword) {
        return ngonNguRepository.search(keyword);
    }

    private NgonNguDTO convertToDTO(NgonNgu ngonNgu) {
        NgonNguDTO ngonNguDTO = new NgonNguDTO();
        BeanUtils.copyProperties(ngonNgu, ngonNguDTO);
        return ngonNguDTO;
    }
}
