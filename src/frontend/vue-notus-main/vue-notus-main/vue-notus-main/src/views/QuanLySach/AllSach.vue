<template>
  <div class="min-h-screen w-full bg-gray-100 font-roboto">
    <div class="bg-white p-4 shadow rounded w-full">
      <!-- Thanh tiêu đề -->
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-lg font-bold">Tất cả chi tiết sách</h2>
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

      <div v-if="!loading && !error" class="mb-4 grid grid-cols-5 gap-4">
        <div class="col-span-1">
          <label class="block text-sm font-medium text-gray-700">Tìm kiếm</label>
          <input
              v-model.trim="filters.search"
              type="text"
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              placeholder="Tìm theo mã, tên, số lượng, trọng lượng, kích thước"
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

      <!-- Error state -->
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
          </tr>
          </thead>
          <tbody>
          <tr class="bg-white border-b hover:bg-gray-50" v-for="(chiTiet, index) in paginatedChiTietSanPham" :key="chiTiet.id">
            <td class="px-6 py-4">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
            <td class="px-6 py-4">
              <div class="relative w-20 h-20 mx-auto">
                <div v-if="chiTiet.images && chiTiet.images.length > 0" class="image-container">
                  <img
                      v-for="(image, imgIndex) in chiTiet.images"
                      :key="image.id"
                      :src="image.url"
                      class="w-full h-full object-cover absolute top-0 left-0"
                      :class="{'image-active': imgIndex === currentImageIndex[chiTiet.id], 'image-inactive': imgIndex !== currentImageIndex[chiTiet.id]}"
                      alt="Chi tiết sản phẩm"
                  />
                </div>
                <span v-else class="text-gray-500">Không có ảnh</span>
              </div>
            </td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ chiTiet.maChiTietSanPham }}</td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ chiTiet.tenChiTietSanPham }}</td>
            <td class="px-6 py-4">{{ formatCurrency(chiTiet.gia) }}</td>
            <td class="px-6 py-4">{{ chiTiet.soLuongTon }}</td>
            <td class="px-6 py-4">{{ chiTiet.trongLuong }}</td>
            <td class="px-6 py-4">{{ chiTiet.kichThuoc }}</td>
            <td class="px-6 py-4 text-center">
              <span :class="chiTiet.trangThai
                ? 'bg-green-100 text-green-700 px-2 py-1 text-xs text-center font-semibold rounded-lg'
                : 'bg-red-100 text-red-700 px-2 py-1 text-xs text-center font-semibold rounded-lg'">
                {{ chiTiet.trangThai ? 'Hoạt động' : 'Không hoạt động' }}
              </span>
            </td>
            <td class="px-6 py-4">{{ chiTiet.idLoaiBia?.tenLoaiBia || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idTacGia?.tenTacGia || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idNhaXuatBan?.tenNhaXuatBan || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idChatLieu?.tenChatLieu || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idNguoiDich?.tenNguoiDich || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idTheLoai?.tenTheLoai || 'N/A' }}</td>
            <td class="px-6 py-4">{{ chiTiet.idNgonNgu?.tenNgonNgu || 'N/A' }}</td>
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
    </div>
  </div>
</template>

<script>
import ChiTietSanPhamService from "@/service/ChiTietSanPhamService";
import AnhSanPhamService from "@/service/AnhSanPhamService";
import LoaiBiaService from "@/service/LoaiBiaService";
import TacGiaService from "@/service/TacGiaService";
import NhaXuatBanService from "@/service/NhaXuatBanService";
import ChatLieuService from "@/service/ChatLieuService";
import NguoiDichService from "@/service/NguoiDichService";
import TheLoaiService from "@/service/TheLoaiService";
import NgonNguService from "@/service/NgonNguService";
import { reactive } from "vue";

