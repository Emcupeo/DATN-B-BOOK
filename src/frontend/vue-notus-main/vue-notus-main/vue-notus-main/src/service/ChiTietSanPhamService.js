import axios from "axios";

const chiTietSanPhamAPIBaseURL = "http://localhost:8080/api/chi-tiet-san-pham";

class chiTietSanPhamService {
  getLisTchiTietSanPham() {
    return axios.get(chiTietSanPhamAPIBaseURL);
  }
}

export default new chiTietSanPhamService();
