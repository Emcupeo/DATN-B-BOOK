import ThongKeService from './ThongKeService';

export default {
  // Lấy danh sách sản phẩm bán chạy
  async getSanPhamBanChay() {
    try {
      // Lấy cả sách và bộ sách bán chạy
      const [sachBanChay, boSachBanChay] = await Promise.all([
        ThongKeService.getSachBanChay(100), // Lấy nhiều để có đủ dữ liệu
        ThongKeService.getBoSachBanChay(100)
      ]);
      
      // Kết hợp và format dữ liệu giống như API cũ
      const result = [];
      
      // Thêm sách bán chạy
      if (sachBanChay && Array.isArray(sachBanChay)) {
        sachBanChay.forEach(item => {
          result.push({
            id: item.id,
            soLuongBan: item.soLuongBan || 0,
            tenSanPham: item.tenSanPham || item.title,
            maSanPham: item.maSanPham || item.isbn,
            giaBan: item.giaBan || item.price,
            loai: 'Sản phẩm lẻ',
            hinhAnh: item.hinhAnh || item.image,
            doanhThu: item.doanhThu || 0
          });
        });
      }
      
      // Thêm bộ sách bán chạy
      if (boSachBanChay && Array.isArray(boSachBanChay)) {
        boSachBanChay.forEach(item => {
          result.push({
            id: item.id,
            soLuongBan: item.soLuongBan || 0,
            tenSanPham: item.tenSanPham || item.title,
            maSanPham: item.maSanPham || item.maBoSach,
            giaBan: item.giaBan || item.price,
            loai: 'Bộ sách',
            hinhAnh: item.hinhAnh || item.image,
            doanhThu: item.doanhThu || 0
          });
        });
      }
      
      return result;
    } catch (error) {
      console.error('Error fetching best selling products:', error);
      return [];
    }
  }
};
