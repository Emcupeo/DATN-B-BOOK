<template>
  <div class="flex flex-wrap mt-4">
    <div class="w-full mb-12 px-4">
      <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded bg-white">
        <div class="rounded-t mb-0 px-4 py-3 border-0">
          <div class="flex flex-wrap items-center">
            <div class="relative w-full px-4 max-w-full flex-grow flex-1">
              <h3 class="font-semibold text-lg text-blueGray-700">
                Thêm nhân viên mới
              </h3>
            </div>
          </div>
        </div>

        <div class="block w-full overflow-x-auto p-4">
          <form @submit.prevent="validateAndSave">
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
              <div class="flex items-center space-x-2">
                <input v-model="formData.soCanCuoc" type="text" class="flex-grow border rounded px-3 py-2" required>
                <button type="button" @click="toggleCamera" class="bg-green-500 hover:bg-green-600 text-white px-3 py-2 rounded text-sm">
                  <i class="fas fa-camera mr-1"></i> {{ showScanner ? 'Đóng' : 'Quét' }}
                </button>
              </div>
              <div v-if="errorMessage" class="text-red-500 text-xs mt-1">{{ errorMessage }}</div>
            </div>

            <!-- vue-qrcode-reader Scanner -->
            <div v-if="showScanner" class="mb-4 border p-4 rounded bg-gray-100 scanner-container">
              <qrcode-stream @decode="onDecode" @init="onInit"></qrcode-stream>
              <div class="mt-2 text-sm text-gray-600">
                 Đặt mã QR trên CCCD vào khung quét
               </div>
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
// Import vue3-qrcode-reader component
import { QrcodeStream } from 'vue3-qrcode-reader';

