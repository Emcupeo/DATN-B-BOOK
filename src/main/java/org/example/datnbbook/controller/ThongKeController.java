package org.example.datnbbook.controller;

import org.example.datnbbook.model.ChiTietSanPham;
import org.example.datnbbook.model.BoSach;
import org.example.datnbbook.repository.ChiTietSanPhamRepository;
import org.example.datnbbook.repository.BoSachRepository;
import org.example.datnbbook.repository.HoaDonChiTietRepository;
import org.example.datnbbook.repository.DotGiamGiaChiTietRepository;
import org.example.datnbbook.repository.DotGiamGiaBoSachChiTietRepository;
import org.example.datnbbook.dto.ProductWithDiscountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/thong-ke")
@CrossOrigin(origins = "*")
public class ThongKeController {

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Autowired
    private BoSachRepository boSachRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private DotGiamGiaChiTietRepository dotGiamGiaChiTietRepository;

    @Autowired
    private DotGiamGiaBoSachChiTietRepository dotGiamGiaBoSachChiTietRepository;

    // Lấy 5 sách mới nhất
    @GetMapping("/sach-moi-nhat")
    public List<ProductWithDiscountDTO> getSachMoiNhat(@RequestParam(defaultValue = "5") int limit) {
        List<ChiTietSanPham> products = chiTietSanPhamRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .limit(limit)
                .collect(Collectors.toList());
        
        // Force load relationships để tránh lazy loading
        for (ChiTietSanPham product : products) {
            if (product.getIdSanPham() != null) {
                product.getIdSanPham().getTenSanPham(); // Force load
                if (product.getIdSanPham().getDanhMuc() != null) {
                    product.getIdSanPham().getDanhMuc().getTenDanhMuc(); // Force load
                }
            }
            if (product.getIdTacGia() != null) {
                product.getIdTacGia().getTenTacGia(); // Force load
            }
            if (product.getIdNhaXuatBan() != null) {
                product.getIdNhaXuatBan().getTenNhaXuatBan(); // Force load
            }
            if (product.getIdLoaiBia() != null) {
                product.getIdLoaiBia().getTenLoaiBia(); // Force load
            }
            if (product.getIdNgonNgu() != null) {
                product.getIdNgonNgu().getTenNgonNgu(); // Force load
            }
            // Force load ảnh sản phẩm
            if (product.getAnhSanPhams() != null) {
                product.getAnhSanPhams().size(); // Force load
            }
        }
        
        // Transform to DTO with discount info
        return products.stream()
                .map(this::transformToProductWithDiscountDTO)
                .collect(Collectors.toList());
    }

    // Lấy 5 bộ sách mới nhất
    @GetMapping("/bo-sach-moi-nhat")
    public List<ProductWithDiscountDTO> getBoSachMoiNhat(@RequestParam(defaultValue = "5") int limit) {
        List<BoSach> bookSets = boSachRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .limit(limit)
                .collect(Collectors.toList());
        
        // Force load relationships để tránh lazy loading
        for (BoSach bookSet : bookSets) {
            // BoSach sử dụng trường url trực tiếp, không cần force load
            bookSet.getUrl(); // Force load url
        }
        
        // Transform to DTO with discount info
        return bookSets.stream()
                .map(this::transformToBookSetWithDiscountDTO)
                .collect(Collectors.toList());
    }

