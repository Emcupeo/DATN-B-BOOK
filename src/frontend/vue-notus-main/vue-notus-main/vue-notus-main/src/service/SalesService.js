import api from './api';

export default {
  // Lấy danh sách sản phẩm bán chạy từ API thống kê
  async getSanPhamBanChay() {
    try {
      // Gọi trực tiếp API thống kê như admin
      const response = await api.get('/thong-ke-doanh-thu/san-pham-ban-chay');
      console.log('[DEBUG] Raw API response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error fetching best selling products:', error);
      return [];
    }
  }
};
