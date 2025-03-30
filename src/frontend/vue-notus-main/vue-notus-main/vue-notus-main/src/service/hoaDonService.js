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

    // Xuất danh sách hóa đơn ra Excel
    exportToExcel() {
        return axios.get(`${API_URL_ListHoaDon}/export-excel`, {
            responseType: 'blob' // Đảm bảo nhận dữ liệu dạng blob cho file Excel
        });
    }

    // updateTrangThaiHoaDon(id, trangThaiMoi) {
    //     return axios.put(`${API_URL}/hoa-don/${id}/cap-nhat-trang-thai`, {
    //         trangThaiMoi
    //     });
    // }
}

export default new HoaDonService();