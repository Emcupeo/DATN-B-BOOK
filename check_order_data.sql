-- Kiểm tra dữ liệu đơn hàng trong database
-- 1. Xem tất cả đơn hàng với mã đơn hàng và số điện thoại
SELECT 
    hd.id,
    hd.maHoaDon,
    hd.ngayDatHang,
    hd.tongTien,
    hd.trangThai,
    kh.hoTen,
    kh.soDienThoai,
    kh.email
FROM HoaDon hd
LEFT JOIN KhachHang kh ON hd.khachHangId = kh.id
ORDER BY hd.id DESC;

-- 2. Tìm đơn hàng với mã HD00148
SELECT 
    hd.id,
    hd.maHoaDon,
    hd.ngayDatHang,
    hd.tongTien,
    hd.trangThai,
    kh.hoTen,
    kh.soDienThoai,
    kh.email
FROM HoaDon hd
LEFT JOIN KhachHang kh ON hd.khachHangId = kh.id
WHERE hd.maHoaDon = 'HD00148';

-- 3. Tìm đơn hàng với số điện thoại 0825144147
SELECT 
    hd.id,
    hd.maHoaDon,
    hd.ngayDatHang,
    hd.tongTien,
    hd.trangThai,
    kh.hoTen,
    kh.soDienThoai,
    kh.email
FROM HoaDon hd
LEFT JOIN KhachHang kh ON hd.khachHangId = kh.id
WHERE kh.soDienThoai = '0825144147';

-- 4. Tìm đơn hàng với mã HD00148 VÀ số điện thoại 0825144147
SELECT 
    hd.id,
    hd.maHoaDon,
    hd.ngayDatHang,
    hd.tongTien,
    hd.trangThai,
    kh.hoTen,
    kh.soDienThoai,
    kh.email
FROM HoaDon hd
LEFT JOIN KhachHang kh ON hd.khachHangId = kh.id
WHERE hd.maHoaDon = 'HD00148' 
  AND kh.soDienThoai = '0825144147';

-- 5. Xem tất cả mã đơn hàng có trong hệ thống
SELECT DISTINCT maHoaDon 
FROM HoaDon 
ORDER BY maHoaDon;

-- 6. Xem tất cả số điện thoại có trong hệ thống
SELECT DISTINCT kh.soDienThoai, kh.hoTen
FROM KhachHang kh
WHERE kh.soDienThoai IS NOT NULL
ORDER BY kh.soDienThoai;

-- 7. Đếm tổng số đơn hàng
SELECT COUNT(*) as total_orders FROM HoaDon;

-- 8. Đếm tổng số khách hàng
SELECT COUNT(*) as total_customers FROM KhachHang;
