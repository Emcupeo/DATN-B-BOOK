package org.example.datnbbook.service;

import org.example.datnbbook.model.AnhSanPham;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.ChiTietSanPhamAnh;
import org.example.datnbbook.model.ChiTietSanPhamAnhId;
import org.example.datnbbook.repository.AnhSanPhamRepository;
import org.example.datnbbook.repository.ChiTietSanPhamAnhRepository;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnhSanPhamService {

    private static final Logger logger = LoggerFactory.getLogger(AnhSanPhamService.class);

    @Autowired
    private AnhSanPhamRepository anhSanPhamRepository;
    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;
    @Autowired
    private ChiTietSanPhamAnhRepository chiTietSanPhamAnhRepository;

    public AnhSanPham save(AnhSanPham anhSanPham) {
        return anhSanPhamRepository.save(anhSanPham);
    }

    public Optional<AnhSanPham> findById(Integer id) {
        return anhSanPhamRepository.findById(id);
    }

    public void linkToChiTietSanPham(Integer chiTietSanPhamId, Integer anhSanPhamId) {
        ChiTietSanPham chiTiet = chiTietSanPhamRepository.findById(chiTietSanPhamId)
                .orElseThrow(() -> new RuntimeException("ChiTietSanPham not found"));
        AnhSanPham anh = anhSanPhamRepository.findById(anhSanPhamId)
                .orElseThrow(() -> new RuntimeException("AnhSanPham not found"));

        ChiTietSanPhamAnhId id = new ChiTietSanPhamAnhId(chiTietSanPhamId, anhSanPhamId);
        if (!chiTietSanPhamAnhRepository.existsById(id)) {
            ChiTietSanPhamAnh chiTietSanPhamAnh = new ChiTietSanPhamAnh();
            chiTietSanPhamAnh.setId(id);
            chiTietSanPhamAnh.setChiTietSanPham(chiTiet);
            chiTietSanPhamAnh.setAnh(anh);
            chiTietSanPhamAnhRepository.save(chiTietSanPhamAnh);
            logger.info("Linked ChiTietSanPham ID: {} with AnhSanPham ID: {}", chiTietSanPhamId, anhSanPhamId);
        }
    }

    public List<AnhSanPham> getImagesByChiTietSanPhamId(Integer chiTietSanPhamId) {
        return chiTietSanPhamAnhRepository.findByChiTietSanPhamId(chiTietSanPhamId)
                .stream()
                .map(ChiTietSanPhamAnh::getAnh)
                .collect(Collectors.toList());
    }

    public AnhSanPham findByUrlAndIdLoaiBia(String url, Integer idLoaiBia) {
        List<AnhSanPham> results = anhSanPhamRepository.findByUrlAndIdLoaiBiaId(url, idLoaiBia);
        return results.isEmpty() ? null : results.get(0);
    }

    public AnhSanPham findByUrl(String url) {
        List<AnhSanPham> results = anhSanPhamRepository.findByUrl(url);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<AnhSanPham> findByIdLoaiBia(Integer idLoaiBia) {
        return anhSanPhamRepository.findByIdLoaiBiaId(idLoaiBia);
    }
}