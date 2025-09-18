import api from './api';

class ThongKeDoanhThuService {
  // Lấy thống kê tổng quan
  async getThongKeTongQuan() {
    try {
      const response = await api.get('/thong-ke-doanh-thu/tong-quan');
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy thống kê tổng quan:', error);
      throw error;
    }
  }

  // Lấy doanh thu theo ngày (7 ngày gần nhất)
  async getDoanhThuTheoNgay() {
    try {
      const response = await api.get('/thong-ke-doanh-thu/doanh-thu-theo-ngay');
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy doanh thu theo ngày:', error);
      throw error;
    }
  }

  // Lấy doanh thu theo tháng (6 tháng gần nhất)
  async getDoanhThuTheoThang() {
    try {
      const response = await api.get('/thong-ke-doanh-thu/doanh-thu-theo-thang');
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy doanh thu theo tháng:', error);
      throw error;
    }
  }

  // Lấy top đơn hàng có giá trị cao nhất
  async getTopDonHangCao() {
    try {
      const response = await api.get('/thong-ke-doanh-thu/top-don-hang-cao');
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy top đơn hàng:', error);
      throw error;
    }
  }

  // Lấy thống kê đơn hàng theo trạng thái
  async getDonHangTheoTrangThai() {
    try {
      const response = await api.get('/thong-ke-doanh-thu/don-hang-theo-trang-thai');
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy thống kê đơn hàng theo trạng thái:', error);
      throw error;
    }
  }

  // Lấy sản phẩm bán chạy
  async getSanPhamBanChay() {
    try {
      const response = await api.get('/thong-ke-doanh-thu/san-pham-ban-chay');
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy sản phẩm bán chạy:', error);
      throw error;
    }
  }


  // Lấy doanh thu theo năm
  async getDoanhThuTheoNam() {
    try {
      const response = await api.get('/thong-ke-doanh-thu/doanh-thu-theo-nam');
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy doanh thu theo năm:', error);
      throw error;
    }
  }

  // Lấy thống kê theo thời gian (ngày/tuần/tháng/năm)
  async getThongKeTheoThoiGian(loai = 'ngay') {
    try {
      const response = await api.get(`/thong-ke-doanh-thu/thong-ke-theo-thoi-gian?loai=${loai}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy thống kê theo thời gian:', error);
      throw error;
    }
  }

  // So sánh doanh thu
  async getSoSanhDoanhThu(loai = 'thang') {
    try {
      const response = await api.get(`/thong-ke-doanh-thu/so-sanh-doanh-thu?loai=${loai}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy so sánh doanh thu:', error);
      throw error;
    }
  }


  // Lấy tất cả dữ liệu thống kê
  async getAllThongKe() {
    try {
      const [
        tongQuan,
        doanhThuNgay,
        doanhThuThang,
        doanhThuNam,
        topDonHang,
        donHangTrangThai,
        sanPhamBanChay,
        soSanhThang
      ] = await Promise.all([
        this.getThongKeTongQuan(),
        this.getDoanhThuTheoNgay(),
        this.getDoanhThuTheoThang(),
        this.getDoanhThuTheoNam(),
        this.getTopDonHangCao(),
        this.getDonHangTheoTrangThai(),
        this.getSanPhamBanChay(),
        this.getSoSanhDoanhThu('thang')
      ]);

      return {
        tongQuan,
        doanhThuNgay,
        doanhThuThang,
        doanhThuNam,
        topDonHang,
        donHangTrangThai,
        sanPhamBanChay,
        soSanhThang
      };
    } catch (error) {
      console.error('Lỗi khi lấy tất cả thống kê:', error);
      throw error;
    }
  }
}

export default new ThongKeDoanhThuService();
