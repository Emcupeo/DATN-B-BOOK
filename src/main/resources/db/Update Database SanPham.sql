
-- Bước 1: Thêm cột vào bảng san_pham (nếu chưa có)
-- Kiểm tra kỹ để tránh lỗi khi cột đã tồn tại
IF COL_LENGTH('dbo.san_pham', 'created_at') IS NULL
    BEGIN
        ALTER TABLE dbo.san_pham
            ADD created_at DATETIME NULL;
    END;

IF COL_LENGTH('dbo.san_pham', 'created_by') IS NULL
    BEGIN
        ALTER TABLE dbo.san_pham
            ADD created_by VARCHAR(255) NULL;
    END;

IF COL_LENGTH('dbo.san_pham', 'updated_at') IS NULL
    BEGIN
        ALTER TABLE dbo.san_pham
            ADD updated_at DATETIME NULL;
    END;

IF COL_LENGTH('dbo.san_pham', 'updated_by') IS NULL
    BEGIN
        ALTER TABLE dbo.san_pham
            ADD updated_by VARCHAR(255) NULL;
    END;

-- Bước 2: Cập nhật dữ liệu từ chi_tiet_san_pham
UPDATE sp
SET sp.created_at = ct.created_at,
    sp.created_by = ct.created_by,
    sp.updated_at = ct.updated_at,
    sp.updated_by = ct.updated_by
FROM dbo.san_pham sp
         OUTER APPLY (
    SELECT TOP 1 created_at, created_by, updated_at, updated_by
    FROM dbo.chi_tiet_san_pham
    WHERE chi_tiet_san_pham.id_san_pham = sp.id
      AND deleted = 0 -- Chỉ lấy bản ghi chưa bị xóa
    ORDER BY updated_at DESC -- Lấy bản ghi mới nhất
) AS ct;
