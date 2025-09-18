import api from './api';

class ThongKeService {
  // Lấy sách mới nhất
  async getSachMoiNhat(limit = 5) {
    try {
      const response = await api.get(`/thong-ke/sach-moi-nhat?limit=${limit}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy sách mới nhất:', error);
      throw error;
    }
  }

  // Lấy bộ sách mới nhất
  async getBoSachMoiNhat(limit = 5) {
    try {
      const response = await api.get(`/thong-ke/bo-sach-moi-nhat?limit=${limit}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy bộ sách mới nhất:', error);
      throw error;
    }
  }

  // Lấy sách bán chạy nhất
  async getSachBanChay(limit = 5) {
    try {
      const response = await api.get(`/thong-ke/sach-ban-chay?limit=${limit}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy sách bán chạy:', error);
      throw error;
    }
  }

  // Lấy bộ sách bán chạy nhất
  async getBoSachBanChay(limit = 5) {
    try {
      const response = await api.get(`/thong-ke/bo-sach-ban-chay?limit=${limit}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi khi lấy bộ sách bán chạy:', error);
      throw error;
    }
  }

  // Lấy thống kê tổng quan
  async getThongKeTongQuan() {
    try {
      // Lấy tất cả dữ liệu cần thiết
      const [sachMoiNhat, boSachMoiNhat, sachBanChay, boSachBanChay] = await Promise.all([
        this.getSachMoiNhat(10),
        this.getBoSachMoiNhat(10),
        this.getSachBanChay(10),
        this.getBoSachBanChay(10)
      ]);

      return {
        sachMoiNhat,
        boSachMoiNhat,
        sachBanChay,
        boSachBanChay,
        tongSoSach: sachMoiNhat.length + boSachMoiNhat.length,
        tongSoSachBanChay: sachBanChay.length + boSachBanChay.length
      };
    } catch (error) {
      console.error('Lỗi khi lấy thống kê tổng quan:', error);
      throw error;
    }
  }
}

export default new ThongKeService();