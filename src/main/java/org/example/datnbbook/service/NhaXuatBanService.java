package org.example.datnbbook.service;

import org.example.datnbbook.dto.NhaXuatBanDTO;
import org.example.datnbbook.model.NhaXuatBan;
import org.example.datnbbook.repository.NhaXuatBanRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NhaXuatBanService {
    @Autowired
    private NhaXuatBanRepository nhaXuatBanRepository;


    public List<NhaXuatBan> getAll() {
        return nhaXuatBanRepository.findByDeletedFalse();
    }

    public NhaXuatBan getById(Integer id) {
        return nhaXuatBanRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Nhà sản xuất không tồn tại với id: " + id));
    }

    public NhaXuatBan create(NhaXuatBan sp) {
        if (sp.getMaNhaXuatBan() == null || sp.getMaNhaXuatBan().isEmpty()) {
            String nextMaNhaXuatBan = nhaXuatBanRepository.getNextSequenceValue();
            sp.setMaNhaXuatBan(nextMaNhaXuatBan);
        }
        sp.setDeleted(false); // Đảm bảo Nhà sản xuất mới không bị đánh dấu xóa
        return nhaXuatBanRepository.save(sp);
    }

    public NhaXuatBanDTO update(Integer id, NhaXuatBanDTO nhaXuatBanDTO) {
        NhaXuatBan nhaXuatBan = nhaXuatBanRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Nhà sản xuất không tồn tại với id: " + id));

        BeanUtils.copyProperties(nhaXuatBanDTO, nhaXuatBan ,"maNhaXuatBan","id", "deleted");
        nhaXuatBan = nhaXuatBanRepository.save(nhaXuatBan);
        return convertToDTO(nhaXuatBan);
    }

    public void delete(Integer id) {
        NhaXuatBan nhaXuatBan = nhaXuatBanRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Nhà sản xuất không tồn tại với id: " + id));
        nhaXuatBan.setDeleted(true);
        nhaXuatBanRepository.save(nhaXuatBan);
    }

    public List<NhaXuatBan> search(String keyword) {
        return nhaXuatBanRepository.search(keyword);
    }

    private NhaXuatBanDTO convertToDTO(NhaXuatBan nhaXuatBan) {
        NhaXuatBanDTO nhaXuatBanDTO = new NhaXuatBanDTO();
        BeanUtils.copyProperties(nhaXuatBan, nhaXuatBanDTO);
        return nhaXuatBanDTO;
    }
}

