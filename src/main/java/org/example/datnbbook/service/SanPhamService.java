package org.example.datnbbook.service;

import org.example.datnbbook.dto.SanPhamDTO;
import org.example.datnbbook.dto.SanPhamRequest;
import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.LoaiBia;
import org.example.datnbbook.model.NgonNgu;
import org.example.datnbbook.model.NguoiDich;
import org.example.datnbbook.model.NhaXuatBan;
import org.example.datnbbook.model.SanPham;
import org.example.datnbbook.model.TacGia;
import org.example.datnbbook.model.TheLoai;
import org.example.datnbbook.repository.ChatLieuRepository;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.example.datnbbook.repository.LoaiBiaRepository;
import org.example.datnbbook.repository.NgonNguRepository;
import org.example.datnbbook.repository.NguoiDichRepository;
import org.example.datnbbook.repository.NhaXuatBanRepository;
import org.example.datnbbook.repository.SanPhamRepository;
import org.example.datnbbook.repository.TacGiaRepository;
import org.example.datnbbook.repository.TheLoaiRepository;
import org.example.datnbbook.service.SanPhamService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;
    @Autowired
    private LoaiBiaRepository loaiBiaRepository;
    @Autowired
    private ChatLieuRepository chatLieuRepository;
    @Autowired
    private TacGiaRepository tacGiaRepository;
    @Autowired
    private NhaXuatBanRepository nhaXuatBanRepository;
    @Autowired
    private NguoiDichRepository nguoiDichRepository;
    @Autowired
    private TheLoaiRepository theLoaiRepository;
    @Autowired
    private NgonNguRepository ngonNguRepository;

    public List<SanPham> getAll() {
        List<SanPham> sanPhams = sanPhamRepository.findAll().stream()
                .filter(sp -> !sp.getDeleted()) // Lọc deleted = false
                .sorted(Comparator.comparing(SanPham::getId, Comparator.reverseOrder())) // Sắp xếp theo id giảm dần
                .collect(Collectors.toList());

        // Tính tổng số lượng tồn cho mỗi sản phẩm
        for (SanPham sanPham : sanPhams) {
            int tongSoLuongTon = chiTietSanPhamRepository.findByIdSanPham_Id(sanPham.getId())
                    .stream()
                    .filter(ctsp -> !ctsp.getDeleted()) // Chỉ tính các ChiTietSanPham chưa xóa
                    .mapToInt(ChiTietSanPham::getSoLuongTon)
                    .sum();
            sanPham.setTongSoLuongTon(tongSoLuongTon); // Giả sử có setter này trong SanPham
        }

        return sanPhams;
    }

    public SanPham getById(Integer id) {
        return sanPhamRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + id));
    }

    public SanPham create(SanPham sp) {
        if (sp.getMaSanPham() == null || sp.getMaSanPham().isEmpty()) {
            String nextMaSanPham = sanPhamRepository.getNextSequenceValue();
            sp.setMaSanPham(nextMaSanPham);
        }
        sp.setDeleted(false); // Đảm bảo sản phẩm mới không bị đánh dấu xóa
        return sanPhamRepository.save(sp);
    }

