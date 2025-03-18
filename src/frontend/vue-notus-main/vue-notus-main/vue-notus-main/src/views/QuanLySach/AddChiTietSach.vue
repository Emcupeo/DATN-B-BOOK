<template>
  <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded bg-white p-4">
    <h3 class="font-semibold text-lg text-blueGray-700 mb-4">Thêm sách và chi tiết</h3>

    <!-- Form -->
    <form @submit.prevent="saveSanPhamAndChiTiet">
      <div class="grid grid-cols-2 gap-4">
        <!-- Tên sách -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Tên sách</label>
          <input
              v-model="formData.tenSanPham"
              type="text"
              class="mt-1 w-full border rounded px-3 py-2"
              required
          />
        </div>

        <!-- Mô tả sách -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Mô tả sách</label>
          <input
              v-model="formData.moTaSanPham"
              type="text"
              class="mt-1 w-full border rounded px-3 py-2"
          />
        </div>

        <!-- Tác giả -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Tác giả</label>
          <select
              v-model="formData.idTacGia"
              class="mt-1 w-full border rounded px-3 py-2"
          >
            <option v-for="item in tacGiaList" :key="item.id" :value="item.id">
              {{ item.tenTacGia }}
            </option>
          </select>
        </div>

        <!-- Nhà xuất bản -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Nhà xuất bản</label>
          <select
              v-model="formData.idNhaXuatBan"
              class="mt-1 w-full border rounded px-3 py-2"
          >
            <option v-for="item in nhaXuatBanList" :key="item.id" :value="item.id">
              {{ item.tenNhaXuatBan }}
            </option>
          </select>
        </div>

        <!-- Người dịch -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Người dịch</label>
          <select
              v-model="formData.idNguoiDich"
              class="mt-1 w-full border rounded px-3 py-2"
          >
            <option v-for="item in nguoiDichList" :key="item.id" :value="item.id">
              {{ item.tenNguoiDich }}
            </option>
          </select>
        </div>

        <!-- Thể loại -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Thể loại</label>
          <select
              v-model="formData.idTheLoai"
              class="mt-1 w-full border rounded px-3 py-2"
          >
            <option v-for="item in theLoaiList" :key="item.id" :value="item.id">
              {{ item.tenTheLoai }}
            </option>
          </select>
        </div>

        <!-- Ngôn ngữ -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Ngôn ngữ</label>
          <select
              v-model="formData.idNgonNgu"
              class="mt-1 w-full border rounded px-3 py-2"
          >
            <option v-for="item in ngonNguList" :key="item.id" :value="item.id">
              {{ item.tenNgonNgu }}
            </option>
          </select>
        </div>

        <!-- Giá -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Giá</label>
          <input
              v-model.number="formData.gia"
              type="number"
              step="0.01"
              class="mt-1 w-full border rounded px-3 py-2"
              required
          />
        </div>

        <!-- Số lượng tồn -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Số lượng tồn</label>
          <input
              v-model.number="formData.soLuongTon"
              type="number"
              class="mt-1 w-full border rounded px-3 py-2"
              required
          />
        </div>

        <!-- Trọng lượng -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Trọng lượng (gram)</label>
          <input
              v-model.number="formData.trongLuong"
              type="number"
              step="0.01"
              class="mt-1 w-full border rounded px-3 py-2"
              required
          />
        </div>

        <!-- Kích thước -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Kích thước (cm)</label>
          <input
              v-model.number="formData.kichThuoc"
              type="number"
              step="0.01"
              class="mt-1 w-full border rounded px-3 py-2"
              required
          />
        </div>

        <!-- Mô tả chi tiết -->
        <div class="col-span-2">
          <label class="block text-sm font-medium text-gray-700">Mô tả chi tiết</label>
          <textarea
              v-model="formData.moTaChiTiet"
              class="mt-1 w-full border rounded px-3 py-2"
              rows="3"
          ></textarea>
        </div>
      </div>

      <!-- Ô riêng cho Loại bìa và Chất liệu -->
      <div class="mt-6 border-t pt-4">
        <h4 class="font-semibold text-md text-gray-700 mb-4">Thuộc tính chi tiết</h4>
        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">Loại bìa</label>
            <button
                type="button"
                @click="openModal('loaiBia')"
                class="mt-1 w-full border rounded px-3 py-2 bg-blue-500 text-white hover:bg-blue-600 flex items-center justify-center"
            >
              <span class="mr-2">+</span> Chọn loại bìa
            </button>
            <div class="mt-2" v-if="formData.idLoaiBiaList.length > 0">
              <span v-for="id in formData.idLoaiBiaList" :key="id" class="inline-block bg-gray-200 rounded px-2 py-1 mr-2 mb-2">
                {{ getLoaiBiaName(id) }}
              </span>
            </div>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700">Chất liệu</label>
            <button
                type="button"
                @click="openModal('chatLieu')"
                class="mt-1 w-full border rounded px-3 py-2 bg-blue-500 text-white hover:bg-blue-600 flex items-center justify-center"
            >
              <span class="mr-2">+</span> Chọn chất liệu
            </button>
            <div class="mt-2" v-if="formData.idChatLieuList.length > 0">
              <span v-for="id in formData.idChatLieuList" :key="id" class="inline-block bg-gray-200 rounded px-2 py-1 mr-2 mb-2">
                {{ getChatLieuName(id) }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- Bảng hiển thị ChiTietSanPham -->
      <div class="mt-6" :class="{ 'opacity-50 pointer-events-none': showModal }" v-if="chiTietSanPhamList.length > 0">
        <h4 class="font-semibold text-md text-gray-700 mb-4">Danh sách chi tiết sản phẩm</h4>
        <div class="overflow-x-auto">
          <table class="min-w-full bg-white border">
            <thead class="bg-gray-100">
            <tr>
              <th class="px-4 py-2 text-left text-sm font-medium text-gray-700">STT</th>
              <th class="px-4 py-2 text-left text-sm font-medium text-gray-700">Tên chi tiết sản phẩm</th>
              <th class="px-4 py-2 text-left text-sm font-medium text-gray-700">Số lượng</th>
              <th class="px-4 py-2 text-left text-sm font-medium text-gray-700">Giá bán</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(chiTiet, index) in chiTietSanPhamList" :key="index" class="border-t">
              <td class="px-4 py-2">{{ index + 1 }}</td>
              <td class="px-4 py-2">{{ chiTiet.tenChiTietSanPham }}</td>
              <td class="px-4 py-2">{{ chiTiet.soLuongTon }}</td>
              <td class="px-4 py-2">{{ formatCurrency(chiTiet.gia) }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Nút hành động -->
      <div class="flex justify-end space-x-3 mt-6">
        <button
            type="button"
            @click="goBack"
            class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
        >
          Hủy
        </button>
        <button
            type="submit"
            class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          Lưu
        </button>
      </div>
    </form>

    <!-- Modal chọn Loại bìa/Chất liệu -->
    <div v-if="showModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded shadow-lg w-1/3">
        <h3 class="text-lg font-semibold mb-4">{{ modalType === 'loaiBia' ? 'Chọn loại bìa' : 'Chọn chất liệu' }}</h3>
        <div class="max-h-64 overflow-y-auto">
          <div v-if="modalType === 'loaiBia'">
            <label v-for="item in loaiBiaList" :key="item.id" class="block mb-2">
              <input
                  type="checkbox"
                  :value="item.id"
                  v-model="tempLoaiBiaList"
                  class="mr-2"
              />
              {{ item.tenLoaiBia }}
            </label>
          </div>
          <div v-else>
            <label v-for="item in chatLieuList" :key="item.id" class="block mb-2">
              <input
                  type="checkbox"
                  :value="item.id"
                  v-model="tempChatLieuList"
                  class="mr-2"
              />
              {{ item.tenChatLieu }}
            </label>
          </div>
        </div>
        <div class="flex justify-end space-x-3 mt-4">
          <button @click="closeModal" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600">
            Hủy
          </button>
          <button @click="saveModalSelection" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
            Xác nhận
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LoaiBiaService from "@/service/LoaiBiaService";
import TacGiaService from "@/service/TacGiaService";
import NhaXuatBanService from "@/service/NhaXuatBanService";
import ChatLieuService from "@/service/ChatLieuService";
import NguoiDichService from "@/service/NguoiDichService";
import TheLoaiService from "@/service/TheLoaiService";
import NgonNguService from "@/service/NgonNguService";
import api from "@/service/api";

export default {
  name: "AddSanPhamAndChiTiet",

  data() {
    return {
      formData: {
        tenSanPham: "",
        moTaSanPham: "",
        idLoaiBiaList: [],
        idTacGia: "",
        idNhaXuatBan: "",
        idChatLieuList: [],
        idNguoiDich: "",
        idTheLoai: "",
        idNgonNgu: "",
        gia: 0,
        soLuongTon: 0,
        trongLuong: 0,
        kichThuoc: 0,
        moTaChiTiet: "",
      },
      loaiBiaList: [],
      tacGiaList: [],
      nhaXuatBanList: [],
      chatLieuList: [],
      nguoiDichList: [],
      theLoaiList: [],
      ngonNguList: [],
      chiTietSanPhamList: [],
      loading: false,
      error: null,
      showModal: false,
      modalType: '',
      tempLoaiBiaList: [],
      tempChatLieuList: [],
    };
  },

  async created() {
    await this.loadInitialData();
  },

  watch: {
    'formData.idLoaiBiaList': 'generateChiTietSanPham',
    'formData.idChatLieuList': 'generateChiTietSanPham',
  },

  methods: {
    async loadInitialData() {
      try {
        this.loading = true;
        this.loaiBiaList = await LoaiBiaService.getAll();
        this.tacGiaList = await TacGiaService.getAll();
        this.nhaXuatBanList = await NhaXuatBanService.getAll();
        this.chatLieuList = await ChatLieuService.getAll();
        this.nguoiDichList = await NguoiDichService.getAll();
        this.theLoaiList = await TheLoaiService.getAll();
        this.ngonNguList = await NgonNguService.getAll();

        if (this.tacGiaList.length > 0) this.formData.idTacGia = this.tacGiaList[0].id;
        if (this.nhaXuatBanList.length > 0) this.formData.idNhaXuatBan = this.nhaXuatBanList[0].id;
        if (this.nguoiDichList.length > 0) this.formData.idNguoiDich = this.nguoiDichList[0].id;
        if (this.theLoaiList.length > 0) this.formData.idTheLoai = this.theLoaiList[0].id;
        if (this.ngonNguList.length > 0) this.formData.idNgonNgu = this.ngonNguList[0].id;
      } catch (error) {
        console.error("Error loading initial data:", error);
        this.error = "Có lỗi xảy ra khi tải dữ liệu";
      } finally {
        this.loading = false;
      }
    },

    generateChiTietSanPham() {
      this.chiTietSanPhamList = [];
      const { idLoaiBiaList, idChatLieuList, tenSanPham, gia, soLuongTon } = this.formData;

      if (idLoaiBiaList.length > 0 && idChatLieuList.length > 0) {
        idLoaiBiaList.forEach(idLoaiBia => {
          idChatLieuList.forEach(idChatLieu => {
            this.chiTietSanPhamList.push({
              tenChiTietSanPham: `${tenSanPham} (${this.getLoaiBiaName(idLoaiBia)} - ${this.getChatLieuName(idChatLieu)})`,
              idLoaiBia,
              idChatLieu,
              gia,
              soLuongTon,
            });
          });
        });
      }
    },

    getLoaiBiaName(id) {
      const loaiBia = this.loaiBiaList.find(item => item.id === id);
      return loaiBia ? loaiBia.tenLoaiBia : 'N/A';
    },

    getChatLieuName(id) {
      const chatLieu = this.chatLieuList.find(item => item.id === id);
      return chatLieu ? chatLieu.tenChatLieu : 'N/A';
    },

    formatCurrency(value) {
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
    },

    openModal(type) {
      this.modalType = type;
      this.showModal = true;
      if (type === 'loaiBia') {
        this.tempLoaiBiaList = [...this.formData.idLoaiBiaList];
      } else {
        this.tempChatLieuList = [...this.formData.idChatLieuList];
      }
    },

    closeModal() {
      this.showModal = false;
      this.modalType = '';
    },

    saveModalSelection() {
      if (this.modalType === 'loaiBia') {
        this.formData.idLoaiBiaList = [...this.tempLoaiBiaList];
      } else {
        this.formData.idChatLieuList = [...this.tempChatLieuList];
      }
      this.closeModal();
    },

    async saveSanPhamAndChiTiet() {
      try {
        const payload = {
          tenSanPham: this.formData.tenSanPham,
          moTaSanPham: this.formData.moTaSanPham,
          idTacGia: this.formData.idTacGia || null,
          idNhaXuatBan: this.formData.idNhaXuatBan || null,
          idNguoiDich: this.formData.idNguoiDich || null,
          idTheLoai: this.formData.idTheLoai || null,
          idNgonNgu: this.formData.idNgonNgu || null,
          trongLuong: this.formData.trongLuong,
          kichThuoc: this.formData.kichThuoc,
          moTaChiTiet: this.formData.moTaChiTiet,
          chiTietSanPhamList: this.chiTietSanPhamList.map(ctsp => ({
            tenChiTietSanPham: ctsp.tenChiTietSanPham,
            idLoaiBia: ctsp.idLoaiBia,
            idChatLieu: ctsp.idChatLieu,
            gia: ctsp.gia,
            soLuongTon: ctsp.soLuongTon,
          })),
        };

        // Sửa đường dẫn: loại bỏ /api vì baseURL đã chứa /api
        await api.post('/san-pham/create-with-details', payload);
        alert("Thêm sách và chi tiết thành công!");
        this.goBack();
      } catch (error) {
        console.error("Error saving:", error);
        alert("Có lỗi xảy ra khi lưu");
      }
    },

    goBack() {
      this.$router.push("/admin/sach");
    },
  },
};
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
}
th, td {
  border: 1px solid #e5e7eb;
}
.opacity-50 {
  opacity: 0.5;
}
.pointer-events-none {
  pointer-events: none;
}
.z-50 {
  z-index: 50;
}
</style>