import axios from "axios";

const khachHangAPIBaseURL= "http://localhost:8080/api/khach-hang";
class KhachHangService{
    getListKhachHang(){
        return axios.get(khachHangAPIBaseURL)
    }
    // addKhachHang(khachHang){
    //     return axios.post(khachHangAPIBaseURL,khachHang)
    // }
}
export default new KhachHangService();