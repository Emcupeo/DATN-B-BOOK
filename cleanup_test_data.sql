-- Script để xóa dữ liệu test cũ
USE BBook;

-- Xóa nhân viên test (chỉ chạy nếu cần thiết)
-- DELETE FROM nhan_vien WHERE ma_nhan_vien IN ('NV00000', 'NV00001', 'NV00002');

-- Xóa người dùng test (chỉ chạy nếu cần thiết)
-- DELETE FROM nguoi_dung WHERE ten_dang_nhap IN ('NV00000', 'NV00001', 'NV00002');

-- Kiểm tra dữ liệu hiện tại
SELECT '=== NHÂN VIÊN ===' as info;
SELECT 
    id,
    ma_nhan_vien,
    ten_tai_khoan,
    ho_ten,
    email,
    trang_thai,
    deleted
FROM nhan_vien 
ORDER BY ma_nhan_vien;

SELECT '=== NGƯỜI DÙNG ===' as info;
SELECT 
    id,
    ten_dang_nhap,
    ho_ten,
    email,
    trang_thai,
    deleted
FROM nguoi_dung 
WHERE ten_dang_nhap LIKE 'NV%'
ORDER BY ten_dang_nhap;

-- Để xóa dữ liệu test, bỏ comment các dòng DELETE ở trên
-- Và chạy lại script này 