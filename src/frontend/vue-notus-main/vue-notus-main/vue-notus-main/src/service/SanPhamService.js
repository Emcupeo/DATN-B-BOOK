import api from './api';
import SanPham from '../models/SanPham';

class SanPhamService {
  async getAll() {
    const response = await api.get('/san-pham');
    console.log('Response từ GET /api/san-pham/all:', response.data);
    return response.data.map(item => new SanPham(item));
  }

  async getById(id) {
    const response = await api.get(`/san-pham/${id}`);
    return new SanPham(response.data);
  }

  async create(sanPham) {
    const response = await api.post('/san-pham', sanPham.toJSON());
    return new SanPham(response.data);
  }

  async update(id, sanPham) {
    const response = await api.put(`/san-pham/${id}`, sanPham.toJSON());
    return new SanPham(response.data);
  }

  async delete(id) {
    await api.delete(`/san-pham/${id}`);
  }

  async search(keyword) {
    const response = await api.get('/san-pham/search', {
      params: { keyword }
    });
    return response.data.map(item => new SanPham(item));
  }
}

export default new SanPhamService();