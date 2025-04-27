import axios from 'axios';

const API_URL = 'http://localhost:8080/api/admin/phieu-giam-gia';

export default {
  getAll(params = {}) {
    // Convert params to URLSearchParams
    const searchParams = new URLSearchParams();
    
    // Handle pagination params
    if (typeof params.page !== 'undefined') searchParams.append('page', params.page);
    if (typeof params.size !== 'undefined') searchParams.append('size', params.size);
    
    // Handle sorting params
    if (params.sortBy) searchParams.append('sortBy', params.sortBy);
    if (params.sortDir) searchParams.append('sortDir', params.sortDir);
    
    // Handle filter params
    if (params.loaiApDung) searchParams.append('loaiApDung', params.loaiApDung);
    if (params.loaiPhieu) searchParams.append('loaiPhieu', params.loaiPhieu);
    if (params.trangThai !== undefined) searchParams.append('trangThai', params.trangThai);
    if (params.tinhTrang) searchParams.append('tinhTrang', params.tinhTrang);
    if (params.searchQuery) searchParams.append('searchQuery', params.searchQuery);
    if (params.fromDate) searchParams.append('fromDate', params.fromDate);
    if (params.toDate) searchParams.append('toDate', params.toDate);

    // Log query params for debugging
    console.log('Sending API with query:', searchParams.toString());

    return axios.get(`${API_URL}?${searchParams.toString()}`);
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
  }
};