//    public void createSanPhamAndChiTiet(String tenSanPham, String moTa, String tenCTSP) {
//        sanPhamRepository.createSanPhamAndChiTiet(tenSanPham, moTa, tenCTSP);
//    }

    public SanPhamDTO update(Integer id, SanPhamDTO sanPhamDTO) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + id));

        BeanUtils.copyProperties(sanPhamDTO, sanPham, "maSanPham", "id", "deleted");
        sanPham = sanPhamRepository.save(sanPham);
        return convertToDTO(sanPham);
    }

    public void delete(Integer id) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + id));
        sanPham.setDeleted(true);
        sanPhamRepository.save(sanPham);
    }

    public List<SanPham> search(String keyword) {
        return sanPhamRepository.search(keyword);
    }

    private SanPhamDTO convertToDTO(SanPham sanPham) {
        SanPhamDTO sanPhamDTO = new SanPhamDTO();
        BeanUtils.copyProperties(sanPham, sanPhamDTO);
        return sanPhamDTO;
    }

    @Transactional
    public SanPham createWithDetails(SanPhamRequest request) {
        // Tạo và lưu SanPham
        SanPham sanPham = new SanPham();
        sanPham.setMaSanPham(sanPhamRepository.getNextSequenceValue());
        sanPham.setTenSanPham(request.getTenSanPham());
        sanPham.setMoTa(request.getMoTaSanPham());
        sanPham.setDeleted(false);
        SanPham savedSanPham = sanPhamRepository.save(sanPham);

        // Tạo và lưu danh sách ChiTietSanPham
        List<SanPhamRequest.ChiTietSanPhamDTO> chiTietList = request.getChiTietSanPhamList();
        for (SanPhamRequest.ChiTietSanPhamDTO dto : chiTietList) {
            ChiTietSanPham ctsp = new ChiTietSanPham();
            ctsp.setMaChiTietSanPham(chiTietSanPhamRepository.getNextCTSPSequenceValue());
            ctsp.setTenChiTietSanPham(dto.getTenChiTietSanPham());
            ctsp.setIdSanPham(savedSanPham);

            if (dto.getIdLoaiBia() != null) {
                LoaiBia loaiBia = loaiBiaRepository.findById(dto.getIdLoaiBia())
                        .orElseThrow(() -> new RuntimeException("LoaiBia not found"));
                ctsp.setIdLoaiBia(loaiBia);
            }
            if (dto.getIdChatLieu() != null) {
                ChatLieu chatLieu = chatLieuRepository.findById(dto.getIdChatLieu())
                        .orElseThrow(() -> new RuntimeException("ChatLieu not found"));
                ctsp.setIdChatLieu(chatLieu);
            }
            if (request.getIdTacGia() != null) {
                TacGia tacGia = tacGiaRepository.findById(request.getIdTacGia())
                        .orElseThrow(() -> new RuntimeException("TacGia not found"));
                ctsp.setIdTacGia(tacGia);
            }
            if (request.getIdNhaXuatBan() != null) {
                NhaXuatBan nhaXuatBan = nhaXuatBanRepository.findById(request.getIdNhaXuatBan())
                        .orElseThrow(() -> new RuntimeException("NhaXuatBan not found"));
                ctsp.setIdNhaXuatBan(nhaXuatBan);
            }
            if (request.getIdNguoiDich() != null) {
                NguoiDich nguoiDich = nguoiDichRepository.findById(request.getIdNguoiDich())
                        .orElseThrow(() -> new RuntimeException("NguoiDich not found"));
                ctsp.setIdNguoiDich(nguoiDich);
            }
            if (request.getIdTheLoai() != null) {
                TheLoai theLoai = theLoaiRepository.findById(request.getIdTheLoai())
                        .orElseThrow(() -> new RuntimeException("TheLoai not found"));
                ctsp.setIdTheLoai(theLoai);
            }
            if (request.getIdNgonNgu() != null) {
                NgonNgu ngonNgu = ngonNguRepository.findById(request.getIdNgonNgu())
                        .orElseThrow(() -> new RuntimeException("NgonNgu not found"));
                ctsp.setIdNgonNgu(ngonNgu);
            }

            ctsp.setGia(BigDecimal.valueOf(dto.getGia()));
            ctsp.setSoLuongTon(dto.getSoLuongTon());
            ctsp.setTrongLuong(BigDecimal.valueOf(request.getTrongLuong()));
            ctsp.setKichThuoc(BigDecimal.valueOf(request.getKichThuoc()));
            ctsp.setMoTa(request.getMoTaChiTiet());
            ctsp.setTrangThai(true);
            ctsp.setCreatedAt(Instant.now());
            ctsp.setUpdatedAt(Instant.now());
            ctsp.setDeleted(false);

            chiTietSanPhamRepository.save(ctsp);
        }

        return savedSanPham;
    }


}