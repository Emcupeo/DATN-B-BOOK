<template>
  <div class="min-h-screen w-full bg-gray-100 font-roboto">
    <div class="bg-white p-4 shadow rounded w-full">
      <!-- Thanh tiêu đề và công cụ -->
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-lg font-bold">Chi tiết bộ sách</h2>
        <div class="flex items-center justify-between gap-3">
          <button
              @click="goBack"
              type="button"
              class="flex items-center gap-2 text-white bg-gradient-to-r from-indigo-500 via-indigo-600 to-indigo-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-indigo-300 shadow-lg shadow-indigo-500/50 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M10.5 19.5 3 12m0 0 7.5-7.5M3 12h18" />
            </svg>
            Quay lại
          </button>
        </div>
      </div>

      <!-- Bộ lọc -->
      <div v-if="!loading && !error" class="mb-4 grid grid-cols-5 gap-4">
        <div class="col-span-1">
          <label class="block text-sm font-medium text-gray-700">Tìm kiếm</label>
          <input
              v-model="filters.searchQuery"
              type="text"
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="Tìm theo mã, tên, số lượng"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Giá tối thiểu</label>
          <input
              v-model.number="filters.minPrice"
              type="number"
              min="0"
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="0"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Giá tối đa</label>
          <input
              v-model.number="filters.maxPrice"
              type="number"
              min="0"
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="∞"
          />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Loại bìa</label>
          <select v-model="filters.idLoaiBia" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
            <option :value="null">Tất cả</option>
            <option v-for="item in loaiBiaList" :key="item.id" :value="item.id">{{ item.tenLoaiBia }}</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Tác giả</label>
          <select v-model="filters.idTacGia" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
            <option :value="null">Tất cả</option>
            <option v-for="item in tacGiaList" :key="item.id" :value="item.id">{{ item.tenTacGia }}</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Nhà xuất bản</label>
          <select v-model="filters.idNhaXuatBan" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
            <option :value="null">Tất cả</option>
            <option v-for="item in nhaXuatBanList" :key="item.id" :value="item.id">{{ item.tenNhaXuatBan }}</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Chất liệu</label>
          <select v-model="filters.idChatLieu" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
            <option :value="null">Tất cả</option>
            <option v-for="item in chatLieuList" :key="item.id" :value="item.id">{{ item.tenChatLieu }}</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Người dịch</label>
          <select v-model="filters.idNguoiDich" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
            <option :value="null">Tất cả</option>
            <option v-for="item in nguoiDichList" :key="item.id" :value="item.id">{{ item.tenNguoiDich }}</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Thể loại</label>
          <select v-model="filters.idTheLoai" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
            <option :value="null">Tất cả</option>
            <option v-for="item in theLoaiList" :key="item.id" :value="item.id">{{ item.tenTheLoai }}</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700">Ngôn ngữ</label>
          <select v-model="filters.idNgonNgu" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500">
            <option :value="null">Tất cả</option>
            <option v-for="item in ngonNguList" :key="item.id" :value="item.id">{{ item.tenNgonNgu }}</option>
          </select>
        </div>
      </div>

      <!-- Loading state -->
      <div v-if="loading" class="p-4 text-center">
        <svg class="animate-spin inline-block w-5 h-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 12a8 8 0 1116 0 8 8 0 01-16 0z" />
        </svg>
        Đang tải...
      </div>

      <!-- Error state - Not Found -->
      <div v-else-if="error && error.includes('không tìm thấy')" class="p-8 text-center">
        <div class="mb-4">
          <svg class="w-10 h-10 text-yellow-500 mb-4 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01M12 3a9 9 0 110 18 9 9 0 010-18z" />
          </svg>
          <p class="text-lg text-gray-700 mb-6">
            Chưa có chi tiết bộ sách
          </p>
        </div>
      </div>

      <!-- Other Error state -->
      <div v-else-if="error" class="p-4 text-red-500 text-center">
        <svg class="w-5 h-5 inline mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01M12 3a9 9 0 110 18 9 9 0 010-18z" />
        </svg>
        {{ error }}
      </div>

      <!-- Nội dung bảng -->
      <div v-else class="relative overflow-x-auto sm:rounded-lg w-full h-[calc(130vh-250px)]">
        <table class="w-full text-sm text-center text-gray-500">
          <thead class="text-xs text-gray-700 uppercase bg-gray-50">
          <tr>
            <th scope="col" class="px-6 py-3">STT</th>
            <th scope="col" class="px-6 py-3">Ảnh</th>
            <th scope="col" class="px-6 py-3">Mã chi tiết sản phẩm</th>
            <th scope="col" class="px-6 py-3">Tên sách</th>
            <th scope="col" class="px-6 py-3">Số lượng</th>
            <th scope="col" class="px-6 py-3">Giá</th>
            <th scope="col" class="px-6 py-3">Số lượng tồn</th>
            <th scope="col" class="px-6 py-3">Trọng lượng (gram)</th>
            <th scope="col" class="px-6 py-3">Kích thước (cm)</th>
            <th scope="col" class="px-6 py-3">Trạng thái</th>
            <th scope="col" class="px-6 py-3">Loại bìa</th>
            <th scope="col" class="px-6 py-3">Tác giả</th>
            <th scope="col" class="px-6 py-3">Nhà xuất bản</th>
            <th scope="col" class="px-6 py-3">Chất liệu</th>
            <th scope="col" class="px-6 py-3">Người dịch</th>
            <th scope="col" class="px-6 py-3">Thể loại</th>
            <th scope="col" class="px-6 py-3">Ngôn ngữ</th>
            <th scope="col" class="px-6 py-3">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <tr class="bg-white border-b hover:bg-gray-50" v-for="(chiTiet, index) in paginatedBoSachChiTiet" :key="chiTiet.idChiTietSanPham.id">
            <td class="px-6 py-4">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
            <td class="px-6 py-4">
              <div class="relative w-20 h-20 mx-auto">
                <div v-if="chiTiet.idChiTietSanPham.images && chiTiet.idChiTietSanPham.images.length > 0" class="image-container">
                  <img
                      v-for="(image, imgIndex) in chiTiet.idChiTietSanPham.images"
                      :key="image.id"
                      :src="image.url"
                      class="w-full h-full object-cover absolute top-0 left-0"
                      :class="{'image-active': imgIndex === currentImageIndex[chiTiet.idChiTietSanPham.id], 'image-inactive': imgIndex !== currentImageIndex[chiTiet.idChiTietSanPham.id]}"
                      alt="Chi tiết sản phẩm"
                  />
                </div>
                <span v-else class="text-gray-500">Không có ảnh</span>
              </div>
            </td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ chiTiet.idChiTietSanPham.maChiTietSanPham }}</td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ chiTiet.idChiTietSanPham.tenChiTietSanPham }}</td>
            <td class="px-6 py-4">{{ chiTiet.soLuong }}</td>
            <td class="px-6 py-4">{{ formatCurrency(chiTiet.idChiTietSanPham.gia) }}</td>
            <td class="px-6 py-4">{{ chiTiet.idChiTietSanPham.soLuongTon }}</td>
            <td class="px-6 py-4">{{ chiTiet.idChiTietSanPham.trongLuong }}</td>
            <td class="px-6 py-4">{{ chiTiet.idChiTietSanPham.kichThuoc }}</td>
            <td class="px-6 py-4 text-center">
                <span :class="chiTiet.idChiTietSanPham.trangThai
                  ? 'bg-green-100 text-green-700 px-2 py-1 text-xs text-center font-semibold rounded-lg'
                  : 'bg-red-100 text-red-700 px-2 py-1 text-xs text-center font-semibold rounded-lg'">
                  {{ chiTiet.idChiTietSanPham.trangThai ? 'Hoạt động' : 'Không hoạt động' }}
                </span>
            </td>
            <td class="px-6 py-4">{{ chiTiet.idChiTietSanPham.idLoaiBia?.tenLoaiBia || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idChiTietSanPham.idTacGia?.tenTacGia || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idChiTietSanPham.idNhaXuatBan?.tenNhaXuatBan || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idChiTietSanPham.idChatLieu?.tenChatLieu || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idChiTietSanPham.idNguoiDich?.tenNguoiDich || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idChiTietSanPham.idTheLoai?.tenTheLoai || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idChiTietSanPham.idNgonNgu?.tenNgonNgu || 'N/A' }}</td>
            <td class="px-6 py-4 align-middle">
              <div class="flex items-center justify-center space-x-3 h-full">
                <a href="#" @click.prevent="deleteBoSachChiTiet(chiTiet.idChiTietSanPham.id)" class="text-red-600 hover:text-red-800">
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                  </svg>
                </a>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Phân trang -->
      <div v-if="!loading && !error" class="flex justify-center items-center mt-4 space-x-2">
        <button
            @click="prevPage"
            :disabled="currentPage === 1"
            class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm p-2 text-center flex items-center justify-center"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
            <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5" />
          </svg>
        </button>
        <span class="text-xs font-semibold text-gray-700">
          Trang {{ currentPage }} / {{ totalPages }}
        </span>
        <button
            @click="nextPage"
            :disabled="currentPage === totalPages"
            class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 shadow-lg shadow-blue-500/50 font-medium rounded-lg text-sm p-2 text-center flex items-center justify-center"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
            <path stroke-linecap="round" stroke-linejoin="round" d="m8.25 4.5 7.5 7.5-7.5 7.5" />
          </svg>
        </button>
      </div>

      <!-- Modal chỉnh sửa -->
      <div v-if="showEditModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white p-6 rounded-lg w-3/4 shadow-lg">
          <h2 class="text-xl font-bold mb-4">Sửa chi tiết bộ sách</h2>
          <form @submit.prevent="handleUpdate" class="space-y-6">
            <div class="grid grid-cols-3 gap-6">
              <!-- Cột 1 -->
              <div class="space-y-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700">Mã chi tiết sản phẩm</label>
                  <input
                      v-model="editForm.idChiTietSanPham.maChiTietSanPham"
                      type="text"
                      class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 bg-gray-100"
                      disabled
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700">Tên chi tiết sản phẩm</label>
                  <input
                      v-model="editForm.idChiTietSanPham.tenChiTietSanPham"
                      type="text"
                      class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 bg-gray-100"
                      disabled
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700">Số lượng</label>
                  <input
                      v-model.number="editForm.soLuong"
                      type="number"
                      min="1"
                      class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                      required
                  />
                </div>
              </div>
            </div>

            <!-- Buttons -->
            <div class="flex justify-end space-x-3">
              <button
                  type="button"
                  @click="closeEditModal"
                  class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 transition"
              >
                Hủy
              </button>
              <button
                  type="submit"
                  class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
              >
                Cập nhật
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BoSachService from "@/service/BoSachService";
import LoaiBiaService from "@/service/LoaiBiaService";
import TacGiaService from "@/service/TacGiaService";
import NhaXuatBanService from "@/service/NhaXuatBanService";
import ChatLieuService from "@/service/ChatLieuService";
import NguoiDichService from "@/service/NguoiDichService";
import TheLoaiService from "@/service/TheLoaiService";
import NgonNguService from "@/service/NgonNguService";
import AnhSanPhamService from "@/service/AnhSanPhamService";
import { reactive } from "vue";

