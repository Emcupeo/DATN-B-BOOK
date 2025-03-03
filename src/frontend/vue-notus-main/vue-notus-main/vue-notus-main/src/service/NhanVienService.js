import axios from "axios";
const nhanVienAPIBaseURL = "http://localhost:8080/api/nhan-vien";
class NhanVienService{
    getListNhanVien(){
        return axios.get(nhanVienAPIBaseURL)
    }
}
export default new NhanVienService();