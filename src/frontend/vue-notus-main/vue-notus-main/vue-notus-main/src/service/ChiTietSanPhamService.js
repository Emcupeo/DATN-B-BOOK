import axios from "axios";
import ChiTietSanPham from "../models/ChiTietSanPham";

const chiTietSanPhamAPIBaseURL = "http://localhost:8080/api/chi-tiet-san-pham";

class chiTietSanPhamService {
    async getAll() {
        const response = await axios.get(chiTietSanPhamAPIBaseURL);
        return response.data.map(item => new ChiTietSanPham(item));
    }
    async getById(id) {
        const response = await axios.get(`${chiTietSanPhamAPIBaseURL}/${id}`);
        return new ChiTietSanPham(response.data);
    }

    async getByMa(ma) {
        const response = await axios.get(`${chiTietSanPhamAPIBaseURL}/ma/${ma}`);
        return new ChiTietSanPham(response.data);
    }

    async create(chiTietSanPham) {
        const response = await axios.post(chiTietSanPhamAPIBaseURL, chiTietSanPham.toJSON());
        return new ChiTietSanPham(response.data);
    }

    async update(id, chiTietSanPham) {
        const response = await axios.put(`${chiTietSanPhamAPIBaseURL}/${id}`, chiTietSanPham.toJSON());
        return new ChiTietSanPham(response.data);
    }

    async delete(id) {
        await axios.delete(`${chiTietSanPhamAPIBaseURL}/${id}`);
    }

    async search(keyword) {
        const response = await axios.get(`${chiTietSanPhamAPIBaseURL}/search`, {
            params: { keyword }
        });
        return response.data.map(item => new ChiTietSanPham(item));
    }
}

export default new chiTietSanPhamService();
