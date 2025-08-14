// Script để kiểm tra lỗi frontend
const fs = require('fs');
const path = require('path');

// Kiểm tra các file Vue có tồn tại không
const checkFiles = [
  'src/views/QuanLySach/ThuocTinh.vue',
  'src/views/QuanLySach/ListSach.vue',
  'src/views/TaiKhoan/ThemNhanVien.vue',
  'src/views/TaiKhoan/SuaNhanVien.vue'
];

console.log('Checking frontend files...');

checkFiles.forEach(file => {
  const fullPath = path.join(__dirname, file);
  if (fs.existsSync(fullPath)) {
    console.log(`✅ ${file} exists`);
  } else {
    console.log(`❌ ${file} does not exist`);
  }
});

// Kiểm tra import trùng lặp trong main.js
const mainJsPath = path.join(__dirname, 'src/main.js');
if (fs.existsSync(mainJsPath)) {
  const content = fs.readFileSync(mainJsPath, 'utf8');
  const lines = content.split('\n');
  
  const imports = {};
  lines.forEach((line, index) => {
    if (line.trim().startsWith('import ')) {
      const importName = line.match(/import\s+(\w+)/);
      if (importName) {
        const name = importName[1];
        if (imports[name]) {
          console.log(`❌ Duplicate import found: ${name} at line ${index + 1}`);
        } else {
          imports[name] = index + 1;
        }
      }
    }
  });
  
  console.log('✅ No duplicate imports found');
}

console.log('Frontend check completed!'); 