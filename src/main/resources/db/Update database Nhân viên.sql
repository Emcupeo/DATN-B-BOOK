ALTER TABLE nhan_vien
    ADD dia_chi_chi_tiet NVARCHAR(255),
        email NVARCHAR(255),
        quan_huyen NVARCHAR(255),
        tinh_thanh NVARCHAR(255),
        xa_phuong NVARCHAR(255);


CREATE TABLE danh_muc (
                          id INT PRIMARY KEY IDENTITY(1,1),
                          ma_danh_muc NVARCHAR(50),
                          ten_danh_muc NVARCHAR(255) NOT NULL,
                          created_at DATETIME DEFAULT GETDATE(),
                          created_by NVARCHAR(100),
                          updated_at DATETIME,
                          updated_by NVARCHAR(100),
                          deleted BIT DEFAULT 0
);

ALTER TABLE san_pham
    ADD id_danh_muc INT;

ALTER TABLE san_pham
    ADD CONSTRAINT FK_san_pham_danh_muc
        FOREIGN KEY (id_danh_muc)
            REFERENCES danh_muc(id);

CREATE SEQUENCE dbo.DMSeq
    START WITH 1
    INCREMENT BY 1;

ALTER TABLE dbo.danh_muc
    ADD CONSTRAINT DF_danh_muc_ma
        DEFAULT ('DM-' + RIGHT('0000' + CAST(NEXT VALUE FOR dbo.DMSeq AS NVARCHAR(5)), 5))
        FOR ma_danh_muc;