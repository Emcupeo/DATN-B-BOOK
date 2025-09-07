-- Kiểm tra dữ liệu sản phẩm trong hóa đơn HD00148
SELECT 
    hd.id as hoa_don_id,
    hd.maHoaDon,
    hdct.id as chi_tiet_id,
    hdct.soLuong,
    hdct.giaSanPham,
    hdct.thanhTien,
    sp.tenSanPham,
    csp.id as chi_tiet_san_pham_id
FROM hoa_don hd
LEFT JOIN hoa_don_chi_tiet hdct ON hd.id = hdct.id_hoa_don
LEFT JOIN chi_tiet_san_pham csp ON hdct.id_chi_tiet_san_pham = csp.id
LEFT JOIN san_pham sp ON csp.id_san_pham = sp.id
WHERE hd.maHoaDon = 'HD00148';
