<template>
  <div class="flex flex-wrap mt-4">
    <div class="w-full mb-12 px-4">
      <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded bg-white">
        <div class="rounded-t mb-0 px-4 py-3 border-0">
          <div class="flex flex-wrap items-center">
            <div class="relative w-full px-4 max-w-full flex-grow flex-1">
              <h3 class="font-semibold text-lg text-blueGray-700">
                Danh sách khách hàng
              </h3>
            </div>
            <div class="flex space-x-2">
              <button type="button" @click="exportToExcel"
                      class="flex items-center gap-2 text-white bg-gradient-to-r from-green-600 via-green-700 to-green-800 hover:from-green-700 hover:via-green-800 hover:to-green-900 focus:ring-4 focus:outline-none focus:ring-green-400 dark:focus:ring-green-900 font-medium rounded-lg text-sm px-5 py-2.5 text-center mt-5">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" viewBox="0,0,256,256">
                  <g fill="#ffffff">
                    <g transform="scale(5.12,5.12)">
                      <path
                          d="M16,4c-1.65,0 -3,1.35 -3,3v4h2v-4c0,-0.55 0.45,-1 1,-1h14v8h-3.49023c0.29,0.61 0.46047,1.28 0.48047,2h3.00977v8h-3v2h3v8h-3.00977c-0.02,0.72 -0.19047,1.39 -0.48047,2h3.49023v8h-14c-0.55,0 -1,-0.45 -1,-1v-4h-2v4c0,1.65 1.35,3 3,3h30c1.65,0 3,-1.35 3,-3v-36c0,-1.65 -1.35,-3 -3,-3zM32,6h14c0.55,0 1,0.45 1,1v7h-15zM4.19922,13c-1.75547,0 -3.19922,1.44375 -3.19922,3.19922v17.60156c0,1.75547 1.44375,3.19922 3.19922,3.19922h17.60156c1.75547,0 3.19922,-1.44375 3.19922,-3.19922v-17.60156c0,-1.75547 -1.44375,-3.19922 -3.19922,-3.19922zM4.19922,15h17.60156c0.67453,0 1.19922,0.52468 1.19922,1.19922v17.60156c0,0.67453 -0.52469,1.19922 -1.19922,1.19922h-17.60156c-0.67453,0 -1.19922,-0.52468 -1.19922,-1.19922v-17.60156c0,-0.67453 0.52469,-1.19922 1.19922,-1.19922zM32,16h15v8h-15zM7.96875,19l3.49414,5.97852l-3.83203,6.02148h2.86328l2.52148,-4.7168l2.5332,4.7168h2.82031l-3.76953,-6l3.68555,-6h-2.67578l-2.45508,4.50586l-2.32422,-4.50586zM32,26h15v8h-15zM32,36h15v7c0,0.55 -0.45,1 -1,1h-14z">
                      </path>
                    </g>
                  </g>
                </svg>
                Xuất Excel
              </button>

              <router-link to="/admin/khach-hang/them-moi"
                           class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-600 via-blue-700 to-blue-800 hover:from-blue-700 hover:via-blue-800 hover:to-blue-900 focus:ring-4 focus:outline-none focus:ring-blue-400 dark:focus:ring-blue-900 font-medium rounded-lg text-sm px-5 py-2.5 text-center mt-5">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                     stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M12 5v14M5 12h14" />
                </svg>
                Thêm mới
              </router-link>

            </div>
          </div>
        </div>

        <div class="block w-full overflow-x-auto">
          <!-- Search and filter -->
          <div class="p-4 flex space-x-4">
            <div class="flex-1">
              <input type="text" v-model="searchQuery"
                     placeholder="Tìm kiếm theo tên, email, số điện thoại..."
                     class="w-full border rounded px-3 py-2"
                     @input="handleSearch">
            </div>
            <div>
              <select v-model="statusFilter" class="border rounded px-3 py-2" @change="handleSearch">
                <option value="">Tất cả trạng thái</option>
                <option value="true">Hoạt động</option>
                <option value="false">Không hoạt động</option>
              </select>
            </div>
          </div>

          <table class="items-center w-full bg-transparent border-collapse">
            <thead>
            <tr>
              <th class="px-6 py-3 text-xs uppercase font-semibold text-left bg-blueGray-50 text-blueGray-500 border border-blueGray-100">
                #
              </th>
              <th class="px-6 align-middle border border-solid py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left bg-blueGray-50 text-blueGray-500 border-blueGray-100">
                Mã khách hàng
              </th>
              <th class="px-6 align-middle border border-solid py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left bg-blueGray-50 text-blueGray-500 border-blueGray-100">
                Họ tên
              </th>
              <th class="px-6 align-middle border border-solid py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left bg-blueGray-50 text-blueGray-500 border-blueGray-100">
                Email
              </th>
              <th class="px-6 align-middle border border-solid py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left bg-blueGray-50 text-blueGray-500 border-blueGray-100">
                Số điện thoại
              </th>
              <th class="px-6 align-middle border border-solid py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left bg-blueGray-50 text-blueGray-500 border-blueGray-100">
                Địa chỉ mặc định
              </th>
              <th class="px-6 align-middle border border-solid py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left bg-blueGray-50 text-blueGray-500 border-blueGray-100">
                Trạng thái
              </th>
              <th class="px-6 align-middle border border-solid py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left bg-blueGray-50 text-blueGray-500 border-blueGray-100">
                Thao tác
              </th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(khachHang, index) in displayedKhachHang" :key="khachHang.id">
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                {{ index + 1 }}
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                {{ khachHang.maKhachHang }}
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                {{ khachHang.hoTen }}
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                {{ khachHang.email }}
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                {{ khachHang.soDienThoai }}
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs p-4">
              <span :title="getFullAddress(khachHang)">
                {{ getDefaultAddress(khachHang) }}
              </span>
              </td>

              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                <div class="flex items-center">
                  <label class="relative inline-flex items-center cursor-pointer">
                    <input type="checkbox"
                           :checked="khachHang.trangThai === 1"
                           class="sr-only peer"
                           @change="toggleStatus(khachHang)">
                    <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                  </label>
                  <span class="ml-2">{{ khachHang.trangThai === 1 ? 'Hoạt động' : 'Không hoạt động' }}</span>
                </div>
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                <div class="flex space-x-2">
                  <router-link :to="'/admin/khach-hang/chinh-sua/' + khachHang.id"
                               class="text-blue-600 hover:text-blue-800">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                         stroke="currentColor" class="w-5 h-5">
                      <path stroke-linecap="round" stroke-linejoin="round"
                            d="M16.862 3.487a2.25 2.25 0 1 1 3.182 3.182L6.75 19.5l-4.5 1.5 1.5-4.5 13.294-13.294z" />
                    </svg>
                  </router-link>
                  <button @click="deleteKhachHang(khachHang.id)" class="text-red-600 hover:text-red-800">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                         stroke="currentColor" class="w-5 h-5">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                    </svg>
                  </button>

                </div>

              </td>
            </tr>
            </tbody>
          </table>

          <div class="p-4 flex justify-between items-center">
            <div>
              <select v-model="pageSize" class="border rounded px-3 py-2" @change="handlePageSizeChange">
                <option :value="10">10 / trang</option>
                <option :value="20">20 / trang</option>
                <option :value="50">50 / trang</option>
              </select>
            </div>
            <div class="flex space-x-2">
              <button @click="currentPage--"
                      :disabled="currentPage === 1"
                      :class="[
                        'px-3 py-1 rounded',
                        currentPage === 1 ? 'bg-gray-100 text-gray-400' : 'bg-blue-500 text-white'
                      ]">
                Trước
              </button>
              <span class="px-3 py-1">
                Trang {{ currentPage }} / {{ totalPages }}
              </span>
              <button @click="currentPage++"
                      :disabled="currentPage === totalPages"
                      :class="[
                        'px-3 py-1 rounded',
                        currentPage === totalPages ? 'bg-gray-100 text-gray-400' : 'bg-blue-500 text-white'
                      ]">
                Sau
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal thêm/sửa khách hàng -->
    <div v-if="showModal" class="fixed inset-0 z-50 overflow-y-auto">
      <div class="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:block sm:p-0">
        <!-- Background overlay -->
        <div class="fixed inset-0 transition-opacity" aria-hidden="true">
          <div class="absolute inset-0 bg-gray-500 opacity-75"></div>
        </div>

        <!-- Modal panel -->
        <div class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <!-- Modal content -->
          <div class="max-h-[80vh] overflow-y-auto">
            <div class="bg-white px-4 pt-5 pb-4 sm:p-6">
              <h2 class="text-xl font-bold mb-4">{{ isEditing ? 'Sửa khách hàng' : 'Thêm khách hàng' }}</h2>
              <form id="khachHangForm" @submit.prevent="saveKhachHang">
                <!-- Form fields -->
                <div class="space-y-4">
                  <div>
                    <label class="block text-sm font-medium mb-1">Họ tên</label>
                    <input v-model="formData.hoTen" type="text" class="w-full border rounded px-3 py-2" required>
                  </div>

                  <div>
                    <label class="block text-sm font-medium mb-1">Email</label>
                    <input v-model="formData.email" type="email" class="w-full border rounded px-3 py-2" required>
                  </div>

                  <div>
                    <label class="block text-sm font-medium mb-1">Số điện thoại</label>
                    <input v-model="formData.soDienThoai" type="text"
                           class="w-full border rounded px-3 py-2"
                           pattern="[0-9]{10}"
                           title="Số điện thoại phải có 10 chữ số"
                           required>
                  </div>

                  <div>
                    <label class="block text-sm font-medium mb-1">Ngày sinh</label>
                    <input v-model="formData.ngaySinh" type="date" class="w-full border rounded px-3 py-2" required>
                  </div>

                  <div>
                    <label class="block text-sm font-medium mb-1">Giới tính</label>
                    <select v-model.number="formData.gioiTinh" class="w-full border rounded px-3 py-2">
                      <option :value="1">Nam</option>
                      <option :value="0">Nữ</option>
                    </select>
                  </div>

                  <div>
                    <label class="block text-sm font-medium mb-1">Trạng thái</label>
                    <select v-model.number="formData.trangThai" class="w-full border rounded px-3 py-2">
                      <option :value="1">Hoạt động</option>
                      <option :value="0">Không hoạt động</option>
                    </select>
                  </div>
                </div>

                <!-- Modal footer -->
                <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
                  <button type="submit"
                          class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-blue-600 text-base font-medium text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:ml-3 sm:w-auto sm:text-sm"
                  >
                    {{ isEditing ? 'Cập nhật' : 'Thêm' }}
                  </button>
                  <button type="button"
                          class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
                          @click="closeModal"
                  >
                    Hủy
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal quản lý địa chỉ -->
    <div v-if="showAddressModal" class="fixed inset-0 z-50 overflow-y-auto">
      <div class="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:block sm:p-0">
        <div class="fixed inset-0 transition-opacity" aria-hidden="true">
          <div class="absolute inset-0 bg-gray-500 opacity-75"></div>
        </div>
        <div class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
          <div class="bg-white px-4 pt-5 pb-4 sm:p-6">
            <div class="flex justify-between items-center mb-4">
              <h2 class="text-xl font-bold">Quản lý địa chỉ cho {{ selectedKhachHang?.hoTen }}</h2>
              <button @click="addAddress" class="bg-green-500 text-white px-3 py-1 rounded">
                Thêm địa chỉ
              </button>
            </div>

            <!-- Address form -->
            <div v-if="showAddressForm" class="mb-4">
              <form @submit.prevent="saveAddress" class="space-y-4">
                <div>
                  <label class="block text-sm font-medium mb-1">Tỉnh/Thành phố</label>
                  <select v-model="selectedAddressProvince" @change="handleAddressProvinceChange" class="w-full border rounded px-3 py-2" required>
                    <option value="">Chọn Tỉnh/Thành phố</option>
                    <option v-for="province in provinces" :key="province.code" :value="province">
                      {{ province.name }}
                    </option>
                  </select>
                </div>

                <div>
                  <label class="block text-sm font-medium mb-1">Quận/Huyện</label>
                  <select v-model="selectedAddressDistrict" @change="handleAddressDistrictChange" class="w-full border rounded px-3 py-2" required :disabled="!selectedAddressProvince">
                    <option value="">Chọn Quận/Huyện</option>
                    <option v-for="district in districts" :key="district.code" :value="district">
                      {{ district.name }}
                    </option>
                  </select>
                </div>

                <div>
                  <label class="block text-sm font-medium mb-1">Xã/Phường/Thị trấn</label>
                  <select v-model="selectedAddressWard" class="w-full border rounded px-3 py-2" required :disabled="!selectedAddressDistrict">
                    <option value="">Chọn Xã/Phường/Thị trấn</option>
                    <option v-for="ward in wards" :key="ward.code" :value="ward">
                      {{ ward.name }}
                    </option>
                  </select>
                </div>

                <div>
                  <label class="block text-sm font-medium mb-1">Địa chỉ chi tiết</label>
                  <input v-model="addressFormData.diaChiChiTiet" type="text" class="w-full border rounded px-3 py-2" required
                         placeholder="Số nhà, tên đường...">
                </div>

                <div class="flex items-center">
                  <input v-model="addressFormData.macDinh" type="checkbox" class="mr-2">
                  <label>Đặt làm địa chỉ mặc định</label>
                </div>

                <div class="flex justify-end space-x-2">
                  <button type="button" @click="showAddressForm = false" class="px-4 py-2 border rounded">
                    Hủy
                  </button>
                  <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded">
                    {{ editingAddressId ? 'Cập nhật' : 'Thêm' }}
                  </button>
                </div>
              </form>
            </div>

            <!-- Address list -->
            <div v-else class="space-y-4">
              <div v-for="diaChi in selectedKhachHang?.danhSachDiaChi" :key="diaChi.id"
                   class="border p-4 rounded flex justify-between items-center">
                <div>
                  <p class="font-medium">{{ diaChi.diaChiChiTiet }}</p>
                  <p class="text-sm text-gray-600">
                    {{ diaChi.xaPhuong }}, {{ diaChi.quanHuyen }}, {{ diaChi.tinhThanh }}
                  </p>
                  <span v-if="diaChi.macDinh" class="text-xs bg-green-100 text-green-800 px-2 py-1 rounded-full">
                    Mặc định
                  </span>
                </div>
                <div class="flex space-x-2">
                  <button @click="editAddress(diaChi)" class="bg-blue-500 text-white px-2 py-1 rounded">
                    Sửa
                  </button>
                  <button @click="deleteAddress(diaChi.id)" class="bg-red-500 text-white px-2 py-1 rounded">
                    Xóa
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import KhachHangService from '@/service/KhachHangService';
import AddressService from '@/service/AddressService';
import DiaChiService from '@/service/DiaChiService';
import KhachHang from '@/models/KhachHang';
import * as XLSX from 'xlsx';

