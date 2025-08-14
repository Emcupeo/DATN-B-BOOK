# Hướng dẫn sửa lỗi Vue.js Dependencies

## Lỗi: "Cannot find module 'crypto-browserify'"

### Nguyên nhân:
- Vue.js project sử dụng Node.js modules trong browser environment
- Thiếu các polyfills cần thiết cho browser

### Đã sửa:

#### 1. Cài đặt dependencies cần thiết:
```bash
npm install crypto-browserify path-browserify assert buffer process stream-browserify util --save-dev --legacy-peer-deps
```

#### 2. Cấu hình webpack trong vue.config.js:
```javascript
const path = require('path');

module.exports = {
  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src'),
      },
      fallback: {
        "crypto": require.resolve("crypto-browserify"),
        "path": require.resolve("path-browserify"),
        "assert": require.resolve("assert"),
        "buffer": require.resolve("buffer"),
        "process": require.resolve("process/browser"),
        "stream": require.resolve("stream-browserify"),
        "util": require.resolve("util")
      }
    },
    plugins: [
      new (require('webpack')).ProvidePlugin({
        process: 'process/browser',
        Buffer: ['buffer', 'Buffer']
      })
    ]
  },
  devServer: {
    port: 3000,
  },
};
```

### Các bước đã thực hiện:

#### A. Cài đặt polyfills:
- ✅ `crypto-browserify` - Polyfill cho crypto module
- ✅ `path-browserify` - Polyfill cho path module
- ✅ `assert` - Polyfill cho assert module
- ✅ `buffer` - Polyfill cho buffer module
- ✅ `process` - Polyfill cho process module
- ✅ `stream-browserify` - Polyfill cho stream module
- ✅ `util` - Polyfill cho util module

#### B. Sử dụng --legacy-peer-deps:
- Giải quyết xung đột dependency giữa Vue 3.0.7 và Pinia 3.0.3
- Cho phép cài đặt các dependencies cần thiết

### Test ứng dụng:
```bash
# Chạy development server
npm run serve

# Hoặc build production
npm run build
```

### Nếu vẫn gặp lỗi:

#### 1. Xóa node_modules và cài lại:
```bash
rm -rf node_modules package-lock.json
npm install --legacy-peer-deps
```

#### 2. Kiểm tra version conflicts:
```bash
npm ls vue pinia
```

#### 3. Update Vue version nếu cần:
```bash
npm install vue@^3.5.11 --save
```

### Các file đã sửa:
- ✅ `vue.config.js` - Cấu hình webpack polyfills
- ✅ `package.json` - Thêm dependencies cần thiết

### Kết quả:
- ✅ Đã cài đặt tất cả polyfills cần thiết
- ✅ Đã giải quyết xung đột dependency
- ✅ Vue app có thể chạy được 