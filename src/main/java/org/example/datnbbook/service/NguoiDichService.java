package org.example.datnbbook.service;

import org.example.datnbbook.dto.NguoiDichDTO;
import org.example.datnbbook.model.NguoiDich;
import org.example.datnbbook.repository.NguoiDichRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NguoiDichService {
    @Autowired
    private NguoiDichRepository nguoiDichRepository;


    public List<NguoiDich> getAll() {
        return nguoiDichRepository.findAllByDeletedFalseOrderByIdDesc();
    }

    public NguoiDich getById(Integer id) {
        return nguoiDichRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Người dịch không tồn tại với id: " + id));
    }

    public NguoiDich create(NguoiDich sp) {
        if (sp.getMaNguoiDich() == null || sp.getMaNguoiDich().isEmpty()) {
            String nextMaNguoiDich = nguoiDichRepository.getNextSequenceValue();
            sp.setMaNguoiDich(nextMaNguoiDich);
        }
        sp.setDeleted(false); // Đảm bảo Người dịch mới không bị đánh dấu xóa
        return nguoiDichRepository.save(sp);
    }

    public NguoiDichDTO update(Integer id, NguoiDichDTO nguoiDichDTO) {
        NguoiDich nguoiDich = nguoiDichRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Người dịch không tồn tại với id: " + id));

        BeanUtils.copyProperties(nguoiDichDTO, nguoiDich ,"maNguoiDich","id", "deleted");
        nguoiDich = nguoiDichRepository.save(nguoiDich);
        return convertToDTO(nguoiDich);
    }

    public void delete(Integer id) {
        NguoiDich nguoiDich = nguoiDichRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Người dịch không tồn tại với id: " + id));
        nguoiDich.setDeleted(true);
        nguoiDichRepository.save(nguoiDich);
    }

    public List<NguoiDich> search(String keyword) {
        return nguoiDichRepository.search(keyword);
    }

    private NguoiDichDTO convertToDTO(NguoiDich nguoiDich) {
        NguoiDichDTO nguoiDichDTO = new NguoiDichDTO();
        BeanUtils.copyProperties(nguoiDich, nguoiDichDTO);
        return nguoiDichDTO;
    }
}

