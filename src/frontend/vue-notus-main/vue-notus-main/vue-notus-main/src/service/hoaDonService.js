import axios from "axios";

const API_URL_ListHoaDon = "http://localhost:8080/api/hoa-don";
const API_URL_Invoice = "http://localhost:8080/api/invoice";

class HoaDonService {
    // Lấy danh sách hóa đơn
    getListHoaDon() {
        return axios.get(API_URL_ListHoaDon);
    }

    // Lấy chi tiết hóa đơn theo ID
    getListHoaDonById(id) {
        return axios.get(`${API_URL_ListHoaDon}/${id}`);
    }

    // In hóa đơn (trả về file PDF)
    printInvoice(id) {
        return axios.get(`${API_URL_Invoice}/${id}`, {
            responseType: 'blob' // Đảm bảo nhận dữ liệu dạng blob cho file PDF
        });
    }
}

export default new HoaDonService();