package org.example.datnbbook.service;

import jakarta.transaction.Transactional;
import org.example.datnbbook.model.SanPham;
import org.example.datnbbook.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;
//
//    public SanPhamService(SanPhamRepository sanPhamRepository) {
//        this.sanPhamRepository = sanPhamRepository;
//    }


    public List<SanPham> getAllSanPham(){
        return sanPhamRepository.findAll();
    }

    @Transactional
    public SanPham addSanPham(SanPham sanPham){
        return sanPhamRepository.save(sanPham);
    }

}
