package org.example.datnbbook.dto;

import org.example.datnbbook.model.AnhSanPham;
import org.example.datnbbook.model.ChiTietSanPham;

import java.util.List;
import java.util.stream.Collectors;

public class AnhSanPhamDTO {
    private Integer id;
    private String url;
    private Integer idLoaiBia;
    private List<ChiTietSanPham> chiTietSanPhams;

    public AnhSanPhamDTO(AnhSanPham anh) {
        this.id = anh.getId();
        this.url = anh.getUrl();
        this.idLoaiBia = anh.getIdLoaiBia() != null ? anh.getIdLoaiBia().getId() : null;
        this.chiTietSanPhams = anh.getChiTietSanPhams() != null
                ? anh.getChiTietSanPhams().stream().map(ct -> new ChiTietSanPham(ct.getId())).collect(Collectors.toList())
                : null;
    }
}