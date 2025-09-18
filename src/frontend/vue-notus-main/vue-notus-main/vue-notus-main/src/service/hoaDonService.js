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

    huyDon(id) {
        return axios.put(`${API_URL_ListHoaDon}/${id}/huy-don`, null, {
            headers: { "Content-Type": "application/json" }
        });
    }

    addProductToOrder(hoaDonId, productData) {
        return axios.post(`${API_URL_ListHoaDon}/${hoaDonId}/add-product`, productData, {
            headers: { "Content-Type": "application/json" }
        });
    }

    updateProductQuantity(hoaDonId, chiTietId, updatedData) {
        return axios.put(`${API_URL_ListHoaDon}/${hoaDonId}/chi-tiet/${chiTietId}/cap-nhat-so-luong`, updatedData, {
            headers: { "Content-Type": "application/json" }
        });
    }

    removeProductFromOrder(hoaDonId, chiTietId) {
        return axios.delete(`${API_URL_ListHoaDon}/${hoaDonId}/chi-tiet/${chiTietId}`);
    }

    updateCustomerInfo(hoaDonId, customerData) {
        return axios.put(`${API_URL_ListHoaDon}/${hoaDonId}/cap-nhat-thong-tin-khach-hang`, customerData, {
            headers: { "Content-Type": "application/json" }
        });
    }

    updatePayment(hoaDonId, paymentData) {
        return axios.put(`${API_URL_ListHoaDon}/${hoaDonId}/cap-nhat-thanh-toan`, paymentData, {
            headers: { "Content-Type": "application/json" }
        });
    }

    addBoSachToOrder(hoaDonId, productData) {
        return axios.post(`${API_URL_ListHoaDon}/${hoaDonId}/add-bosach`, productData, {
            headers: { "Content-Type": "application/json" }
        });
    }

    updateHoaDon(id, data) {
        return axios.put(`${API_URL_ListHoaDon}/${id}`, data, {
            headers: { "Content-Type": "application/json" }
        });
    }

    createHoaDon(data) {
        return axios.post(API_URL_ListHoaDon, data, {
            headers: { "Content-Type": "application/json" }
        });
    }

    // Tra cứu đơn hàng cho khách hàng chưa đăng nhập
    lookupOrder(orderCode, phoneNumber) {
        const requestData = {
            orderCode: orderCode,
            phoneNumber: phoneNumber
        };
        
        console.log('🔍 DEBUG: Sending request to:', `${API_URL_ListHoaDon}/lookup-order`);
        console.log('🔍 DEBUG: Request data:', requestData);
        
        return axios.post(`${API_URL_ListHoaDon}/lookup-order`, requestData, {
            headers: { "Content-Type": "application/json" }
        });
    }
}

export default new HoaDonService();