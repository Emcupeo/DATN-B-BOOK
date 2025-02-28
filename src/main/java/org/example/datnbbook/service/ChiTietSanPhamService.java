package org.example.datnbbook.service;

import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChiTietSanPhamService {
    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;


    public List<ChiTietSanPham> getAllChiTietSanPham() {
        return chiTietSanPhamRepository.findAll();
    }

}
