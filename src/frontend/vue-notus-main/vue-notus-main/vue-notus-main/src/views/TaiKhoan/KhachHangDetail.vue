<template>
  <div class="flex flex-wrap mt-4">
    <div class="w-full mb-12 px-4">
      <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded bg-white overflow-hidden">
        <!-- Customer Information -->
        <div class="rounded-t mb-0 px-4 py-3 border-0">
          <div class="flex flex-wrap items-center">
            <div class="relative w-full px-4 max-w-full flex-grow flex-1">
              <h3 class="font-semibold text-lg text-blueGray-700">
                Thông tin khách hàng
              </h3>
            </div>
          </div>
        </div>

        <div class="block w-full overflow-x-auto p-4">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <p class="text-sm font-medium text-gray-500">Mã khách hàng</p>
              <p>{{ khachHang.maKhachHang }}</p>
            </div>
            <div>
              <p class="text-sm font-medium text-gray-500">Họ tên</p>
              <p>{{ khachHang.hoTen }}</p>
            </div>
            <div>
              <p class="text-sm font-medium text-gray-500">Email</p>
              <p>{{ khachHang.email }}</p>
            </div>
            <div>
              <p class="text-sm font-medium text-gray-500">Số điện thoại</p>
              <p>{{ khachHang.soDienThoai }}</p>
            </div>
            <div>
              <p class="text-sm font-medium text-gray-500">Ngày sinh</p>
              <p>{{ formatDate(khachHang.ngaySinh) }}</p>
            </div>
            <div>
              <p class="text-sm font-medium text-gray-500">Giới tính</p>
              <p>{{ khachHang.gioiTinh ? 'Nam' : 'Nữ' }}</p>
            </div>
            <div>
              <p class="text-sm font-medium text-gray-500">Trạng thái</p>
              <span :class="getStatusClass(khachHang.trangThai)">
                {{ khachHang.trangThai ? 'Hoạt động' : 'Không hoạt động' }}
              </span>
            </div>
          </div>
        </div>

        <!-- Address List -->
        <div class="rounded-t mb-0 px-4 py-3 border-t border-blueGray-300">
          <div class="flex flex-wrap items-center">
            <div class="relative w-full px-4 max-w-full flex-grow flex-1">
              <h3 class="font-semibold text-lg text-blueGray-700">
                Danh sách địa chỉ
              </h3>
            </div>
            <div class="relative w-full px-4 max-w-full flex-grow flex-1 text-right">
              <button @click="addAddress" class="bg-indigo-500 text-white active:bg-indigo-600 text-xs font-bold uppercase px-3 py-1 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150">
                Thêm địa chỉ
              </button>
            </div>
          </div>
        </div>

        <div class="block w-full overflow-x-auto p-4">
          <!-- Address Form -->
          <div v-if="showAddressForm" class="mb-4 border rounded p-4">
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
                <button type="button" @click="cancelAddressForm" class="px-4 py-2 border rounded">
                  Hủy
                </button>
                <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded">
                  {{ editingAddressId ? 'Cập nhật' : 'Thêm' }}
                </button>
              </div>
            </form>
          </div>

          <!-- Address List -->
          <div class="space-y-4">
            <div v-for="diaChi in khachHang.danhSachDiaChi" :key="diaChi.id"
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
</template>

<script>
import KhachHangService from '@/service/KhachHangService';
import AddressService from '@/service/AddressService';
import DiaChiService from '@/service/DiaChiService';

export default {
  name: "KhachHangDetail",
  data() {
    return {
      khachHang: {
        id: null,
        maKhachHang: '',
        hoTen: '',
        email: '',
        soDienThoai: '',
        ngaySinh: '',
        gioiTinh: true,
        trangThai: true,
        danhSachDiaChi: []
      },
      showAddressForm: false,
      provinces: [],
      districts: [],
      wards: [],
      selectedAddressProvince: null,
      selectedAddressDistrict: null,
      selectedAddressWard: null,
      addressFormData: {
        diaChiChiTiet: '',
        tinhThanh: '',
        quanHuyen: '',
        xaPhuong: '',
        macDinh: false,
        khachHangId: null
      },
      editingAddressId: null
    };
  },
  async created() {
    await this.loadKhachHang();
    await this.loadDiaChiList();
    await this.loadProvinces();
  },
  methods: {
    formatDate(date) {
      if (!date) return '';
      return new Date(date).toLocaleDateString('vi-VN');
    },

    getStatusClass(status) {
      return status
          ? 'bg-green-100 text-green-800 px-2 py-1 rounded-full'
          : 'bg-red-100 text-red-800 px-2 py-1 rounded-full';
    },

    async loadKhachHang() {
      try {
        const id = this.$route.params.id;
        this.khachHang = await KhachHangService.getById(id);
      } catch (error) {
        console.error('Error loading customer:', error);
        alert('Không thể tải thông tin khách hàng');
      }
    },

    async loadDiaChiList() {
      try {
        const danhSachDiaChi = await DiaChiService.getByKhachHangId(this.khachHang.id);
        this.khachHang.danhSachDiaChi = danhSachDiaChi;
      } catch (error) {
        console.error('Error loading addresses:', error);
        alert('Không thể tải danh sách địa chỉ');
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

    addAddress() {
      this.showAddressForm = true;
      this.resetAddressForm();
      this.addressFormData.khachHangId = this.khachHang.id;
    },

    editAddress(diaChi) {
      this.showAddressForm = true;
      this.editingAddressId = diaChi.id;
      this.addressFormData = {...diaChi};

      // Set selected locations
      this.selectedAddressProvince = this.provinces.find(p => p.name === diaChi.tinhThanh);
      if (this.selectedAddressProvince) {
        this.handleAddressProvinceChange();
      }
    },

    async deleteAddress(id) {
      if (confirm('Bạn có chắc chắn muốn xóa địa chỉ này?')) {
        try {
          await DiaChiService.delete(id);
          await this.loadDiaChiList();
          alert('Xóa địa chỉ thành công!');
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
          xaPhuong: this.selectedAddressWard?.name || '',
          khachHangId: this.khachHang.id
        };

        if (this.editingAddressId) {
          await DiaChiService.update(this.editingAddressId, addressData);
        } else {
          await DiaChiService.create(addressData);
        }

        this.showAddressForm = false;
        this.resetAddressForm();
        await this.loadDiaChiList();
        alert(this.editingAddressId ? 'Cập nhật địa chỉ thành công!' : 'Thêm địa chỉ thành công!');
      } catch (error) {
        console.error('Error saving address:', error);
        alert(error.response?.data?.message || 'Có lỗi xảy ra khi lưu địa chỉ');
      }
    },

    cancelAddressForm() {
      this.showAddressForm = false;
      this.resetAddressForm();
    },

    resetAddressForm() {
      this.addressFormData = {
        diaChiChiTiet: '',
        tinhThanh: '',
        quanHuyen: '',
        xaPhuong: '',
        macDinh: false,
        khachHangId: this.khachHang.id
      };
      this.editingAddressId = null;
      this.selectedAddressProvince = null;
      this.selectedAddressDistrict = null;
      this.selectedAddressWard = null;
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