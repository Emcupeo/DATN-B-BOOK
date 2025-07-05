import api from './api';
import DanhMuc from '../models/DanhMuc';

class DanhMucService {
  async getAll() {
    const response = await api.get('/danh-muc');
    return response.data.map(item => new DanhMuc(item));
  }

  async getById(id) {
    const response = await api.get(`/danh-muc/${id}`);
    return new DanhMuc(response.data);
  }

  async create(danhMuc) {
    const response = await api.post('/danh-muc', danhMuc);
    return new DanhMuc(response.data);
  }

  async update(id, danhMuc) {
    const response = await api.put(`/danh-muc/${id}`, danhMuc.toJSON());
    return new DanhMuc(response.data);
  }

  async delete(id) {
    await api.delete(`/danh-muc/${id}`);
  }

  async search(keyword) {
    const response = await api.get('/danh-muc/search', {
      params: { keyword }
    });
    return response.data.map(item => new DanhMuc(item));
  }

  async getActiveCategories() {
    const response = await api.get('/danh-muc/active');
    return response.data.map(item => new DanhMuc(item));
  }
}

export default new DanhMucService(); 