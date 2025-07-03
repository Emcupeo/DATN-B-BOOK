<template>
  <div class="flex flex-wrap mt-4">
    <div class="w-full mb-12 px-4">
      <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded bg-white">
        <div class="rounded-t mb-0 px-4 py-3 border-0">
          <div class="flex flex-wrap items-center">
            <div class="relative w-full px-4 max-w-full flex-grow flex-1">
              <h3 class="font-semibold text-lg text-blueGray-700">
                {{ isEditing ? 'Chỉnh sửa khách hàng' : 'Thêm mới khách hàng' }}
              </h3>
            </div>
          </div>
        </div>

        <div class="block w-full overflow-x-auto p-4">
          <form @submit.prevent="saveKhachHang">
            <!-- Thông tin khách hàng -->
            <div class="mb-6">
              <h4 class="font-semibold mb-4">Thông tin khách hàng</h4>
              <div class="grid grid-cols-2 gap-4">
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
            </div>

            <!-- Địa chỉ -->
            <div class="mb-6">
              <div class="flex justify-between items-center mb-4">
                <h4 class="font-semibold">Địa chỉ</h4>
                <button type="button" @click="showAddressForm = true" 
                        v-if="!showAddressForm"
                        class="bg-green-500 text-white px-3 py-1 rounded text-sm">
                  Thêm địa chỉ
                </button>
              </div>

              <!-- Form thêm/sửa địa chỉ -->
              <div v-if="showAddressForm" class="mb-4 p-4 border rounded">
                <h5 class="font-semibold mb-3">{{ editingAddressId ? 'Sửa địa chỉ' : 'Thêm địa chỉ mới' }}</h5>
                <div class="grid grid-cols-2 gap-4">
                  <div>
                    <label class="block text-sm font-medium mb-1">Tỉnh/Thành phố</label>
                    <select v-model="selectedAddressProvince" class="w-full border rounded px-3 py-2" required>
                      <option v-for="province in provinces" :key="province.code" :value="province">
                        {{ province.name }}
                      </option>
                    </select>
                  </div>

                  <div>
                    <label class="block text-sm font-medium mb-1">Quận/Huyện</label>
                    <select v-model="selectedAddressDistrict" class="w-full border rounded px-3 py-2" required>
                      <option v-for="district in districts" :key="district.code" :value="district">
                        {{ district.name }}
                      </option>
                    </select>
                  </div>

                  <div>
                    <label class="block text-sm font-medium mb-1">Xã/Phường</label>
                    <select v-model="selectedAddressWard" class="w-full border rounded px-3 py-2" required>
                      <option v-for="ward in wards" :key="ward.code" :value="ward">
                        {{ ward.name }}
                      </option>
                    </select>
                  </div>

                  <div>
                    <label class="block text-sm font-medium mb-1">Địa chỉ chi tiết</label>
                    <input v-model="addressFormData.diaChiChiTiet" type="text" class="w-full border rounded px-3 py-2" required>
                  </div>

                  <div class="col-span-2">
                    <label class="flex items-center">
                      <input type="checkbox" v-model="addressFormData.macDinh" class="mr-2">
                      Đặt làm địa chỉ mặc định
                    </label>
                  </div>
                </div>

                <div class="flex justify-end space-x-2 mt-4">
                  <button type="button" @click="cancelAddressForm" class="px-4 py-2 border rounded">
                    Hủy
                  </button>
                  <button type="button" @click="saveAddress" class="px-4 py-2 bg-blue-500 text-white rounded">
                    {{ editingAddressId ? 'Cập nhật' : 'Thêm' }}
                  </button>
                </div>
              </div>

              <!-- Danh sách địa chỉ -->
              <div class="space-y-4">
                <div v-for="diaChi in formData.danhSachDiaChi" :key="diaChi.id" 
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
                    <button type="button" @click="editAddress(diaChi)" class="bg-blue-500 text-white px-2 py-1 rounded">
                      Sửa
                    </button>
                    <button type="button" @click="deleteAddress(diaChi.id)" class="bg-red-500 text-white px-2 py-1 rounded">
                      Xóa
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- Buttons -->
            <div class="flex justify-end space-x-2 mt-6">
              <router-link to="/admin/khach-hang" class="px-4 py-2 border rounded">
                Hủy
              </router-link>
              <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded">
                {{ isEditing ? 'Cập nhật' : 'Thêm mới' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import KhachHangService from '@/service/KhachHangService';
import AddressService from '@/service/AddressService';
import KhachHang from '@/models/KhachHang';

export default {
  name: 'KhachHangForm',
  data() {
    return {
      isEditing: false,
      formData: new KhachHang({
        hoTen: '',
        email: '',
        soDienThoai: '',
        ngaySinh: '',
        gioiTinh: 1,
        trangThai: 1,
        danhSachDiaChi: []
      }),
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
        macDinh: false
      },
      editingAddressId: null
    };
  },
  async created() {
    const id = this.$route.params.id;
    if (id) {
      this.isEditing = true;
      await this.loadKhachHang(id);
    }
    await this.loadProvinces();
  },
  methods: {
    async loadKhachHang(id) {
      try {
        const response = await KhachHangService.getById(id);
        console.log('Raw customer data:', response); // Debug log
        
        // Convert date format for input
        if (response.ngaySinh) {
          response.ngaySinh = response.ngaySinh.split('T')[0];
        }

        // Create a new KhachHang instance with the response data
        this.formData = new KhachHang({
          ...response,
          gioiTinh: parseInt(response.gioiTinh), // Ensure it's a number
          trangThai: parseInt(response.trangThai) // Ensure it's a number
        });

        console.log('Processed form data:', this.formData); // Debug log
      } catch (error) {
        console.error('Error loading customer:', error);
        alert('Không thể tải thông tin khách hàng');
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
      if (this.selectedAddressProvince) {
        try {
          this.districts = await AddressService.getDistricts(this.selectedAddressProvince.code);
          this.selectedAddressDistrict = null;
          this.selectedAddressWard = null;
          this.wards = [];
        } catch (error) {
          console.error('Error loading districts:', error);
          alert('Không thể tải danh sách quận/huyện');
        }
      }
    },

    async handleDistrictChange() {
      if (this.selectedAddressDistrict) {
        try {
          this.wards = await AddressService.getWards(this.selectedAddressDistrict.code);
          this.selectedAddressWard = null;
        } catch (error) {
          console.error('Error loading wards:', error);
          alert('Không thể tải danh sách xã/phường');
        }
      }
    },

    async saveKhachHang() {
      try {
        if (this.isEditing) {
          // Xử lý cập nhật với địa chỉ
          const customerData = new KhachHang({
            id: this.formData.id,
            hoTen: this.formData.hoTen,
            email: this.formData.email,
            soDienThoai: this.formData.soDienThoai,
            ngaySinh: this.formData.ngaySinh,
            gioiTinh: this.formData.gioiTinh,
            trangThai: this.formData.trangThai,
            danhSachDiaChi: this.formData.danhSachDiaChi.map(diaChi => ({
              id: diaChi.id,
              diaChiChiTiet: diaChi.diaChiChiTiet,
              tinhThanh: diaChi.tinhThanh,
              quanHuyen: diaChi.quanHuyen,
              xaPhuong: diaChi.xaPhuong,
              macDinh: diaChi.macDinh
            }))
          });
          await KhachHangService.update(this.formData.id, customerData);
          alert('Cập nhật khách hàng thành công!');
        } else {
          // Sử dụng API mới để tạo khách hàng và địa chỉ cùng lúc
          const customerData = {
            hoTen: this.formData.hoTen,
            email: this.formData.email,
            soDienThoai: this.formData.soDienThoai,
            ngaySinh: this.formData.ngaySinh,
            gioiTinh: this.formData.gioiTinh,
            trangThai: this.formData.trangThai,
            danhSachDiaChi: this.formData.danhSachDiaChi.map(diaChi => ({
              diaChiChiTiet: diaChi.diaChiChiTiet,
              tinhThanh: diaChi.tinhThanh,
              quanHuyen: diaChi.quanHuyen,
              xaPhuong: diaChi.xaPhuong,
              macDinh: diaChi.macDinh
            }))
          };
          
          await KhachHangService.createWithAddresses(customerData);
          alert('Thêm khách hàng thành công!');
        }
        
        this.$router.push('/admin/khach-hang');
      } catch (error) {
        console.error('Error saving customer:', error);
        alert(error.response?.data?.message || error.message || 'Có lỗi xảy ra khi lưu thông tin khách hàng');
      }
    },

    saveAddress() {
      if (!this.selectedAddressProvince || !this.selectedAddressDistrict || !this.selectedAddressWard) {
        alert('Vui lòng chọn đầy đủ thông tin địa chỉ');
        return;
      }

      const addressData = {
        ...this.addressFormData,
        tinhThanh: this.selectedAddressProvince.name,
        quanHuyen: this.selectedAddressDistrict.name,
        xaPhuong: this.selectedAddressWard.name
      };

      // If this address is set as default, unset default for all other addresses
      if (addressData.macDinh) {
        this.formData.danhSachDiaChi.forEach(diaChi => {
          diaChi.macDinh = false;
        });
      }
      // If no address is marked as default, make this one default if it's the first address
      else if (this.formData.danhSachDiaChi.length === 0 || 
               !this.formData.danhSachDiaChi.some(diaChi => diaChi.macDinh)) {
        addressData.macDinh = true;
      }

      if (this.editingAddressId) {
        // Update existing address
        const index = this.formData.danhSachDiaChi.findIndex(d => d.id === this.editingAddressId);
        if (index !== -1) {
          this.formData.danhSachDiaChi[index] = addressData;
        }
      } else {
        // Add new address
        // Generate temporary id for new address
        addressData.id = Date.now();
        this.formData.danhSachDiaChi.push(addressData);
      }
      
      this.cancelAddressForm();
    },

    editAddress(diaChi) {
      this.editingAddressId = diaChi.id;
      this.addressFormData = { ...diaChi };
      
      // Find and set selected locations
      this.selectedAddressProvince = this.provinces.find(p => p.name === diaChi.tinhThanh);
      this.handleProvinceChange().then(() => {
        this.selectedAddressDistrict = this.districts.find(d => d.name === diaChi.quanHuyen);
        this.handleDistrictChange().then(() => {
          this.selectedAddressWard = this.wards.find(w => w.name === diaChi.xaPhuong);
        });
      });
      
      this.showAddressForm = true;
    },

    deleteAddress(id) {
      this.formData.danhSachDiaChi = this.formData.danhSachDiaChi.filter(d => d.id !== id);
    },

    cancelAddressForm() {
      this.showAddressForm = false;
      this.editingAddressId = null;
      this.addressFormData = {
        diaChiChiTiet: '',
        tinhThanh: '',
        quanHuyen: '',
        xaPhuong: '',
        macDinh: false
      };
      this.selectedAddressProvince = null;
      this.selectedAddressDistrict = null;
      this.selectedAddressWard = null;
    },
  },
  watch: {
    selectedAddressProvince() {
      this.handleProvinceChange();
    },
    selectedAddressDistrict() {
      this.handleDistrictChange();
    }
  }
};
</script> 