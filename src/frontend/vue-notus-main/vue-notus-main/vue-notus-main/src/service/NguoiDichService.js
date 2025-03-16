import api from './api';
import NguoiDich from '../models/NguoiDich';

class NguoiDichService {
  async getAll() {
    const response = await api.get('/nguoi-dich');
    return response.data.map(item => new NguoiDich(item));
  }

  async getById(id) {
    const response = await api.get(`/nguoi-dich/${id}`);
    return new NguoiDich(response.data);
  }

  async create(nguoiDich) {
    const response = await api.post('/nguoi-dich', nguoiDich.toJSON());
    return new NguoiDich(response.data);
  }

  async update(id, nguoiDich) {
    const response = await api.put(`/nguoi-dich/${id}`, nguoiDich.toJSON());
    return new NguoiDich(response.data);
  }

  async delete(id) {
    await api.delete(`/nguoi-dich/${id}`);
  }

  async search(keyword) {
    const response = await api.get('/nguoi-dich/search', {
      params: { keyword }
    });
    return response.data.map(item => new NguoiDich(item));
  }
}

export default new NguoiDichService();