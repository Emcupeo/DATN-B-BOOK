package org.example.datnbbook.service;

import org.example.datnbbook.dto.TacGiaDTO;
import org.example.datnbbook.model.TacGia;
import org.example.datnbbook.repository.TacGiaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TacGiaService {
    @Autowired
    private TacGiaRepository tacGiaRepository;


    public List<TacGia> getAll() {
        return tacGiaRepository.findAllByDeletedFalseOrderByIdDesc();
    }

    public TacGia getById(Integer id) {
        return tacGiaRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Tác giả không tồn tại với id: " + id));
    }

    public TacGia create(TacGia sp) {
        if (sp.getMaTacGia() == null || sp.getMaTacGia().isEmpty()) {
            String nextMaTacGia = tacGiaRepository.getNextSequenceValue();
            sp.setMaTacGia(nextMaTacGia);
        }
        sp.setDeleted(false); // Đảm bảo Tác giả mới không bị đánh dấu xóa
        return tacGiaRepository.save(sp);
    }

    public TacGiaDTO update(Integer id, TacGiaDTO tacGiaDTO) {
        TacGia tacGia = tacGiaRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Tác giả không tồn tại với id: " + id));

        BeanUtils.copyProperties(tacGiaDTO, tacGia ,"maTacGia","id", "deleted");
        tacGia = tacGiaRepository.save(tacGia);
        return convertToDTO(tacGia);
    }

    public void delete(Integer id) {
        TacGia tacGia = tacGiaRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Tác giả không tồn tại với id: " + id));
        tacGia.setDeleted(true);
        tacGiaRepository.save(tacGia);
    }

    public List<TacGia> search(String keyword) {
        return tacGiaRepository.search(keyword);
    }

    private TacGiaDTO convertToDTO(TacGia tacGia) {
        TacGiaDTO tacGiaDTO = new TacGiaDTO();
        BeanUtils.copyProperties(tacGia, tacGiaDTO);
        return tacGiaDTO;
    }
}

