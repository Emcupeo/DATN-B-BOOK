import axios from "axios";

const API_URL_ListHoaDon = "http://localhost:8080/api/hoa-don";

class HoaDonService {
    getListHoaDon() {
        return axios.get(API_URL_ListHoaDon);
    }

    getListHoaDonById(id){
        return axios.get(`${API_URL_ListHoaDon}/${id}`);
    }
}

export default new HoaDonService();
