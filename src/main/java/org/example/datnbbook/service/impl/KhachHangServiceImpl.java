package org.example.datnbbook.service.impl;

import jakarta.persistence.EntityManager;
import org.example.datnbbook.dto.DiaChiDTO;
import org.example.datnbbook.dto.KhachHangAddressDTO;
import org.example.datnbbook.dto.KhachHangDTO;
import org.example.datnbbook.model.DiaChi;
import org.example.datnbbook.model.KhachHang;
import org.example.datnbbook.repository.DiaChiRepository;
import org.example.datnbbook.repository.KhachHangRepository;
import org.example.datnbbook.service.KhachHangService;
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

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

            // Create or update addresses
            List<DiaChi> updatedAddresses = new ArrayList<>();

            // Check if there's any default address in the new list
            boolean hasDefaultAddress = khachHangDTO.getDanhSachDiaChi().stream()
                    .anyMatch(DiaChiDTO::getMacDinh);

            for (DiaChiDTO diaChiDTO : khachHangDTO.getDanhSachDiaChi()) {
                DiaChi diaChi;
                if (diaChiDTO.getId() != null) {
                    // Try to find existing address
                    Optional<DiaChi> existingAddress = existingAddresses.stream()
                            .filter(a -> a.getId().equals(diaChiDTO.getId()))
                            .findFirst();

                    if (existingAddress.isPresent()) {
                        diaChi = existingAddress.get();
                        diaChi.setDeleted(false);
                    } else {
                        diaChi = new DiaChi();
                    }
                } else {
                    diaChi = new DiaChi();
                }

                // Update address properties
                diaChi.setDiaChiChiTiet(diaChiDTO.getDiaChiChiTiet());
                diaChi.setTinhThanh(diaChiDTO.getTinhThanh());
                diaChi.setQuanHuyen(diaChiDTO.getQuanHuyen());
                diaChi.setXaPhuong(diaChiDTO.getXaPhuong());

                // Handle default address logic
                if (diaChiDTO.getMacDinh()) {
                    // If this address is set as default, set all other addresses to non-default
                    updatedAddresses.forEach(addr -> {
                        addr.setMacDinh(false);
                        diaChiRepository.save(addr);
                    });
                    diaChi.setMacDinh(true);
                } else {
                    // If no address is set as default in the list, make the first one default
                    diaChi.setMacDinh(!hasDefaultAddress && updatedAddresses.isEmpty());
                }

                diaChi.setKhachHang(existingKhachHang);

                // Save the address
                diaChi = diaChiRepository.save(diaChi);
                updatedAddresses.add(diaChi);
            }

            // Clear and add all addresses
            existingKhachHang.getDanhSachDiaChi().clear();
            existingKhachHang.getDanhSachDiaChi().addAll(updatedAddresses);
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

        // Generate customer code using SQL Server sequence
        String customerCode = generateCustomerCode();
        khachHang.setMaKhachHang(customerCode);

        // Save customer first to get ID
        KhachHang savedKhachHang = khachHangRepository.save(khachHang);

        // Create addresses
        if (dto.getDanhSachDiaChi() != null && !dto.getDanhSachDiaChi().isEmpty()) {
            List<DiaChi> addresses = dto.getDanhSachDiaChi().stream()
                    .map(diaChiDTO -> createDiaChiFromDTO(diaChiDTO, savedKhachHang))
                    .collect(Collectors.toList());

            diaChiRepository.saveAll(addresses);
            savedKhachHang.setDanhSachDiaChi(addresses);
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
        // Execute native query to get next sequence value
        Long seqVal = (Long) entityManager
                .createNativeQuery("SELECT NEXT VALUE FOR [dbo].[KHSeq]")
                .getSingleResult();

        // Format: KH00001, KH00002, etc.
        return String.format("KH%05d", seqVal);
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