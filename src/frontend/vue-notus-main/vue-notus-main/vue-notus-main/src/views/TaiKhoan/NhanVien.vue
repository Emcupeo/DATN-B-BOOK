<template>
  <div class="flex flex-wrap mt-4 flex justify-center items-center h-screen">
    <div class="w-full mb-12 px-4 bg-white p-6 shadow-lg rounded-lg w-4/5 h-4/5">
      <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded bg-white">
        <div class="rounded-t mb-0 px-4 py-3 border-0">
          <div class="flex flex-wrap items-center">
            <div class="relative w-full px-4 max-w-full flex-grow flex-1">
              <h3 class="font-semibold text-lg text-blueGray-700">
                Danh sách nhân viên
              </h3>
            </div>
            <div class="relative w-full px-4 max-w-full flex-grow flex-1 text-right">
              <button @click="openAddModal" class="bg-indigo-500 text-white active:bg-indigo-600 text-xs font-bold uppercase px-3 py-1 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150">
                Thêm nhân viên
              </button>
            </div>
          </div>
          <div class="mt-4">
            <input v-model="searchKeyword" @input="searchNhanVien" type="text" placeholder="Tìm kiếm nhân viên..." class="border px-3 py-2 rounded">
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
              </td><td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                  <span :class="nhanVien.trangThai ? 'bg-green-500' : 'bg-red-500'" class="px-2 py-1 text-white rounded">
                    {{ nhanVien.trangThai ? 'Hoạt động' : 'Ngừng hoạt động' }}
                  </span>
            </td>

              <td class="border-t-0 px-6 align-middle border-l-0 border-r-0 text-xs whitespace-nowrap p-4">
                <button @click="editNhanVien(nhanVien)" class="bg-orange-500 text-white px-2 py-1 rounded mr-2">Sửa</button>
                <button @click="deleteNhanVien(nhanVien.id)" class="bg-red-500 text-white px-2 py-1 rounded">Xóa</button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Modal thêm/sửa nhân viên -->
    <div v-if="showModal" class="fixed inset-0 bg-opacity-50 flex items-center justify-center backdrop-blur-sm z-50">
      <div class="bg-white p-6 rounded-lg w-1/2">
        <h2 class="text-xl font-bold mb-4">{{ isEditing ? 'Sửa nhân viên' : 'Thêm nhân viên' }}</h2>
        <form @submit.prevent="validateAndSave">
<!--          <div class="mb-4">-->
<!--            <label class="block text-sm font-medium mb-1">Mã nhân viên</label>-->
<!--            <input v-model="formData.maNhanVien" type="text"-->
<!--                   :class="['w-full border rounded px-3 py-2', {'bg-gray-100': isEditing}]"-->
<!--                   :disabled="isEditing"-->
<!--                   :placeholder="isEditing ? '' : 'VD: NV00001'"-->
<!--                   pattern="NV[0-9]{5}"-->
<!--                   title="Mã nhân viên phải có định dạng NVxxxxx (x là số)"-->
<!--                   required>-->
<!--            <span class="text-xs text-gray-500" v-if="!isEditing">Định dạng: NVxxxxx (x là số)</span>-->
<!--          </div>-->
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
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">Tên tài khoản</label>
            <input v-model="formData.tenTaiKhoan" type="text" class="w-full border rounded px-3 py-2" required>
          </div>
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">Mật khẩu</label>
            <input v-model="formData.matKhau" type="password" class="w-full border rounded px-3 py-2" required>
          </div>
          <div class="flex justify-end">
            <button type="button" @click="closeModal" class="bg-gray-500 text-white px-4 py-2 rounded mr-2">Hủy</button>
            <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">Lưu</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import NhanVienService from '@/service/NhanVienService';
import NhanVien from '@/models/NhanVien';

export default {
  name: "NhanVien",
  data() {
    return {
      nhanViens: [],
      showModal: false,
      isEditing: false,
      searchKeyword: '',
      formData: new NhanVien(),
      editingId: null
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
        // Thêm xử lý thông báo lỗi nếu cần
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

    openAddModal() {
      this.isEditing = false;
      this.formData = new NhanVien();
      this.showModal = true;
    },

    editNhanVien(nhanVien) {
      this.isEditing = true;
      this.editingId = nhanVien.id;
      this.formData = new NhanVien(nhanVien);
      this.showModal = true;
    },

    async saveNhanVien() {
      try {
        if (this.isEditing) {
          await NhanVienService.update(this.editingId, this.formData);
        } else {
          await NhanVienService.create(this.formData);
        }
        this.closeModal();
        await this.loadNhanViens();
      } catch (error) {
        console.error('Error saving nhan vien:', error);
        // Thêm xử lý thông báo lỗi nếu cần
      }
    },

    async deleteNhanVien(id) {
      if (confirm('Bạn có chắc chắn muốn xóa nhân viên này?')) {
        try {
          await NhanVienService.delete(id);
          await this.loadNhanViens();
        } catch (error) {
          console.error('Error deleting nhan vien:', error);
          // Thêm xử lý thông báo lỗi nếu cần
        }
      }
    },

    closeModal() {
      this.showModal = false;
      this.formData = new NhanVien();
      this.editingId = null;
    },

    validateAndSave() {
      // Kiểm tra định dạng mã nhân viên
      // const maNhanVienPattern = /^NV[0-9]{5}$/;
      // if (!this.isEditing && !maNhanVienPattern.test(this.formData.maNhanVien)) {
      //   alert('Mã nhân viên phải có định dạng NVxxxxx (x là số)');
      //   return;
      // }

      // Kiểm tra các trường không được trùng lặp
      if (!this.isEditing) {
        // Kiểm tra mã nhân viên
        // if (this.nhanViens.some(nv => nv.maNhanVien === this.formData.maNhanVien)) {
        //   alert('Mã nhân viên đã tồn tại');
        //   return;
        // }

        // Kiểm tra số điện thoại
        if (this.nhanViens.some(nv => nv.soDienThoai === this.formData.soDienThoai)) {
          alert('Số điện thoại đã tồn tại');
          return;
        }

        // Kiểm tra số căn cước
        if (this.nhanViens.some(nv => nv.soCanCuoc === this.formData.soCanCuoc)) {
          alert('Số căn cước đã tồn tại');
          return;
        }

        // Kiểm tra tên tài khoản
        if (this.nhanViens.some(nv => nv.tenTaiKhoan === this.formData.tenTaiKhoan)) {
          alert('Tên tài khoản đã tồn tại');
          return;
        }
      }

      this.saveNhanVien();
    }
  }
};
</script>