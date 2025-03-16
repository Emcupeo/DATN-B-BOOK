package org.example.datnbbook.service;

import org.example.datnbbook.dto.KhachHangDTO;

import java.util.List;

public interface KhachHangService {
    List<KhachHangDTO> getAll();
    KhachHangDTO getById(Long id);
    KhachHangDTO create(KhachHangDTO khachHangDTO);
    KhachHangDTO update(Long id, KhachHangDTO khachHangDTO);
    void delete(Long id);
    List<KhachHangDTO> search(String keyword);
}
