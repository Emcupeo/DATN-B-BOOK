package org.example.datnbbook.service;

import org.example.datnbbook.model.HoaDonChiTiet;
import org.example.datnbbook.repository.HoaDonChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    public List<HoaDonChiTiet> getAllHoaDonChiTiet(){
        return hoaDonChiTietRepository.findAll();
    }

//    public Integer getTotalProductQuantity(Integer hoaDonId) {
//        return hoaDonChiTietRepository.getTotalProductQuantityByHoaDonId(hoaDonId);
//    }
}
