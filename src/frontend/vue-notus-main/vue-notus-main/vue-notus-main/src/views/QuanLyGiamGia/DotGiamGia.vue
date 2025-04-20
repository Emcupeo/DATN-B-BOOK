<template>
  <div class="min-h-screen bg-gray-100 p-6 font-sans relative">
    <!-- White Frame (Main Interface) -->
    <div class="bg-white rounded-lg shadow-md p-6">
      <!-- Header and Actions -->
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-2xl font-semibold text-gray-800">Đợt giảm giá</h1>
        <!-- Show buttons only when not in create form -->
        <div v-if="!showForm && !selectedDiscount" class="flex space-x-4">
          <select
            v-model="selectedStatus"
            class="w-48 p-2 bg-gray-50 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
          >
            <option value="">Tất cả trạng thái</option>
            <option v-for="status in statusOptions" :key="status.value" :value="status.value">
              {{ status.text }}
            </option>
          </select>
          <input
            v-model="searchTerm"
            type="text"
            class="w-48 p-2 bg-gray-50 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
            placeholder="Tìm kiếm theo tên..."
          />
          <button
            @click="openCreateForm"
            class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-all duration-200 flex items-center"
          >
            <span class="mr-2">➕</span> Thêm đợt giảm giá
          </button>
        </div>
      </div>

      <!-- Create/Update Form (Two Columns) -->
      <div v-if="showForm || selectedDiscount" class="mb-6 flex flex-col md:flex-row gap-6">
        <!-- Form (Left Column) -->
        <div class="w-full md:w-1/2 p-4 bg-gray-50 rounded-md shadow-inner">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-xl font-semibold text-gray-800">{{ isUpdate ? 'Cập nhật đợt giảm giá' : 'Thêm đợt giảm giá' }}</h2>
            <button @click="closeForm" class="text-gray-500 hover:text-gray-700 text-xl transition-all duration-200">×</button>
          </div>

          <form @submit.prevent="handleSubmit" class="space-y-4">
            <div v-if="errorMessage" class="p-2 bg-red-50 border-l-4 border-red-500 text-red-700 rounded-md text-xs">
              {{ errorMessage }}
            </div>

            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Mã</label>
                <input
                  v-model="formData.maDotGiamGia"
                  class="w-full p-2 bg-gray-200 border border-gray-300 rounded-md"
                  placeholder="Nhập mã đợt giảm giá"
                  disabled
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">* Tên Đợt giảm giá</label>
                <input
                  v-model="formData.tenDotGiamGia"
                  class="w-full p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
                  placeholder="Nhập tên đợt giảm giá"
                  required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">* Loại giảm giá</label>
                <select
                  v-model="formData.loaiGiamGia"
                  class="w-full p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
                  required
                >
                  <option value="Phần trăm">Phần trăm</option>
                  <option value="Tiền mặt">Tiền mặt</option>
                </select>
              </div>
              <div v-if="formData.loaiGiamGia === 'Phần trăm'">
                <label class="block text-sm font-medium text-gray-700 mb-1">* Giá trị (%)</label>
                <input
                  v-model.number="formData.soPhanTramGiam"
                  type="number"
                  min="1"
                  max="100"
                  class="w-full p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
                  required
                />
              </div>
              <div v-if="formData.loaiGiamGia === 'Tiền mặt'">
                <label class="block text-sm font-medium text-gray-700 mb-1">* Số tiền tối đa</label>
                <input
                  v-model.number="formData.giaTriGiam"
                  type="number"
                  min="0"
                  class="w-full p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
                  required
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">* Ngày bắt đầu</label>
                <input
                  v-model="formData.ngayBatDau"
                  type="date"
                  :class="{ 'border-red-500': isDateInvalid }"
                  class="w-full p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
                  required
                  @input="validateDates"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">* Ngày kết thúc</label>
                <input
                  v-model="formData.ngayKetThuc"
                  type="date"
                  :class="{ 'border-red-500': isDateInvalid }"
                  class="w-full p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200"
                  required
                  @input="validateDates"
                />
              </div>
            </div>

            <div class="flex justify-end mt-4 space-x-2">
              <button
                type="submit"
                class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition-all duration-200"
              >
                {{ isUpdate ? 'CẬP NHẬT' : 'TẠO MỚI' }}
              </button>
              <button
                v-if="!isUpdate"
                type="button"
                @click="selectAllProducts"
                class="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 transition-all duration-200"
              >
                Chọn tất cả
              </button>
              <button
                v-if="!isUpdate"
                type="button"
                @click="deselectAllProducts"
                class="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600 transition-all duration-200"
              >
                Bỏ chọn tất cả
              </button>
            </div>
          </form>
        </div>

        <!-- Product Selection (Right Column) -->
        <div class="w-full md:w-1/2 p-4 bg-gray-50 rounded-md shadow-inner">
          <!-- Product Selection -->
          <div>
            <div class="flex justify-between items-center mb-4">
              <h3 class="text-lg font-semibold text-gray-800">Sản Phẩm</h3>
              <input
                v-model="productSearchTerm"
                type="text"
                class="p-2 bg-white border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none transition-all duration-200 w-1/2"
                placeholder="Tìm kiếm theo tên sách..."
              />
            </div>
            <table class="min-w-full bg-white border border-gray-200">
              <thead>
                <tr class="bg-gray-100 text-gray-700 text-left">
                  <th class="px-4 py-2 text-sm font-semibold border-b"></th>
                  <th class="px-4 py-2 text-sm font-semibold border-b">#</th>
                  <th class="px-4 py-2 text-sm font-semibold border-b">Tên sách</th>
                  <th class="px-4 py-2 text-sm font-semibold border-b">Số lượng tồn</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="loadingProducts" class="text-center">
                  <td colspan="4" class="px-4 py-3 text-gray-600">
                    <span class="flex items-center justify-center">
                      <svg class="animate-spin h-5 w-5 mr-2 text-blue-500" viewBox="0 0 24 24">
                        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8h8a8 8 0 01-8 8v-8H4z"></path>
                      </svg>
                      Đang tải sản phẩm...
                    </span>
                  </td>
                </tr>
                <tr v-else-if="paginatedProducts.length === 0" class="text-center">
                  <td colspan="4" class="px-4 py-3 text-gray-600">Không có dữ liệu</td>
                </tr>
                <tr
                  v-for="(product, index) in paginatedProducts"
                  :key="product.id"
                  class="border-b border-gray-200 hover:bg-gray-50 transition-all duration-200 cursor-pointer"
                  @click="toggleProductSelection(product.id)"
                >
                  <td class="px-4 py-2 text-gray-700">
                    <input
                      type="checkbox"
                      v-model="formData.selectedProducts"
                      :value="product.id"
                      class="mr-2"
                      @click.stop
                    />
                  </td>
                  <td class="px-4 py-2 text-gray-700">{{ (currentProductPage - 1) * productsPerPage + index + 1 }}</td>
                  <td class="px-4 py-2 text-gray-700">{{ product.tenChiTietSanPham }}</td>
                  <td class="px-4 py-2 text-gray-700">{{ product.soLuongTon || 0 }}</td>
                </tr>
              </tbody>
            </table>

            <div class="mt-4 flex justify-end">
              <div class="flex items-center space-x-2">
                <button
                  @click="prevProductPage"
                  :disabled="currentProductPage === 1"
                  class="bg-blue-500 text-white px-2 py-1 rounded-md hover:bg-blue-700 transition-all duration-200 disabled:bg-gray-300 disabled:text-gray-500"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
                  </svg>
                </button>
                <span class="text-sm text-gray-700">{{ currentProductPage }} / {{ totalProductPages }}</span>
                <button
                  @click="nextProductPage"
                  :disabled="currentProductPage === totalProductPages"
                  class="bg-blue-500 text-white px-2 py-1 rounded-md hover:bg-blue-700 transition-all duration-200 disabled:bg-gray-300 disabled:text-gray-500"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                  </svg>
                </button>
              </div>
            </div>

            <!-- Selected Products -->
            <div v-if="formData.selectedProducts.length > 0" class="mt-6">
              <h3 class="text-lg font-semibold text-gray-800 mb-4">Chi Tiết Sản Phẩm</h3>
              <table class="min-w-full bg-white border border-gray-200">
                <thead>
                  <tr class="bg-gray-100 text-gray-700 text-left">
                    <th class="px-4 py-2 text-sm font-semibold border-b">#</th>
                    <th class="px-4 py-2 text-sm font-semibold border-b">Tên sách & Mã số</th>
                    <th class="px-4 py-2 text-sm font-semibold border-b">Nhà xuất bản</th>
                    <th class="px-4 py-2 text-sm font-semibold border-b">Số lượng tồn</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="selectedProductDetails.length === 0" class="text-center">
                    <td colspan="4" class="px-4 py-3 text-gray-600">Chưa chọn sản phẩm</td>
                  </tr>
                  <tr
                    v-for="(product, index) in selectedProductDetails"
                    :key="product.id"
                    class="border-b border-gray-200 hover:bg-gray-50 transition-all duration-200"
                  >
                    <td class="px-4 py-2 text-gray-700">{{ index + 1 }}</td>
                    <td class="px-4 py-2 text-gray-700">
                      {{ product.tenChiTietSanPham }} - {{ product.maChiTietSanPham }}
                    </td>
                    <td class="px-4 py-2 text-gray-700">{{ product.tenNhaXuatBan || 'Không xác định' }}</td>
                    <td class="px-4 py-2 text-gray-700">{{ product.soLuongTon || 0 }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <!-- Discount Table (Show only when not in create form) -->
      <div v-if="!showForm && !selectedDiscount" class="overflow-x-auto">
        <table class="min-w-full bg-white border border-gray-200">
          <thead>
            <tr class="bg-gray-100 text-gray-700 text-left">
              <th class="px-4 py-2 text-sm font-semibold border-b">#</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Tên Đợt giảm giá</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Loại giảm giá</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Giá trị giảm</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Trạng thái</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Thời gian bắt đầu</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Thời gian kết thúc</th>
              <th class="px-4 py-2 text-sm font-semibold border-b">Hoạt động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="loading" class="text-center">
              <td colspan="8" class="px-4 py-3 text-gray-600">
                <span class="flex items-center justify-center">
                  <svg class="animate-spin h-5 w-5 mr-2 text-blue-500" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8h8a8 8 0 01-8 8v-8H4z"></path>
                  </svg>
                  Đang tải...
                </span>
              </td>
            </tr>
            <tr v-else-if="paginatedDiscounts.length === 0" class="text-center">
              <td colspan="8" class="px-4 py-3 text-gray-600">Không có dữ liệu</td>
            </tr>
            <tr
              v-for="(discount, index) in paginatedDiscounts"
              :key="discount.id"
              class="border-b border-gray-200 hover:bg-gray-50 transition-all duration-200"
            >
              <td class="px-4 py-2 text-gray-700">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
              <td class="px-4 py-2 text-gray-700 font-medium">{{ discount.tenDotGiamGia }}</td>
              <td class="px-4 py-2 text-gray-700">{{ discount.loaiGiamGia }}</td>
              <td class="px-4 py-2 text-gray-700">
                {{ discount.loaiGiamGia === 'Phần trăm' ? `${discount.soPhanTramGiam}%` : `${discount.giaTriGiam || 0} đ` }}
              </td>
              <td class="px-4 py-2">
                <span
                  :class="{
                    'bg-green-100 text-green-700': discount.trangThai === 'Đang diễn ra',
                    'bg-red-100 text-red-700': discount.trangThai === 'Đã kết thúc',
                    'bg-yellow-100 text-yellow-700': discount.trangThai === 'Chưa bắt đầu',
                  }"
                  class="px-2 py-1 rounded-full text-xs font-medium"
                >
                  {{ discount.trangThai || getDiscountStatus(discount) }}
                </span>
              </td>
              <td class="px-4 py-2 text-gray-700">{{ formatDate(discount.ngayBatDau) }}</td>
              <td class="px-4 py-2 text-gray-700">{{ formatDate(discount.ngayKetThuc) }}</td>
              <td class="px-4 py-2">
                <button
                  @click="openUpdateForm(discount)"
                  class="text-blue-500 hover:text-blue-700 transition-all duration-200 mr-2"
                  title="Xem chi tiết"
                >
                  👁️
                </button>
                <button
                  @click="deleteDiscount(discount.id)"
                  class="text-red-500 hover:text-red-700 transition-all duration-200"
                  title="Xóa đợt giảm giá"
                >
                  🗑️
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- Pagination -->
        <div class="mt-4 flex justify-center">
          <div class="flex items-center space-x-2">
            <button
              @click="prevPage"
              :disabled="currentPage === 1"
              class="bg-blue-500 text-white px-2 py-1 rounded-md hover:bg-blue-700 transition-all duration-200 disabled:bg-gray-300 disabled:text-gray-500"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
              </svg>
            </button>
            <span class="text-sm text-gray-700">Trang {{ currentPage }} / {{ totalPages }}</span>
            <button
              @click="nextPage"
              :disabled="currentPage === totalPages"
              class="bg-blue-500 text-white px-2 py-1 rounded-md hover:bg-blue-700 transition-all duration-200 disabled:bg-gray-300 disabled:text-gray-500"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
              </svg>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DotGiamGiaService from '@/service/DotGiamGiaService';

