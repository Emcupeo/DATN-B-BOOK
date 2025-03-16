import api from './api';
import KhachHang from '../models/KhachHang';

class KhachHangService {
    async getAll() {
        const response = await api.get('/khach-hang');
        return response.data.map(item => new KhachHang(item));
    }

    async getById(id) {
        const response = await api.get(`/khach-hang/${id}`);
        return new KhachHang(response.data);
    }

    async create(khachHang) {
        const response = await api.post('/khach-hang', khachHang.toJSON());
        return new KhachHang(response.data);
    }

    async update(id, khachHang) {
        const response = await api.put(`/khach-hang/${id}`, khachHang.toJSON());
        return new KhachHang(response.data);
    }

    async delete(id) {
        await api.delete(`/khach-hang/${id}`);
    }

    async search(keyword) {
        const response = await api.get('/khach-hang/search', {
            params: { keyword }
        });
        return response.data.map(item => new KhachHang(item));
    }
}

export default new KhachHangService();