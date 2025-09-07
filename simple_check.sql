-- Kiểm tra đơn hàng đơn giản
SELECT TOP 5
    hd.id,
    hd.maHoaDon,
    kh.soDienThoai,
    kh.hoTen
FROM HoaDon hd
LEFT JOIN KhachHang kh ON hd.khachHangId = kh.id
ORDER BY hd.id DESC;

-- Tìm đơn hàng HD00148
SELECT 
    hd.id,
    hd.maHoaDon,
    kh.soDienThoai,
    kh.hoTen
FROM HoaDon hd
LEFT JOIN KhachHang kh ON hd.khachHangId = kh.id
WHERE hd.maHoaDon = 'HD00148';
