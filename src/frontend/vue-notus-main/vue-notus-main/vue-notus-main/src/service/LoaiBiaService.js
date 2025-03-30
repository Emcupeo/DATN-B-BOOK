import api from './api';
import LoaiBia from '../models/LoaiBia';

class LoaiBiaService {
  async getAll() {
    const response = await api.get('/loai-bia');
    return response.data.map(item => new LoaiBia(item));
  }

  async getById(id) {
    const response = await api.get(`/loai-bia/${id}`);
    return new LoaiBia(response.data);
  }

  async create(loaiBia) {
    const response = await api.post('/loai-bia', loaiBia);
    return new LoaiBia(response.data);
  }

  async update(id, loaiBia) {
    const response = await api.put(`/loai-bia/${id}`, loaiBia.toJSON());
    return new LoaiBia(response.data);
  }

  async delete(id) {
    await api.delete(`/loai-bia/${id}`);
  }

  async search(keyword) {
    const response = await api.get('/loai-bia/search', {
      params: { keyword }
    });
    return response.data.map(item => new LoaiBia(item));
  }
}

export default new LoaiBiaService();