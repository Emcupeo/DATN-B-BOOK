// phieuGiamGiaService.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/admin/phieu-giam-gia';

export default {
  getAll(page = 0, size = 12, sortBy = 'id', sortDir = 'asc', searchQuery = '') {
    return axios.get(API_URL, {
      params: {
        page,
        size,
        sort: `${sortBy},${sortDir}`, // Gộp sortBy và sortDir thành sort
        searchQuery,
      },
    });
  },
  getById(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  create(phieuGiamGia) {
    return axios.post(API_URL, phieuGiamGia);
  },
  update(id, phieuGiamGia) {
    return axios.put(`${API_URL}/${id}`, phieuGiamGia);
  },
  delete(id) {
    return axios.delete(`${API_URL}/${id}`);
  },
};