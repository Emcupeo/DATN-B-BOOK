import api from './api';
import NhaXuatBan from '../models/NhaXuatBan';

class NhaXuatBanService {
  async getAll() {
    const response = await api.get('/nha-xuat-ban');
    return response.data.map(item => new NhaXuatBan(item));
  }

  async getById(id) {
    const response = await api.get(`/nha-xuat-ban/${id}`);
    return new NhaXuatBan(response.data);
  }

  async create(nhaXuatBan) {
    const response = await api.post('/nha-xuat-ban', nhaXuatBan);
    return new NhaXuatBan(response.data);
  }

  async update(id, nhaXuatBan) {
    const response = await api.put(`/nha-xuat-ban/${id}`, nhaXuatBan.toJSON());
    return new NhaXuatBan(response.data);
  }

  async delete(id) {
    await api.delete(`/nha-xuat-ban/${id}`);
  }

  async search(keyword) {
    const response = await api.get('/nha-xuat-ban/search', {
      params: { keyword }
    });
    return response.data.map(item => new NhaXuatBan(item));
  }
}

export default new NhaXuatBanService();