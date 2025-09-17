package org.example.datnbbook.service;

import org.example.datnbbook.dto.SanPhamDTO;
import org.example.datnbbook.dto.SanPhamRequest;
import org.example.datnbbook.model.ChatLieu;
import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.DanhMuc;
import org.example.datnbbook.model.LoaiBia;
import org.example.datnbbook.model.NgonNgu;
import org.example.datnbbook.model.NguoiDich;
import org.example.datnbbook.model.NhaXuatBan;
import org.example.datnbbook.model.SanPham;
import org.example.datnbbook.model.TacGia;
import org.example.datnbbook.model.TheLoai;
import org.example.datnbbook.repository.ChatLieuRepository;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.example.datnbbook.repository.BoSachChiTietRepository;
import org.example.datnbbook.repository.DanhMucRepository;
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
    @Autowired
    private DanhMucRepository danhMucRepository;
    @Autowired
    private BoSachChiTietRepository boSachChiTietRepository;

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
        
        // Set created_at và updated_at nếu chưa có
        if (sp.getCreatedAt() == null) {
            sp.setCreatedAt(Instant.now());
        }
        if (sp.getUpdatedAt() == null) {
            sp.setUpdatedAt(Instant.now());
        }
        
        return sanPhamRepository.save(sp);
    }

//    public void createSanPhamAndChiTiet(String tenSanPham, String moTa, String tenCTSP) {
//        sanPhamRepository.createSanPhamAndChiTiet(tenSanPham, moTa, tenCTSP);
//    }

    public SanPhamDTO update(Integer id, SanPhamDTO sanPhamDTO) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + id));

        BeanUtils.copyProperties(sanPhamDTO, sanPham, "maSanPham", "id", "deleted", "danhMuc");
        
        // Set updated_at
        sanPham.setUpdatedAt(Instant.now());
        
        // Xử lý danh mục
        if (sanPhamDTO.getIdDanhMuc() != null) {
            DanhMuc danhMuc = danhMucRepository.findById(sanPhamDTO.getIdDanhMuc())
                    .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại với id: " + sanPhamDTO.getIdDanhMuc()));
            sanPham.setDanhMuc(danhMuc);
        } else {
            sanPham.setDanhMuc(null);
        }
        
        sanPham = sanPhamRepository.save(sanPham);
        return convertToDTO(sanPham);
    }

    public void delete(Integer id) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .filter(sp -> !sp.getDeleted())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + id));
        // Soft delete sản phẩm
        sanPham.setDeleted(true);
        sanPhamRepository.save(sanPham);

        // Kiểm tra toàn bộ chi tiết. Nếu có CTSP thuộc bộ sách active => chặn và thông báo tên bộ đầu tiên
        List<ChiTietSanPham> ctList = chiTietSanPhamRepository.findByIdSanPham_Id(id)
                .stream().filter(ct -> !ct.getDeleted()).collect(java.util.stream.Collectors.toList());
        for (ChiTietSanPham ct : ctList) {
            if (boSachChiTietRepository.existsActiveByChiTietSanPhamId(ct.getId())) {
                var names = boSachChiTietRepository.findActiveBoSachNamesByChiTietSanPhamId(ct.getId());
                String name = (names != null && !names.isEmpty()) ? names.get(0) : "";
                String suffix = name.isEmpty() ? "" : " (" + name + ")";
                throw new IllegalArgumentException("Sản phẩm này đang nằm trong bộ sách" + suffix + ". Vui lòng gỡ khỏi bộ sách trước khi xóa.");
            }
        }

        // Nếu pass validate -> soft delete tất cả CTSP con
        for (ChiTietSanPham ct : ctList) {
            ct.setDeleted(true);
            ct.setUpdatedAt(Instant.now());
            chiTietSanPhamRepository.save(ct);
        }
    }

    public List<SanPham> search(String keyword) {
        return sanPhamRepository.search(keyword);
    }

    public List<SanPham> getByDanhMucId(Integer danhMucId) {
        List<SanPham> sanPhams = sanPhamRepository.findByDanhMuc_Id(danhMucId).stream()
                .filter(sp -> !sp.getDeleted())
                .sorted(Comparator.comparing(SanPham::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Tính tổng số lượng tồn cho mỗi sản phẩm
        for (SanPham sanPham : sanPhams) {
            int tongSoLuongTon = chiTietSanPhamRepository.findByIdSanPham_Id(sanPham.getId())
                    .stream()
                    .filter(ctsp -> !ctsp.getDeleted())
                    .mapToInt(ChiTietSanPham::getSoLuongTon)
                    .sum();
            sanPham.setTongSoLuongTon(tongSoLuongTon);
        }

        return sanPhams;
    }

    private SanPhamDTO convertToDTO(SanPham sanPham) {
        SanPhamDTO sanPhamDTO = new SanPhamDTO();
        BeanUtils.copyProperties(sanPham, sanPhamDTO);
        
        // Thêm thông tin danh mục
        if (sanPham.getDanhMuc() != null) {
            sanPhamDTO.setIdDanhMuc(sanPham.getDanhMuc().getId());
            sanPhamDTO.setTenDanhMuc(sanPham.getDanhMuc().getTenDanhMuc());
        }
        
        sanPhamDTO.setCreatedAt(sanPham.getCreatedAt());
        sanPhamDTO.setUpdatedAt(sanPham.getUpdatedAt());
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
        
        // Set created_at và updated_at
        Instant now = Instant.now();
        sanPham.setCreatedAt(now);
        sanPham.setUpdatedAt(now);
        
        // Xử lý danh mục
        if (request.getIdDanhMuc() != null) {
            DanhMuc danhMuc = danhMucRepository.findById(request.getIdDanhMuc())
                    .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại với id: " + request.getIdDanhMuc()));
            sanPham.setDanhMuc(danhMuc);
        }
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
            ctsp.setTrongLuong(dto.getTrongLuong() != null ? BigDecimal.valueOf(dto.getTrongLuong()) : BigDecimal.ZERO);
            ctsp.setKichThuoc(dto.getKichThuoc() != null ? BigDecimal.valueOf(dto.getKichThuoc()) : BigDecimal.ZERO); // Lấy từ DTO
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