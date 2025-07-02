import axios from "axios";

const API_URL_ListHoaDon = "http://localhost:8080/api/hoa-don";
const API_URL_Invoice = "http://localhost:8080/api/invoice";

class HoaDonService {
    getListHoaDon() {
        return axios.get(API_URL_ListHoaDon);
    }

    getListHoaDonById(id) {
        return axios.get(`${API_URL_ListHoaDon}/${id}`);
    }

    printInvoice(id) {
        return axios.get(`${API_URL_Invoice}/${id}`, {
            responseType: 'blob'
        });
    }

    exportToExcel() {
        return axios.get(`${API_URL_ListHoaDon}/export-excel`, {
            responseType: 'blob'
        });
    }

    updateTrangThaiHoaDon(id, trangThaiMoi) {
        return axios.put(`${API_URL_ListHoaDon}/${id}/cap-nhat-trang-thai`, `"${trangThaiMoi}"`, {
            headers: { "Content-Type": "application/json" }
        });
    }

    deleteHoaDon(id) {
        return axios.put(`${API_URL_ListHoaDon}/${id}/huy-don`, null, {
            headers: { "Content-Type": "application/json" }
        });
    }

    addProductToOrder(hoaDonId, productData) {
        return axios.post(`${API_URL_ListHoaDon}/${hoaDonId}/chi-tiet`, productData, {
            headers: { "Content-Type": "application/json" }
        });
    }

    updatePayment(id, paymentData) {
        return axios.put(`${API_URL_ListHoaDon}/${id}/cap-nhat-thanh-toan`, paymentData, {
            headers: { "Content-Type": "application/json" }
        });
    }

    updateProductQuantity(hoaDonId, chiTietId, data) {
        return axios.put(`${API_URL_ListHoaDon}/${hoaDonId}/chi-tiet/${chiTietId}`, {
            soLuong: data.soLuong,
            chiTietSanPham: { id: data.chiTietSanPhamId }
        }, {
            headers: { "Content-Type": "application/json" }
        });
    }

    removeProductFromOrder(hoaDonId, chiTietId) {
        return axios.delete(`${API_URL_ListHoaDon}/${hoaDonId}/chi-tiet/${chiTietId}`, {
            headers: { "Content-Type": "application/json" }
        });
    }

    updateCustomerInfo(hoaDonId, data) {
        return axios.put(`${API_URL_ListHoaDon}/${hoaDonId}/cap-nhat-thong-tin-khach-hang`, data, {
            headers: { "Content-Type": "application/json" }
        });
    }
}

export default new HoaDonService();