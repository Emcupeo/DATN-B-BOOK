<template>
    <div class="min-h-screen bg-gray-50 p-6 font-sans relative">
      <!-- Header -->
      <header class="text-center mb-10">
        <h1 class="text-4xl font-bold text-indigo-700">Danh Sách Đợt Giảm Giá</h1>
        <p class="text-gray-500 mt-2">Quản lý các đợt giảm giá một cách hiệu quả</p>
      </header>
  
      <!-- Search and Actions -->
      <div class="flex flex-col md:flex-row justify-between items-center mb-8 gap-4">
        <div class="w-full md:w-1/3 relative">
          <input
            v-model="searchTerm"
            type="text"
            class="w-full p-3 pl-10 bg-white border border-gray-200 rounded-lg shadow-sm focus:ring-2 focus:ring-indigo-300 focus:outline-none transition-all duration-200"
            placeholder="Tìm kiếm đợt giảm giá..."
          />
          <span class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400">🔍</span>
        </div>
        <div class="flex space-x-3">
          <button
            @click="exportExcel"
            class="bg-indigo-600 text-white px-5 py-2 rounded-lg shadow-md hover:bg-indigo-700 transition-all duration-200 flex items-center"
          >
            <span class="mr-2">📤</span> Export Excel
          </button>
          <button
            @click="openCreateModal"
            class="bg-green-600 text-white px-5 py-2 rounded-lg shadow-md hover:bg-green-700 transition-all duration-200 flex items-center"
          >
            <span class="mr-2">➕</span> Thêm mới
          </button>
        </div>
      </div>
  
      <!-- Filter Section -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <div class="bg-white p-4 rounded-lg shadow-sm">
          <label class="block text-sm font-medium text-gray-700 mb-1">Từ ngày</label>
          <input
            v-model="fromDate"
            type="date"
            class="w-full p-2 bg-gray-50 border border-gray-200 rounded-md focus:ring-2 focus:ring-indigo-300 focus:outline-none transition-all duration-200"
          />
        </div>
        <div class="bg-white p-4 rounded-lg shadow-sm">
          <label class="block text-sm font-medium text-gray-700 mb-1">Đến ngày</label>
          <input
            v-model="toDate"
            type="date"
            class="w-full p-2 bg-gray-50 border border-gray-200 rounded-md focus:ring-2 focus:ring-indigo-300 focus:outline-none transition-all duration-200"
          />
        </div>
        <div class="bg-white p-4 rounded-lg shadow-sm">
          <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <select
            v-model="selectedStatus"
            class="w-full p-2 bg-gray-50 border border-gray-200 rounded-md focus:ring-2 focus:ring-indigo-300 focus:outline-none transition-all duration-200"
          >
            <option value="">Tất cả</option>
            <option v-for="status in statusOptions" :key="status.value" :value="status.value">
              {{ status.text }}
            </option>
          </select>
        </div>
        <div class="bg-white p-4 rounded-lg shadow-sm">
          <label class="block text-sm font-medium text-gray-700 mb-1">Giá trị</label>
          <select
            v-model="selectedValue"
            class="w-full p-2 bg-gray-50 border border-gray-200 rounded-md focus:ring-2 focus:ring-indigo-300 focus:outline-none transition-all duration-200"
          >
            <option value="">Tất cả</option>
            <option v-for="value in valueOptions" :key="value.value" :value="value.value">
              {{ value.text }}
            </option>
          </select>
        </div>
      </div>
  
      <!-- Discount Table -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <table class="min-w-full">
          <thead>
            <tr class="bg-indigo-600 text-white">
              <th class="px-4 py-3 text-left text-sm font-semibold">STT</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">Mã</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">Tên Đợt</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">Loại</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">Giảm (%)</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">Giá trị (VNĐ)</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">Bắt đầu</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">Kết thúc</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">Trạng thái</th>
              <th class="px-4 py-3 text-left text-sm font-semibold">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="loading" class="text-center">
              <td colspan="10" class="px-4 py-3 text-gray-600">
                <span class="flex items-center justify-center">
                  <svg class="animate-spin h-5 w-5 mr-2 text-indigo-500" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8h8a8 8 0 01-8 8v-8H4z"></path>
                  </svg>
                  Đang tải...
                </span>
              </td>
            </tr>
            <tr v-else-if="paginatedDiscounts.length === 0" class="text-center">
              <td colspan="10" class="px-4 py-3 text-gray-600">Không có dữ liệu</td>
            </tr>
            <tr
              v-for="(discount, index) in paginatedDiscounts"
              :key="discount.id"
              class="border-b border-gray-200 hover:bg-gray-50 transition-all duration-200"
            >
              <td class="px-4 py-3 text-gray-700">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
              <td class="px-4 py-3 text-gray-700 font-medium">{{ discount.maDotGiamGia }}</td>
              <td class="px-4 py-3 text-gray-700">{{ discount.tenDotGiamGia }}</td>
              <td class="px-4 py-3 text-gray-700">{{ discount.loaiGiamGia }}</td>
              <td class="px-4 py-3 text-gray-700">{{ discount.soPhanTramGiam || 0 }}%</td>
              <td class="px-4 py-3 text-gray-700">{{ (discount.giaTriGiam || 0).toLocaleString('vi-VN') }}</td>
              <td class="px-4 py-3 text-gray-700">{{ formatDate(discount.ngayBatDau) }}</td>
              <td class="px-4 py-3 text-gray-700">{{ formatDate(discount.ngayKetThuc) }}</td>
              <td class="px-4 py-3">
                <span
                  :class="{
                    'bg-green-100 text-green-700': getDiscountStatus(discount) === 'Đang diễn ra',
                    'bg-red-100 text-red-700': getDiscountStatus(discount) === 'Đã kết thúc',
                    'bg-yellow-100 text-yellow-700': getDiscountStatus(discount) === 'Chưa bắt đầu',
                  }"
                  class="px-2 py-1 rounded-full text-xs font-medium"
                >
                  {{ getDiscountStatus(discount) }}
                </span>
              </td>
              <td class="px-4 py-3">
                <div class="flex space-x-2">
                  <button
                    @click="openUpdateModal(discount)"
                    class="text-indigo-600 hover:text-indigo-800 transition-all duration-200"
                    title="Chỉnh sửa"
                  >
                    ✏️
                  </button>
                  <button
                    @click="deleteDiscount(discount.id)"
                    class="text-red-600 hover:text-red-800 transition-all duration-200"
                    title="Xóa"
                  >
                    🗑️
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
  
        <!-- Pagination -->
        <div class="mt-4 px-4 py-3 flex justify-center items-center bg-gray-50">
          <button
            @click="prevPage"
            :disabled="currentPage === 1"
            class="bg-indigo-600 text-white px-4 py-2 rounded-l-md hover:bg-indigo-700 transition-all duration-200 disabled:bg-gray-300 disabled:text-gray-500"
          >
            Trước
          </button>
          <span class="mx-4 text-sm text-gray-600">Trang {{ currentPage }} / {{ totalPages }}</span>
          <button
            @click="nextPage"
            :disabled="currentPage === totalPages"
            class="bg-indigo-600 text-white px-4 py-2 rounded-r-md hover:bg-indigo-700 transition-all duration-200 disabled:bg-gray-300 disabled:text-gray-500"
          >
            Sau
          </button>
        </div>
      </div>
  
      <!-- Mini Modal for Create/Update -->
      <div
        v-if="showModal"
        class="fixed top-20 right-6 w-full max-w-md bg-white rounded-lg shadow-xl p-6 z-50 animate-mini-modal"
      >
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold text-indigo-700">
            {{ isUpdate ? 'Chỉnh sửa đợt giảm giá' : 'Thêm đợt giảm giá mới' }}
          </h2>
          <button @click="closeModal" class="text-gray-500 hover:text-gray-700 text-xl transition-all duration-200">×</button>
        </div>
  
        <form @submit.prevent="handleSubmit" class="space-y-4">
          <!-- Error Message -->
          <div v-if="errorMessage" class="p-2 bg-red-50 border-l-4 border-red-500 text-red-700 rounded-md text-xs">
            {{ errorMessage }}
          </div>
  
          <div class="space-y-4">
          
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên đợt giảm giá <span class="text-red-500">*</span></label>
              <input
                v-model="modalData.tenDotGiamGia"
                class="w-full p-2 bg-gray-50 border border-gray-200 rounded-md focus:ring-2 focus:ring-indigo-300 focus:outline-none transition-all duration-200"
                placeholder="VD: Giảm giá mùa hè"
                required
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Loại giảm giá <span class="text-red-500">*</span></label>
              <select
                v-model="modalData.loaiGiamGia"
                class="w-full p-2 bg-gray-50 border border-gray-200 rounded-md focus:ring-2 focus:ring-indigo-300 focus:outline-none transition-all duration-200"
                required
              >
                <option value="" disabled>Chọn loại</option>
                <option value="Phần trăm">Phần trăm</option>
                <option value="Tiền mặt">Tiền mặt</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Số phần trăm giảm</label>
              <input
                v-model.number="modalData.soPhanTramGiam"
                type="number"
                min="0"
                max="100"
                class="w-full p-2 bg-gray-50 border border-gray-200 rounded-md focus:ring-2 focus:ring-indigo-300 focus:outline-none transition-all duration-200"
                placeholder="0-100"
                :disabled="modalData.loaiGiamGia === 'Tiền mặt'"
              />
              <span v-if="modalData.loaiGiamGia === 'Phần trăm'" class="text-xs text-gray-500 mt-1 block">Từ 1 đến 100%</span>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Giá trị giảm (VNĐ)</label>
              <input
                v-model.number="modalData.giaTriGiam"
                type="number"
                min="0"
                class="w-full p-2 bg-gray-50 border border-gray-200 rounded-md focus:ring-2 focus:ring-indigo-300 focus:outline-none transition-all duration-200"
                placeholder="VD: 50000"
                :disabled="modalData.loaiGiamGia === 'Phần trăm'"
              />
              <span v-if="modalData.loaiGiamGia === 'Tiền mặt'" class="text-xs text-gray-500 mt-1 block">Giá trị > 0</span>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Ngày bắt đầu <span class="text-red-500">*</span></label>
              <input
                v-model="modalData.ngayBatDau"
                type="date"
                class="w-full p-2 bg-gray-50 border border-gray-200 rounded-md focus:ring-2 focus:ring-indigo-300 focus:outline-none transition-all duration-200"
                required
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Ngày kết thúc <span class="text-red-500">*</span></label>
              <input
                v-model="modalData.ngayKetThuc"
                type="date"
                class="w-full p-2 bg-gray-50 border border-gray-200 rounded-md focus:ring-2 focus:ring-indigo-300 focus:outline-none transition-all duration-200"
                required
              />
            </div>
          </div>
  
          <div class="flex justify-end space-x-3">
            <button
              type="button"
              @click="closeModal"
              class="bg-gray-200 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-300 transition-all duration-200"
            >
              Hủy
            </button>
            <button
              type="submit"
              class="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 transition-all duration-200"
            >
              {{ isUpdate ? 'Lưu' : 'Thêm' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script>
import axios from 'axios';

export default {
  data() {
    return {
      searchTerm: '',
      fromDate: '',
      toDate: '',
      selectedStatus: '',
      selectedValue: '',
      currentPage: 1,
      itemsPerPage: 5,
      discountList: [],
      statusOptions: [
        { value: 'Chưa bắt đầu', text: 'Chưa bắt đầu' },
        { value: 'Đang diễn ra', text: 'Đang diễn ra' },
        { value: 'Đã kết thúc', text: 'Đã kết thúc' },
      ],
      valueOptions: [
        { value: 'low', text: 'Thấp (<50k)' },
        { value: 'medium', text: 'Trung bình (50k-100k)' },
        { value: 'high', text: 'Cao (>100k)' },
      ],
      showModal: false,
      isUpdate: false,
      modalData: {
        id: null,
        maDotGiamGia: '', // Không cần nhập tay khi tạo mới
        tenDotGiamGia: '',
        loaiGiamGia: '',
        soPhanTramGiam: null,
        giaTriGiam: null,
        ngayBatDau: '',
        ngayKetThuc: '',
        trangThai: true,
        moTa: '',
        deleted: false,
      },
      errorMessage: '',
      loading: false,
    };
  },
  computed: {
    filteredDiscounts() {
      return this.discountList.filter((discount) => {
        const matchesSearch =
          this.searchTerm === '' ||
          discount.tenDotGiamGia.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
          discount.maDotGiamGia.toLowerCase().includes(this.searchTerm.toLowerCase());
        const matchesFromDate = !this.fromDate || new Date(discount.ngayBatDau) >= new Date(this.fromDate);
        const matchesToDate = !this.toDate || new Date(discount.ngayKetThuc) <= new Date(this.toDate);
        const matchesStatus = this.selectedStatus === '' || this.getDiscountStatus(discount) === this.selectedStatus;
        const matchesValue =
          this.selectedValue === '' ||
          (this.selectedValue === 'low' && (discount.giaTriGiam || 0) < 50000) ||
          (this.selectedValue === 'medium' && (discount.giaTriGiam || 0) >= 50000 && (discount.giaTriGiam || 0) <= 100000) ||
          (this.selectedValue === 'high' && (discount.giaTriGiam || 0) > 100000);

        return matchesSearch && matchesFromDate && matchesToDate && matchesStatus && matchesValue;
      });
    },
    totalPages() {
      return Math.ceil(this.filteredDiscounts.length / this.itemsPerPage);
    },
    paginatedDiscounts() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      return this.filteredDiscounts.slice(start, start + this.itemsPerPage);
    },
  },
  methods: {
    formatDate(date) {
      return date ? new Date(date).toLocaleDateString('vi-VN') : '';
    },
    getDiscountStatus(discount) {
      const today = new Date();
      const startDate = new Date(discount.ngayBatDau);
      const endDate = new Date(discount.ngayKetThuc);

      if (today < startDate) return 'Chưa bắt đầu';
      if (today >= startDate && today <= endDate) return 'Đang diễn ra';
      return 'Đã kết thúc';
    },
    async fetchDiscounts() {
      this.loading = true;
      try {
        const response = await axios.get('http://localhost:8080/api/discounts');
        this.discountList = response.data.map((discount) => ({
          ...discount,
          ngayBatDau: new Date(discount.ngayBatDau).toISOString().split('T')[0],
          ngayKetThuc: new Date(discount.ngayKetThuc).toISOString().split('T')[0],
        }));
      } catch (error) {
        console.error('Lỗi khi lấy dữ liệu:', error);
        this.errorMessage = 'Không thể tải dữ liệu từ server!';
      } finally {
        this.loading = false;
      }
    },
    exportExcel() {
      // TODO: Thêm logic xuất Excel (dùng thư viện như xlsx)
      console.log('Exporting to Excel...');
    },
    openCreateModal() {
      this.isUpdate = false;
      this.errorMessage = '';
      this.modalData = {
        id: null,
        maDotGiamGia: '', // Backend tự sinh
        tenDotGiamGia: '',
        loaiGiamGia: '',
        soPhanTramGiam: null,
        giaTriGiam: null,
        ngayBatDau: '',
        ngayKetThuc: '',
        trangThai: true,
        moTa: '',
        deleted: false,
      };
      this.showModal = true;
    },
    openUpdateModal(discount) {
      this.isUpdate = true;
      this.errorMessage = '';
      this.modalData = {
        ...discount,
        ngayBatDau: new Date(discount.ngayBatDau).toISOString().split('T')[0],
        ngayKetThuc: new Date(discount.ngayKetThuc).toISOString().split('T')[0],
      };
      this.showModal = true;
    },
    closeModal() {
      this.showModal = false;
      this.errorMessage = '';
    },
    validateForm() {
      if (!this.modalData.tenDotGiamGia || !this.modalData.loaiGiamGia || !this.modalData.ngayBatDau || !this.modalData.ngayKetThuc) {
        this.errorMessage = 'Vui lòng điền đầy đủ các trường bắt buộc!';
        return false;
      }
      if (this.modalData.moTa && this.modalData.moTa.length > 100) {
        this.errorMessage = 'Mô tả không được vượt quá 100 ký tự!';
        return false;
      }
      if (new Date(this.modalData.ngayBatDau) >= new Date(this.modalData.ngayKetThuc)) {
        this.errorMessage = 'Ngày bắt đầu phải nhỏ hơn ngày kết thúc!';
        return false;
      }
      if (
        this.modalData.loaiGiamGia === 'Phần trăm' &&
        (this.modalData.soPhanTramGiam === null || this.modalData.soPhanTramGiam <= 0 || this.modalData.soPhanTramGiam > 100)
      ) {
        this.errorMessage = 'Số phần trăm giảm phải từ 1 đến 100!';
        return false;
      }
      if (
        this.modalData.loaiGiamGia === 'Tiền mặt' &&
        (this.modalData.giaTriGiam === null || this.modalData.giaTriGiam <= 0)
      ) {
        this.errorMessage = 'Giá trị giảm phải lớn hơn 0!';
        return false;
      }
      return true;
    },
    async handleSubmit() {
      if (!this.validateForm()) return;

      const payload = {
        ...this.modalData,
        ngayBatDau: new Date(this.modalData.ngayBatDau).toISOString(),
        ngayKetThuc: new Date(this.modalData.ngayKetThuc).toISOString(),
        soPhanTramGiam: this.modalData.loaiGiamGia === 'Phần trăm' ? this.modalData.soPhanTramGiam : null,
        giaTriGiam: this.modalData.loaiGiamGia === 'Tiền mặt' ? this.modalData.giaTriGiam : null,
        trangThai: this.modalData.trangThai ?? true,
        deleted: this.modalData.deleted ?? false,
        maDotGiamGia: this.isUpdate ? this.modalData.maDotGiamGia : undefined, // Không gửi maDotGiamGia khi tạo mới
      };

      try {
        if (this.isUpdate) {
          const response = await axios.put(`http://localhost:8080/api/discounts/${this.modalData.id}`, payload);
          const index = this.discountList.findIndex((d) => d.id === this.modalData.id);
          if (index !== -1) this.discountList.splice(index, 1, response.data);
        } else {
          const response = await axios.post('http://localhost:8080/api/discounts', payload);
          this.discountList.push(response.data);
        }
        this.closeModal();
        await this.fetchDiscounts(); // Reload để đảm bảo dữ liệu mới nhất
      } catch (error) {
        console.error('Lỗi khi lưu:', error);
        this.errorMessage = error.response?.data?.message || 'Không thể lưu dữ liệu!';
      }
    },
    async deleteDiscount(id) {
      if (confirm('Bạn có chắc chắn muốn xóa đợt giảm giá này?')) {
        try {
          await axios.delete(`http://localhost:8080/api/discounts/${id}`);
          // Xóa bản ghi khỏi danh sách mà không cần reload toàn bộ
          this.discountList = this.discountList.filter((d) => d.id !== id);
        } catch (error) {
          console.error('Lỗi khi xóa:', error);
          this.errorMessage = 'Không thể xóa dữ liệu!';
          await this.fetchDiscounts(); // Reload nếu có lỗi để đồng bộ dữ liệu
        }
      }
    },
    prevPage() {
      if (this.currentPage > 1) this.currentPage--;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) this.currentPage++;
    },
  },
  mounted() {
    this.fetchDiscounts();
  },
};

</script>
  
  <style scoped>
  .font-sans {
    font-family: 'Inter', sans-serif;
  }
  
  /* Mini Modal Animation */
  @keyframes miniModalFadeIn {
    from { opacity: 0; transform: translateX(100px); }
    to { opacity: 1; transform: translateX(0); }
  }
  
  .animate-mini-modal {
    animation: miniModalFadeIn 0.3s ease-out forwards;
  }
  
  /* General Styling */
  .shadow-md {
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  }
  
  .shadow-xl {
    box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  }
  
  input:disabled,
  select:disabled {
    background-color: #e5e7eb;
    cursor: not-allowed;
    opacity: 0.7;
  }
  
  /* Responsive */
  @media (max-width: 768px) {
    .md\:flex-row {
      flex-direction: column;
    }
   
    .md\:grid-cols-2 {
      grid-template-columns: 1fr;
    }
    .fixed.top-20.right-6 {
      top: 10px;
      right: 10px;
      width: calc(100% - 20px);
      max-width: none;
    }
  }
  
  @media (max-width: 640px) {
    .sm\:grid-cols-2 {
      grid-template-columns: 1fr;
    }
  }
  </style>