export default {
  name: "ChiTietBoSach",

  data() {
    return {
      boSachChiTiet: [],
      loading: true,
      error: null,
      showEditModal: false,
      currentPage: 1,
      itemsPerPage: 6,
      currentImageIndex: reactive({}),
      imageIntervals: {},
      filters: {
        searchQuery: '',
        minPrice: null,
        maxPrice: null,
        idLoaiBia: null,
        idTacGia: null,
        idNhaXuatBan: null,
        idChatLieu: null,
        idNguoiDich: null,
        idTheLoai: null,
        idNgonNgu: null,
      },
      editForm: {
        idBoSach: null,
        idChiTietSanPham: {
          id: null,
          maChiTietSanPham: '',
          tenChiTietSanPham: '',
        },
        soLuong: 1,
      },
      loaiBiaList: [],
      tacGiaList: [],
      nhaXuatBanList: [],
      chatLieuList: [],
      nguoiDichList: [],
      theLoaiList: [],
      ngonNguList: [],
    };
  },

  computed: {
    filteredBoSachChiTiet() {
      return this.boSachChiTiet.filter(item => {
        const query = this.filters.searchQuery.toLowerCase().trim();
        const numericQuery = parseFloat(query);

        const matchesSearchQuery = !query || (
            item.idChiTietSanPham.maChiTietSanPham.toLowerCase().includes(query) ||
            item.idChiTietSanPham.tenChiTietSanPham.toLowerCase().includes(query) ||
            (isFinite(numericQuery) && item.soLuong === numericQuery)
        );

        return (
            matchesSearchQuery &&
            (this.filters.minPrice === null || item.idChiTietSanPham.gia >= this.filters.minPrice) &&
            (this.filters.maxPrice === null || item.idChiTietSanPham.gia <= this.filters.maxPrice) &&
            (this.filters.idLoaiBia === null || (item.idChiTietSanPham.idLoaiBia && item.idChiTietSanPham.idLoaiBia.id === this.filters.idLoaiBia)) &&
            (this.filters.idTacGia === null || (item.idChiTietSanPham.idTacGia && item.idChiTietSanPham.idTacGia.id === this.filters.idTacGia)) &&
            (this.filters.idNhaXuatBan === null || (item.idChiTietSanPham.idNhaXuatBan && item.idChiTietSanPham.idNhaXuatBan.id === this.filters.idNhaXuatBan)) &&
            (this.filters.idChatLieu === null || (item.idChiTietSanPham.idChatLieu && item.idChiTietSanPham.idChatLieu.id === this.filters.idChatLieu)) &&
            (this.filters.idNguoiDich === null || (item.idChiTietSanPham.idNguoiDich && item.idChiTietSanPham.idNguoiDich.id === this.filters.idNguoiDich)) &&
            (this.filters.idTheLoai === null || (item.idChiTietSanPham.idTheLoai && item.idChiTietSanPham.idTheLoai.id === this.filters.idTheLoai)) &&
            (this.filters.idNgonNgu === null || (item.idChiTietSanPham.idNgonNgu && item.idChiTietSanPham.idNgonNgu.id === this.filters.idNgonNgu))
        );
      });
    },
    paginatedBoSachChiTiet() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredBoSachChiTiet.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.filteredBoSachChiTiet.length / this.itemsPerPage) || 1;
    }
  },

  watch: {
    filters: {
      handler() {
        this.currentPage = 1;
      },
      deep: true
    },
    boSachChiTiet: {
      handler() {
        console.log("[DEBUG] boSachChiTiet updated, resetting image intervals");
        this.clearImageIntervals();
        if (document.visibilityState === 'visible') {
          this.setupImageIntervals();
        }
      },
      deep: true
    }
  },

  created() {
    console.log("[INFO] Initializing ChiTietBoSach component");
    this.loadInitialData();
    this.loadBoSachChiTiet();
    document.addEventListener('visibilitychange', this.handleVisibilityChange);
  },

  beforeUnmount() {
    console.log("[INFO] Component ChiTietBoSach is being destroyed, clearing image intervals and removing event listener");
    this.clearImageIntervals();
    document.removeEventListener('visibilitychange', this.handleVisibilityChange);
  },

  methods: {
    handleVisibilityChange() {
      if (document.visibilityState === 'hidden') {
        console.log("[INFO] Tab is hidden, clearing image intervals");
        this.clearImageIntervals();
      } else if (document.visibilityState === 'visible') {
        console.log("[INFO] Tab is visible, setting up image intervals");
        this.setupImageIntervals();
      }
    },

    async loadBoSachChiTiet() {
      console.log("[INFO] Loading boSachChiTiet for idBoSach:", this.$route.params.id);
      try {
        this.loading = true;
        this.error = null;
        const idBoSach = this.$route.params.id;
        const response = await BoSachService.getBoSachChiTietByBoSachId(idBoSach);
        this.boSachChiTiet = Array.isArray(response) ? response : [];
        if (this.boSachChiTiet.length === 0) {
          console.log("[DEBUG] No boSachChiTiet found for idBoSach:", idBoSach);
          this.error = "Không tìm thấy chi tiết bộ sách";
          return;
        }
        console.log("[DEBUG] Loaded boSachChiTiet:", this.boSachChiTiet.length);
        for (let chiTiet of this.boSachChiTiet) {
          if (chiTiet.idChiTietSanPham?.id) {
            chiTiet.idChiTietSanPham.images = await AnhSanPhamService.getImagesByChiTietSanPhamId(chiTiet.idChiTietSanPham.id) || [];
            console.log("[DEBUG] Loaded images for chiTietSanPhamId:", chiTiet.idChiTietSanPham.id, chiTiet.idChiTietSanPham.images.length);
            this.currentImageIndex[chiTiet.idChiTietSanPham.id] = 0;
            chiTiet.idChiTietSanPham.images.forEach(image => {
              const img = new Image();
              img.src = image.url;
            });
          } else {
            console.warn("[WARN] Chi tiết sản phẩm không có ID:", chiTiet);
            chiTiet.idChiTietSanPham.images = [];
          }
        }
      } catch (error) {
        console.error("[ERROR] Error loading bo sach chi tiet:", error);
        this.error = error.message || "Có lỗi xảy ra khi tải chi tiết bộ sách";
      } finally {
        this.loading = false;
        console.log("[INFO] Finished loading boSachChiTiet");
      }
    },

    async loadInitialData() {
      console.log("[INFO] Loading initial data");
      try {
        this.loaiBiaList = await LoaiBiaService.getAll();
        this.tacGiaList = await TacGiaService.getAll();
        this.nhaXuatBanList = await NhaXuatBanService.getAll();
        this.chatLieuList = await ChatLieuService.getAll();
        this.nguoiDichList = await NguoiDichService.getAll();
        this.theLoaiList = await TheLoaiService.getAll();
        this.ngonNguList = await NgonNguService.getAll();
        console.log("[DEBUG] Loaded initial data:", {
          loaiBiaList: this.loaiBiaList.length,
          tacGiaList: this.tacGiaList.length,
          nhaXuatBanList: this.nhaXuatBanList.length,
          chatLieuList: this.chatLieuList.length,
          nguoiDichList: this.nguoiDichList.length,
          theLoaiList: this.theLoaiList.length,
          ngonNguList: this.ngonNguList.length
        });
      } catch (error) {
        console.error("[ERROR] Error loading initial data:", error);
        this.error = "Có lỗi xảy ra khi tải dữ liệu danh sách";
      }
    },

    setupImageIntervals() {
      console.log("[INFO] Setting up image intervals");
      this.boSachChiTiet.forEach(chiTiet => {
        if (chiTiet.idChiTietSanPham?.id && chiTiet.idChiTietSanPham.images && chiTiet.idChiTietSanPham.images.length > 1) {
          console.log("[DEBUG] Setting up interval for chiTietSanPhamId:", chiTiet.idChiTietSanPham.id, "with", chiTiet.idChiTietSanPham.images.length, "images");
          this.imageIntervals[chiTiet.idChiTietSanPham.id] = setInterval(() => {
            const currentIndex = this.currentImageIndex[chiTiet.idChiTietSanPham.id] || 0;
            const nextIndex = (currentIndex + 1) % chiTiet.idChiTietSanPham.images.length;
            console.log("[DEBUG] Changing image for chiTietSanPhamId:", chiTiet.idChiTietSanPham.id, "from index:", currentIndex, "to:", nextIndex);
            this.currentImageIndex[chiTiet.idChiTietSanPham.id] = nextIndex;
          }, 3000);
        } else {
          console.log("[DEBUG] No interval set for chiTietSanPhamId:", chiTiet.idChiTietSanPham?.id, "images count:", chiTiet.idChiTietSanPham?.images?.length || 0);
        }
      });
    },

    clearImageIntervals() {
      console.log("[INFO] Clearing image intervals");
      Object.keys(this.imageIntervals).forEach(id => {
        console.log("[DEBUG] Clearing interval for chiTietSanPhamId:", id);
        clearInterval(this.imageIntervals[id]);
      });
      this.imageIntervals = {};
    },

    formatCurrency(value) {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(value);
    },

    goBack() {
      console.log("[INFO] Navigating back to /admin/bo-sach");
      this.$router.push("/admin/bo-sach");
    },

    showAllChiTiet() {
      console.log("[INFO] Navigating to /admin/bo-sach/all-bo-sach");
      this.$router.push("/admin/bo-sach/all-bo-sach");
    },

    openEditModal(chiTiet) {
      console.log("[INFO] Opening edit modal for chiTietSanPhamId:", chiTiet.idChiTietSanPham.id);
      this.editForm = {
        idBoSach: chiTiet.idBoSach.id,
        idChiTietSanPham: {
          id: chiTiet.idChiTietSanPham.id,
          maChiTietSanPham: chiTiet.idChiTietSanPham.maChiTietSanPham,
          tenChiTietSanPham: chiTiet.idChiTietSanPham.tenChiTietSanPham,
        },
        soLuong: chiTiet.soLuong,
      };
      this.showEditModal = true;
    },

    closeEditModal() {
      console.log("[INFO] Closing edit modal");
      this.showEditModal = false;
      this.editForm = {
        idBoSach: null,
        idChiTietSanPham: {
          id: null,
          maChiTietSanPham: '',
          tenChiTietSanPham: '',
        },
        soLuong: 1,
      };
    },

    async handleUpdate() {
      console.log("[INFO] Handling update boSachChiTiet for chiTietSanPhamId:", this.editForm.idChiTietSanPham.id);
      try {
        const updatedData = {
          id: {
            idBoSach: this.editForm.idBoSach,
            idChiTietSanPham: this.editForm.idChiTietSanPham.id,
          },
          idBoSach: { id: this.editForm.idBoSach },
          idChiTietSanPham: { id: this.editForm.idChiTietSanPham.id },
          soLuong: this.editForm.soLuong,
        };
        console.log("[DEBUG] Updating boSachChiTiet:", updatedData);

        await BoSachService.updateBoSachChiTiet(this.editForm.idBoSach, this.editForm.idChiTietSanPham.id, updatedData);
        alert("Cập nhật chi tiết bộ sách thành công!");
        this.closeEditModal();
        await this.loadBoSachChiTiet();
      } catch (error) {
        console.error("[ERROR] Error updating bo sach chi tiet:", error);
        alert(error.message || "Có lỗi xảy ra khi cập nhật chi tiết bộ sách");
      }
    },

    async deleteBoSachChiTiet(idChiTietSanPham) {
      console.log("[INFO] Confirming deletion for chiTietSanPhamId:", idChiTietSanPham);
      if (confirm("Bạn có chắc chắn muốn xóa chi tiết bộ sách này?")) {
        try {
          await BoSachService.deleteBoSachChiTiet(this.$route.params.id, idChiTietSanPham);
          console.log("[INFO] Deleted chiTietSanPhamId:", idChiTietSanPham);
          alert("Xóa chi tiết bộ sách thành công!");
          await this.loadBoSachChiTiet();
        } catch (error) {
          console.error("[ERROR] Error deleting bo sach chi tiet:", error);
          alert(error.message || "Có lỗi xảy ra khi xóa chi tiết bộ sách");
        }
      }
    },

    prevPage() {
      if (this.currentPage > 1) {
        console.log("[INFO] Navigating to previous page:", this.currentPage - 1);
        this.currentPage--;
      }
    },

    nextPage() {
      if (this.currentPage < this.totalPages) {
        console.log("[INFO] Navigating to next page:", this.currentPage + 1);
        this.currentPage++;
      }
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');
.font-roboto {
  font-family: 'Roboto', sans-serif;
}
th {
  min-width: 150px;
}
.image-container {
  position: relative;
  width: 100%;
  height: 100%;
}
.image-active {
  opacity: 1;
  transition: opacity 0.5s ease-in-out;
  z-index: 1;
}
.image-inactive {
  opacity: 0;
  transition: opacity 0.5s ease-in-out;
  z-index: 0;
}
</style>