# Hướng dẫn sửa lỗi DateTime trong Database

## Lỗi: "Schema-validation: wrong column type encountered in column [created_at]"

### Nguyên nhân:
- Entity sử dụng `Instant` (tương ứng với `datetimeoffset(6)` trong SQL Server)
- Database thực tế sử dụng `DATETIME`
- Hibernate validation phát hiện sự không khớp

### Các bước đã thực hiện:

#### 1. Sửa Entity BoSach.java
```java
@ColumnDefault("getdate()")
@Column(name = "created_at", columnDefinition = "DATETIME")
private Instant createdAt;

@ColumnDefault("getdate()")
@Column(name = "updated_at", columnDefinition = "DATETIME")
private Instant updatedAt;
```

#### 2. Sửa Entity BoSachChiTiet.java
```java
@ColumnDefault("getdate()")
@Column(name = "created_at", columnDefinition = "DATETIME")
private Instant createdAt;

@ColumnDefault("getdate()")
@Column(name = "updated_at", columnDefinition = "DATETIME")
private Instant updatedAt;
```

#### 3. Tạm thời tắt validation schema
```properties
spring.jpa.hibernate.ddl-auto=none
```

### Các bước tiếp theo:

#### A. Kiểm tra database
```sql
-- Chạy script này để kiểm tra kiểu dữ liệu
USE BBook;
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE COLUMN_NAME IN ('created_at', 'updated_at')
ORDER BY TABLE_NAME, COLUMN_NAME;
```

#### B. Sửa các entity khác nếu cần
Nếu có entity khác cũng gặp vấn đề tương tự, thêm `columnDefinition = "DATETIME"`:

```java
@Column(name = "created_at", columnDefinition = "DATETIME")
private Instant createdAt;

@Column(name = "updated_at", columnDefinition = "DATETIME")
private Instant updatedAt;
```

#### C. Kiểm tra và sửa database nếu cần
```sql
-- Nếu cần sửa kiểu dữ liệu trong database
ALTER TABLE bo_sach ALTER COLUMN created_at DATETIME;
ALTER TABLE bo_sach ALTER COLUMN updated_at DATETIME;
ALTER TABLE bo_sach_chi_tiet ALTER COLUMN created_at DATETIME;
ALTER TABLE bo_sach_chi_tiet ALTER COLUMN updated_at DATETIME;
```

#### D. Bật lại validation (sau khi sửa xong)
```properties
spring.jpa.hibernate.ddl-auto=validate
```

### Các file đã tạo:
- ✅ `fix_datetime_columns.sql` - Kiểm tra kiểu dữ liệu
- ✅ `fix_all_datetime_entities.sql` - Sửa tất cả entity
- ✅ `FIX_DATETIME_ISSUES.md` - Hướng dẫn này

### Test ứng dụng:
```bash
./mvnw spring-boot:run
```

Nếu vẫn gặp lỗi, hãy:
1. Chạy script kiểm tra database
2. Sửa các entity khác nếu cần
3. Cập nhật database nếu cần thiết 