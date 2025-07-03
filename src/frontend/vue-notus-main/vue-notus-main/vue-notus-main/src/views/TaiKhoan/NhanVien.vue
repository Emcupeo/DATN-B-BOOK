<template>
  <div class="flex flex-wrap mt-4">
    <div class="w-full mb-12 px-4">
      <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded bg-white">
        <div class="rounded-t mb-0 px-4 py-3 border-0">
          <div class="flex flex-wrap items-center">
            <div class="relative w-full px-4 max-w-full flex-grow flex-1">
              <h3 class="font-semibold text-lg text-blueGray-700">
                Danh sách nhân viên
              </h3>
            </div>
            <div class="flex space-x-2">
              <button type="button" @click="exportToExcel"
                      class="flex items-center gap-2 text-white bg-gradient-to-r from-green-600 via-green-700 to-green-800 hover:from-green-700 hover:via-green-800 hover:to-green-900 focus:ring-4 focus:outline-none focus:ring-green-400 dark:focus:ring-green-900 font-medium rounded-lg text-sm px-5 py-2.5 text-center mt-5">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" viewBox="0,0,256,256">
                  <g fill="#ffffff">
                    <g transform="scale(5.12,5.12)">
                      <path d="M16,4c-1.65,0 -3,1.35 -3,3v4h2v-4c0,-0.55 0.45,-1 1,-1h14v8h-3.49023c0.29,0.61 0.46047,1.28 0.48047,2h3.00977v8h-3v2h3v8h-3.00977c-0.02,0.72 -0.19047,1.39 -0.48047,2h3.49023v8h-14c-0.55,0 -1,-0.45 -1,-1v-4h-2v4c0,1.65 1.35,3 3,3h30c1.65,0 3,-1.35 3,-3v-36c0,-1.65 -1.35,-3 -3,-3zM32,6h14c0.55,0 1,0.45 1,1v7h-15zM4.19922,13c-1.75547,0 -3.19922,1.44375 -3.19922,3.19922v17.60156c0,1.75547 1.44375,3.19922 3.19922,3.19922h17.60156c1.75547,0 3.19922,-1.44375 3.19922,-3.19922v-17.60156c0,-1.75547 -1.44375,-3.19922 -3.19922,-3.19922zM4.19922,15h17.60156c0.67453,0 1.19922,0.52468 1.19922,1.19922v17.60156c0,0.67453 -0.52469,1.19922 -1.19922,1.19922h-17.60156c-0.67453,0 -1.19922,-0.52468 -1.19922,-1.19922v-17.60156c0,-0.67453 0.52469,-1.19922 1.19922,-1.19922zM32,16h15v8h-15zM7.96875,19l3.49414,5.97852l-3.83203,6.02148h2.86328l2.52148,-4.7168l2.5332,4.7168h2.82031l-3.76953,-6l3.68555,-6h-2.67578l-2.45508,4.50586l-2.32422,-4.50586zM32,26h15v8h-15zM32,36h15v7c0,0.55 -0.45,1 -1,1h-14z"></path>
                    </g>
                  </g>
                </svg>
                Xuất Excel
              </button>

              <router-link to="/admin/nhan-vien/them-moi"
                           class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-600 via-blue-700 to-blue-800 hover:from-blue-700 hover:via-blue-800 hover:to-blue-900 focus:ring-4 focus:outline-none focus:ring-blue-400 dark:focus:ring-blue-900 font-medium rounded-lg text-sm px-5 py-2.5 text-center mt-5">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                     stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M12 5v14M5 12h14" />
                </svg>
                Thêm mới
              </router-link>
            </div>
          </div>
          <br>
          <div class="flex-1">
            <input type="text" v-model="searchKeyword"
                   placeholder="Tìm kiếm theo tên, email, số điện thoại..."
                   class="w-full border rounded px-3 py-2"
                   @input="searchNhanVien">
          </div>

        </div>

        <div class="block w-full overflow-x-auto">
          <table class="items-center w-full bg-transparent border-collapse">
            <thead>
            <tr>
              <th class="px-6 align-middle border border-solid py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left bg-blueGray-50 text-blueGray-500 border-blueGray-100">
                Mã NV
              </th>
              <th class="px-6 align-middle border border-solid py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left bg-blueGray-50 text-blueGray-500 border-blueGray-100">
                Họ tên
              </th>
              <th class="px-6 align-middle border border-solid py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left bg-blueGray-50 text-blueGray-500 border-blueGray-100">
                Số điện thoại
              </th>
              <th class="px-6 align-middle border border-solid py-3 text-xs uppercase border-l-0 border-r-0 whitespace-nowrap font-semibold text-left bg-blueGray-50 text-blueGray-500 border-blueGray-100">
                Email
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
            <tr v-for="nhanVien in nhanViens" :key="nhanVien.id">
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                {{ nhanVien.maNhanVien }}
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                {{ nhanVien.hoTen }}
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                {{ nhanVien.soDienThoai }}
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                {{ nhanVien.email }}
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs p-4">
                {{ formatDiaChi(nhanVien) }}
              </td>
              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                <div class="flex items-center">
                  <label class="switch">
                    <input type="checkbox" :checked="nhanVien.trangThai" @change="toggleStatus(nhanVien.id)">
                    <span class="slider round"></span>
                  </label>
                  <span class="ml-2" :class="nhanVien.trangThai ? 'text-green-500' : 'text-red-500'">
                      {{ nhanVien.trangThai ? 'Hoạt động' : 'Không hoạt động' }}
                    </span>
                </div>
              </td>





              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                <div class="flex space-x-2">
                  <router-link :to="'/admin/nhan-vien/chinh-sua/' + nhanVien.id"
                               class="text-blue-600 hover:text-blue-800">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                         stroke="currentColor" class="w-5 h-5">
                      <path stroke-linecap="round" stroke-linejoin="round"
                            d="M16.862 3.487a2.25 2.25 0 1 1 3.182 3.182L6.75 19.5l-4.5 1.5 1.5-4.5 13.294-13.294z" />
                    </svg>
                  </router-link>
                  <button @click="deleteNhanVien(nhanVien.id)" class="text-red-600 hover:text-red-800">
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
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NhanVienService from '@/service/NhanVienService';
import * as XLSX from 'xlsx';

