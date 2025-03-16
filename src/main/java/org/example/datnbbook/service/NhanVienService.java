package org.example.datnbbook.service;

import org.example.datnbbook.dto.NhanVienDTO;

import java.util.List;

public interface NhanVienService {
    List<NhanVienDTO> getAll();
    NhanVienDTO getById(Integer id);
    NhanVienDTO create(NhanVienDTO nhanVienDTO);
    NhanVienDTO update(Integer id, NhanVienDTO nhanVienDTO);
    void delete(Integer id);
    List<NhanVienDTO> search(String keyword);
}