export default {
  name: "KhachHang",
  data() {
    return {
      khachHangs: [],
      showModal: false,
      isEditing: false,
      searchQuery: '',
      statusFilter: '',
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      formData: {
        id: null,
        maKhachHang: '',
        hoTen: '',
        email: '',
        soDienThoai: '',
        ngaySinh: '',
        gioiTinh: 1,
        trangThai: 1,
        createdAt: null,
        createdBy: null,
        updatedAt: null,
        updatedBy: null,
        danhSachDiaChi: []
      },
      editingId: null,
      provinces: [],
      districts: [],
      wards: [],
      selectedProvince: null,
      selectedDistrict: null,
      selectedWard: null,
      showAddressModal: false,
      showAddressForm: false,
      selectedKhachHang: null,
      addressFormData: {
        diaChiChiTiet: '',
        tinhThanh: '',
        quanHuyen: '',
        xaPhuong: '',
        macDinh: false,
        khachHangId: null
      },
      editingAddressId: null,
      selectedAddressProvince: null,
      selectedAddressDistrict: null,
      selectedAddressWard: null,
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredKhachHang.length / this.pageSize);
    },
    filteredKhachHang() {
      return this.khachHangs.filter(kh => {
        const matchesSearch = !this.searchQuery ||
            kh.hoTen?.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
            kh.email?.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
            kh.soDienThoai?.includes(this.searchQuery);

        const matchesStatus = this.statusFilter === '' ||
            (this.statusFilter === 'true' && kh.trangThai === 1) ||
            (this.statusFilter === 'false' && kh.trangThai === 0);

        return matchesSearch && matchesStatus;
      });
    },
    displayedKhachHang() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredKhachHang.slice(start, end);
    }
  },
  async created() {
    await this.loadKhachHangs();
    await this.loadProvinces();
  },
  methods: {
    formatDate(date) {
      if (!date) return '';
      return new Date(date).toLocaleDateString('vi-VN');
    },

    async loadKhachHangs() {
      try {
        this.khachHangs = await KhachHangService.getAll();
      } catch (error) {
        console.error('Error loading khach hang:', error);
        // Thêm xử lý thông báo lỗi nếu cần
      }
    },

    async searchKhachHang() {
      if (this.searchQuery.trim()) {
        try {
          this.khachHangs = await KhachHangService.search(this.searchQuery);
        } catch (error) {
          console.error('Error searching khach hang:', error);
        }
      } else {
        await this.loadKhachHangs();
      }
    },

    openModal() {
      this.isEditing = false;
      this.formData = new KhachHang();
      this.selectedProvince = null;
      this.selectedDistrict = null;
      this.selectedWard = null;
      this.districts = [];
      this.wards = [];
      this.showModal = true;
    },

    async editKhachHang(id) {
      try {
        this.isEditing = true;
        this.editingId = id;

        // Fetch customer data
        const khachHang = await KhachHangService.getById(id);
        console.log('Raw customer data:', khachHang); // Debug log

        // Convert date format for input
        if (khachHang.ngaySinh) {
          khachHang.ngaySinh = khachHang.ngaySinh.split('T')[0];
        }

        // Reset form data first
        this.formData = {
          id: khachHang.id,
          maKhachHang: khachHang.maKhachHang,
          hoTen: khachHang.hoTen,
          email: khachHang.email,
          soDienThoai: khachHang.soDienThoai,
          ngaySinh: khachHang.ngaySinh,
          gioiTinh: parseInt(khachHang.gioiTinh), // Ensure it's a number
          trangThai: parseInt(khachHang.trangThai), // Ensure it's a number
          createdAt: khachHang.createdAt,
          createdBy: khachHang.createdBy,
          updatedAt: khachHang.updatedAt,
          updatedBy: khachHang.updatedBy,
          danhSachDiaChi: khachHang.danhSachDiaChi || []
        };

        console.log('Form data after setting:', this.formData); // Debug log
        this.showModal = true;

        // Load provinces for address management
        await this.loadProvinces();
      } catch (error) {
        console.error('Error loading customer data:', error);
        alert('Có lỗi xảy ra khi tải thông tin khách hàng');
      }
    },

    async saveKhachHang() {
      try {
        console.log('Saving customer:', this.formData); // Log data being sent

        if (this.isEditing) {
          const response = await KhachHangService.update(this.editingId, this.formData);
          console.log('Update response:', response);
        } else {
          const response = await KhachHangService.create(this.formData);
          console.log('Create response:', response);
        }

        this.closeModal();
        await this.loadKhachHangs();
        // Thông báo thành công
        alert(this.isEditing ? 'Cập nhật khách hàng thành công!' : 'Thêm khách hàng thành công!');
      } catch (error) {
        console.error('Error saving customer:', error);
        // Hiển thị thông báo lỗi chi tiết
        alert(error.response?.data?.message || 'Có lỗi xảy ra khi lưu thông tin khách hàng');
      }
    },

    async deleteKhachHang(id) {
      if (confirm('Bạn có chắc chắn muốn xóa khách hàng này?')) {
        try {
          await KhachHangService.delete(id);
          await this.loadKhachHangs();
        } catch (error) {
          console.error('Error deleting khach hang:', error);
          // Thêm xử lý thông báo lỗi nếu cần
        }
      }
    },

    closeModal() {
      this.showModal = false;
      this.formData = new KhachHang();
      this.editingId = null;
      this.selectedProvince = null;
      this.selectedDistrict = null;
      this.selectedWard = null;
      this.districts = [];
      this.wards = [];
    },

    formatDiaChi(khachHang) {
      if (!khachHang.danhSachDiaChi || khachHang.danhSachDiaChi.length === 0) return '';
      const diaChi = khachHang.danhSachDiaChi[0]; // Assuming the first address is the primary one
      return `${diaChi.diaChiChiTiet}, ${diaChi.xaPhuong}, ${diaChi.quanHuyen}, ${diaChi.tinhThanh}`;
    },

    getStatusClass(status) {
      return status
          ? 'bg-green-100 text-green-800 px-2 py-1 rounded-full'
          : 'bg-red-100 text-red-800 px-2 py-1 rounded-full';
    },

    handleSearch() {
      this.currentPage = 1;
    },

    handlePageSizeChange() {
      this.currentPage = 1;
    },

    async loadProvinces() {
      try {
        this.provinces = await AddressService.getProvinces();
      } catch (error) {
        console.error('Error loading provinces:', error);
        alert('Không thể tải danh sách tỉnh/thành phố');
      }
    },

    async handleProvinceChange() {
      this.districts = [];
      this.wards = [];
      this.selectedDistrict = null;
      this.selectedWard = null;
      this.formData.tinhThanh = this.selectedProvince ? this.selectedProvince.name : '';

      if (this.selectedProvince) {
        try {
          this.districts = await AddressService.getDistrictsByProvinceCode(this.selectedProvince.code);
        } catch (error) {
          console.error('Error loading districts:', error);
          alert('Không thể tải danh sách quận/huyện');
        }
      }
    },

    async handleDistrictChange() {
      this.wards = [];
      this.selectedWard = null;
      this.formData.quanHuyen = this.selectedDistrict ? this.selectedDistrict.name : '';

      if (this.selectedDistrict) {
        try {
          this.wards = await AddressService.getWardsByDistrictCode(this.selectedDistrict.code);
        } catch (error) {
          console.error('Error loading wards:', error);
          alert('Không thể tải danh sách xã/phường');
        }
      }
    },

    handleWardChange() {
      this.formData.xaPhuong = this.selectedWard ? this.selectedWard.name : '';
    },

    async openAddressModal(khachHang) {
      this.selectedKhachHang = khachHang;
      this.showAddressModal = true;
      await this.loadProvinces();
    },

    closeAddressModal() {
      this.showAddressModal = false;
      this.showAddressForm = false;
      this.selectedKhachHang = null;
      this.resetAddressForm();
    },

    resetAddressForm() {
      this.addressFormData = {
        diaChiChiTiet: '',
        tinhThanh: '',
        quanHuyen: '',
        xaPhuong: '',
        macDinh: false,
        khachHangId: null
      };
      this.editingAddressId = null;
      this.selectedAddressProvince = null;
      this.selectedAddressDistrict = null;
      this.selectedAddressWard = null;
    },

    addAddress() {
      this.showAddressForm = true;
      this.resetAddressForm();
      this.addressFormData.khachHangId = this.selectedKhachHang.id;
    },

    async editAddress(diaChi) {
      this.showAddressForm = true;
      this.editingAddressId = diaChi.id;
      this.addressFormData = { ...diaChi };

      // Set selected locations
      this.selectedAddressProvince = this.provinces.find(p => p.name === diaChi.tinhThanh);
      if (this.selectedAddressProvince) {
        await this.handleAddressProvinceChange();
        this.selectedAddressDistrict = this.districts.find(d => d.name === diaChi.quanHuyen);
        if (this.selectedAddressDistrict) {
          await this.handleAddressDistrictChange();
          this.selectedAddressWard = this.wards.find(w => w.name === diaChi.xaPhuong);
        }
      }
    },

    async deleteAddress(id) {
      if (confirm('Bạn có chắc chắn muốn xóa địa chỉ này?')) {
        try {
          await DiaChiService.delete(id);
          this.selectedKhachHang.danhSachDiaChi = this.selectedKhachHang.danhSachDiaChi.filter(d => d.id !== id);
          await this.loadKhachHangs(); // Reload to update the list
        } catch (error) {
          console.error('Error deleting address:', error);
          alert('Có lỗi xảy ra khi xóa địa chỉ');
        }
      }
    },

    async saveAddress() {
      try {
        const addressData = {
          ...this.addressFormData,
          tinhThanh: this.selectedAddressProvince?.name || '',
          quanHuyen: this.selectedAddressDistrict?.name || '',
          xaPhuong: this.selectedAddressWard?.name || ''
        };

        let response;
        if (this.editingAddressId) {
          response = await DiaChiService.update(this.editingAddressId, addressData);
        } else {
          response = await DiaChiService.create(addressData);
        }

        // Update the list of addresses
        if (this.editingAddressId) {
          const index = this.selectedKhachHang.danhSachDiaChi.findIndex(d => d.id === this.editingAddressId);
          if (index !== -1) {
            this.selectedKhachHang.danhSachDiaChi[index] = response;
          }
        } else {
          this.selectedKhachHang.danhSachDiaChi.push(response);
        }

        this.showAddressForm = false;
        this.resetAddressForm();
        await this.loadKhachHangs(); // Reload to update the list
        alert(this.editingAddressId ? 'Cập nhật địa chỉ thành công!' : 'Thêm địa chỉ thành công!');
      } catch (error) {
        console.error('Error saving address:', error);
        alert(error.response?.data?.message || 'Có lỗi xảy ra khi lưu địa chỉ');
      }
    },

    async handleAddressProvinceChange() {
      this.districts = [];
      this.wards = [];
      this.selectedAddressDistrict = null;
      this.selectedAddressWard = null;

      if (this.selectedAddressProvince) {
        try {
          this.districts = await AddressService.getDistrictsByProvinceCode(this.selectedAddressProvince.code);
        } catch (error) {
          console.error('Error loading districts:', error);
          alert('Không thể tải danh sách quận/huyện');
        }
      }
    },

    async handleAddressDistrictChange() {
      this.wards = [];
      this.selectedAddressWard = null;

      if (this.selectedAddressDistrict) {
        try {
          this.wards = await AddressService.getWardsByDistrictCode(this.selectedAddressDistrict.code);
        } catch (error) {
          console.error('Error loading wards:', error);
          alert('Không thể tải danh sách xã/phường');
        }
      }
    },

    async toggleStatus(khachHang) {
      try {
        const newStatus = khachHang.trangThai === 1 ? 0 : 1;
        await KhachHangService.updateStatus(khachHang.id, newStatus);
        khachHang.trangThai = newStatus;
      } catch (error) {
        console.error('Error updating status:', error);
        alert('Không thể cập nhật trạng thái khách hàng');
      }
    },

    getDefaultAddress(khachHang) {
      const defaultAddress = khachHang.danhSachDiaChi?.find(d => d.macDinh);
      if (!defaultAddress) return "Chưa có địa chỉ mặc định";

      // Ghép địa chỉ đầy đủ
      const fullAddress = `${defaultAddress.diaChiChiTiet}, ${defaultAddress.xaPhuong}, ${defaultAddress.quanHuyen}, ${defaultAddress.tinhThanh}`;

      // Giới hạn độ dài hiển thị
      const maxLength = 30;
      return fullAddress.length > maxLength
          ? fullAddress.substring(0, maxLength) + "..."
          : fullAddress;
    },
    getFullAddress(khachHang) {
      const defaultAddress = khachHang.danhSachDiaChi?.find(d => d.macDinh);
      if (!defaultAddress) return "Chưa có địa chỉ mặc định";
      return `${defaultAddress.diaChiChiTiet}, ${defaultAddress.xaPhuong}, ${defaultAddress.quanHuyen}, ${defaultAddress.tinhThanh}`;
    },

    exportToExcel() {
      const data = this.filteredKhachHang.map(kh => ({
        'Mã khách hàng': kh.maKhachHang,
        'Họ tên': kh.hoTen,
        'Email': kh.email,
        'Số điện thoại': kh.soDienThoai,
        'Ngày sinh': kh.ngaySinh,
        'Giới tính': kh.gioiTinh ? 'Nam' : 'Nữ',
        'Địa chỉ mặc định': this.getDefaultAddress(kh),
        'Trạng thái': kh.trangThai === 1 ? 'Hoạt động' : 'Không hoạt động'
      }));

      const ws = XLSX.utils.json_to_sheet(data);
      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, 'Khách hàng');
      XLSX.writeFile(wb, 'danh-sach-khach-hang.xlsx');
    }
  },
  watch: {
    searchQuery() {
      this.handleSearch();
    },
    statusFilter() {
      this.handleSearch();
    }
  }
};
</script>

<style scoped>
/* Thêm style cho switch nếu cần */
</style>
