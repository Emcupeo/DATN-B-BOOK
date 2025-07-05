# Tính năng Quản lý Danh mục

## Tổng quan
Tính năng quản lý danh mục cho phép admin tạo, chỉnh sửa, xóa và quản lý các danh mục sản phẩm trong hệ thống.

## Cấu trúc Database

### Bảng `danh_muc`
```sql
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
```

### Quan hệ với bảng `san_pham`
- Thêm cột `id_danh_muc INT` vào bảng `san_pham`
- Foreign key constraint: `FK_san_pham_danh_muc`

## Backend API

### Endpoints
- `GET /api/danh-muc` - Lấy tất cả danh mục
- `GET /api/danh-muc/{id}` - Lấy danh mục theo ID
- `POST /api/danh-muc` - Tạo danh mục mới
- `PUT /api/danh-muc/{id}` - Cập nhật danh mục
- `DELETE /api/danh-muc/{id}` - Xóa danh mục (soft delete)
- `GET /api/danh-muc/search?keyword={keyword}` - Tìm kiếm danh mục
- `GET /api/danh-muc/active` - Lấy danh sách danh mục active

### Files đã tạo/cập nhật
1. **Model**: `src/main/java/org/example/datnbbook/model/DanhMuc.java`
2. **DTO**: `src/main/java/org/example/datnbbook/dto/DanhMucDTO.java`
3. **Repository**: `src/main/java/org/example/datnbbook/repository/DanhMucRepository.java`
4. **Service**: `src/main/java/org/example/datnbbook/service/DanhMucService.java`
5. **Controller**: `src/main/java/org/example/datnbbook/controller/DanhMucController.java`
6. **Migration**: `src/main/resources/db/migration/V4__add_danh_muc_table.sql`

## Frontend

### Files đã tạo/cập nhật
1. **Model**: `src/frontend/vue-notus-main/vue-notus-main/vue-notus-main/src/models/DanhMuc.js`
2. **Service**: `src/frontend/vue-notus-main/vue-notus-main/vue-notus-main/src/service/DanhMucService.js`
3. **Component**: `src/frontend/vue-notus-main/vue-notus-main/vue-notus-main/src/views/QuanLySach/DanhMucSach.vue`
4. **Router**: Cập nhật `src/frontend/vue-notus-main/vue-notus-main/vue-notus-main/src/main.js`
5. **Sidebar**: Cập nhật `src/frontend/vue-notus-main/vue-notus-main/vue-notus-main/src/components/Sidebar/Sidebar.vue`

### Tính năng
- Hiển thị danh sách danh mục với phân trang
- Tìm kiếm danh mục theo tên hoặc mã
- Sắp xếp theo tên hoặc mã danh mục
- Thêm danh mục mới
- Chỉnh sửa danh mục
- Xóa danh mục (soft delete)

## Cách sử dụng

### Truy cập trang quản lý danh mục
1. Đăng nhập vào admin panel
2. Vào menu "Quản lý sách" > "Thuộc tính sách" > "Danh mục"
3. Hoặc truy cập trực tiếp: `/admin/danh-muc`

### Thêm danh mục mới
1. Nhập tên danh mục vào form bên trái
2. Click "Thêm mới"
3. Mã danh mục sẽ được tự động tạo theo format: `DM-00001`, `DM-00002`, ...

### Chỉnh sửa danh mục
1. Click nút "Sửa" bên cạnh danh mục cần chỉnh sửa
2. Thay đổi thông tin trong form
3. Click "Cập nhật"

### Xóa danh mục
1. Click nút "Xoá" bên cạnh danh mục cần xóa
2. Xác nhận trong hộp thoại
3. Danh mục sẽ được đánh dấu xóa (soft delete)

## Tích hợp với sản phẩm
- Khi tạo sản phẩm mới, có thể chọn danh mục cho sản phẩm
- Thông tin danh mục sẽ được hiển thị trong chi tiết sản phẩm
- Có thể lọc sản phẩm theo danh mục

## Lưu ý
- Mã danh mục được tự động tạo và không thể chỉnh sửa
- Danh mục bị xóa sẽ không hiển thị trong danh sách nhưng vẫn tồn tại trong database
- Cần chạy migration `V4__add_danh_muc_table.sql` để tạo bảng và quan hệ 