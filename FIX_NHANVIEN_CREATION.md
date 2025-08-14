# Hướng dẫn sửa lỗi tạo nhân viên

## Lỗi: "Tên đăng nhập NV00001 đã tồn tại"

### Nguyên nhân:
- Logic tạo mã nhân viên không kiểm tra mã đã tồn tại
- Có thể có dữ liệu test cũ trong database
- Logic `generateSafeMaNhanVien()` chưa đủ robust

### Đã sửa:

#### 1. Cải thiện logic `generateSafeMaNhanVien()`:
```java
private String generateSafeMaNhanVien() {
    String maxMaNhanVien = nhanVienRepository.getMaxMaNhanVien();
    int nextNumber = 1;
    
    if (maxMaNhanVien != null && maxMaNhanVien.startsWith("NV")) {
        try {
            String numberStr = maxMaNhanVien.substring(2);
            int currentNumber = Integer.parseInt(numberStr);
            nextNumber = Math.max(currentNumber + 1, 1);
        } catch (NumberFormatException e) {
            nextNumber = 1;
        }
    }
    
    if (nextNumber == 0) {
        nextNumber = 1;
    }
    
    // Tạo mã và kiểm tra xem đã tồn tại chưa
    String candidateMaNhanVien = String.format("NV%05d", nextNumber);
    
    // Kiểm tra xem mã này đã tồn tại chưa (cả trong nhan_vien và nguoi_dung)
    while (nhanVienRepository.existsByMaNhanVien(candidateMaNhanVien) || 
           authService.existsByTenDangNhap(candidateMaNhanVien)) {
        nextNumber++;
        candidateMaNhanVien = String.format("NV%05d", nextNumber);
        
        if (nextNumber > 99999) {
            throw new RuntimeException("Đã đạt giới hạn số lượng nhân viên (99999)");
        }
    }
    
    return candidateMaNhanVien;
}
```

#### 2. Cải thiện logic `create()`:
```java
@Override
public NhanVienDTO create(NhanVienDTO nhanVienDTO) {
    // Xóa dữ liệu NV00000 nếu có
    cleanupInvalidData();
    
    // Tạo mã nhân viên an toàn
    String maNhanVien = generateSafeMaNhanVien();
    
    // Kiểm tra xem tên đăng nhập đã tồn tại chưa
    if (authService.existsByTenDangNhap(maNhanVien)) {
        throw new RuntimeException("Tên đăng nhập " + maNhanVien + " đã tồn tại");
    }
    
    // ... rest of the method
}
```

### Các bước kiểm tra:

#### 1. Kiểm tra database:
```sql
-- Chạy script check_nv00001.sql để kiểm tra dữ liệu
USE BBook;

SELECT 
    id,
    ma_nhan_vien,
    ten_tai_khoan,
    ho_ten,
    email,
    trang_thai,
    deleted
FROM nhan_vien 
WHERE ma_nhan_vien = 'NV00001' OR ten_tai_khoan = 'NV00001';
```

#### 2. Xóa dữ liệu test nếu cần:
```sql
-- Chạy script cleanup_test_data.sql
-- Bỏ comment các dòng DELETE nếu cần xóa dữ liệu test
```

#### 3. Test logic tạo mã:
```bash
# Chạy TestMaNhanVienLogic.java để test logic
javac TestMaNhanVienLogic.java
java TestMaNhanVienLogic
```

### Các file đã sửa:
- ✅ `NhanVienServiceImpl.java` - Cải thiện logic tạo mã nhân viên
- ✅ `check_nv00001.sql` - Script kiểm tra dữ liệu
- ✅ `cleanup_test_data.sql` - Script xóa dữ liệu test
- ✅ `TestMaNhanVienLogic.java` - Test logic tạo mã

### Test ứng dụng:
```bash
# Chạy Spring Boot
./mvnw spring-boot:run

# Test tạo nhân viên mới
curl -X POST http://localhost:8080/api/nhan-vien \
  -H "Content-Type: application/json" \
  -d '{
    "hoTen": "Test Employee",
    "email": "test@example.com",
    "soDienThoai": "0123456789"
  }'
```

### Kết quả mong đợi:
- ✅ Logic tạo mã nhân viên an toàn
- ✅ Không tạo ra mã đã tồn tại
- ✅ Tự động tìm mã tiếp theo nếu mã hiện tại đã tồn tại
- ✅ Xử lý lỗi gracefully

### Nếu vẫn gặp lỗi:
1. Chạy script kiểm tra database
2. Xóa dữ liệu test nếu cần
3. Restart ứng dụng
4. Test lại tạo nhân viên 