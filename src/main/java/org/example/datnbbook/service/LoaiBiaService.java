package org.example.datnbbook.service;

import org.example.datnbbook.dto.ChatLieuDTO;
import org.example.datnbbook.dto.LoaiBiaDTO;
import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.model.LoaiBia;
import org.example.datnbbook.repository.ChatLieuRepository;
import org.example.datnbbook.repository.LoaiBiaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoaiBiaService {
    @Autowired
    private LoaiBiaRepository loaiBiaRepository;


    public List<LoaiBia> getAll() {
        return loaiBiaRepository.findByDeletedFalse();
    }

    public LoaiBia getById(Integer id) {
        return loaiBiaRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Loại bìa không tồn tại với id: " + id));
    }

    public LoaiBia create(LoaiBia sp) {
        if (sp.getMaLoaiBia() == null || sp.getMaLoaiBia().isEmpty()) {
            String nextMaLoaiBia = loaiBiaRepository.getNextSequenceValue();
            sp.setMaLoaiBia(nextMaLoaiBia);
        }
        sp.setDeleted(false); // Đảm bảo Loại bìa mới không bị đánh dấu xóa
        return loaiBiaRepository.save(sp);
    }

    public LoaiBiaDTO update(Integer id, LoaiBiaDTO loaiBiaDTO) {
        LoaiBia loaiBia = loaiBiaRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Loại bìa không tồn tại với id: " + id));

        BeanUtils.copyProperties(loaiBiaDTO, loaiBia ,"maLoaiBia","id", "deleted");
        loaiBia = loaiBiaRepository.save(loaiBia);
        return convertToDTO(loaiBia);
    }

    public void delete(Integer id) {
        LoaiBia loaiBia = loaiBiaRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Loại bìa không tồn tại với id: " + id));
        loaiBia.setDeleted(true);
        loaiBiaRepository.save(loaiBia);
    }

    public List<LoaiBia> search(String keyword) {
        return loaiBiaRepository.search(keyword);
    }

    private LoaiBiaDTO convertToDTO(LoaiBia loaiBia) {
        LoaiBiaDTO loaiBiaDTO = new LoaiBiaDTO();
        BeanUtils.copyProperties(loaiBia, loaiBiaDTO);
        return loaiBiaDTO;
    }
}

