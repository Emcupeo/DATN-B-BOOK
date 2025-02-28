package org.example.datnbbook.service;

import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.model.LoaiBia;
import org.example.datnbbook.model.NgonNgu;
import org.example.datnbbook.model.NguoiDich;
import org.example.datnbbook.model.NhaXuatBan;
import org.example.datnbbook.model.TacGia;
import org.example.datnbbook.model.TheLoai;
import org.example.datnbbook.repository.ChatLieuRepository;
import org.example.datnbbook.repository.LoaiBiaRepository;
import org.example.datnbbook.repository.NgonNguRepository;
import org.example.datnbbook.repository.NguoiDichRepository;
import org.example.datnbbook.repository.NhaXuatBanRepository;
import org.example.datnbbook.repository.TacGiaRepository;
import org.example.datnbbook.repository.TheLoaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThuocTinhService {

    @Autowired
    private TacGiaRepository tacGiaRepository;

    @Autowired
    private LoaiBiaRepository loaiBiaRepository;

    @Autowired
    private NguoiDichRepository nguoiDichRepository;

    @Autowired
    private ChatLieuRepository chatLieuRepository;

    @Autowired
    private NhaXuatBanRepository nhaXuatBanRepository;

    @Autowired
    private TheLoaiRepository theLoaiRepository;

    @Autowired
    private NgonNguRepository ngonNguRepository;

    public List<TacGia> getAllTacGia() {
        return tacGiaRepository.findAll();
    }


    public List<LoaiBia> getAllLoaiBia() {
        return loaiBiaRepository.findAll();
    }

    public List<NguoiDich> getAllNguoiDich() {
        return nguoiDichRepository.findAll();
    }

    public List<ChatLieu> getAllChatLieu() {
        return chatLieuRepository.findAll();
    }

    public List<NhaXuatBan> getAllNhaXuatBan() {
        return nhaXuatBanRepository.findAll();
    }

    public List<TheLoai> getAllTheLoai() {
        return theLoaiRepository.findAll();
    }
    public List<NgonNgu> getAllNgonNgu() {
        return ngonNguRepository.findAll();
    }
}