    // Lấy 5 sách bán chạy nhất
    @GetMapping("/sach-ban-chay")
    public List<ProductWithDiscountDTO> getSachBanChay(@RequestParam(defaultValue = "5") int limit) {
        List<Object[]> results = hoaDonChiTietRepository.findTopSellingProducts(limit);
        List<ChiTietSanPham> products = results.stream()
                .map(result -> {
                    Integer chiTietSanPhamId = (Integer) result[0];
                    return chiTietSanPhamRepository.findById(chiTietSanPhamId).orElse(null);
                })
                .filter(chiTietSanPham -> chiTietSanPham != null)
                .collect(Collectors.toList());
        
        // Force load relationships để tránh lazy loading
        for (ChiTietSanPham product : products) {
            if (product.getIdSanPham() != null) {
                product.getIdSanPham().getTenSanPham(); // Force load
                if (product.getIdSanPham().getDanhMuc() != null) {
                    product.getIdSanPham().getDanhMuc().getTenDanhMuc(); // Force load
                }
            }
            if (product.getIdTacGia() != null) {
                product.getIdTacGia().getTenTacGia(); // Force load
            }
            if (product.getIdNhaXuatBan() != null) {
                product.getIdNhaXuatBan().getTenNhaXuatBan(); // Force load
            }
            if (product.getIdLoaiBia() != null) {
                product.getIdLoaiBia().getTenLoaiBia(); // Force load
            }
            if (product.getIdNgonNgu() != null) {
                product.getIdNgonNgu().getTenNgonNgu(); // Force load
            }
            // Force load ảnh sản phẩm
            if (product.getAnhSanPhams() != null) {
                product.getAnhSanPhams().size(); // Force load
            }
        }
        
        // Transform to DTO with discount info
        return products.stream()
                .map(this::transformToProductWithDiscountDTO)
                .collect(Collectors.toList());
    }

    // Lấy 5 bộ sách bán chạy nhất
    @GetMapping("/bo-sach-ban-chay")
    public List<ProductWithDiscountDTO> getBoSachBanChay(@RequestParam(defaultValue = "5") int limit) {
        List<Object[]> results = hoaDonChiTietRepository.findTopSellingBookSets(limit);
        List<BoSach> bookSets = results.stream()
                .map(result -> {
                    Integer boSachId = (Integer) result[0];
                    return boSachRepository.findById(boSachId).orElse(null);
                })
                .filter(boSach -> boSach != null)
                .collect(Collectors.toList());
        
        // Force load relationships để tránh lazy loading
        for (BoSach bookSet : bookSets) {
            bookSet.getUrl(); // Force load url
        }
        
        // Transform to DTO with discount info
        return bookSets.stream()
                .map(this::transformToBookSetWithDiscountDTO)
                .collect(Collectors.toList());
    }

    // Transform ChiTietSanPham to ProductWithDiscountDTO
    private ProductWithDiscountDTO transformToProductWithDiscountDTO(ChiTietSanPham product) {
        ProductWithDiscountDTO dto = new ProductWithDiscountDTO();
        
        // Basic info
        dto.setId(product.getId());
        dto.setTitle(product.getTenChiTietSanPham() != null ? 
                    product.getTenChiTietSanPham() : 
                    (product.getIdSanPham() != null ? product.getIdSanPham().getTenSanPham() : "Không có tên"));
        dto.setAuthor(product.getIdTacGia() != null ? 
                     product.getIdTacGia().getTenTacGia() : 
                     "Không có tác giả");
        dto.setPrice(product.getGia() != null ? product.getGia() : BigDecimal.ZERO);
        dto.setImage((product.getAnhSanPhams() != null && !product.getAnhSanPhams().isEmpty()) ? 
                    product.getAnhSanPhams().get(0).getUrl() : 
                    "https://via.placeholder.com/300x400?text=No+Image");
        dto.setCategory(product.getIdSanPham() != null && product.getIdSanPham().getDanhMuc() != null ? 
                       product.getIdSanPham().getDanhMuc().getTenDanhMuc() : "Sách lẻ");
        dto.setPublisher(product.getIdNhaXuatBan() != null ? 
                        product.getIdNhaXuatBan().getTenNhaXuatBan() : 
                        "NXB Trẻ");
        dto.setLanguage(product.getIdNgonNgu() != null ? 
                       product.getIdNgonNgu().getTenNgonNgu() : 
                       "Tiếng Việt");
        dto.setFormat(product.getIdLoaiBia() != null ? 
                     product.getIdLoaiBia().getTenLoaiBia() : 
                     "Bìa mềm");
        dto.setPages(300); // ChiTietSanPham không có soTrang, dùng giá trị mặc định
        dto.setStockQuantity(product.getSoLuongTon() != null ? product.getSoLuongTon() : 0);
        dto.setInStock((product.getSoLuongTon() != null ? product.getSoLuongTon() : 0) > 0);
        dto.setProductCode(product.getMaChiTietSanPham());
        dto.setDescription(product.getMoTa() != null ? product.getMoTa() : 
                          (product.getIdSanPham() != null ? product.getIdSanPham().getMoTa() : ""));
        dto.setIsBookSet(false);
        
        // Check for active discount
        try {
            var activeDiscount = dotGiamGiaChiTietRepository.findActiveByChiTietSanPhamId(product.getId());
            if (activeDiscount != null) {
                dto.setHasDiscount(true);
                dto.setOriginalPrice(activeDiscount.getGiaBanDau());
                dto.setDiscountAmount(activeDiscount.getSoTienGiam());
                dto.setDiscountPercentage(activeDiscount.getSoPhanTramGiam());
            } else {
                dto.setHasDiscount(false);
                dto.setOriginalPrice(dto.getPrice());
                dto.setDiscountAmount(BigDecimal.ZERO);
                dto.setDiscountPercentage(BigDecimal.ZERO);
            }
        } catch (Exception e) {
            dto.setHasDiscount(false);
            dto.setOriginalPrice(dto.getPrice());
            dto.setDiscountAmount(BigDecimal.ZERO);
            dto.setDiscountPercentage(BigDecimal.ZERO);
        }
        
        return dto;
    }

