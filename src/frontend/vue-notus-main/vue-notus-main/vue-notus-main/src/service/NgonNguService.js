import api from './api';
import NgonNgu from '../models/NgonNgu';

class NgonNguService {
  async getAll() {
    const response = await api.get('/ngon-ngu');
    return response.data.map(item => new NgonNgu(item));
  }

  async getById(id) {
    const response = await api.get(`/ngon-ngu/${id}`);
    return new NgonNgu(response.data);
  }

  async create(ngonNgu) {
    const response = await api.post('/ngon-ngu', ngonNgu);
    return new NgonNgu(response.data);
  }

  async update(id, ngonNgu) {
    const response = await api.put(`/ngon-ngu/${id}`, ngonNgu.toJSON());
    return new NgonNgu(response.data);
  }

  async delete(id) {
    await api.delete(`/ngon-ngu/${id}`);
  }

  async search(keyword) {
    const response = await api.get('/ngon-ngu/search', {
      params: { keyword }
    });
    return response.data.map(item => new NgonNgu(item));
  }
}

export default new NgonNguService();