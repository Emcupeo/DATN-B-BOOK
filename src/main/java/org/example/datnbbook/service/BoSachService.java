package org.example.datnbbook.service;

import org.example.datnbbook.model.BoSach;
import org.example.datnbbook.repository.BoSachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BoSachService {
    @Autowired
    private BoSachRepository boSachRepository;

    public List<BoSach> getAll() {
        return boSachRepository.findAllByDeletedFalseOrderByIdDesc();
    }

    public BoSach getById(Integer id) {
        return boSachRepository.findById(id)
                .filter(bs -> !bs.getDeleted())
                .orElseThrow(() -> new RuntimeException("Bộ sách không tồn tại với id: " + id));
    }

    public BoSach create(BoSach boSach) {
        boSach.setMaBoSach(boSachRepository.getNextSequenceValue());
        boSach.setDeleted(false);
        boSach.setCreatedAt(Instant.from(LocalDateTime.now()));
        boSach.setUpdatedAt(Instant.from(LocalDateTime.now()));
        // Giả định createdBy và updatedBy lấy từ user hiện tại (token)
        boSach.setCreatedBy("admin"); // Mock
        boSach.setUpdatedBy("admin"); // Mock
        return boSachRepository.save(boSach);
    }

    public void delete(Integer id) {
        BoSach boSach = boSachRepository.findById(id)
                .filter(bs -> !bs.getDeleted())
                .orElseThrow(() -> new RuntimeException("Bộ sách không tồn tại với id: " + id));
        boSach.setDeleted(true);
        boSachRepository.save(boSach);
    }

    public List<BoSach> search(String keyword) {
        return boSachRepository.search(keyword);
    }
}