import axios from "axios";

// const API_URL = "http://localhost:8080/api/san-pham";

// export const getAllProducts = async () => {
//   try {
//     const response = await axios.get(`${API_URL}/all`);
//     return response.data;
//   } catch (error) {
//     console.error("ĐỊt mẹ lỗi :", error);
//     return [];
//   }
// };

const sanPhamAPIBaseURL = "http://localhost:8080/api/san-pham";

class SanPhamService {
  getListSanPham() {
    return axios.get(sanPhamAPIBaseURL);
  }
}

export default new SanPhamService();

// export const getProductsByCategoryId = async (sanPhamId) => {
//   try {
//     const response = await axios.get(`${API_URL}/sanPham/${sanPhamId}`);
//     return response.data;
//   } catch (error) {
//     console.error(`Bruh ${SanPhamId}:`, error);
//     return [];
//   }
// };
