-- Tạo bảng dot_giam_gia_bo_sach_chi_tiet
CREATE TABLE dot_giam_gia_bo_sach_chi_tiet (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    id_bo_sach INT NOT NULL,
    id_dot_giam_gia INT NOT NULL,
    so_tien_giam DECIMAL(10,2) NULL,
    so_phan_tram_giam DECIMAL(5,2) NULL,
    gia_sau_giam DECIMAL(10,2) NULL,
    gia_ban_dau DECIMAL(10,2) NULL,
    trang_thai BIT NOT NULL DEFAULT 1,
    deleted BIT NOT NULL DEFAULT 0,
    created_at DATETIME2 DEFAULT GETDATE(),
    created_by NVARCHAR(50) NULL,
    updated_at DATETIME2 DEFAULT GETDATE(),
    updated_by NVARCHAR(50) NULL,
    
    CONSTRAINT fk_bo_sach FOREIGN KEY (id_bo_sach) REFERENCES bo_sach(id),
    CONSTRAINT fk_dot_giam_gia FOREIGN KEY (id_dot_giam_gia) REFERENCES dot_giam_gia(id)
);

GO

-- Tạo index để tối ưu performance
CREATE INDEX idx_dot_giam_gia_bo_sach_chi_tiet_bo_sach ON dot_giam_gia_bo_sach_chi_tiet(id_bo_sach);
CREATE INDEX idx_dot_giam_gia_bo_sach_chi_tiet_dot_giam_gia ON dot_giam_gia_bo_sach_chi_tiet(id_dot_giam_gia);
CREATE INDEX idx_dot_giam_gia_bo_sach_chi_tiet_deleted ON dot_giam_gia_bo_sach_chi_tiet(deleted);