export default {
  data() {
    return {
      searchTerm: '',
      selectedStatus: '',
      currentPage: 1,
      itemsPerPage: 5,
      discountList: [],
      statusOptions: [
        { value: 'Chưa bắt đầu', text: 'Chưa bắt đầu' },
        { value: 'Đang diễn ra', text: 'Đang diễn ra' },
        { value: 'Đã kết thúc', text: 'Đã kết thúc' },
      ],
      showForm: false,
      isUpdate: false,
      selectedDiscount: null,
      formData: {
        id: null,
        maDotGiamGia: '',
        tenDotGiamGia: '',
        loaiGiamGia: 'Phần trăm',
        soPhanTramGiam: 0,
        giaTriGiam: 0,
        ngayBatDau: '',
        ngayKetThuc: '',
        trangThai: 'Chưa bắt đầu',
        selectedProducts: [],
      },
      errorMessage: '',
      loading: false,
      loadingProducts: false,
      isDateInvalid: false,
      products: [],
      selectedProductDetails: [],
      productSearchTerm: '',
      currentProductPage: 1,
      productsPerPage: 5,
    };
  },
  computed: {
    filteredDiscounts() {
      return this.discountList.filter((discount) => {
        const matchesSearch =
          this.searchTerm === '' ||
          discount.tenDotGiamGia.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
          discount.maDotGiamGia.toLowerCase().includes(this.searchTerm.toLowerCase());
        const matchesStatus = this.selectedStatus === '' || discount.trangThai === this.selectedStatus;
        return matchesSearch && matchesStatus;
      });
    },
    totalPages() {
      return Math.ceil(this.filteredDiscounts.length / this.itemsPerPage);
    },
    paginatedDiscounts() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      return this.filteredDiscounts.slice(start, start + this.itemsPerPage);
    },
    filteredProducts() {
      return this.products.filter((product) =>
        product.tenChiTietSanPham.toLowerCase().includes(this.productSearchTerm.toLowerCase())
      );
    },
    totalProductPages() {
      return Math.ceil(this.filteredProducts.length / this.productsPerPage);
    },
    paginatedProducts() {
      const start = (this.currentProductPage - 1) * this.productsPerPage;
      return this.filteredProducts.slice(start, start + this.productsPerPage);
    },
  },
  methods: {
    formatDate(date) {
      return date ? new Date(date).toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' }) : '';
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
        const discounts = await DotGiamGiaService.getListDiscounts();
        this.discountList = discounts.map((discount) => ({
          ...discount,
          maDotGiamGia: discount.maDotGiamGia || '',
          loaiGiamGia: discount.loaiGiamGia || 'Phần trăm',
          soPhanTramGiam: discount.soPhanTramGiam || 0,
          giaTriGiam: discount.giaTriGiam || 0,
          ngayBatDau: discount.ngayBatDau,
          ngayKetThuc: discount.ngayKetThuc,
          trangThai: this.getDiscountStatus(discount),
          dotGiamGiaChiTiets: discount.dotGiamGiaChiTiets || [],
        }));
      } catch (error) {
        console.error('Lỗi khi lấy dữ liệu:', error);
        this.errorMessage = error.message;
      } finally {
        this.loading = false;
      }
    },
    async fetchProducts() {
      this.loadingProducts = true;
      try {
        const products = await DotGiamGiaService.getAllProducts();
        this.products = products.map((product) => ({
          ...product,
          tenChiTietSanPham: product.tenChiTietSanPham || 'Không xác định',
          maChiTietSanPham: product.maChiTietSanPham || '',
          soLuongTon: product.soLuongTon || 0,
          tenNhaXuatBan: product.tenNhaXuatBan || 'Không xác định',
        }));
      } catch (error) {
        console.error('Lỗi khi lấy sản phẩm:', error);
        this.errorMessage = error.message;
      } finally {
        this.loadingProducts = false;
      }
    },
    toggleProductSelection(productId) {
      const index = this.formData.selectedProducts.indexOf(productId);
      if (index === -1) {
        this.formData.selectedProducts.push(productId);
      } else {
        this.formData.selectedProducts.splice(index, 1);
      }
      this.updateSelectedProductDetails();
    },
    updateSelectedProductDetails() {
      this.selectedProductDetails = this.formData.selectedProducts
        .map((id) => this.products.find((p) => p.id === id))
        .filter(Boolean);
    },
    openCreateForm() {
      this.isUpdate = false;
      this.selectedDiscount = null;
      this.errorMessage = '';
      this.formData = {
        id: null,
        maDotGiamGia: '',
        tenDotGiamGia: '',
        loaiGiamGia: 'Phần trăm',
        soPhanTramGiam: 0,
        giaTriGiam: 0,
        ngayBatDau: '',
        ngayKetThuc: '',
        trangThai: 'Chưa bắt đầu',
        selectedProducts: [],
      };
      this.selectedProductDetails = [];
      this.showForm = true;
      this.fetchProducts();
    },
    openUpdateForm(discount) {
      this.isUpdate = true;
      this.selectedDiscount = discount;
      this.errorMessage = '';
      this.formData = {
        id: discount.id,
        maDotGiamGia: discount.maDotGiamGia || '',
        tenDotGiamGia: discount.tenDotGiamGia || '',
        loaiGiamGia: discount.loaiGiamGia || 'Phần trăm',
        soPhanTramGiam: discount.soPhanTramGiam || 0,
        giaTriGiam: discount.giaTriGiam || 0,
        ngayBatDau: discount.ngayBatDau ? new Date(discount.ngayBatDau).toISOString().split('T')[0] : '',
        ngayKetThuc: discount.ngayKetThuc ? new Date(discount.ngayKetThuc).toISOString().split('T')[0] : '',
        trangThai: discount.trangThai || 'Chưa bắt đầu',
        selectedProducts: discount.dotGiamGiaChiTiets ? discount.dotGiamGiaChiTiets.map(item => item.idChiTietSanPham.id) : [],
      };
      this.updateSelectedProductDetails();
      this.showForm = false;
      this.fetchProducts();
    },
    closeForm() {
      this.showForm = false;
      this.selectedDiscount = null;
      this.errorMessage = '';
      this.isDateInvalid = false;
      this.selectedProductDetails = [];
      this.formData.selectedProducts = [];
    },
    validateDates() {
      if (!this.formData.ngayBatDau || !this.formData.ngayKetThuc) {
        this.isDateInvalid = false;
        this.errorMessage = '';
        return;
      }
      const startDate = new Date(this.formData.ngayBatDau);
      const endDate = new Date(this.formData.ngayKetThuc);
      this.isDateInvalid = startDate >= endDate;
      if (this.isDateInvalid) {
        this.errorMessage = 'Ngày bắt đầu phải nhỏ hơn ngày kết thúc!';
      } else {
        this.errorMessage = '';
      }
    },
    validateForm() {
      if (!this.formData.tenDotGiamGia || !this.formData.ngayBatDau || !this.formData.ngayKetThuc) {
        this.errorMessage = 'Vui lòng điền đầy đủ các trường bắt buộc!';
        return false;
      }
      if (this.isDateInvalid) {
        this.errorMessage = 'Ngày bắt đầu phải nhỏ hơn ngày kết thúc!';
        return false;
      }
      if (this.formData.loaiGiamGia === 'Phần trăm' && (this.formData.soPhanTramGiam <= 0 || this.formData.soPhanTramGiam > 100)) {
        this.errorMessage = 'Giá trị giảm phải từ 1 đến 100!';
        return false;
      }
      if (this.formData.loaiGiamGia === 'Tiền mặt' && this.formData.giaTriGiam <= 0) {
        this.errorMessage = 'Số tiền tối đa phải lớn hơn 0!';
        return false;
      }
      if (!this.isUpdate && this.formData.selectedProducts.length === 0) {
        this.errorMessage = 'Vui lòng chọn ít nhất một sản phẩm!';
        return false;
      }
      return true;
    },
    async handleSubmit() {
      if (!this.validateForm()) return;

      const payload = {
        tenDotGiamGia: this.formData.tenDotGiamGia,
        loaiGiamGia: this.formData.loaiGiamGia,
        soPhanTramGiam: this.formData.loaiGiamGia === 'Phần trăm' ? this.formData.soPhanTramGiam : null,
        giaTriGiam: this.formData.loaiGiamGia === 'Tiền mặt' ? this.formData.giaTriGiam : null,
        ngayBatDau: new Date(this.formData.ngayBatDau).toISOString(),
        ngayKetThuc: new Date(this.formData.ngayKetThuc).toISOString(),
        selectedProducts: this.formData.selectedProducts,
      };

      try {
        if (this.isUpdate) {
          const response = await DotGiamGiaService.updateDiscount(this.formData.id, payload);
          const index = this.discountList.findIndex((d) => d.id === this.formData.id);
          if (index !== -1) {
            this.discountList.splice(index, 1, {
              ...response,
              maDotGiamGia: response.maDotGiamGia || '',
              loaiGiamGia: response.loaiGiamGia || 'Phần trăm',
              soPhanTramGiam: response.soPhanTramGiam || 0,
              giaTriGiam: response.giaTriGiam || 0,
              ngayBatDau: response.ngayBatDau,
              ngayKetThuc: response.ngayKetThuc,
              trangThai: this.getDiscountStatus(response),
              dotGiamGiaChiTiets: response.dotGiamGiaChiTiets || [],
            });
          }
        } else {
          const response = await DotGiamGiaService.createDiscount(payload);
          this.discountList.push({
            ...response,
            maDotGiamGia: response.maDotGiamGia || '',
            loaiGiamGia: response.loaiGiamGia || 'Phần trăm',
            soPhanTramGiam: response.soPhanTramGiam || 0,
            giaTriGiam: response.giaTriGiam || 0,
            ngayBatDau: response.ngayBatDau,
            ngayKetThuc: response.ngayKetThuc,
            trangThai: this.getDiscountStatus(response),
            dotGiamGiaChiTiets: response.dotGiamGiaChiTiets || [],
          });
        }
        this.closeForm();
      } catch (error) {
        console.error('Lỗi khi lưu:', error);
        this.errorMessage = error.message || 'Có lỗi xảy ra khi lưu dữ liệu!';
      }
    },
    async deleteDiscount(discountId) {
      if (!confirm('Bạn có chắc chắn muốn xóa đợt giảm giá này không?')) return;

      try {
        await DotGiamGiaService.deleteDiscount(discountId); // Gọi API để cập nhật deleted = 1
        const index = this.discountList.findIndex((d) => d.id === discountId);
        if (index !== -1) {
          this.discountList.splice(index, 1); // Xóa khỏi danh sách giao diện
        }
        if (this.paginatedDiscounts.length === 0 && this.currentPage > 1) {
          this.currentPage--; // Giảm trang nếu trang hiện tại trống
        }
      } catch (error) {
        console.error('Lỗi khi xóa:', error);
        this.errorMessage = error.message || 'Có lỗi xảy ra khi xóa đợt giảm giá!';
      }
    },
    selectAllProducts() {
      this.formData.selectedProducts = this.paginatedProducts.map(product => product.id);
      this.updateSelectedProductDetails();
    },
    deselectAllProducts() {
      this.formData.selectedProducts = [];
      this.selectedProductDetails = [];
    },
    prevPage() {
      if (this.currentPage > 1) this.currentPage--;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) this.currentPage++;
    },
    prevProductPage() {
      if (this.currentProductPage > 1) this.currentProductPage--;
    },
    nextProductPage() {
      if (this.currentProductPage < this.totalProductPages) this.currentProductPage++;
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

input:disabled,
select:disabled {
  background-color: #e5e7eb;
  cursor: not-allowed;
  opacity: 0.7;
}

input,
select {
  padding: 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  background-color: #f9fafb;
  transition: all 0.2s ease-in-out;
}

input:focus,
select:focus {
  outline: none;
  ring: 2px solid #3b82f6;
}

.bg-gray-50 {
  background-color: #f9fafb;
}

.border-red-500 {
  border-color: #ef4444;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 0.5rem;
  border-bottom: 1px solid #e5e5e5;
}

th {
  background-color: #f3f4f6;
  font-weight: 600;
  font-size: 0.875rem;
}

tr:hover {
  background-color: #f9fafb;
}

tr:nth-child(even) {
  background-color: #f9fafb;
}

@media (max-width: 768px) {
  .md\:flex-row {
    flex-direction: column;
  }

  .w-48 {
    width: 100%;
  }
}
</style>