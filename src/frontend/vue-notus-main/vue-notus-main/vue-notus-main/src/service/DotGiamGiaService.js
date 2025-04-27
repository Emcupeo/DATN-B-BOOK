import axios from 'axios';

const API_URL = 'http://localhost:8080/api/discounts';
const PRODUCT_API_URL = 'http://localhost:8080/api/chi-tiet-san-pham';
const SAN_PHAM_API_URL = 'http://localhost:8080/api/san-pham';

const axiosInstance = axios.create({
  baseURL: API_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

const productAxiosInstance = axios.create({
  baseURL: PRODUCT_API_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

const sanPhamAxiosInstance = axios.create({
  baseURL: SAN_PHAM_API_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

export default {
  async getListDiscounts() {
    try {
      const response = await axiosInstance.get('');
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy danh sách đợt giảm giá!');
    }
  },

  async createDiscount(discount) {
    try {
      const response = await axiosInstance.post('', discount);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể tạo đợt giảm giá!');
    }
  },

  async updateDiscount(id, discount) {
    try {
      const response = await axiosInstance.put(`/${id}`, discount);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể cập nhật đợt giảm giá!');
    }
  },

  async deleteDiscount(id) {
    try {
      await axiosInstance.delete(`/${id}`);
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể xóa đợt giảm giá!');
    }
  },

  async getAllProducts() {
    try {
      const response = await productAxiosInstance.get('/all');
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy danh sách sản phẩm!');
    }
  },

  async getProductById(id) {
    try {
      const response = await productAxiosInstance.get(`/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy chi tiết sản phẩm!');
    }
  },

 async getAllSanPham() {
  try {
    const response = await sanPhamAxiosInstance.get('');
    return response.data;
  } catch (error) {
    throw new Error(error.response?.data?.message || 'Không thể lấy danh sách sản phẩm từ bảng sanpham!');
  }
},

  async getChiTietSanPhamBySanPhamId(idSanPham) {
    try {
      const response = await productAxiosInstance.get(`/by-san-pham/${idSanPham}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Không thể lấy chi tiết sản phẩm theo sản phẩm!');
    }
  },
};