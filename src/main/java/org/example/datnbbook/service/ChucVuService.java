package org.example.datnbbook.service;

import org.example.datnbbook.model.ChucVu;
import org.example.datnbbook.model.NhanVien;
import org.example.datnbbook.repository.ChucVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChucVuService {
    @Autowired
    private ChucVuRepository chucVuRepository;

    public List<ChucVu> getAllChucVu() {
        return chucVuRepository.findAll();
    }
}
