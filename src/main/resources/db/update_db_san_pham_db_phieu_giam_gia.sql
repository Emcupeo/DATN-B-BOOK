
ALTER TABLE chi_tiet_san_pham DROP COLUMN anh;
GO
-- Tạo bảng anh_san_pham để lưu trữ ảnh
CREATE TABLE anh_san_pham (
                              id INT IDENTITY(1,1) PRIMARY KEY,
                              url NVARCHAR(500) NOT NULL
);

Go

-- Tạo bảng trung gian để thiết lập mối quan hệ nhiều - nhiều
CREATE TABLE chi_tiet_san_pham_anh (
                                       chi_tiet_san_pham_id INT NOT NULL,
                                       anh_id INT NOT NULL,
                                       PRIMARY KEY (chi_tiet_san_pham_id, anh_id),
                                       FOREIGN KEY (chi_tiet_san_pham_id) REFERENCES chi_tiet_san_pham(id) ON DELETE CASCADE,
                                       FOREIGN KEY (anh_id) REFERENCES anh_san_pham(id) ON DELETE CASCADE
);
Go
ALTER TABLE anh_san_pham ADD id_loai_bia INT NULL;
Go
-- Thiết lập khóa ngoại từ anh_san_pham đến loai_bia
ALTER TABLE anh_san_pham
    ADD CONSTRAINT fk_anh_san_pham_loai_bia
        FOREIGN KEY (id_loai_bia) REFERENCES loai_bia(id) ON DELETE SET NULL;
Go

ALTER TABLE anh_san_pham
    ADD CONSTRAINT uk_anh_san_pham_url_loai_bia UNIQUE (url, id_loai_bia);
Go




ALTER TABLE phieu_giam_gia
    add ten_phieu_giam_gia nvarchar(255)
Go

-- Xóa cột tính toán hiện tại nếu có
ALTER TABLE [dbo].[phieu_giam_gia] DROP COLUMN [tinh_trang]
    GO

-- Thêm lại cột tính toán với logic mới
ALTER TABLE [dbo].[phieu_giam_gia]
    ADD [tinh_trang] AS (
    CASE
    WHEN [trang_thai] = 0 THEN N'Đã kết thúc'
    WHEN [trang_thai] = 1 AND [ngay_bat_dau] > GETDATE() THEN N'Chưa bắt đầu'
    WHEN [trang_thai] = 1 AND [ngay_bat_dau] <= GETDATE() AND [ngay_ket_thuc] >= GETDATE() THEN N'Đang diễn ra'
    WHEN [trang_thai] = 1 AND [ngay_ket_thuc] < GETDATE() THEN N'Đã kết thúc'
    ELSE N'Không xác định'
    END
    )
    GO
Alter table phieu_giam_gia add loai_ap_dung nvarchar(255)
Go
Alter table phieu_giam_gia add loai_phieu nvarchar(255)
Go
ALTER TABLE phieu_giam_gia
ALTER COLUMN ma_phieu_giam_gia VARCHAR(50);
