package org.example.datnbbook.service.impl;

import jakarta.persistence.EntityManager;
import org.example.datnbbook.dto.DiaChiDTO;
import org.example.datnbbook.dto.KhachHangAddressDTO;
import org.example.datnbbook.dto.KhachHangDTO;
import org.example.datnbbook.dto.NguoiDungDTO;
import org.example.datnbbook.model.DiaChi;
import org.example.datnbbook.model.KhachHang;
import org.example.datnbbook.model.NguoiDung;
import org.example.datnbbook.repository.DiaChiRepository;
import org.example.datnbbook.repository.KhachHangRepository;
import org.example.datnbbook.service.KhachHangService;
import org.example.datnbbook.service.AuthService;
import org.example.datnbbook.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class KhachHangServiceImpl implements KhachHangService {
    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private DiaChiRepository diaChiRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    private final Random random = new Random();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    @Override
    public List<KhachHangDTO> getAll() {
        List<KhachHang> khachHangs = khachHangRepository.findByDeletedFalse();
        return khachHangs.stream()
                .map(khachHang -> {
                    KhachHangDTO dto = convertToDTO(khachHang);
                    // Fetch and set addresses
                    List<DiaChi> addresses = diaChiRepository.findByKhachHangIdAndDeletedFalse(khachHang.getId());
                    dto.setDanhSachDiaChi(addresses.stream()
                            .map(diaChi -> {
                                DiaChiDTO diaChiDTO = new DiaChiDTO();
                                diaChiDTO.setId(diaChi.getId());
                                diaChiDTO.setDiaChiChiTiet(diaChi.getDiaChiChiTiet());
                                diaChiDTO.setTinhThanh(diaChi.getTinhThanh());
                                diaChiDTO.setQuanHuyen(diaChi.getQuanHuyen());
                                diaChiDTO.setXaPhuong(diaChi.getXaPhuong());
                                diaChiDTO.setMacDinh(diaChi.getMacDinh());
                                diaChiDTO.setTenNguoiNhan(diaChi.getTenNguoiNhan());
                                diaChiDTO.setSoDienThoaiNguoiNhan(diaChi.getSoDienThoaiNguoiNhan());
                                return diaChiDTO;
                            })
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public KhachHangDTO getById(Long id) {
        validateId(id);
        KhachHang khachHang = findKhachHangById(id);

        // Convert to DTO and set addresses separately
        KhachHangDTO dto = convertToDTO(khachHang);

        // Fetch and set addresses
        List<DiaChi> addresses = diaChiRepository.findByKhachHangIdAndDeletedFalse(id);
        dto.setDanhSachDiaChi(addresses.stream()
                .map(diaChi -> {
                    DiaChiDTO diaChiDTO = new DiaChiDTO();
                    diaChiDTO.setId(diaChi.getId());
                    diaChiDTO.setDiaChiChiTiet(diaChi.getDiaChiChiTiet());
                    diaChiDTO.setTinhThanh(diaChi.getTinhThanh());
                    diaChiDTO.setQuanHuyen(diaChi.getQuanHuyen());
                    diaChiDTO.setXaPhuong(diaChi.getXaPhuong());
                    diaChiDTO.setMacDinh(diaChi.getMacDinh());
                    diaChiDTO.setTenNguoiNhan(diaChi.getTenNguoiNhan());
                    diaChiDTO.setSoDienThoaiNguoiNhan(diaChi.getSoDienThoaiNguoiNhan());
                    return diaChiDTO;
                })
                .collect(Collectors.toList()));

        return dto;
    }

    @Override
    public KhachHangDTO create(KhachHangDTO khachHangDTO) {
        validateKhachHangDTO(khachHangDTO);

        KhachHang khachHang = convertToEntity(khachHangDTO);
        khachHang = khachHangRepository.save(khachHang);
        return convertToDTO(khachHang);
    }

    @Override
    @Transactional
    public KhachHangDTO update(Long id, KhachHangDTO khachHangDTO) {
        validateId(id);
        validateKhachHangDTO(khachHangDTO);

        KhachHang existingKhachHang = findKhachHangById(id);
        updateKhachHangFromDTO(existingKhachHang, khachHangDTO);

        // Update addresses if provided
        if (khachHangDTO.getDanhSachDiaChi() != null) {
            // Get existing addresses
            List<DiaChi> existingAddresses = diaChiRepository.findByKhachHangIdAndDeletedFalse(id);

            // Mark all existing addresses as deleted first
            existingAddresses.forEach(address -> {
                address.setDeleted(true);
                diaChiRepository.save(address);
            });

            // Create new addresses
            List<DiaChi> newAddresses = new ArrayList<>();

            // Check if there's any default address in the new list
            boolean hasDefaultAddress = khachHangDTO.getDanhSachDiaChi().stream()
                    .anyMatch(DiaChiDTO::getMacDinh);

            for (DiaChiDTO diaChiDTO : khachHangDTO.getDanhSachDiaChi()) {
                DiaChi diaChi = new DiaChi();

                // Update address properties
                diaChi.setDiaChiChiTiet(diaChiDTO.getDiaChiChiTiet());
                diaChi.setTinhThanh(diaChiDTO.getTinhThanh());
                diaChi.setQuanHuyen(diaChiDTO.getQuanHuyen());
                diaChi.setXaPhuong(diaChiDTO.getXaPhuong());

                // Handle default address logic
                if (diaChiDTO.getMacDinh()) {
                    // If this address is set as default, set all other addresses to non-default
                    newAddresses.forEach(addr -> addr.setMacDinh(false));
                    diaChi.setMacDinh(true);
                } else {
                    // If no address is set as default in the list, make the first one default
                    diaChi.setMacDinh(!hasDefaultAddress && newAddresses.isEmpty());
                }

                diaChi.setKhachHang(existingKhachHang);
                diaChi.setDeleted(false);

                // Save the address
                diaChi = diaChiRepository.save(diaChi);
                newAddresses.add(diaChi);
            }

            // Don't touch the collection, let Hibernate handle it automatically
        }

        // Save the customer
        KhachHang savedKhachHang = khachHangRepository.save(existingKhachHang);

        // Convert to DTO
        KhachHangDTO updatedDTO = convertToDTO(savedKhachHang);

        // Set addresses in DTO
        List<DiaChi> addresses = diaChiRepository.findByKhachHangIdAndDeletedFalse(savedKhachHang.getId());
        updatedDTO.setDanhSachDiaChi(addresses.stream()
                .map(diaChi -> {
                    DiaChiDTO diaChiDTO = new DiaChiDTO();
                    diaChiDTO.setId(diaChi.getId());
                    diaChiDTO.setDiaChiChiTiet(diaChi.getDiaChiChiTiet());
                    diaChiDTO.setTinhThanh(diaChi.getTinhThanh());
                    diaChiDTO.setQuanHuyen(diaChi.getQuanHuyen());
                    diaChiDTO.setXaPhuong(diaChi.getXaPhuong());
                    diaChiDTO.setMacDinh(diaChi.getMacDinh());
                    return diaChiDTO;
                })
                .collect(Collectors.toList()));

        return updatedDTO;
    }

    @Override
    public void delete(Long id) {
        validateId(id);
        if (!khachHangRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy khách hàng với ID: " + id);
        }
        khachHangRepository.deleteById(id);
    }

    @Override
    public List<KhachHangDTO> search(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return getAll();
        }

        List<KhachHang> khachHangs = khachHangRepository
                .findByHoTenContainingIgnoreCaseOrEmailContainingIgnoreCaseOrSoDienThoaiContaining(keyword);
        return khachHangs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public KhachHang createWithAddresses(KhachHangAddressDTO dto) {
        validateKhachHangAddressDTO(dto);

        // Create new customer
        KhachHang khachHang = new KhachHang();
        updateKhachHangFromAddressDTO(khachHang, dto);

        // Generate customer code using MAX logic
        String customerCode = generateCustomerCode();
        khachHang.setMaKhachHang(customerCode);

        // Save customer first to get ID
        KhachHang savedKhachHang = khachHangRepository.save(khachHang);

        // Tạo tài khoản NguoiDung tương ứng
        String randomPassword = generateRandomPassword();
        try {
            NguoiDungDTO nguoiDungDTO = new NguoiDungDTO();
            nguoiDungDTO.setTenDangNhap(customerCode);
            nguoiDungDTO.setMatKhau(randomPassword);
            nguoiDungDTO.setEmail(savedKhachHang.getEmail());
            nguoiDungDTO.setHoTen(savedKhachHang.getHoTen());
            nguoiDungDTO.setLoaiNguoiDung(NguoiDung.LoaiNguoiDung.KHACH_HANG);
            nguoiDungDTO.setTrangThai(true);
            
            authService.createNguoiDung(nguoiDungDTO);
        } catch (Exception e) {
            System.err.println("Không thể tạo tài khoản đăng nhập cho KH " + customerCode + ": " + e.getMessage());
        }

        // Gửi email thông tin đăng nhập cho khách hàng
        try {
            emailService.sendCustomerCredentials(
                    savedKhachHang.getEmail(),
                    customerCode,
                    randomPassword
            );
        } catch (Exception e) {
            System.err.println("Không thể gửi email thông tin đăng nhập cho KH " + customerCode + ": " + e.getMessage());
        }

        // Create addresses
        if (dto.getDanhSachDiaChi() != null && !dto.getDanhSachDiaChi().isEmpty()) {
            List<DiaChi> addresses = dto.getDanhSachDiaChi().stream()
                    .map(diaChiDTO -> createDiaChiFromDTO(diaChiDTO, savedKhachHang))
                    .collect(Collectors.toList());

            diaChiRepository.saveAll(addresses);
            // Don't set the collection directly, let Hibernate manage it
        }

        return savedKhachHang;
    }

    @Override
    public KhachHangDTO updateStatus(Long id, Integer trangThai) {
        validateId(id);
        KhachHang khachHang = findKhachHangById(id);
        khachHang.setTrangThai(trangThai);
        khachHang = khachHangRepository.save(khachHang);
        return convertToDTO(khachHang);
    }

    private String generateCustomerCode() {
        // Use MAX() logic instead of sequence
        String result = (String) entityManager
                .createNativeQuery("SELECT 'KH' + RIGHT('00000' + CAST((SELECT ISNULL(MAX(CAST(SUBSTRING(ma_khach_hang, 3, 5) AS INT)), 0) + 1 FROM khach_hang WHERE ma_khach_hang LIKE 'KH%') AS VARCHAR(5)), 5)")
                .getSingleResult();
        
        return result;
    }

    // Helper methods
    private KhachHang findKhachHangById(Long id) {
        return khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
    }

    private void validateKhachHangDTO(KhachHangDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dữ liệu khách hàng không được để trống");
        }
        if (!StringUtils.hasText(dto.getHoTen())) {
            throw new IllegalArgumentException("Họ tên không được để trống");
        }
        if (!StringUtils.hasText(dto.getEmail())) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        if (!StringUtils.hasText(dto.getSoDienThoai())) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }
        validateNgaySinh(dto.getNgaySinh());
    }

    private void validateKhachHangAddressDTO(KhachHangAddressDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dữ liệu khách hàng không được để trống");
        }
        if (!StringUtils.hasText(dto.getHoTen())) {
            throw new IllegalArgumentException("Họ tên không được để trống");
        }
        if (!StringUtils.hasText(dto.getEmail())) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        if (!StringUtils.hasText(dto.getSoDienThoai())) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }
        validateNgaySinh(dto.getNgaySinh());
    }

    private void validateNgaySinh(String ngaySinh) {
        if (StringUtils.hasText(ngaySinh)) {
            try {
                LocalDate.parse(ngaySinh, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Ngày sinh không đúng định dạng (yyyy-MM-dd)");
            }
        }
    }

    private KhachHangDTO convertToDTO(KhachHang khachHang) {
        KhachHangDTO dto = new KhachHangDTO();
        dto.setId(khachHang.getId());
        dto.setMaKhachHang(khachHang.getMaKhachHang());
        dto.setHoTen(khachHang.getHoTen());
        dto.setEmail(khachHang.getEmail());
        dto.setSoDienThoai(khachHang.getSoDienThoai());
        dto.setNgaySinh(khachHang.getNgaySinh() != null ? khachHang.getNgaySinh().format(DATE_FORMATTER) : null);
        dto.setGioiTinh(khachHang.getGioiTinh());
        dto.setTrangThai(khachHang.getTrangThai());
        dto.setCreatedAt(khachHang.getCreatedAt());
        dto.setCreatedBy(khachHang.getCreatedBy());
        dto.setUpdatedAt(khachHang.getUpdatedAt());
        dto.setUpdatedBy(khachHang.getUpdatedBy());
        return dto;
    }

    private KhachHang convertToEntity(KhachHangDTO dto) {
        KhachHang khachHang = new KhachHang();
        khachHang.setId(dto.getId());
        khachHang.setMaKhachHang(dto.getMaKhachHang());
        khachHang.setHoTen(dto.getHoTen());
        khachHang.setEmail(dto.getEmail());
        khachHang.setSoDienThoai(dto.getSoDienThoai());
        khachHang.setNgaySinh(StringUtils.hasText(dto.getNgaySinh()) ? LocalDate.parse(dto.getNgaySinh(), DATE_FORMATTER) : null);
        khachHang.setGioiTinh(dto.getGioiTinh());
        khachHang.setTrangThai(dto.getTrangThai());
        return khachHang;
    }

    private void updateKhachHangFromDTO(KhachHang khachHang, KhachHangDTO dto) {
        khachHang.setHoTen(dto.getHoTen());
        khachHang.setEmail(dto.getEmail());
        khachHang.setSoDienThoai(dto.getSoDienThoai());
        khachHang.setNgaySinh(StringUtils.hasText(dto.getNgaySinh()) ? LocalDate.parse(dto.getNgaySinh(), DATE_FORMATTER) : null);
        khachHang.setGioiTinh(dto.getGioiTinh());
        khachHang.setTrangThai(dto.getTrangThai());
    }

    private void updateKhachHangFromAddressDTO(KhachHang khachHang, KhachHangAddressDTO dto) {
        khachHang.setHoTen(dto.getHoTen());
        khachHang.setEmail(dto.getEmail());
        khachHang.setSoDienThoai(dto.getSoDienThoai());
        khachHang.setNgaySinh(StringUtils.hasText(dto.getNgaySinh()) ? LocalDate.parse(dto.getNgaySinh(), DATE_FORMATTER) : null);
        khachHang.setGioiTinh(dto.getGioiTinh());
        khachHang.setTrangThai(dto.getTrangThai());
    }

    private DiaChi createDiaChiFromDTO(KhachHangAddressDTO.DiaChiDTO diaChiDTO, KhachHang khachHang) {
        DiaChi diaChi = new DiaChi();
        diaChi.setDiaChiChiTiet(diaChiDTO.getDiaChiChiTiet());
        diaChi.setTinhThanh(diaChiDTO.getTinhThanh());
        diaChi.setQuanHuyen(diaChiDTO.getQuanHuyen());
        diaChi.setXaPhuong(diaChiDTO.getXaPhuong());
        diaChi.setMacDinh(diaChiDTO.getMacDinh());
        diaChi.setKhachHang(khachHang);
        return diaChi;
    }
} 