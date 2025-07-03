<template>
  <div class="flex flex-wrap mt-4">
    <div class="w-full mb-12 px-4">
      <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded bg-white">
        <div class="rounded-t mb-0 px-4 py-3 border-0">
          <div class="flex flex-wrap items-center">
            <div class="relative w-full px-4 max-w-full flex-grow flex-1">
              <h3 class="font-semibold text-lg text-blueGray-700">
                Sửa thông tin nhân viên
              </h3>
            </div>
          </div>
        </div>

        <div class="block w-full overflow-x-auto p-4">
          <form @submit.prevent="validateAndSave">
            <div class="mb-4">
              <label class="block text-sm font-medium mb-1">Mã nhân viên</label>
              <input v-model="formData.maNhanVien" type="text" 
                     class="w-full border rounded px-3 py-2 bg-gray-100" 
                     disabled>
            </div>
            <div class="mb-4">
              <label class="block text-sm font-medium mb-1">Họ tên</label>
              <input v-model="formData.hoTen" type="text" class="w-full border rounded px-3 py-2" required>
            </div>
            <div class="mb-4">
              <label class="block text-sm font-medium mb-1">Số điện thoại</label>
              <input v-model="formData.soDienThoai" type="text" class="w-full border rounded px-3 py-2" required>
            </div>
            <div class="mb-4">
              <label class="block text-sm font-medium mb-1">Số căn cước</label>
              <input v-model="formData.soCanCuoc" type="text" class="w-full border rounded px-3 py-2" required>
            </div>
            <div class="mb-4">
              <label class="block text-sm font-medium mb-1">Năm sinh</label>
              <input v-model="formData.namSinh" type="number" class="w-full border rounded px-3 py-2" required>
            </div>
            <div class="mb-4">
              <label class="block text-sm font-medium mb-1">Giới tính</label>
              <select v-model="formData.gioiTinh" class="w-full border rounded px-3 py-2">
                <option :value="true">Nam</option>
                <option :value="false">Nữ</option>
              </select>
            </div>

            <!-- Thêm phần địa chỉ -->
            <div class="mb-4">
              <label class="block text-sm font-medium mb-1">Tỉnh/Thành phố</label>
              <select v-model="selectedProvince" @change="handleProvinceChange" class="w-full border rounded px-3 py-2" required>
                <option value="">Chọn Tỉnh/Thành phố</option>
                <option v-for="province in provinces" :key="province.code" :value="province">
                  {{ province.name }}
                </option>
              </select>
            </div>

            <div class="mb-4">
              <label class="block text-sm font-medium mb-1">Quận/Huyện</label>
              <select v-model="selectedDistrict" @change="handleDistrictChange" class="w-full border rounded px-3 py-2" required :disabled="!selectedProvince">
                <option value="">Chọn Quận/Huyện</option>
                <option v-for="district in districts" :key="district.code" :value="district">
                  {{ district.name }}
                </option>
              </select>
            </div>

            <div class="mb-4">
              <label class="block text-sm font-medium mb-1">Xã/Phường/Thị trấn</label>
              <select v-model="selectedWard" @change="handleWardChange" class="w-full border rounded px-3 py-2" required :disabled="!selectedDistrict">
                <option value="">Chọn Xã/Phường/Thị trấn</option>
                <option v-for="ward in wards" :key="ward.code" :value="ward">
                  {{ ward.name }}
                </option>
              </select>
            </div>

            <div class="mb-4">
              <label class="block text-sm font-medium mb-1">Địa chỉ chi tiết</label>
              <input v-model="formData.diaChiChiTiet" type="text" class="w-full border rounded px-3 py-2" required 
                     placeholder="Số nhà, tên đường...">
            </div>

            <div class="mb-4">
              <label class="block text-sm font-medium mb-1">Email</label>
              <input v-model="formData.email" type="email" class="w-full border rounded px-3 py-2" required>
            </div>

            <div class="flex justify-end">
              <router-link to="/admin/nhan-vien" class="bg-gray-500 text-white px-4 py-2 rounded mr-2">
                Hủy
              </router-link>
              <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">Lưu</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NhanVienService from '@/service/NhanVienService';
import AddressService from '@/service/AddressService';

