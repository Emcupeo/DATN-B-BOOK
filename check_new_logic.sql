-- Kiểm tra logic tra cứu mới: mã hóa đơn + số điện thoại người nhận

-- 1. Xem tất cả hóa đơn với mã hóa đơn và số điện thoại người nhận
SELECT TOP 10
    hd.id,
    hd.maHoaDon,
    hd.soDienThoaiNguoiNhan,
    hd.tenNguoiNhan,
    hd.ngayDatHang,
    hd.tongTien,
    hd.trangThai
FROM hoa_don hd
WHERE hd.soDienThoaiNguoiNhan IS NOT NULL
ORDER BY hd.id DESC;

-- 2. Tìm hóa đơn với mã HD00148 và số điện thoại người nhận
SELECT 
    hd.id,
    hd.maHoaDon,
    hd.soDienThoaiNguoiNhan,
    hd.tenNguoiNhan,
    hd.ngayDatHang,
    hd.tongTien,
    hd.trangThai
FROM hoa_don hd
WHERE hd.maHoaDon = 'HD00148' 
  AND hd.soDienThoaiNguoiNhan = '0825144147';

-- 3. Xem tất cả mã hóa đơn có trong hệ thống
SELECT DISTINCT maHoaDon 
FROM hoa_don 
WHERE maHoaDon IS NOT NULL
ORDER BY maHoaDon;

-- 4. Xem tất cả số điện thoại người nhận có trong hệ thống
SELECT DISTINCT soDienThoaiNguoiNhan, tenNguoiNhan
FROM hoa_don
WHERE soDienThoaiNguoiNhan IS NOT NULL
ORDER BY soDienThoaiNguoiNhan;

-- 5. Đếm tổng số hóa đơn có số điện thoại người nhận
SELECT COUNT(*) as total_orders_with_phone 
FROM hoa_don 
WHERE soDienThoaiNguoiNhan IS NOT NULL;
