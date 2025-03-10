package org.example.datnbbook.service;

import lombok.RequiredArgsConstructor;
import org.example.datnbbook.dto.ChiTietSanPhamDTO;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChiTietSanPhamService {


    private final ChiTietSanPhamRepository repository;
    private ModelMapper modelMapper;

    public List<ChiTietSanPham> getAll() {
        return repository.findByDeletedFalse();
    }

    public ChiTietSanPham getById(Integer id) {
        ChiTietSanPham entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với id: " + id));
        return entity;
    }

    public ChiTietSanPham getByMa(String ma) {
        ChiTietSanPham entity = repository.findByMaChiTietSanPham(ma)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với mã: " + ma));
        return entity;
    }

    public ChiTietSanPham create(ChiTietSanPham ctsp) {
        if (repository.existsByMaChiTietSanPham(ctsp.getMaChiTietSanPham())) {
            throw new RuntimeException("Mã chi tiết sản phẩm đã tồn tại: " + ctsp.getMaChiTietSanPham());
        }

        ChiTietSanPham entity = ctsp;
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setDeleted(false);

        return repository.save(entity);
    }


    public ChiTietSanPham update(Integer id, ChiTietSanPham ctsp) {
        ChiTietSanPham existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với id: " + id));

        // Cập nhật thông tin
        modelMapper.map(ctsp, existingEntity);
        existingEntity.setId(id); // Đảm bảo không thay đổi id
        existingEntity.setUpdatedAt(Instant.now());

        return repository.save(existingEntity);
    }

    public void delete(Integer id) {
        ChiTietSanPham entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với id: " + id));
        entity.setDeleted(true);
        entity.setUpdatedAt(Instant.now());
        repository.save(entity);
    }

    public List<ChiTietSanPham> search(String keyword) {
        return repository.search(keyword);
    }

//    private ChiTietSanPhamDTO toDTO(ChiTietSanPham entity) {
//        return modelMapper.map(entity, ChiTietSanPhamDTO.class);
//    }
//
//    private ChiTietSanPham toEntity(ChiTietSanPhamDTO dto) {
//        return modelMapper.map(dto, ChiTietSanPham.class);
//    }
}
