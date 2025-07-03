import api from './api';
import SanPham from '@/models/SanPham';

class SanPhamService {
  async getAll() {
    try {
      const response = await api.get('/san-pham');
      console.log("[DEBUG] Response from GET /api/san-pham:", response.data);
      return response.data.map(item => new SanPham(item));
    } catch (error) {
      console.error("[ERROR] Error fetching /api/san-pham:", error.response || error);
      throw new Error(error.response?.data?.message || 'Không lấy được danh sách sản phẩm');
    }
  }

  async getById(id) {
    try {
      const response = await api.get(`/san-pham/${id}`);
      console.log("[DEBUG] Response from GET /api/san-pham/${id}:", response.data);
      return new SanPham(response.data);
    } catch (error) {
      console.error("[ERROR] Error fetching /san-pham/${id}:", error.response || error);
      throw new Error(error.response?.data?.message || 'Không tìm thấy sản phẩm');
    }
  }

  async createWithDetails(formData) {
    try {
      console.log("[DEBUG] Creating SanPham with details");
      const response = await api.post('/san-pham/create-with-details', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      console.log("[DEBUG] Created SanPham:", response.data);
      return new SanPham(response.data);
    } catch (error) {
      console.error("[ERROR] Error creating SanPham:", error.response || error);
      throw new Error(error.response?.data?.tenSanPham || 'Tạo sản phẩm thất bại');
    }
  }

  async update(id, sanPham) {
    try {
      const response = await api.put(`/san-pham/${id}`, sanPham);
      console.log("[DEBUG] Updated SanPham:", response.data);
      return new SanPham(response.data);
    } catch (error) {
      console.error("[ERROR] Error updating /san-pham/${id}:", error.response || error);
      throw new Error(error.response?.data?.message || 'Cập nhật sản phẩm thất bại');
    }
  }

  async delete(id) {
    try {
      await api.delete(`/san-pham/${id}`);
      console.log("[INFO] Deleted SanPham with id:", id);
    } catch (error) {
      console.error("[ERROR] Error deleting /san-pham/${id}:", error.response || error);
      throw new Error(error.response?.data?.message || 'Xóa sản phẩm thất bại');
    }
  }
}

export default new SanPhamService();