package org.example.datnbbook.service;

import lombok.RequiredArgsConstructor;
import org.example.datnbbook.dto.ChiTietSanPhamDTO;
import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.LoaiBia;
import org.example.datnbbook.model.NgonNgu;
import org.example.datnbbook.model.NguoiDich;
import org.example.datnbbook.model.NhaXuatBan;
import org.example.datnbbook.model.SanPham;
import org.example.datnbbook.model.TacGia;
import org.example.datnbbook.model.TheLoai;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.example.datnbbook.repository.SanPhamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChiTietSanPhamService {


    private final ChiTietSanPhamRepository repository;
    private final SanPhamRepository sanPhamRepository;
    private final LoaiBiaService loaiBiaService;
    private final ChatLieuService chatLieuService;
    private final SanPhamService sanPhamService;
    private final TacGiaService tacGiaService;
    private final NhaXuatBanService nhaXuatBanService;
    private final NguoiDichService nguoiDichService;
    private final NgonNguService ngonNguService;
    private final TheLoaiService theLoaiService;
    private ModelMapper modelMapper;

    public List<ChiTietSanPham> getAll() {
        return repository.findByDeletedFalse();
    }

    public ChiTietSanPham getById(Integer id) {
        ChiTietSanPham entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với id: " + id));
        return entity;
    }

    public ChiTietSanPham getByMa(String ma) {
        ChiTietSanPham entity = repository.findByMaChiTietSanPham(ma)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với mã: " + ma));
        return entity;
    }


    public ChiTietSanPham createSanPhamAndChiTiet(String tenSanPham, String moTaSanPham,
                                                  Integer idLoaiBia, Integer idTacGia, Integer idNhaXuatBan,
                                                  Integer idChatLieu, Integer idNguoiDich, Integer idTheLoai,
                                                  Integer idNgonNgu, BigDecimal gia, Integer soLuongTon,
                                                  BigDecimal trongLuong, BigDecimal kichThuoc, String moTaChiTiet) {
        // Tạo và lưu SanPham
        SanPham sanPham = new SanPham();
        sanPham.setMaSanPham(sanPhamRepository.getNextSequenceValue());
        sanPham.setTenSanPham(tenSanPham);
        sanPham.setMoTa(moTaSanPham);
        sanPham.setDeleted(false);
        SanPham savedSanPham = sanPhamRepository.save(sanPham);

        // Tạo và lưu ChiTietSanPham
        ChiTietSanPham chiTietSanPham = new ChiTietSanPham();
        chiTietSanPham.setMaChiTietSanPham(repository.getNextCTSPSequenceValue());
        chiTietSanPham.setTenChiTietSanPham(tenSanPham); // Giống tenSanPham
        chiTietSanPham.setIdSanPham(savedSanPham);

        // Gán các trường @ManyToOne
        if (idLoaiBia != null) {
            LoaiBia loaiBia = new LoaiBia();
            loaiBia.setId(idLoaiBia);
            chiTietSanPham.setIdLoaiBia(loaiBia);
        }
        if (idTacGia != null) {
            TacGia tacGia = new TacGia();
            tacGia.setId(idTacGia);
            chiTietSanPham.setIdTacGia(tacGia);
        }
        if (idNhaXuatBan != null) {
            NhaXuatBan nhaXuatBan = new NhaXuatBan();
            nhaXuatBan.setId(idNhaXuatBan);
            chiTietSanPham.setIdNhaXuatBan(nhaXuatBan);
        }
        if (idChatLieu != null) {
            ChatLieu chatLieu = new ChatLieu();
            chatLieu.setId(idChatLieu);
            chiTietSanPham.setIdChatLieu(chatLieu);
        }
        if (idNguoiDich != null) {
            NguoiDich nguoiDich = new NguoiDich();
            nguoiDich.setId(idNguoiDich);
            chiTietSanPham.setIdNguoiDich(nguoiDich);
        }
        if (idTheLoai != null) {
            TheLoai theLoai = new TheLoai();
            theLoai.setId(idTheLoai);
            chiTietSanPham.setIdTheLoai(theLoai);
        }
        if (idNgonNgu != null) {
            NgonNgu ngonNgu = new NgonNgu();
            ngonNgu.setId(idNgonNgu);
            chiTietSanPham.setIdNgonNgu(ngonNgu);
        }

        chiTietSanPham.setGia(gia != null ? gia : BigDecimal.ZERO);
        chiTietSanPham.setSoLuongTon(soLuongTon != null ? soLuongTon : 0);
        chiTietSanPham.setTrongLuong(trongLuong != null ? trongLuong : BigDecimal.ZERO); // Thêm trongLuong
        chiTietSanPham.setKichThuoc(kichThuoc != null ? kichThuoc : BigDecimal.ZERO); // Thêm kichThuoc
        chiTietSanPham.setMoTa(moTaChiTiet);
        chiTietSanPham.setDeleted(false);
        chiTietSanPham.setTrangThai(true);
        chiTietSanPham.setCreatedAt(Instant.now());
        chiTietSanPham.setUpdatedAt(Instant.now());

        return repository.save(chiTietSanPham);
    }


    public ChiTietSanPham update(Integer id, ChiTietSanPham updatedData) {
        ChiTietSanPham existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với ID: " + id));

        // Cập nhật các trường cơ bản
        existing.setTenChiTietSanPham(updatedData.getTenChiTietSanPham());
        existing.setGia(updatedData.getGia());
        existing.setSoLuongTon(updatedData.getSoLuongTon());
        existing.setTrongLuong(updatedData.getTrongLuong());
        existing.setKichThuoc(updatedData.getKichThuoc());
        existing.setMoTa(updatedData.getMoTa());
        existing.setGhiChu(updatedData.getGhiChu());
        existing.setTrangThai(updatedData.getTrangThai());
        existing.setUpdatedAt(Instant.now());

        // Cập nhật các trường @ManyToOne
        if (updatedData.getIdSanPham() != null) {
            SanPham sanPham = sanPhamService.getById(updatedData.getIdSanPham().getId());
            existing.setIdSanPham(sanPham);
        }

        if (updatedData.getIdLoaiBia() != null) {
            LoaiBia loaiBia = loaiBiaService.getById(updatedData.getIdLoaiBia().getId());
            existing.setIdLoaiBia(loaiBia);
        } else {
            existing.setIdLoaiBia(null);
        }

        if (updatedData.getIdTacGia() != null) {
            TacGia tacGia = tacGiaService
                    .getById(updatedData.getIdTacGia().getId());
            existing.setIdTacGia(tacGia);
        } else {
            existing.setIdTacGia(null);
        }

        if (updatedData.getIdNhaXuatBan() != null) {
            NhaXuatBan nhaXuatBan = nhaXuatBanService.getById(updatedData.getIdNhaXuatBan().getId());
            existing.setIdNhaXuatBan(nhaXuatBan);
        } else {
            existing.setIdNhaXuatBan(null);
        }

        if (updatedData.getIdChatLieu() != null) {
            ChatLieu chatLieu = chatLieuService.getById(updatedData.getIdChatLieu().getId());
            existing.setIdChatLieu(chatLieu);
        } else {
            existing.setIdChatLieu(null);
        }

        if (updatedData.getIdNguoiDich() != null) {
            NguoiDich nguoiDich = nguoiDichService.getById(updatedData.getIdNguoiDich().getId());
            existing.setIdNguoiDich(nguoiDich);
        } else {
            existing.setIdNguoiDich(null);
        }

        if (updatedData.getIdTheLoai() != null) {
            TheLoai theLoai = theLoaiService.getById(updatedData.getIdTheLoai().getId());
            existing.setIdTheLoai(theLoai);
        } else {
            existing.setIdTheLoai(null);
        }

        if (updatedData.getIdNgonNgu() != null) {
            NgonNgu ngonNgu = ngonNguService.getById(updatedData.getIdNgonNgu().getId());
            existing.setIdNgonNgu(ngonNgu);
        } else {
            existing.setIdNgonNgu(null);
        }

        return repository.save(existing);
    }

    public void delete(Integer id) {
        ChiTietSanPham entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với id: " + id));
        entity.setDeleted(true);
        entity.setUpdatedAt(Instant.now());
        repository.save(entity);
    }

    public List<ChiTietSanPham> getBySanPhamId(Integer idSanPham) {
        return repository.findByIdSanPham_Id(idSanPham)
                .stream()
                .filter(ctsp -> !ctsp.getDeleted())
                .collect(Collectors.toList());
    }

    public ChiTietSanPham create(ChiTietSanPham newData) {
        ChiTietSanPham chiTietSanPham = new ChiTietSanPham();

        if(newData.getMaChiTietSanPham() == null || newData.getMaChiTietSanPham().isEmpty()) {
            chiTietSanPham.setMaChiTietSanPham(repository.getNextCTSPSequenceValue());
        }
        chiTietSanPham.setTenChiTietSanPham(newData.getTenChiTietSanPham());
        chiTietSanPham.setGia(newData.getGia());
        chiTietSanPham.setSoLuongTon(newData.getSoLuongTon());
        chiTietSanPham.setTrongLuong(newData.getTrongLuong());
        chiTietSanPham.setKichThuoc(newData.getKichThuoc());
        chiTietSanPham.setMoTa(newData.getMoTa());
        chiTietSanPham.setGhiChu(newData.getGhiChu());
        chiTietSanPham.setTrangThai(newData.getTrangThai());
        chiTietSanPham.setCreatedAt(Instant.now());
        chiTietSanPham.setUpdatedAt(Instant.now());
        chiTietSanPham.setDeleted(false);

        // Gán các trường @ManyToOne
        if (newData.getIdSanPham() != null) {
            SanPham sanPham = sanPhamService.getById(newData.getIdSanPham().getId());
            chiTietSanPham.setIdSanPham(sanPham);
        }

        if (newData.getIdLoaiBia() != null) {
            LoaiBia loaiBia = loaiBiaService.getById(newData.getIdLoaiBia().getId());
            chiTietSanPham.setIdLoaiBia(loaiBia);
        }

        if (newData.getIdTacGia() != null) {
            TacGia tacGia = tacGiaService.getById(newData.getIdTacGia().getId());
            chiTietSanPham.setIdTacGia(tacGia);
        }

        if (newData.getIdNhaXuatBan() != null) {
            NhaXuatBan nhaXuatBan = nhaXuatBanService.getById(newData.getIdNhaXuatBan().getId());
            chiTietSanPham.setIdNhaXuatBan(nhaXuatBan);
        }

        if (newData.getIdChatLieu() != null) {
            ChatLieu chatLieu = chatLieuService.getById(newData.getIdChatLieu().getId());
            chiTietSanPham.setIdChatLieu(chatLieu);
        }

        if (newData.getIdNguoiDich() != null) {
            NguoiDich nguoiDich = nguoiDichService.getById(newData.getIdNguoiDich().getId());
            chiTietSanPham.setIdNguoiDich(nguoiDich);
        }

        if (newData.getIdTheLoai() != null) {
            TheLoai theLoai = theLoaiService.getById(newData.getIdTheLoai().getId());
            chiTietSanPham.setIdTheLoai(theLoai);
        }

        if (newData.getIdNgonNgu() != null) {
            NgonNgu ngonNgu = ngonNguService.getById(newData.getIdNgonNgu().getId());
            chiTietSanPham.setIdNgonNgu(ngonNgu);
        }

        return repository.save(chiTietSanPham);
    }

    public List<ChiTietSanPham> search(String keyword) {
        return repository.search(keyword);
    }

//    private ChiTietSanPhamDTO toDTO(ChiTietSanPham entity) {
//        return modelMapper.map(entity, ChiTietSanPhamDTO.class);
//    }
//
//    private ChiTietSanPham toEntity(ChiTietSanPhamDTO dto) {
//        return modelMapper.map(dto, ChiTietSanPham.class);
//    }
}
