import axios from "axios";

const API_URL = "http://localhost:8080/api/hoa-don";

class LichSuHoaDonService {
    getListLSHDById(id) {
        return axios.get(`${API_URL}/${id}`);
    }
}

export default new LichSuHoaDonService();