export default {
  name: "ThemNhanVien",
  components: {
    // Register the component
    QrcodeStream,
  },
  data() {
    return {
      formData: {
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
      provinces: [],
      districts: [],
      wards: [],
      selectedProvince: null,
      selectedDistrict: null,
      selectedWard: null,
      // Scanner state
      showScanner: false,
      errorMessage: '',
    };
  },
  async created() {
    await this.loadNhanViens();
    await this.loadProvinces();
  },
  methods: {
    async loadNhanViens() {
      try {
        this.nhanViens = await NhanVienService.getAll();
      } catch (error) {
        console.error('Error loading nhan vien:', error);
        this.errorMessage = 'Lỗi tải danh sách nhân viên.';
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
      this.formData.xaPhuong = this.selectedWard ? this.selectedWard.name : '';
    },

    // --- vue-qrcode-reader Methods ---
    toggleCamera() {
      // Simply toggle the visibility
      this.showScanner = !this.showScanner;
      if (!this.showScanner) {
        this.errorMessage = ''; // Clear error when closing
      }
    },

    onDecode(decodedString) {
      console.log('Decoded QR Code:', decodedString);
      this.processQrCode(decodedString);
      // Automatically close scanner after successful decode
      this.showScanner = false;
    },

    async onInit(promise) {
      // Show loading indicator or messages
      this.errorMessage = 'Đang khởi tạo camera...';
      try {
        await promise;
        // Successfully initialized
        this.errorMessage = ''; 
        console.log('Camera initialized successfully');
      } catch (error) {
        console.error('Error initializing camera:', error);
        this.errorMessage = 'Lỗi khởi tạo camera: ';
        if (error.name === 'NotAllowedError') {
          this.errorMessage += 'Bạn cần cấp quyền truy cập camera.';
        } else if (error.name === 'NotFoundError') {
          this.errorMessage += 'Không tìm thấy camera.';
        } else if (error.name === 'NotSupportedError') {
          this.errorMessage += 'Trình duyệt không hỗ trợ truy cập camera an toàn (HTTPS?).';
        } else if (error.name === 'NotReadableError') {
          this.errorMessage += 'Camera đang được sử dụng bởi ứng dụng khác?';
        } else if (error.name === 'OverconstrainedError') {
          this.errorMessage += 'Camera không đáp ứng yêu cầu.';
        } else {
           this.errorMessage += error.message || 'Lỗi không xác định.';
        }
        // Optionally hide the scanner if init fails
        this.showScanner = false;
      }
    },

    processQrCode(qrData) {
      try {
        const parts = qrData.split('|');
        console.log('QR Parts:', parts);
        // Assuming CCCD (0), Name (2), Birth Date (3 - DDMMYYYY), Gender (4), Address (5)
        if (parts && 
            parts.length > 5 && 
            /^\d+$/.test(parts[0]) && 
            parts[3] && parts[3].length === 8 &&
            parts[4] && // Check if gender exists
            parts[5]    // Check if address exists
            ) { 
          const cccdNumber = parts[0];
          const hoTen = parts[2];
          const ngaySinhDDMMYYYY = parts[3];
          const namSinh = ngaySinhDDMMYYYY.substring(4);
          const gioiTinhString = parts[4]; // e.g., "Nam"
          const diaChiDayDu = parts[5]; // Full address string

          // Convert gender string to boolean for the form
          const gioiTinhBoolean = gioiTinhString.toLowerCase() === 'nam';

          // Check for CCCD duplicates
          if (this.nhanViens.some(nv => nv.soCanCuoc === cccdNumber)) {
            this.errorMessage = `Số CCCD '${cccdNumber}' đã tồn tại.`;
            alert(this.errorMessage); // Show alert
            // Clear the input fields
            this.formData.soCanCuoc = ''; 
            this.formData.hoTen = ''; 
            this.formData.namSinh = '';
            this.formData.gioiTinh = true; // Reset gender to default (e.g., Nam)
            this.formData.diaChiChiTiet = ''; // Clear detailed address
            // Reset address dropdowns (optional but good practice)
            this.selectedProvince = null;
            this.selectedDistrict = null;
            this.selectedWard = null;
            this.districts = [];
            this.wards = [];
            this.formData.tinhThanh = '';
            this.formData.quanHuyen = '';
            this.formData.xaPhuong = '';
          } else {
            // Populate the input fields
            this.formData.soCanCuoc = cccdNumber;
            this.formData.hoTen = hoTen;
            this.formData.namSinh = namSinh;
            this.formData.gioiTinh = gioiTinhBoolean; // Populate gender
            this.formData.diaChiChiTiet = diaChiDayDu; // Populate full address into detail field
            
            // Clear dropdown selections as they are not derived from QR
            this.selectedProvince = null;
            this.selectedDistrict = null;
            this.selectedWard = null;
            this.districts = [];
            this.wards = [];
            this.formData.tinhThanh = '';
            this.formData.quanHuyen = '';
            this.formData.xaPhuong = '';

            this.errorMessage = ''; // Clear any previous error message
            alert(`Đã quét thành công:\nCCCD: ${cccdNumber}\nHọ tên: ${hoTen}\nNăm sinh: ${namSinh}\nGiới tính: ${gioiTinhString}\nĐịa chỉ: ${diaChiDayDu}`); 
          }
        } else {
          this.errorMessage = 'Dữ liệu QR không đúng định dạng mong đợi hoặc thiếu thông tin (CCCD, Họ tên, Ngày sinh, Giới tính, Địa chỉ).';
          alert(this.errorMessage);
        }
      } catch (error) {
          console.error("Error processing QR data:", error);
          this.errorMessage = 'Lỗi xử lý dữ liệu QR.';
          alert(this.errorMessage);
      }
    },

    async validateAndSave() {
      // Kiểm tra các trường không được trùng lặp (vẫn giữ kiểm tra này)
      if (this.nhanViens.some(nv => nv.soDienThoai === this.formData.soDienThoai && nv.id !== this.formData.id)) { // Thêm kiểm tra id nếu là update
        alert('Số điện thoại đã tồn tại');
        return;
      }

      // Corrected CCCD validation logic
      if (this.formData.soCanCuoc && this.nhanViens.some(nv => nv.soCanCuoc === this.formData.soCanCuoc && nv.id !== this.formData.id)) {
        alert('Số căn cước đã tồn tại');
        return;
      }

      // Kiểm tra địa chỉ
      if (!this.formData.tinhThanh || !this.formData.quanHuyen || !this.formData.xaPhuong || !this.formData.diaChiChiTiet) {
        alert('Vui lòng nhập đầy đủ thông tin địa chỉ');
        return;
      }

      // Kiểm tra các trường bắt buộc khác nếu cần

      try {
        console.log("Saving data:", this.formData); // Log dữ liệu gửi đi
        await NhanVienService.create(this.formData);
        this.$router.push('/admin/nhan-vien');
      } catch (error) {
        console.error('Error saving nhan vien:', error);
         // Hiển thị thông báo lỗi cụ thể hơn nếu có từ backend
        const errorMessage = error.response?.data?.message || 'Có lỗi xảy ra khi thêm nhân viên';
        alert(errorMessage);
      }
    }
  }
};
</script>

<style scoped>
.scanner-container {
  width: 100%;
  max-width: 500px; /* Adjust as needed */
  margin: 0 auto;
  height: auto; /* Let the library manage height */
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  background-color: #eee; 
}

</style> 