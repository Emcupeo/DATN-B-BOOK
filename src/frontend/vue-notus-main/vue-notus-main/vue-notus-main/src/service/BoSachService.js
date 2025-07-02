import api from './api';
import BoSach from '@/models/BoSach';

class BoSachService {
    async getAll() {
        try {
            const response = await api.get('/bo-sach');
            console.log('Response từ GET /api/bo-sach:', response.data);
            return response.data.map(item => new BoSach(item));
        } catch (error) {
            console.error('Lỗi khi gọi /api/bo-sach:', error.response || error);
            throw error;
        }
    }

    async getById(id) {
        try {
            const response = await api.get(`/bo-sach/${id}`);
            return new BoSach(response.data);
        } catch (error) {
            console.error(`Lỗi khi gọi /bo-sach/${id}:`, error.response || error);
            throw error;
        }
    }

    async create(boSach) {
        try {
            const response = await api.post('/bo-sach', boSach);
            return new BoSach(response.data);
        } catch (error) {
            console.error('Lỗi khi tạo bộ sách:', error.response || error);
            throw error;
        }
    }

    async delete(id) {
        try {
            await api.delete(`/bo-sach/${id}`);
        } catch (error) {
            console.error(`Lỗi khi xóa /bo-sach/${id}:`, error.response || error);
            throw error;
        }
    }

    async search(keyword) {
        try {
            const response = await api.get('/bo-sach/search', {
                params: { keyword }
            });
            return response.data.map(item => new BoSach(item));
        } catch (error) {
            console.error('Lỗi khi tìm kiếm /bo-sach/search:', error);
            throw error;
        }
    }

    async getChiTietSanPhams() {
        try {
            const response = await api.get('/chi-tiet-san-pham/all');
            return response.data;
        } catch (error) {
            console.error('Lỗi khi lấy danh sách chi tiết sản phẩm:', error.response || error);
            throw error;
        }
    }

    async getBoSachChiTietByBoSachId(idBoSach) {
        try {
            const response = await api.get(`/bo-sach/${idBoSach}/chi-tiet`);
            console.log(`Response từ GET /api/bo-sach/${idBoSach}/chi-tiet:`, response.data);
            return response.data;
        } catch (error) {
            console.error(`Lỗi khi gọi /bo-sach/${idBoSach}/chi-tiet:`, error.response || error);
            throw new Error(error.response?.data?.message || 'Không tìm thấy chi tiết bộ sách');
        }
    }

    async updateBoSachChiTiet(idBoSach, idChiTietSanPham, data) {
        try {
            const response = await api.put(`/bo-sach/${idBoSach}/${idChiTietSanPham}`, data);
            console.log(`Response từ PUT /api/bo-sach/${idBoSach}/${idChiTietSanPham}:`, response.data);
            return response.data;
        } catch (error) {
            console.error(`Lỗi khi cập nhật /bo-sach/${idBoSach}/${idChiTietSanPham}:`, error.response || error);
            throw new Error(error.response?.data?.message || 'Cập nhật chi tiết bộ sách thất bại');
        }
    }

    async deleteBoSachChiTiet(idBoSach, idChiTietSanPham) {
        try {
            await api.delete(`/bo-sach/${idBoSach}/${idChiTietSanPham}`);
            console.log(`Xóa thành công /bo-sach/${idBoSach}/${idChiTietSanPham}`);
        } catch (error) {
            console.error(`Lỗi khi xóa /bo-sach/${idBoSach}/${idChiTietSanPham}:`, error.response || error);
            throw new Error(error.response?.data?.message || 'Xóa chi tiết bộ sách thất bại');
        }
    }
}

export default new BoSachService();