export default {
  name: "AllSach",
  data() {
    return {
      chiTietSanPham: [],
      filteredChiTietSanPham: [],
      loading: true,
      error: null,
      currentPage: 1,
      itemsPerPage: 6,
      currentImageIndex: reactive({}),
      imageIntervals: {},
      filters: {
        search: '',
        gia: '',
        soLuongTon: '',
        trangThai: '',
        idLoaiBia: '',
        idTacGia: '',
        idNhaXuatBan: '',
        idChatLieu: '',
        idNguoiDich: '',
        idTheLoai: '',
        idNgonNgu: '',
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
    paginatedChiTietSanPham() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredChiTietSanPham.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.filteredChiTietSanPham.length / this.itemsPerPage);
    },
  },
  watch: {
    chiTietSanPham: {
      handler() {
        console.log("[DEBUG] chiTietSanPham updated, resetting image intervals");
        this.clearImageIntervals();
        if (document.visibilityState === 'visible') {
          this.setupImageIntervals();
        }
        this.applyFilters();
      },
      deep: true,
    },
    filters: {
      handler() {
        console.log("[DEBUG] Filters changed, applying filters", this.filters);
        this.currentPage = 1;
        this.applyFilters();
      },
      deep: true,
    },
  },
  created() {
    console.log("[INFO] Initializing AllSach component");
    this.loadInitialData();
    document.addEventListener('visibilitychange', this.handleVisibilityChange);
  },
  beforeUnmount() {
    console.log("[INFO] Component AllSach is being destroyed, clearing image intervals and removing event listener");
    this.clearImageIntervals();
    document.removeEventListener('visibilitychange', this.handleVisibilityChange);
  },
  methods: {
    async loadInitialData() {
      try {
        this.loading = true;
        this.error = null;
        // Load danh sách bộ lọc
        this.loaiBiaList = await LoaiBiaService.getAll();
        this.tacGiaList = await TacGiaService.getAll();
        this.nhaXuatBanList = await NhaXuatBanService.getAll();
        this.chatLieuList = await ChatLieuService.getAll();
        this.nguoiDichList = await NguoiDichService.getAll();
        this.theLoaiList = await TheLoaiService.getAll();
        this.ngonNguList = await NgonNguService.getAll();
        // Load chi tiết sản phẩm
        const response = await ChiTietSanPhamService.getAll();
        this.chiTietSanPham = Array.isArray(response) ? response : [];
        if (this.chiTietSanPham.length === 0) {
          console.log("[DEBUG] No chiTietSanPham found");
          this.error = "Không tìm thấy chi tiết sản phẩm";
          return;
        }
        console.log("[DEBUG] Loaded chiTietSanPham:", this.chiTietSanPham.length);
        for (let chiTiet of this.chiTietSanPham) {
          if (chiTiet.id) {
            chiTiet.images = await AnhSanPhamService.getImagesByChiTietSanPhamId(chiTiet.id) || [];
            console.log("[DEBUG] Loaded images for chiTietSanPhamId:", chiTiet.id, chiTiet.images.length);
            this.currentImageIndex[chiTiet.id] = 0;
            chiTiet.images.forEach(image => {
              const img = new Image();
              img.src = image.url;
            });
          } else {
            console.warn("[WARN] Chi tiết sản phẩm không có ID:", chiTiet);
            chiTiet.images = [];
          }
        }
        this.applyFilters();
      } catch (error) {
        console.error("[ERROR] Error loading initial data:", error);
        this.error = error.response?.data?.message || "Có lỗi xảy ra khi tải dữ liệu";
      } finally {
        this.loading = false;
        console.log("[INFO] Finished loading initial data");
      }
    },
    applyFilters() {
      console.log("[INFO] Applying filters with search term:", this.filters.search);
      let filtered = [...this.chiTietSanPham];
      const { search, gia, soLuongTon, trangThai, idLoaiBia, idTacGia, idNhaXuatBan, idChatLieu, idNguoiDich, idTheLoai, idNgonNgu } = this.filters;

      if (search && typeof search === 'string') {
        const searchLower = search.toLowerCase().trim();
        filtered = filtered.filter(item => {
          console.log("[DEBUG] Checking item:", {
            id: item.id,
            ten: item.tenChiTietSanPham,
            ma: item.maChiTietSanPham,
            gia: item.gia,
            soLuongTon: item.soLuongTon,
            trongLuong: item.trongLuong,
            kichThuoc: item.kichThuoc
          });
          return (
              (item.tenChiTietSanPham || '').toLowerCase().includes(searchLower) ||
              (item.maChiTietSanPham || '').toLowerCase().includes(searchLower) ||
              (item.gia?.toString() || '').includes(searchLower) ||
              (item.soLuongTon?.toString() || '').includes(searchLower) ||
              (item.trongLuong?.toString() || '').includes(searchLower) ||
              (item.kichThuoc?.toString() || '').includes(searchLower)
          );
        });
        console.log("[DEBUG] After search filter:", filtered.length);
      }

      if (gia) {
        filtered = filtered.filter(item => {
          if (gia === 'low') return item.gia < 100000;
          if (gia === 'medium') return item.gia >= 100000 && item.gia <= 500000;
          if (gia === 'high') return item.gia > 500000;
          return true;
        });
      }

      if (soLuongTon) {
        filtered = filtered.filter(item => {
          if (soLuongTon === 'low') return item.soLuongTon < 10;
          if (soLuongTon === 'medium') return item.soLuongTon >= 10 && item.soLuongTon <= 50;
          if (soLuongTon === 'high') return item.soLuongTon > 50;
          return true;
        });
      }

      if (trangThai !== '') {
        filtered = filtered.filter(item => item.trangThai === trangThai);
      }

      if (idLoaiBia) {
        filtered = filtered.filter(item => item.idLoaiBia?.id === idLoaiBia);
      }

      if (idTacGia) {
        filtered = filtered.filter(item => item.idTacGia?.id === idTacGia);
      }

      if (idNhaXuatBan) {
        filtered = filtered.filter(item => item.idNhaXuatBan?.id === idNhaXuatBan);
      }

      if (idChatLieu) {
        filtered = filtered.filter(item => item.idChatLieu?.id === idChatLieu);
      }

      if (idNguoiDich) {
        filtered = filtered.filter(item => item.idNguoiDich?.id === idNguoiDich);
      }

      if (idTheLoai) {
        filtered = filtered.filter(item => item.idTheLoai?.id === idTheLoai);
      }

      if (idNgonNgu) {
        filtered = filtered.filter(item => item.idNgonNgu?.id === idNgonNgu);
      }

      this.filteredChiTietSanPham = filtered;
      console.log("[DEBUG] Final filtered chiTietSanPham:", this.filteredChiTietSanPham.length);
    },
    setupImageIntervals() {
      console.log("[INFO] Setting up image intervals");
      this.chiTietSanPham.forEach(chiTiet => {
        if (chiTiet.id && chiTiet.images && chiTiet.images.length > 1) {
          console.log("[DEBUG] Setting up interval for chiTietSanPhamId:", chiTiet.id, "with", chiTiet.images.length, "images");
          this.imageIntervals[chiTiet.id] = setInterval(() => {
            const currentIndex = this.currentImageIndex[chiTiet.id] || 0;
            const nextIndex = (currentIndex + 1) % chiTiet.images.length;
            console.log("[DEBUG] Changing image for chiTietSanPhamId:", chiTiet.id, "from index:", currentIndex, "to:", nextIndex);
            this.currentImageIndex[chiTiet.id] = nextIndex;
          }, 3000);
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
    handleVisibilityChange() {
      if (document.visibilityState === 'hidden') {
        console.log("[INFO] Tab is hidden, clearing image intervals");
        this.clearImageIntervals();
      } else if (document.visibilityState === 'visible') {
        console.log("[INFO] Tab is visible, setting up image intervals");
        this.setupImageIntervals();
      }
    },
    formatCurrency(value) {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(value);
    },
    goBack() {
      console.log("[INFO] Navigating back to /admin/sach");
      this.$router.push("/admin/sach");
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
  min-width: 200px;
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