import api from './api';

class DiaChiService {
    async getByKhachHangId(khachHangId) {
        const response = await api.get(`/dia-chi/khach-hang/${khachHangId}`);
        return response.data;
    }

    async create(diaChi) {
        const response = await api.post(`/dia-chi/khach-hang/${diaChi.khachHangId}`, diaChi);
        return response.data;
    }

    async update(id, diaChi) {
        const response = await api.put(`/dia-chi/${id}`, diaChi);
        return response.data;
    }

    async delete(id) {
        await api.delete(`/dia-chi/${id}`);
    }

    async setDefault(id) {
        const response = await api.put(`/dia-chi/${id}/default`);
        return response.data;
    }
}

export default new DiaChiService(); 