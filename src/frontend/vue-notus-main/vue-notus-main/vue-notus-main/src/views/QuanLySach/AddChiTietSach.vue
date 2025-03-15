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

        <!-- Loại bìa -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Loại bìa</label>
          <select
              v-model="formData.idLoaiBia"
              class="mt-1 w-full border rounded px-3 py-2"
          >
            <option v-for="item in loaiBiaList" :key="item.id" :value="item.id">
              {{ item.tenLoaiBia }}
            </option>
          </select>
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

        <!-- Chất liệu -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Chất liệu</label>
          <select
              v-model="formData.idChatLieu"
              class="mt-1 w-full border rounded px-3 py-2"
          >
            <option v-for="item in chatLieuList" :key="item.id" :value="item.id">
              {{ item.tenChatLieu }}
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
              v-model="formData.gia"
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
              v-model="formData.soLuongTon"
              type="number"
              class="mt-1 w-full border rounded px-3 py-2"
              required
          />
        </div>

        <!-- Trọng lượng -->
        <div>
          <label class="block text-sm font-medium text-gray-700">Trọng lượng (gram)</label>
          <input
              v-model="formData.trongLuong"
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
              v-model="formData.kichThuoc"
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
        idLoaiBia: "",
        idTacGia: "",
        idNhaXuatBan: "",
        idChatLieu: "",
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
      loading: false,
      error: null,
    };
  },

  async created() {
    await this.loadInitialData();
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

        // Gán giá trị mặc định là bản ghi đầu tiên (mới nhất)
        if (this.loaiBiaList.length > 0) this.formData.idLoaiBia = this.loaiBiaList[0].id;
        if (this.tacGiaList.length > 0) this.formData.idTacGia = this.tacGiaList[0].id;
        if (this.nhaXuatBanList.length > 0) this.formData.idNhaXuatBan = this.nhaXuatBanList[0].id;
        if (this.chatLieuList.length > 0) this.formData.idChatLieu = this.chatLieuList[0].id;
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

    async saveSanPhamAndChiTiet() {
      try {
        await api.post('/san-pham/create-with-detail', null, {
          params: {
            tenSanPham: this.formData.tenSanPham,
            moTaSanPham: this.formData.moTaSanPham,
            idLoaiBia: this.formData.idLoaiBia || null,
            idTacGia: this.formData.idTacGia || null,
            idNhaXuatBan: this.formData.idNhaXuatBan || null,
            idChatLieu: this.formData.idChatLieu || null,
            idNguoiDich: this.formData.idNguoiDich || null,
            idTheLoai: this.formData.idTheLoai || null,
            idNgonNgu: this.formData.idNgonNgu || null,
            gia: this.formData.gia,
            soLuongTon: this.formData.soLuongTon,
            trongLuong: this.formData.trongLuong,
            kichThuoc: this.formData.kichThuoc,
            moTaChiTiet: this.formData.moTaChiTiet,
          },
        });
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
/* CSS tùy chỉnh nếu cần */
</style>