export default {
  name: "NhanVien",
  data() {
    return {
      nhanViens: [],
      searchKeyword: ''
    };
  },
  async created() {
    await this.loadNhanViens();
  },
  methods: {
    async loadNhanViens() {
      try {
        this.nhanViens = await NhanVienService.getAll();
      } catch (error) {
        console.error('Error loading nhan vien:', error);
      }
    },

    async searchNhanVien() {
      if (this.searchKeyword.trim()) {
        try {
          this.nhanViens = await NhanVienService.search(this.searchKeyword);
        } catch (error) {
          console.error('Error searching nhan vien:', error);
        }
      } else {
        await this.loadNhanViens();
      }
    },

    async deleteNhanVien(id) {
      if (confirm('Bạn có chắc chắn muốn xóa nhân viên này?')) {
        try {
          await NhanVienService.delete(id);
          await this.loadNhanViens();
        } catch (error) {
          console.error('Error deleting nhan vien:', error);
          alert('Có lỗi xảy ra khi xóa nhân viên');
        }
      }
    },

    async toggleStatus(id) {
      try {
        await NhanVienService.toggleStatus(id);
        await this.loadNhanViens();
      } catch (error) {
        console.error('Error toggling status:', error);
        alert('Không thể thay đổi trạng thái nhân viên');
      }
    },

    formatDiaChi(nhanVien) {
      if (!nhanVien.diaChiChiTiet || !nhanVien.xaPhuong || !nhanVien.quanHuyen || !nhanVien.tinhThanh) {
        return 'Chưa có địa chỉ';
      }
      return `${nhanVien.diaChiChiTiet}, ${nhanVien.xaPhuong}, ${nhanVien.quanHuyen}, ${nhanVien.tinhThanh}`;
    },

    exportToExcel() {
      const data = this.nhanViens.map(nv => ({
        'Mã nhân viên': nv.maNhanVien,
        'Họ tên': nv.hoTen,
        'Số điện thoại': nv.soDienThoai,
        'Số căn cước': nv.soCanCuoc,
        'Email': nv.email,
        'Năm sinh': nv.namSinh,
        'Giới tính': nv.gioiTinh ? 'Nam' : 'Nữ',
        'Địa chỉ': this.formatDiaChi(nv),
        'Tên tài khoản': nv.tenTaiKhoan,
        'Trạng thái': nv.trangThai ? 'Hoạt động' : 'Không hoạt động'
      }));

      const ws = XLSX.utils.json_to_sheet(data);
      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, 'Nhân viên');
      XLSX.writeFile(wb, 'danh-sach-nhan-vien.xlsx');
    }
  }
};
</script>

<style scoped>
/* Switch styles */
.switch {
  position: relative;
  display: inline-block;
  width: 48px;
  height: 24px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 18px;
  width: 18px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: .4s;
}

input:checked + .slider {
  background-color: #48bb78;
}

input:focus + .slider {
  box-shadow: 0 0 1px #48bb78;
}

input:checked + .slider:before {
  transform: translateX(24px);
}

.slider.round {
  border-radius: 24px;
}

.slider.round:before {
  border-radius: 50%;
}
</style>
