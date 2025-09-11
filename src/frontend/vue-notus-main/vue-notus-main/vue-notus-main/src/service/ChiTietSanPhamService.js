import axios from "axios";
import ChiTietSanPham from "../models/ChiTietSanPham";

const chiTietSanPhamAPIBaseURL = "http://localhost:8080/api/chi-tiet-san-pham";

class chiTietSanPhamService {
    async getAll() {
        try {
            const response = await axios.get(`${chiTietSanPhamAPIBaseURL}/all`);
            if (Array.isArray(response.data)) {
                return response.data.map(item => new ChiTietSanPham(item));
            } else {
                console.warn('ChiTietSanPhamService.getAll(): response.data is not an array:', response.data);
                return [];
            }
        } catch (error) {
            console.error('Error fetching ChiTietSanPham:', error);
            return [];
        }
    }
    async getById(id) {
        const response = await axios.get(`${chiTietSanPhamAPIBaseURL}/${id}`);
        return new ChiTietSanPham(response.data);
    }

    async getBySanPhamId(idSanPham) {
        const response = await axios.get(`${chiTietSanPhamAPIBaseURL}/by-san-pham/${idSanPham}`);
        return response.data;
    }

    async getByMa(ma) {
        const response = await axios.get(`${chiTietSanPhamAPIBaseURL}/ma/${ma}`);
        return new ChiTietSanPham(response.data);
    }

    async create(data) {
        // data.images là mảng các file ảnh
        const formData = new FormData();
        for (const key in data) {
            if (key !== 'images') {
                formData.append(key, data[key]);
            }
        }
        if (data.images && Array.isArray(data.images)) {
            data.images.forEach(file => {
                formData.append('images', file);
            });
        }
        const response = await axios.post(chiTietSanPhamAPIBaseURL, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
        return response.data;
    }

    async update(id, chiTietSanPham) {
        const response = await axios.put(`${chiTietSanPhamAPIBaseURL}/${id}`, chiTietSanPham);
        return new ChiTietSanPham(response.data);
    }

    async delete(id) {
        try {
            await axios.delete(`${chiTietSanPhamAPIBaseURL}/${id}`);
            console.log("[INFO] Deleted ChiTietSanPham with id:", id);
        } catch (error) {
            console.error("[ERROR] Error deleting ChiTietSanPham:", error.response || error);
            throw new Error(error.response?.data?.message || 'Xóa chi tiết sản phẩm thất bại');
        }
    }

    async search(keyword) {
        try {
            const response = await axios.get(`${chiTietSanPhamAPIBaseURL}/search`, {
                params: { keyword }
            });
            if (Array.isArray(response.data)) {
                return response.data.map(item => new ChiTietSanPham(item));
            } else {
                console.warn('ChiTietSanPhamService.search(): response.data is not an array:', response.data);
                return [];
            }
        } catch (error) {
            console.error('Error searching ChiTietSanPham:', error);
            return [];
        }
    }
}

export default new chiTietSanPhamService();
