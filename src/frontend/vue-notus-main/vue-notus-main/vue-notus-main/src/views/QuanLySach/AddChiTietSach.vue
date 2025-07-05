<template>
  <div class="min-h-screen w-full bg-gray-100 font-roboto">
    <div class="bg-white p-4 shadow rounded w-full">
      <!-- Thanh tiêu đề -->
      <div class="mb-4">
        <h2 class="text-lg font-bold">Thêm sách và chi tiết</h2>
      </div>

      <!-- Form -->
      <form @submit.prevent="saveSanPhamAndChiTiet">
        <div class="grid grid-cols-2 gap-4">
          <!-- Tên sách -->
          <div class="relative">
            <label class="block text-sm font-medium text-gray-700">Tên sách</label>
            <input
                v-model="formData.tenSanPham"
                type="text"
                class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500 mt-1"
                required
                @focus="showSanPhamList = true"
                @blur="delayHideSanPhamList"
                @input="showSanPhamList = false"
            />
            <div
                v-if="showSanPhamList && sanPhamList.length > 0"
                class="absolute z-10 mt-1 w-full bg-white border rounded shadow-lg max-h-60 overflow-y-auto"
            >
              <div
                  v-for="sanPham in sanPhamList"
                  :key="sanPham.id"
                  class="px-3 py-2 hover:bg-gray-100 cursor-pointer"
                  @mousedown="selectSanPham(sanPham.tenSanPham)"
              >
                {{ sanPham.tenSanPham }}
              </div>
            </div>
          </div>

          <!-- Tác giả -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Tác giả</label>
            <div class="flex items-center mt-1">
              <select
                  v-model="formData.idTacGia"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
              >
                <option v-for="item in tacGiaList" :key="item.id" :value="item.id">
                  {{ item.tenTacGia }}
                </option>
              </select>
              <button
                  type="button"
                  @click="openAddModal('tacGia')"
                  class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
              >
                +
              </button>
            </div>
          </div>

          <!-- Nhà xuất bản -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Nhà xuất bản</label>
            <div class="flex items-center mt-1">
              <select
                  v-model="formData.idNhaXuatBan"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
              >
                <option v-for="item in nhaXuatBanList" :key="item.id" :value="item.id">
                  {{ item.tenNhaXuatBan }}
                </option>
              </select>
              <button
                  type="button"
                  @click="openAddModal('nhaXuatBan')"
                  class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
              >
                +
              </button>
            </div>
          </div>

          <!-- Người dịch -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Người dịch</label>
            <div class="flex items-center mt-1">
              <select
                  v-model="formData.idNguoiDich"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
              >
                <option v-for="item in nguoiDichList" :key="item.id" :value="item.id">
                  {{ item.tenNguoiDich }}
                </option>
              </select>
              <button
                  type="button"
                  @click="openAddModal('nguoiDich')"
                  class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
              >
                +
              </button>
            </div>
          </div>

          <!-- Thể loại -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Thể loại</label>
            <div class="flex items-center mt-1">
              <select
                  v-model="formData.idTheLoai"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
              >
                <option v-for="item in theLoaiList" :key="item.id" :value="item.id">
                  {{ item.tenTheLoai }}
                </option>
              </select>
              <button
                  type="button"
                  @click="openAddModal('theLoai')"
                  class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
              >
                +
              </button>
            </div>
          </div>

          <!-- Ngôn ngữ -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Ngôn ngữ</label>
            <div class="flex items-center mt-1">
              <select
                  v-model="formData.idNgonNgu"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
              >
                <option v-for="item in ngonNguList" :key="item.id" :value="item.id">
                  {{ item.tenNgonNgu }}
                </option>
              </select>
              <button
                  type="button"
                  @click="openAddModal('ngonNgu')"
                  class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
              >
                +
              </button>
            </div>
          </div>

          <!-- Loại bìa -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Loại bìa</label>
            <div class="flex items-center mt-1">
              <select
                  v-model="formData.idLoaiBia"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
              >
                <option v-for="item in loaiBiaList" :key="item.id" :value="item.id">
                  {{ item.tenLoaiBia }}
                </option>
              </select>
              <button
                  type="button"
                  @click="openAddModal('loaiBia')"
                  class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
              >
                +
              </button>
            </div>
          </div>

          <!-- Chất liệu -->
          <div>
            <label class="block text-sm font-medium text-gray-700">Chất liệu</label>
            <div class="flex items-center mt-1">
              <select
                  v-model="formData.idChatLieu"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
              >
                <option v-for="item in chatLieuList" :key="item.id" :value="item.id">
                  {{ item.tenChatLieu }}
                </option>
              </select>
              <button
                  type="button"
                  @click="openAddModal('chatLieu')"
                  class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
              >
                +
              </button>
            </div>
          </div>
        </div>

        <!-- Danh mục -->
        <div class="mt-6">
          <label class="block text-sm font-medium text-gray-700">Danh mục</label>
          <div class="flex items-center mt-1">
            <select
                v-model="formData.idDanhMuc"
                class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                :disabled="loading"
            >
              <option value="">Chọn danh mục</option>
              <option v-for="item in danhMucList" :key="item.id" :value="item.id">
                {{ item.tenDanhMuc }}
              </option>
            </select>
            <button
                type="button"
                @click="openAddModal('danhMuc')"
                class="ml-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
            >
              +
            </button>
          </div>
          <div v-if="loading" class="text-sm text-gray-500 mt-1">Đang tải danh mục...</div>
        </div>

        <!-- Mô tả sách -->
        <div class="mt-6">
          <label class="block text-sm font-medium text-gray-700">Mô tả sách</label>
          <textarea
              v-model="formData.moTaSanPham"
              class="block w-full p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500 mt-1"
              rows="5"
          ></textarea>
        </div>

        <!-- Bảng chi tiết sản phẩm -->
        <div class="mt-6" v-if="isFormComplete">
          <h4 class="text-md font-semibold text-gray-700 mb-4">Chi tiết sản phẩm</h4>
          <div class="relative overflow-x-auto sm:rounded-lg">
            <table class="w-full text-sm text-center text-gray-500">
              <thead class="text-xs text-gray-700 uppercase bg-gray-50">
              <tr>
                <th class="px-2 py-3 w-12">STT</th>
                <th class="px-2 py-3 w-40">Tên chi tiết sản phẩm</th>
                <th class="px-2 py-3 w-20">Số lượng</th>
                <th class="px-2 py-3 w-24">Giá bán</th>
                <th class="px-2 py-3 w-24">Trọng lượng (gram)</th>
                <th class="px-2 py-3 w-24">Kích thước (cm)</th>
                <th class="px-2 py-3 w-96">Ảnh</th>
              </tr>
              </thead>
              <tbody>
              <tr class="bg-white border-b hover:bg-gray-50">
                <td class="px-2 py-4">1</td>
                <td class="px-2 py-4 font-medium text-gray-900 whitespace-nowrap">{{ chiTietSanPham.tenChiTietSanPham }}</td>
                <td class="px-2 py-4">
                  <input
                      v-model.number="chiTietSanPham.soLuongTon"
                      type="number"
                      min="1"
                      class="block w-16 h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                  />
                </td>
                <td class="px-2 py-4">
                  <input
                      v-model.number="chiTietSanPham.gia"
                      type="number"
                      step="0.01"
                      class="block w-20 h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                  />
                </td>
                <td class="px-2 py-4">
                  <input
                      v-model.number="chiTietSanPham.trongLuong"
                      type="number"
                      step="0.01"
                      class="block w-20 h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                  />
                </td>
                <td class="px-2 py-4">
                  <input
                      v-model.number="chiTietSanPham.kichThuoc"
                      type="number"
                      step="0.01"
                      class="block w-20 h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                  />
                </td>
                <td class="px-2 py-4">
                  <div class="flex items-center space-x-2">
                    <button
                        type="button"
                        @click="$refs.imageInput.click()"
                        class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-3 py-2"
                        :disabled="selectedImages.length >= 3"
                    >
                      Thêm ảnh
                    </button>
                    <input
                        ref="imageInput"
                        type="file"
                        accept="image/*"
                        multiple
                        style="display: none"
                        :disabled="selectedImages.length >= 3"
                        @change="handleFileSelect"
                    />
                    <div class="flex space-x-2 overflow-x-auto max-w-[300px]">
                      <div v-for="(image, index) in selectedImages" :key="index" class="relative">
                        <img
                            :src="image.preview"
                            class="w-24 h-24 object-cover rounded"
                            alt="Preview"
                        />
                        <button
                            @click="removeImage(index)"
                            class="absolute top-0 right-0 bg-red-500 text-white rounded-full w-5 h-5 flex items-center justify-center"
                        >
                          x
                        </button>
                      </div>
                    </div>
                  </div>
                  <div v-if="selectedImages.length >= 3" class="text-xs text-red-500 mt-1">Chỉ được tải lên tối đa 3 ảnh!</div>
                </td>
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
              class="text-white bg-gradient-to-r from-gray-500 via-gray-600 to-gray-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-gray-300 shadow-lg shadow-gray-500/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
          >
            Hủy
          </button>
          <button
              type="submit"
              class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            Lưu
          </button>
        </div>
      </form>

      <!-- Modal thêm nhanh thuộc tính -->
      <div v-if="showAddModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded-lg w-1/2 shadow-lg">
          <h2 class="text-xl font-bold mb-4">Thêm {{ addModalType }}</h2>
          <form @submit.prevent="saveNewItem">
            <div class="mb-4">
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên {{ addModalType }}</label>
              <input
                  v-model="newItemName"
                  type="text"
                  class="block w-full h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500"
                  :placeholder="`Nhập tên ${addModalType}`"
                  required
              />
            </div>
            <div class="flex justify-end space-x-3">
              <button
                  type="button"
                  @click="closeAddModal"
                  class="text-white bg-gradient-to-r from-gray-500 via-gray-600 to-gray-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-gray-300 shadow-lg shadow-gray-500/50 font-medium rounded-lg text-sm px-4 py-2 text-center"
              >
                Hủy
              </button>
              <button
                  type="submit"
                  class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm px-4 py-2 text-center"
              >
                Thêm
              </button>
            </div>
          </form>
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
import DanhMucService from "@/service/DanhMucService";

