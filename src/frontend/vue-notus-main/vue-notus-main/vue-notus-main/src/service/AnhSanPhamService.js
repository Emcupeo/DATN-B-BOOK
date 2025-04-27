import axios from 'axios';

const API_URL = 'http://localhost:8080/api/anh-san-pham'; // Thay bằng URL backend của bạn

export default {
    async getImagesByChiTietSanPhamId(chiTietSanPhamId) {
        const response = await axios.get(`${API_URL}/by-chi-tiet-san-pham/${chiTietSanPhamId}`);
        return response.data;
    }
};