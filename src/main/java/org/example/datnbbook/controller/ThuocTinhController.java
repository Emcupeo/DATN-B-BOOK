package org.example.datnbbook.controller;

import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.model.LoaiBia;
import org.example.datnbbook.model.NgonNgu;
import org.example.datnbbook.model.NguoiDich;
import org.example.datnbbook.model.NhaXuatBan;
import org.example.datnbbook.model.TacGia;
import org.example.datnbbook.model.TheLoai;
import org.example.datnbbook.service.ThuocTinhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class ThuocTinhController {
    @Autowired
    private ThuocTinhService thuocTinhService;

    @GetMapping("/tac-gia")
    public List<TacGia> getAllTacGia() {
        return thuocTinhService.getAllTacGia();
    }

    @GetMapping("/loai-bia")
    public List<LoaiBia> getAllLoaiBia() {
        return thuocTinhService.getAllLoaiBia();
    }

    @GetMapping("/chat-lieu")
    public List<ChatLieu> getAlChatLieu() {
        return thuocTinhService.getAllChatLieu();
    }

    @GetMapping("/nha-xuat-ban")
    public List<NhaXuatBan> getAllNhaXuatBan() {
        return thuocTinhService.getAllNhaXuatBan();
    }

    @GetMapping("/nguoi-dich")
    public List<NguoiDich> getAllNguoiDich() {
        return thuocTinhService.getAllNguoiDich();
    }

    @GetMapping("/ngon-ngu")
    public List<NgonNgu> getAllNgonNgu() {
        return thuocTinhService.getAllNgonNgu();
    }

    @GetMapping("/the-loai")
    public List<TheLoai> getAllTheLoai() {
        return thuocTinhService.getAllTheLoai();
    }
}