import ImageKitService from "@/service/ImageKitService";
import api from "@/service/api";

export default {
  name: "AddSanPhamAndChiTiet",
  data() {
    return {
      formData: {
        tenSanPham: "",
        moTaSanPham: "",
        idTacGia: null,
        idNhaXuatBan: null,
        idNguoiDich: null,
        idTheLoai: null,
        idNgonNgu: null,
        idLoaiBia: null,
        idChatLieu: null,
        idDanhMuc: null,
      },
      chiTietSanPham: {
        tenChiTietSanPham: "",
        soLuongTon: 1,
        gia: 50000,
        trongLuong: 200,
        kichThuoc: 20,
      },
      loaiBiaList: [],
      tacGiaList: [],
      nhaXuatBanList: [],
      chatLieuList: [],
      nguoiDichList: [],
      theLoaiList: [],
      ngonNguList: [],
      danhMucList: [],
      sanPhamList: [],
      showSanPhamList: false,
      loading: false,
      error: null,
      showAddModal: false,
      addModalType: "",
      newItemName: "",
      selectedImages: [],

    };
  },
  computed: {
    isFormComplete() {
      return (
          this.formData.tenSanPham &&
          this.formData.idTacGia &&
          this.formData.idNhaXuatBan &&
          this.formData.idNguoiDich &&
          this.formData.idTheLoai &&
          this.formData.idNgonNgu &&
          this.formData.idLoaiBia &&
          this.formData.idChatLieu &&
          this.formData.idDanhMuc
      );
    },
  },
  watch: {
    formData: {
      handler() {
        this.generateChiTietSanPham();
      },
      deep: true,
    },
  },
  async created() {
    console.log("[DEBUG] Initializing AddSanPhamAndChiTiet component");
    await this.loadInitialData();
  },
  methods: {
    async loadInitialData() {
      console.log("[INFO] Loading initial data");
      try {
        this.loading = true;
        this.loaiBiaList = await LoaiBiaService.getAll();
        this.tacGiaList = await TacGiaService.getAll();
        this.nhaXuatBanList = await NhaXuatBanService.getAll();
        this.chatLieuList = await ChatLieuService.getAll();
        this.nguoiDichList = await NguoiDichService.getAll();
        this.theLoaiList = await TheLoaiService.getAll();
        this.ngonNguList = await NgonNguService.getAll();
        this.danhMucList = await DanhMucService.getAll();
        this.sanPhamList = await api.get("/san-pham").then((res) => res.data);

        console.log("[DEBUG] Loaded data:", {
          loaiBiaList: this.loaiBiaList.length,
          tacGiaList: this.tacGiaList.length,
          nhaXuatBanList: this.nhaXuatBanList.length,
          chatLieuList: this.chatLieuList.length,
          nguoiDichList: this.nguoiDichList.length,
          theLoaiList: this.theLoaiList.length,
          ngonNguList: this.ngonNguList.length,
          danhMucList: this.danhMucList.length,
          sanPhamList: this.sanPhamList.length,
        });

        if (this.tacGiaList.length > 0)
          this.formData.idTacGia = this.tacGiaList[0].id;
        if (this.nhaXuatBanList.length > 0)
          this.formData.idNhaXuatBan = this.nhaXuatBanList[0].id;
        if (this.nguoiDichList.length > 0)
          this.formData.idNguoiDich = this.nguoiDichList[0].id;
        if (this.theLoaiList.length > 0)
          this.formData.idTheLoai = this.theLoaiList[0].id;
        if (this.ngonNguList.length > 0)
          this.formData.idNgonNgu = this.ngonNguList[0].id;
        if (this.loaiBiaList.length > 0)
          this.formData.idLoaiBia = this.loaiBiaList[0].id;
        if (this.chatLieuList.length > 0)
          this.formData.idChatLieu = this.chatLieuList[0].id;
        if (this.danhMucList.length > 0)
          this.formData.idDanhMuc = this.danhMucList[0].id;
      } catch (error) {
        console.error("[ERROR] Error loading initial data:", error);
        this.error = "Có lỗi xảy ra khi tải dữ liệu";
      } finally {
        this.loading = false;
        console.log("[INFO] Finished loading initial data");
      }
    },
    selectSanPham(tenSanPham) {
      console.log("[DEBUG] Selected sanPham:", tenSanPham);
      this.formData.tenSanPham = tenSanPham;
      this.showSanPhamList = false;
    },
    delayHideSanPhamList() {
      console.log("[DEBUG] Delaying hide of sanPhamList");
      setTimeout(() => {
        this.showSanPhamList = false;
      }, 200);
    },
    generateChiTietSanPham() {
      console.log("[INFO] Generating chiTietSanPham");
      if (this.isFormComplete) {
        this.chiTietSanPham.tenChiTietSanPham = this.formData.tenSanPham;
        console.log("[DEBUG] Generated chiTietSanPham:", this.chiTietSanPham);
      } else {
        this.chiTietSanPham.tenChiTietSanPham = "";
        console.log("[DEBUG] Form incomplete, chiTietSanPham not generated");
      }
    },
    getLoaiBiaName(id) {
      const loaiBia = this.loaiBiaList.find((item) => item.id === id);
      const name = loaiBia ? loaiBia.tenLoaiBia : "N/A";
      console.log("[DEBUG] Retrieved loaiBia name for id:", id, name);
      return name;
    },
    getChatLieuName(id) {
      const chatLieu = this.chatLieuList.find((item) => item.id === id);
      const name = chatLieu ? chatLieu.tenChatLieu : "N/A";
      console.log("[DEBUG] Retrieved chatLieu name for id:", id, name);
      return name;
    },
    openAddModal(type) {
      console.log("[INFO] Opening add modal for type:", type);
      this.addModalType = type;
      this.showAddModal = true;
      this.newItemName = "";
    },
    closeAddModal() {
      console.log("[INFO] Closing add modal");
      this.showAddModal = false;
      this.addModalType = "";
      this.newItemName = "";
    },
    async saveNewItem() {
      console.log("[INFO] Saving new item for addModalType:", this.addModalType);
      if (!this.newItemName) {
        console.log("[DEBUG] No item name provided");
        alert("Vui lòng nhập tên!");
        return;
      }
      try {
        let newItem;
        switch (this.addModalType) {
          case "tacGia":
            newItem = await TacGiaService.create({ tenTacGia: this.newItemName });
            this.tacGiaList.push(newItem);
            this.formData.idTacGia = newItem.id;
            break;
          case "nhaXuatBan":
            newItem = await NhaXuatBanService.create({
              tenNhaXuatBan: this.newItemName,
            });
            this.nhaXuatBanList.push(newItem);
            this.formData.idNhaXuatBan = newItem.id;
            break;
          case "nguoiDich":
            newItem = await NguoiDichService.create({
              tenNguoiDich: this.newItemName,
            });
            this.nguoiDichList.push(newItem);
            this.formData.idNguoiDich = newItem.id;
            break;
          case "theLoai":
            newItem = await TheLoaiService.create({ tenTheLoai: this.newItemName });
            this.theLoaiList.push(newItem);
            this.formData.idTheLoai = newItem.id;
            break;
          case "ngonNgu":
            newItem = await NgonNguService.create({ tenNgonNgu: this.newItemName });
            this.ngonNguList.push(newItem);
            this.formData.idNgonNgu = newItem.id;
            break;
          case "loaiBia":
            newItem = await LoaiBiaService.create({ tenLoaiBia: this.newItemName });
            this.loaiBiaList.push(newItem);
            this.formData.idLoaiBia = newItem.id;
            break;
          case "chatLieu":
            newItem = await ChatLieuService.create({ tenChatLieu: this.newItemName });
            this.chatLieuList.push(newItem);
            this.formData.idChatLieu = newItem.id;
            break;
          case "danhMuc":
            newItem = await DanhMucService.create({ tenDanhMuc: this.newItemName });
            this.danhMucList.push(newItem);
            this.formData.idDanhMuc = newItem.id;
            break;
        }
        console.log("[DEBUG] Saved new item:", newItem);
        this.closeAddModal();
        alert(`Đã thêm ${this.addModalType} thành công!`);
      } catch (error) {
        console.error("[ERROR] Error saving new item:", error);
        alert("Có lỗi xảy ra khi thêm!");
      }
    },
    handleFileSelect(event) {
      console.log("[INFO] Handling file selection");
      const files = event.target.files;
      if (files) {
        Array.from(files).forEach(file => {
          if (this.selectedImages.length < 3) {
            const reader = new FileReader();
            reader.onload = (e) => {
              this.selectedImages.push({
                file: file,
                preview: e.target.result,
                name: file.name
              });
            };
            reader.readAsDataURL(file);
          }
        });
      }
      // Reset input để có thể chọn lại file đã chọn
      event.target.value = '';
    },
    removeImage(index) {
      console.log("[INFO] Removing image at index:", index);
      this.selectedImages.splice(index, 1);
    },
    async saveSanPhamAndChiTiet() {
      console.log("[INFO] Saving SanPham and ChiTietSanPham");
      if (!this.isFormComplete) {
        alert("Vui lòng điền đầy đủ thông tin!");
        return;
      }
      try {
        this.loading = true;
        
        console.log('[DEBUG] AddChiTietSach - Selected images:', this.selectedImages);
        
        // Upload ảnh lên ImageKit
        const imageUrls = [];
        if (this.selectedImages.length > 0) {
          console.log('[DEBUG] AddChiTietSach - Uploading images to ImageKit...');
          for (let image of this.selectedImages) {
            try {
              const result = await ImageKitService.uploadImage(image.file, image.name);
              if (result.success) {
                imageUrls.push(result.url);
                console.log('[DEBUG] AddChiTietSach - Uploaded image:', result.url);
              } else {
                console.error('[ERROR] AddChiTietSach - Upload failed:', result.error);
              }
            } catch (error) {
              console.error('[ERROR] AddChiTietSach - Upload error:', error);
            }
          }
        }
        console.log('[DEBUG] AddChiTietSach - Final imageUrls:', imageUrls);
        const chiTietSanPhamDTO = {
          tenChiTietSanPham: this.chiTietSanPham.tenChiTietSanPham,
          idLoaiBia: this.formData.idLoaiBia,
          idChatLieu: this.formData.idChatLieu,
          gia: this.chiTietSanPham.gia,
          soLuongTon: this.chiTietSanPham.soLuongTon,
          trongLuong: this.chiTietSanPham.trongLuong,
          kichThuoc: this.chiTietSanPham.kichThuoc,
          imageUrls: imageUrls,
        };
        // Tạo request object cho ImageKit
        const sanPhamRequest = {
          tenSanPham: this.formData.tenSanPham,
          moTaSanPham: this.formData.moTaSanPham || '',
          idTacGia: this.formData.idTacGia,
          idNhaXuatBan: this.formData.idNhaXuatBan,
          idNguoiDich: this.formData.idNguoiDich,
          idTheLoai: this.formData.idTheLoai,
          idNgonNgu: this.formData.idNgonNgu,
          idDanhMuc: this.formData.idDanhMuc,
          moTaChiTiet: this.chiTietSanPham.moTa || '',
          chiTietSanPhamList: [chiTietSanPhamDTO]
        };
        console.log("[DEBUG] Sending request with imageUrls:", imageUrls);
        console.log("[DEBUG] Full request:", sanPhamRequest);
        await api.post('/san-pham/create-with-imagekit', sanPhamRequest, {
          headers: { 'Content-Type': 'application/json' },
        });
        console.log("[INFO] Successfully saved SanPham and ChiTietSanPham");
        alert("Thêm sách và chi tiết thành công!");
        window.location.href = "http://localhost:3000/admin/sach";
      } catch (error) {
        console.error("[ERROR] Error saving data:", error);
        alert("Có lỗi xảy ra khi lưu dữ liệu!");
      } finally {
        this.loading = false;
      }
    },
    goBack() {
      console.log("[INFO] Navigating back");
      window.location.href = "http://localhost:3000/admin/sach";
    },
  },
};
</script>