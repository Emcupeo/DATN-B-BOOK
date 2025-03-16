package org.example.datnbbook.service;

import org.example.datnbbook.model.PhuongThucThanhToan;
import org.example.datnbbook.repository.PhuongThucThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhuongThucThanhToanService {
    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    public PhuongThucThanhToan getOne(int id) {
        return phuongThucThanhToanRepository.findById(id).orElse(null);
    }
}
