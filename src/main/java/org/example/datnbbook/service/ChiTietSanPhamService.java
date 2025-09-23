package org.example.datnbbook.service;

import lombok.RequiredArgsConstructor;
import org.example.datnbbook.dto.ChiTietSanPhamDTO;
import org.example.datnbbook.dto.SanPhamRequest;
import org.example.datnbbook.model.AnhSanPham;
import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.ChiTietSanPhamAnh;
import org.example.datnbbook.model.ChiTietSanPhamAnhId;
import org.example.datnbbook.model.LoaiBia;
import org.example.datnbbook.model.NgonNgu;
import org.example.datnbbook.model.NguoiDich;
import org.example.datnbbook.model.NhaXuatBan;
import org.example.datnbbook.model.SanPham;
import org.example.datnbbook.model.TacGia;
import org.example.datnbbook.model.TheLoai;
import org.example.datnbbook.repository.ChatLieuRepository;
import org.example.datnbbook.repository.BoSachChiTietRepository;
import org.example.datnbbook.repository.ChiTietSanPhamAnhRepository;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.example.datnbbook.repository.LoaiBiaRepository;
import org.example.datnbbook.repository.NgonNguRepository;
import org.example.datnbbook.repository.NguoiDichRepository;
import org.example.datnbbook.repository.NhaXuatBanRepository;
import org.example.datnbbook.repository.SanPhamRepository;
import org.example.datnbbook.repository.TacGiaRepository;
import org.example.datnbbook.repository.TheLoaiRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
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
    private final AnhSanPhamService anhSanPhamService;
    private final ChiTietSanPhamAnhRepository chiTietSanPhamAnhRepository;
    private final BoSachChiTietRepository boSachChiTietRepository;

    private ModelMapper modelMapper;

    public List<ChiTietSanPham> getAll() {
        return repository.findAllByDeletedFalseOrderByIdDesc();
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
        // Validate: chặn xóa nếu CTSP đang thuộc bộ sách active
        if (boSachChiTietRepository.existsActiveByChiTietSanPhamId(id)) {
            var names = boSachChiTietRepository.findActiveBoSachNamesByChiTietSanPhamId(id);
            String name = (names != null && !names.isEmpty()) ? names.get(0) : "";
            String suffix = name.isEmpty() ? "" : " (" + name + ")";
            throw new IllegalArgumentException("Sản phẩm này đang nằm trong bộ sách" + suffix + ". Vui lòng gỡ khỏi bộ sách trước khi xóa.");
        }
        entity.setDeleted(true);
        entity.setUpdatedAt(Instant.now());
        repository.save(entity);
    }

    public List<ChiTietSanPham> getBySanPhamId(Integer idSanPham) {
        List<ChiTietSanPham> chiTietSanPhams = repository.findByIdSanPham_Id(idSanPham)
                .stream()
                .filter(ctsp -> !ctsp.getDeleted()) // Lọc deleted = false
                .sorted(Comparator.comparing(ChiTietSanPham::getId, Comparator.reverseOrder())) // Sắp xếp theo id giảm dần
                .collect(Collectors.toList());
        
        return chiTietSanPhams;
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

        // Lưu để có ID trước khi tạo ISBN
        ChiTietSanPham savedChiTietSanPham = repository.save(chiTietSanPham);

        // Tạo ISBN nếu có nhà xuất bản
        if (savedChiTietSanPham.getIdNhaXuatBan() != null) {
            String isbn = generateISBN(
                savedChiTietSanPham.getIdNhaXuatBan().getId().longValue(),
                savedChiTietSanPham.getId().longValue()
            );
            savedChiTietSanPham.setIsbn(isbn);
            savedChiTietSanPham = repository.save(savedChiTietSanPham);
        }

        return savedChiTietSanPham;
    }

    public List<ChiTietSanPham> search(String keyword) {
        return repository.search(keyword);
    }

    public ChiTietSanPham createWithImages(ChiTietSanPhamDTO dto, List<MultipartFile> images) {
        // Tạo chi tiết sản phẩm
        ChiTietSanPham chiTietSanPham = new ChiTietSanPham();
        // ... set các trường ...
        chiTietSanPham = repository.save(chiTietSanPham);
        // Upload ảnh
        if (images != null && !images.isEmpty()) {
            anhSanPhamService.uploadImages(images, chiTietSanPham.getId());
        }
        return chiTietSanPham;
    }

    public void linkImageToChiTietSanPham(Integer chiTietSanPhamId, Integer anhId) {
        ChiTietSanPhamAnhId id = new ChiTietSanPhamAnhId();
        id.setChiTietSanPhamId(chiTietSanPhamId);
        id.setAnhId(anhId);
        ChiTietSanPhamAnh entity = new ChiTietSanPhamAnh();
        entity.setId(id);
        // Nạp entity đầy đủ để mapping
        ChiTietSanPham chiTietSanPham = repository.findById(chiTietSanPhamId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với id: " + chiTietSanPhamId));
        entity.setChiTietSanPham(chiTietSanPham);
        AnhSanPham anh = anhSanPhamService.findById(anhId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ảnh với id: " + anhId));
        entity.setAnh(anh);
        chiTietSanPhamAnhRepository.save(entity);
    }

    private String generateISBN(Long idNhaXuatBan, Long idChiTietSanPham) {
        // Tạo chuỗi 12 số đầu: 978604 + idNhaXuatBan (pad 3 số) + idChiTietSanPham (pad 4 số)
        String prefix = "978604";
        String nhaXB = String.format("%03d", idNhaXuatBan);
        String chiTiet = String.format("%04d", idChiTietSanPham);
        String isbn12 = prefix + nhaXB + chiTiet;
        // Tính số kiểm tra
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn12.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        return String.format("978-604-%s-%s-%d", nhaXB, chiTiet, checkDigit);
    }

//    private ChiTietSanPhamDTO toDTO(ChiTietSanPham entity) {
//        return modelMapper.map(entity, ChiTietSanPhamDTO.class);
//    }
//
//    private ChiTietSanPham toEntity(ChiTietSanPhamDTO dto) {
//        return modelMapper.map(dto, ChiTietSanPham.class);
//    }

    public ChiTietSanPham updateStock(Integer id, Integer soLuongTon) {
        ChiTietSanPham existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm với ID: " + id));

        existing.setSoLuongTon(soLuongTon);
        existing.setUpdatedAt(Instant.now());

        return repository.save(existing);
    }
}
