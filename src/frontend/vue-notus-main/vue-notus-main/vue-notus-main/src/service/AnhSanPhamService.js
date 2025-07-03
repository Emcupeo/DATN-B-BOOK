import api from './api';

class AnhSanPhamService {
    async uploadImage(file, idChiTietSanPham) {
        try {
            console.log("[DEBUG] Uploading image:", file.name, "for idChiTietSanPham:", idChiTietSanPham);
            const formData = new FormData();
            formData.append('file', file);
            formData.append('idChiTietSanPham', idChiTietSanPham);

            const response = await api.post('/anh-san-pham/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            console.log("[DEBUG] Image uploaded successfully:", response.data);
            return response.data;
        } catch (error) {
            console.error("[ERROR] Error uploading image:", error.response || error);
            throw new Error(error.response?.data?.url || 'Tải ảnh thất bại');
        }
    }

    async getImagesByChiTietSanPhamId(idChiTietSanPham) {
        try {
            console.log("[DEBUG] Fetching images for idChiTietSanPham:", idChiTietSanPham);
            const response = await api.get(`/anh-san-pham/chi-tiet-san-pham/${idChiTietSanPham}`);
            console.log("[DEBUG] Fetched images:", response.data);
            return response.data;
        } catch (error) {
            console.error("[ERROR] Error fetching images:", error.response || error);
            throw new Error(error.response?.data?.message || 'Không tìm thấy ảnh');
        }
    }
}

export default new AnhSanPhamService();