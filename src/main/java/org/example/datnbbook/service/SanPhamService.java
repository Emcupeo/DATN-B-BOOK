package org.example.datnbbook.service;

import org.example.datnbbook.dto.SanPhamDTO;
import org.example.datnbbook.model.SanPham;
import org.example.datnbbook.repository.SanPhamRepository;
import org.example.datnbbook.service.SanPhamService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SanPhamService{
    @Autowired
    private SanPhamRepository sanPhamRepository;

//    public SanPhamServiceImpl(SanPhamRepository sanPhamRepository) {
//        this.sanPhamRepository = sanPhamRepository;
//    }

    public List<SanPham> getAll() {
        return sanPhamRepository.findByDeletedFalse();
    }

    public SanPham getById(Integer id) {
        return sanPhamRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + id));
    }

    public SanPham create(SanPham sp) {
        if (sp.getMaSanPham() == null || sp.getMaSanPham().isEmpty()) {
            String nextMaSanPham = sanPhamRepository.getNextSequenceValue();
            sp.setMaSanPham(nextMaSanPham);
        }
        sp.setDeleted(false); // Đảm bảo sản phẩm mới không bị đánh dấu xóa
        return sanPhamRepository.save(sp);
    }

    public SanPhamDTO update(Integer id, SanPhamDTO sanPhamDTO) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + id));

        BeanUtils.copyProperties(sanPhamDTO, sanPham ,"maSanPham","id", "deleted");
        sanPham = sanPhamRepository.save(sanPham);
        return convertToDTO(sanPham);
    }

    public void delete(Integer id) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + id));
        sanPham.setDeleted(true);
        sanPhamRepository.save(sanPham);
    }

    public List<SanPham> search(String keyword) {
        return sanPhamRepository.search(keyword);
    }

    private SanPhamDTO convertToDTO(SanPham sanPham) {
        SanPhamDTO sanPhamDTO = new SanPhamDTO();
        BeanUtils.copyProperties(sanPham, sanPhamDTO);
        return sanPhamDTO;
    }
}