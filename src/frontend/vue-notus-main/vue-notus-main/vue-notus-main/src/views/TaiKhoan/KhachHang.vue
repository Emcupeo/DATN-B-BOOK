<template>
  <div class="flex flex-wrap mt-4 flex justify-center items-center h-screen">
    <div class="w-full mb-12 px-4 bg-white p-6 bg-white p-6 shadow-lg rounded-lg w-4/5 h-4/5">
      <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded bg-white">
        <div class="rounded-t mb-0 px-4 py-3 border-0">
          <div class="flex flex-wrap items-center">
            <div class="relative w-full px-4 max-w-full flex-grow flex-1">
              <h3 class="font-semibold text-lg text-blueGray-700">
                Danh sách khách hàng
              </h3>
            </div>
            <div class="relative w-full px-4 max-w-full flex-grow flex-1 text-right">
              <button @click="openModal" class="bg-indigo-500 text-white active:bg-indigo-600 text-xs font-bold uppercase px-3 py-1 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150">
                Thêm khách hàng
              </button>
            </div>
          </div>
          <div class="mt-4 flex justify-end">
            <input v-model="searchQuery" @input="handleSearch" type="text" placeholder="Tìm kiếm khách hàng..." class="border px-3 py-2 rounded">
          </div>
        </div>

        <div class="block w-full overflow-x-auto">
          <table class="items-center w-full bg-transparent border-collapse">
            <thead>
            <tr>
              <th class="px-6 align-middle border border-solid py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left bg-blueGray-50 text-blueGray-500 border-blueGray-100">
                Mã KH
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
                Địa chỉ
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
            <tr v-for="khachHang in khachHangs" :key="khachHang.id">
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
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                {{ formatDiaChi(khachHang) }}
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                  <span :class="getStatusClass(khachHang.trangThai)" class="px-2 py-1 text-green-700 border border-green-500 bg-green-100 rounded">
                    {{ khachHang.trangThai ? 'Hoạt động' : 'Không hoạt động' }}
                  </span>
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                <button @click="editKhachHang(khachHang)" class="bg-blue-500 text-white px-2 py-1 rounded mr-2">Sửa</button>
                <button @click="deleteKhachHang(khachHang.id)" class="bg-red-500 text-white px-2 py-1 rounded mr-2">Xóa</button>
                <router-link :to="'/admin/khach-hang/' + khachHang.id" class="bg-yellow-500 text-white px-2 py-1 rounded">Chi tiết</router-link>
              </td>
            </tr>
            </tbody>
          </table>
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
                    <select v-model="formData.gioiTinh" class="w-full border rounded px-3 py-2">
                      <option :value="true">Nam</option>
                      <option :value="false">Nữ</option>
                    </select>
                  </div>

                  <div>
                    <label class="block text-sm font-medium mb-1">Trạng thái</label>
                    <select v-model="formData.trangThai" class="w-full border rounded px-3 py-2">
                      <option :value="true">Hoạt động</option>
                      <option :value="false">Không hoạt động</option>
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

export default {
  name: "KhachHang",
  data() {
    return {
      khachHangs: [],
      showModal: false,
      isEditing: false,
      searchQuery: '',
      formData: new KhachHang(),
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

    editKhachHang(khachHang) {
      this.isEditing = true;
      this.editingId = khachHang.id;
      this.formData = new KhachHang(khachHang);

      this.selectedProvince = this.provinces.find(p => p.name === khachHang.tinhThanh) || null;
      if (this.selectedProvince) {
        this.handleProvinceChange();
      }

      this.showModal = true;
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

    async handleSearch() {
      try {
        if (this.searchQuery.trim()) {
          this.khachHangs = await KhachHangService.search(this.searchQuery);
        } else {
          await this.loadKhachHangs();
        }
      } catch (error) {
        console.error('Error searching khach hang:', error);
        alert('Có lỗi xảy ra khi tìm kiếm khách hàng');
      }
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
    }
  }
};
</script>
