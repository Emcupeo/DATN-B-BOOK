-- Kiểm tra dữ liệu với tên cột đúng
SELECT TOP 5
    id,
    ma_hoa_don,
    so_dien_thoai_nguoi_nhan,
    ten_nguoi_nhan
FROM hoa_don
WHERE so_dien_thoai_nguoi_nhan IS NOT NULL
ORDER BY id DESC;
