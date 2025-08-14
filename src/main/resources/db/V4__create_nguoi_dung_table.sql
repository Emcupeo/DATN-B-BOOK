-- Tạo sequence cho mã người dùng
IF NOT EXISTS (SELECT * FROM sys.sequences WHERE name = 'NDUserSeq')
    CREATE SEQUENCE [dbo].[NDUserSeq] 
    AS [int]
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 2147483647
    CACHE;

-- Tạo bảng nguoi_dung
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'nguoi_dung')
CREATE TABLE [dbo].[nguoi_dung] (
    [id] BIGINT IDENTITY(1,1) NOT NULL,
    [ma_nguoi_dung] NVARCHAR(20) NOT NULL,
    [ten_dang_nhap] NVARCHAR(50) NOT NULL,
    [mat_khau] NVARCHAR(255) NOT NULL,
    [email] NVARCHAR(100) NULL,
    [ho_ten] NVARCHAR(100) NULL,
    [so_dien_thoai] NVARCHAR(15) NULL,
    [loai_nguoi_dung] NVARCHAR(20) NOT NULL CHECK ([loai_nguoi_dung] IN ('ADMIN', 'NHAN_VIEN', 'KHACH_HANG')),
    [trang_thai] BIT DEFAULT 1,
    [created_at] DATETIME2 DEFAULT GETDATE(),
    [created_by] NVARCHAR(100) NULL,
    [updated_at] DATETIME2 NULL,
    [updated_by] NVARCHAR(100) NULL,
    [deleted] BIT DEFAULT 0,
    [khach_hang_id] BIGINT NULL,
    [nhan_vien_id] INT NULL,
    
    CONSTRAINT [PK_nguoi_dung] PRIMARY KEY ([id]),
    CONSTRAINT [UK_nguoi_dung_ma] UNIQUE ([ma_nguoi_dung]),
    CONSTRAINT [UK_nguoi_dung_username] UNIQUE ([ten_dang_nhap]),
    CONSTRAINT [UK_nguoi_dung_email] UNIQUE ([email])
);

-- Tạo indexes để tối ưu performance
CREATE INDEX [IX_nguoi_dung_loai] ON [dbo].[nguoi_dung] ([loai_nguoi_dung]);
CREATE INDEX [IX_nguoi_dung_trang_thai] ON [dbo].[nguoi_dung] ([trang_thai]);
CREATE INDEX [IX_nguoi_dung_deleted] ON [dbo].[nguoi_dung] ([deleted]);
CREATE INDEX [IX_nguoi_dung_khach_hang] ON [dbo].[nguoi_dung] ([khach_hang_id]);
CREATE INDEX [IX_nguoi_dung_nhan_vien] ON [dbo].[nguoi_dung] ([nhan_vien_id]);

-- Insert tài khoản admin mặc định
IF NOT EXISTS (SELECT * FROM [dbo].[nguoi_dung] WHERE [ten_dang_nhap] = 'admin')
INSERT INTO [dbo].[nguoi_dung] 
    ([ma_nguoi_dung], [ten_dang_nhap], [mat_khau], [email], [ho_ten], [loai_nguoi_dung], [trang_thai], [deleted], [created_at])
VALUES 
    ('ND' + RIGHT('00000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[NDUserSeq]), 5), 
     'admin', 
     'admin123', 
     'admin@bbook.com', 
     'Administrator', 
     'ADMIN', 
     1, 
     0, 
     GETDATE());

-- Insert một vài tài khoản demo (tùy chọn)
IF NOT EXISTS (SELECT * FROM [dbo].[nguoi_dung] WHERE [ten_dang_nhap] = 'nhanvien1')
INSERT INTO [dbo].[nguoi_dung] 
    ([ma_nguoi_dung], [ten_dang_nhap], [mat_khau], [email], [ho_ten], [loai_nguoi_dung], [trang_thai], [deleted], [created_at])
VALUES 
    ('ND' + RIGHT('00000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[NDUserSeq]), 5), 
     'nhanvien1', 
     '123456', 
     'nhanvien1@bbook.com', 
     'Nhân viên Demo', 
     'NHAN_VIEN', 
     1, 
     0, 
     GETDATE());

IF NOT EXISTS (SELECT * FROM [dbo].[nguoi_dung] WHERE [ten_dang_nhap] = 'khachhang1')
INSERT INTO [dbo].[nguoi_dung] 
    ([ma_nguoi_dung], [ten_dang_nhap], [mat_khau], [email], [ho_ten], [loai_nguoi_dung], [trang_thai], [deleted], [created_at])
VALUES 
    ('ND' + RIGHT('00000' + CONVERT(NVARCHAR(5), NEXT VALUE FOR [dbo].[NDUserSeq]), 5), 
     'khachhang1', 
     '123456', 
     'khachhang1@bbook.com', 
     'Khách hàng Demo', 
     'KHACH_HANG', 
     1, 
     0, 
     GETDATE());