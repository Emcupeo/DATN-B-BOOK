import axios from "axios";

const API_URL = "http://localhost:8080/api/admin/phieu-giam-gia";

export default {
  getAll(page = 0, size = 14) {
    return axios.get(`${API_URL}?page=${page}&size=${size}`);
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