export default {
  name: "SuaNhanVien",
  data() {
    return {
      formData: {
        id: null,
        maNhanVien: '',
        hoTen: '',
        gioiTinh: true,
        namSinh: '',
        soCanCuoc: '',
        soDienThoai: '',
        tinhThanh: '',
        quanHuyen: '',
        xaPhuong: '',
        diaChiChiTiet: '',
        email: '',
        trangThai: true,
        deleted: false
      },
      nhanViens: [],
      originalNhanVien: null,
      provinces: [],
      districts: [],
      wards: [],
      selectedProvince: null,
      selectedDistrict: null,
      selectedWard: null
    };
  },
  async created() {
    try {
      await this.loadNhanViens();
      await this.loadProvinces(); // Load provinces first
      await this.loadNhanVien(); // Then load employee data
    } catch (error) {
      console.error('Error in created:', error);
    }
  },
  methods: {
    async loadNhanViens() {
      try {
        this.nhanViens = await NhanVienService.getAll();
      } catch (error) {
        console.error('Error loading nhan vien:', error);
      }
    },

    async loadProvinces() {
      try {
        this.provinces = await AddressService.getProvinces();
        console.log('Loaded provinces:', this.provinces);
      } catch (error) {
        console.error('Error loading provinces:', error);
        alert('Không thể tải danh sách tỉnh/thành phố');
      }
    },

    async loadNhanVien() {
      try {
        const id = this.$route.params.id;
        const nhanVien = await NhanVienService.getById(id);
        console.log('Loaded nhanVien:', nhanVien);
        
        this.originalNhanVien = { ...nhanVien };
        
        // Copy dữ liệu vào formData, đảm bảo giữ lại các trường địa chỉ
        this.formData = {
          id: nhanVien.id,
          idChucVu: nhanVien.idChucVu,
          maNhanVien: nhanVien.maNhanVien,
          hoTen: nhanVien.hoTen,
          gioiTinh: nhanVien.gioiTinh,
          namSinh: nhanVien.namSinh,
          soCanCuoc: nhanVien.soCanCuoc,
          soDienThoai: nhanVien.soDienThoai,
          tinhThanh: nhanVien.tinhThanh || '',
          quanHuyen: nhanVien.quanHuyen || '',
          xaPhuong: nhanVien.xaPhuong || '',
          diaChiChiTiet: nhanVien.diaChiChiTiet || '',
          email: nhanVien.email || '',
          trangThai: nhanVien.trangThai,
          deleted: nhanVien.deleted
        };

        console.log('Form data after copy:', this.formData);

        // Set địa chỉ
        if (this.formData.tinhThanh && this.provinces.length > 0) {
          console.log('Setting up address for:', this.formData.tinhThanh);
          
          // Tìm tỉnh/thành phố
          this.selectedProvince = this.provinces.find(p => p.name === this.formData.tinhThanh);
          console.log('Found province:', this.selectedProvince);
          
          if (this.selectedProvince) {
            // Load quận/huyện
            this.districts = await AddressService.getDistricts(this.selectedProvince.code);
            console.log('Loaded districts:', this.districts);
            
            if (this.formData.quanHuyen && this.districts.length > 0) {
              // Tìm quận/huyện
              this.selectedDistrict = this.districts.find(d => d.name === this.formData.quanHuyen);
              console.log('Found district:', this.selectedDistrict);
              
              if (this.selectedDistrict) {
                // Load xã/phường
                this.wards = await AddressService.getWards(this.selectedDistrict.code);
                console.log('Loaded wards:', this.wards);
                
                if (this.formData.xaPhuong && this.wards.length > 0) {
                  // Tìm xã/phường
                  this.selectedWard = this.wards.find(w => w.name === this.formData.xaPhuong);
                  console.log('Found ward:', this.selectedWard);
                }
              }
            }
          }
        } else {
          console.log('Missing tinhThanh or provinces not loaded:', {
            tinhThanh: this.formData.tinhThanh,
            provincesCount: this.provinces.length
          });
        }

        // Log toàn bộ thông tin địa chỉ sau khi set up
        console.log('Final address setup:', {
          tinhThanh: this.formData.tinhThanh,
          quanHuyen: this.formData.quanHuyen,
          xaPhuong: this.formData.xaPhuong,
          diaChiChiTiet: this.formData.diaChiChiTiet
        });

      } catch (error) {
        console.error('Error loading nhan vien:', error);
        alert('Không thể tải thông tin nhân viên');
        this.$router.push('/admin/nhan-vien');
      }
    },

    async handleProvinceChange() {
      this.districts = [];
      this.wards = [];
      this.selectedDistrict = null;
      this.selectedWard = null;
      
      // Cập nhật tỉnh/thành và reset các trường khác
      this.formData.tinhThanh = this.selectedProvince ? this.selectedProvince.name : '';
      this.formData.quanHuyen = '';
      this.formData.xaPhuong = '';
      
      if (this.selectedProvince) {
        try {
          this.districts = await AddressService.getDistricts(this.selectedProvince.code);
        } catch (error) {
          console.error('Error loading districts:', error);
          alert('Không thể tải danh sách quận/huyện');
        }
      }
    },

    async handleDistrictChange() {
      this.wards = [];
      this.selectedWard = null;
      
      // Cập nhật quận/huyện và reset xã/phường
      this.formData.quanHuyen = this.selectedDistrict ? this.selectedDistrict.name : '';
      this.formData.xaPhuong = '';
      
      if (this.selectedDistrict) {
        try {
          this.wards = await AddressService.getWards(this.selectedDistrict.code);
        } catch (error) {
          console.error('Error loading wards:', error);
          alert('Không thể tải danh sách xã/phường');
        }
      }
    },

    handleWardChange() {
      // Cập nhật xã/phường
      this.formData.xaPhuong = this.selectedWard ? this.selectedWard.name : '';
    },

    async validateAndSave() {
      // Kiểm tra các trường không được trùng lặp (trừ chính nó)
      const otherNhanViens = this.nhanViens.filter(nv => nv.id !== this.formData.id);

      if (otherNhanViens.some(nv => nv.soDienThoai === this.formData.soDienThoai)) {
        alert('Số điện thoại đã tồn tại');
        return;
      }

      if (otherNhanViens.some(nv => nv.soCanCuoc === this.formData.soCanCuoc)) {
        alert('Số căn cước đã tồn tại');
        return;
      }

      // Kiểm tra địa chỉ
      if (!this.formData.tinhThanh || !this.formData.quanHuyen || !this.formData.xaPhuong || !this.formData.diaChiChiTiet) {
        alert('Vui lòng nhập đầy đủ thông tin địa chỉ');
        return;
      }

      try {
        await NhanVienService.update(this.formData.id, this.formData);
        this.$router.push('/admin/nhan-vien');
      } catch (error) {
        console.error('Error updating nhan vien:', error);
        alert('Có lỗi xảy ra khi cập nhật nhân viên');
      }
    }
  }
};
</script> 