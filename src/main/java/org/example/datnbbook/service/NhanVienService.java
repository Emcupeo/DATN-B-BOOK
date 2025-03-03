package org.example.datnbbook.service;

import org.example.datnbbook.model.NhanVien;
import org.example.datnbbook.repository.NhanVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NhanVienService {
    @Autowired
    private NhanVienRepository nhanVienRepository;
    public List<NhanVien> getAlllNhanVien(){
        return nhanVienRepository.findAll();
    }

    public NhanVien addNhanVien(NhanVien nhanVien) {
        return nhanVienRepository.save(nhanVien);
    }

}