    // Transform BoSach to ProductWithDiscountDTO
    private ProductWithDiscountDTO transformToBookSetWithDiscountDTO(BoSach bookSet) {
        ProductWithDiscountDTO dto = new ProductWithDiscountDTO();
        
        // Basic info
        dto.setId(bookSet.getId());
        dto.setTitle(bookSet.getTenBoSach() != null ? bookSet.getTenBoSach() : "Không có tên");
        dto.setAuthor("Nhiều tác giả");
        dto.setPrice(bookSet.getGiaTien() != null ? bookSet.getGiaTien() : BigDecimal.ZERO);
        dto.setImage(bookSet.getUrl() != null ? bookSet.getUrl() : "https://via.placeholder.com/300x400?text=No+Image");
        dto.setCategory("Bộ sách");
        dto.setPublisher("Nhiều NXB");
        dto.setLanguage("Tiếng Việt");
        dto.setFormat("Bộ sách");
        dto.setPages(0); // Bộ sách không có số trang cố định
        dto.setStockQuantity(bookSet.getSoLuong() != null ? bookSet.getSoLuong() : 0);
        dto.setInStock((bookSet.getSoLuong() != null ? bookSet.getSoLuong() : 0) > 0);
        dto.setProductCode(bookSet.getMaBoSach());
        dto.setDescription(bookSet.getMoTa() != null ? bookSet.getMoTa() : "");
        dto.setIsBookSet(true);
        
        // Check for active discount
        try {
            var activeDiscount = dotGiamGiaBoSachChiTietRepository.findActiveDiscountByBoSachId(bookSet.getId());
            if (activeDiscount != null) {
                dto.setHasDiscount(true);
                dto.setOriginalPrice(activeDiscount.getGiaBanDau());
                dto.setDiscountAmount(activeDiscount.getSoTienGiam());
                dto.setDiscountPercentage(activeDiscount.getSoPhanTramGiam());
            } else {
                dto.setHasDiscount(false);
                dto.setOriginalPrice(dto.getPrice());
                dto.setDiscountAmount(BigDecimal.ZERO);
                dto.setDiscountPercentage(BigDecimal.ZERO);
            }
        } catch (Exception e) {
            dto.setHasDiscount(false);
            dto.setOriginalPrice(dto.getPrice());
            dto.setDiscountAmount(BigDecimal.ZERO);
            dto.setDiscountPercentage(BigDecimal.ZERO);
        }
        
        return dto;
    }
}
