
CREATE TABLE bo_sach (
                         id INT PRIMARY KEY IDENTITY(1,1),
                         ten_bo_sach NVARCHAR(255) NOT NULL,
                         gia_tien DECIMAL(10, 2) NOT NULL,
                         mo_ta TEXT NULL,
                         so_luong INT DEFAULT 0,
                         deleted BIT DEFAULT 0,

                         created_at DATETIME DEFAULT GETDATE(),
                         updated_at DATETIME DEFAULT GETDATE(),
                         created_by NVARCHAR(100) NULL,
                         updated_by NVARCHAR(100) NULL
);

CREATE SEQUENCE dbo.BSSeq
    START WITH 3
    INCREMENT BY 1;

ALTER TABLE dbo.bo_sach
    ADD ma_bo_sach NVARCHAR(20) DEFAULT 'BS' + RIGHT('-000' + CAST(NEXT VALUE FOR dbo.BSSeq AS NVARCHAR(7)), 5) NOT NULL;

CREATE TABLE bo_sach_chi_tiet (
                                  id_bo_sach INT NOT NULL,
                                  id_chi_tiet_san_pham INT NOT NULL,
                                  so_luong INT DEFAULT 1,
                                  deleted BIT DEFAULT 0,

                                  created_at DATETIME DEFAULT GETDATE(),
                                  updated_at DATETIME DEFAULT GETDATE(),
                                  created_by NVARCHAR(100) NULL,
                                  updated_by NVARCHAR(100) NULL,

                                  PRIMARY KEY (id_bo_sach, id_chi_tiet_san_pham),

                                  FOREIGN KEY (id_bo_sach) REFERENCES bo_sach(id) ON DELETE CASCADE,
                                  FOREIGN KEY (id_chi_tiet_san_pham) REFERENCES chi_tiet_san_pham(id) ON DELETE CASCADE
);
