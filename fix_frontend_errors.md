# Hướng dẫn sửa lỗi Frontend Vue.js

## Các lỗi đã gặp:

### 1. **Import không sử dụng**
**Lỗi**: `'useShopStore' is defined but never used`

**Đã sửa**: Xóa import không sử dụng trong `Header.vue`
```javascript
// Đã xóa dòng này:
// import { useShopStore } from '../store'
```

### 2. **Import trùng lặp**
**Lỗi**: `Identifier 'ThemNhanVien' has already been declared`

**Đã sửa**: Xóa import trùng lặp trong `main.js`
```javascript
// Đã xóa các dòng trùng lặp:
// import ThemNhanVien from "@/views/TaiKhoan/ThemNhanVien.vue";
// import SuaNhanVien from "@/views/TaiKhoan/SuaNhanVien.vue";
```

### 3. **Module không tìm thấy**
**Lỗi**: `Can't resolve '@/views/QuanLySach/ThuocTinh.vue'`

**Giải pháp**: Các file này không tồn tại, có thể đã được sửa hoặc không cần thiết.

## Các bước kiểm tra:

### 1. Kiểm tra tất cả import không sử dụng
```bash
# Chạy linter để tìm import không sử dụng
npm run lint
```

### 2. Kiểm tra file tồn tại
```bash
# Kiểm tra các file Vue có tồn tại không
ls src/views/QuanLySach/
ls src/views/TaiKhoan/
```

### 3. Sửa lỗi ESLint
Nếu có lỗi ESLint, có thể tạm thời disable:
```javascript
// eslint-disable-next-line no-unused-vars
import { useShopStore } from '../store'
```

## Các file đã sửa:
- ✅ `src/views/shop/components/Header.vue` - Xóa import không sử dụng
- ✅ `src/main.js` - Xóa import trùng lặp

## Test ứng dụng:
```bash
# Chạy development server
npm run serve

# Hoặc build production
npm run build
```

## Nếu vẫn gặp lỗi:
1. Xóa thư mục `node_modules` và `package-lock.json`
2. Chạy `npm install`
3. Chạy `npm run serve` 