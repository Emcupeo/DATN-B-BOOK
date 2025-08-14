-- Script để kiểm tra nhân viên NV00001
USE BBook;

-- Kiểm tra nhân viên có mã NV00001
SELECT 
    id,
    ma_nhan_vien,
    ten_tai_khoan,
    ho_ten,
    email,
    trang_thai,
    deleted
FROM nhan_vien 
WHERE ma_nhan_vien = 'NV00001' OR ten_tai_khoan = 'NV00001';

-- Kiểm tra người dùng có tên đăng nhập NV00001
SELECT 
    id,
    ten_dang_nhap,
    ho_ten,
    email,
    trang_thai,
    deleted
FROM nguoi_dung 
WHERE ten_dang_nhap = 'NV00001';

-- Kiểm tra tất cả nhân viên để xem pattern
SELECT 
    ma_nhan_vien,
    ten_tai_khoan,
    ho_ten,
    trang_thai,
    deleted
FROM nhan_vien 
WHERE deleted = 0
ORDER BY ma_nhan_vien;

-- Kiểm tra max mã nhân viên
SELECT MAX(ma_nhan_vien) as max_ma_nhan_vien
FROM nhan_vien 
WHERE ma_nhan_vien LIKE 'NV%' 
  AND LEN(ma_nhan_vien) = 7 
  AND ISNUMERIC(SUBSTRING(ma_nhan_vien, 3, 5)) = 1; 