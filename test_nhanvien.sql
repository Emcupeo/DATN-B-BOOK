-- Kiểm tra xem có dữ liệu NV00000 không
SELECT * FROM nhan_vien WHERE ma_nhan_vien = 'NV00000';

-- Kiểm tra xem có dữ liệu NV00000 trong bảng nguoi_dung không
SELECT * FROM nguoi_dung WHERE ten_dang_nhap = 'NV00000';

-- Xóa dữ liệu NV00000 nếu có (chỉ chạy nếu cần)
-- DELETE FROM nguoi_dung WHERE ten_dang_nhap = 'NV00000';
-- DELETE FROM nhan_vien WHERE ma_nhan_vien = 'NV00000';

-- Kiểm tra mã nhân viên lớn nhất hiện tại
SELECT MAX(ma_nhan_vien) FROM nhan_vien WHERE ma_nhan_vien LIKE 'NV%' AND LEN(ma_nhan_vien) = 7 AND ISNUMERIC(SUBSTRING(ma_nhan_vien, 3, 5)) = 1;

-- Kiểm tra tất cả mã nhân viên hiện tại
SELECT ma_nhan_vien FROM nhan_vien WHERE ma_nhan_vien LIKE 'NV%' ORDER BY ma_nhan_vien; 