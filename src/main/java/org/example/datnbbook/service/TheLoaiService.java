package org.example.datnbbook.service;

import org.example.datnbbook.dto.TheLoaiDTO;
import org.example.datnbbook.model.TheLoai;
import org.example.datnbbook.repository.TheLoaiRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheLoaiService {
    @Autowired
    private TheLoaiRepository theLoaiRepository;


    public List<TheLoai> getAll() {
        return theLoaiRepository.findByDeletedFalse();
    }

    public TheLoai getById(Integer id) {
        return theLoaiRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Thể loại không tồn tại với id: " + id));
    }

    public TheLoai create(TheLoai sp) {
        if (sp.getMaTheLoai() == null || sp.getMaTheLoai().isEmpty()) {
            String nextMaTheLoai = theLoaiRepository.getNextSequenceValue();
            sp.setMaTheLoai(nextMaTheLoai);
        }
        sp.setDeleted(false); // Đảm bảo Thể loại mới không bị đánh dấu xóa
        return theLoaiRepository.save(sp);
    }

    public TheLoaiDTO update(Integer id, TheLoaiDTO theLoaiDTO) {
        TheLoai theLoai = theLoaiRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Thể loại không tồn tại với id: " + id));

        BeanUtils.copyProperties(theLoaiDTO, theLoai ,"maTheLoai","id", "deleted");
        theLoai = theLoaiRepository.save(theLoai);
        return convertToDTO(theLoai);
    }

    public void delete(Integer id) {
        TheLoai theLoai = theLoaiRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Thể loại không tồn tại với id: " + id));
        theLoai.setDeleted(true);
        theLoaiRepository.save(theLoai);
    }

    public List<TheLoai> search(String keyword) {
        return theLoaiRepository.search(keyword);
    }

    private TheLoaiDTO convertToDTO(TheLoai theLoai) {
        TheLoaiDTO theLoaiDTO = new TheLoaiDTO();
        BeanUtils.copyProperties(theLoai, theLoaiDTO);
        return theLoaiDTO;
    }
}

