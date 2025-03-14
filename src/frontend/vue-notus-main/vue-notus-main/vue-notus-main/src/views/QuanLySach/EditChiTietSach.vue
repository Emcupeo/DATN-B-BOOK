<template>
  <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded bg-white">
    <div class="rounded-t mb-0 px-4 py-3 border-0">
      <div class="flex flex-wrap items-center">
        <div class="relative w-full px-4 max-w-full flex-grow flex-1">
          <button
            class="bg-indigo-500 text-white active:bg-indigo-600 text-xs font-bold uppercase px-3 py-1 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
            type="button"
            @click="goBack"
          >
            <i class="fas fa-arrow-left mr-2"></i>
            Quay lại
          </button>
          <h3 class="font-semibold text-lg text-blueGray-700 mt-3">
            Sửa chi tiết sách
          </h3>
        </div>
      </div>
    </div>

    <!-- Loading state -->
    <div v-if="loading" class="p-4 text-center">
      <i class="fas fa-spinner fa-spin mr-2"></i>
      Đang tải...
    </div>

    <!-- Error state -->
    <div v-else-if="error" class="p-4 text-red-500">
      <i class="fas fa-exclamation-circle mr-2"></i>
      {{ error }}
    </div>

    <!-- Form -->
    <div v-else class="block w-full overflow-x-auto p-8">
      <form @submit.prevent="handleSubmit" class="space-y-6">
        <div class="grid grid-cols-2 gap-6">
          <!-- Cột trái -->
          <div class="space-y-6">
            <div>
              <label class="block text-sm font-medium text-gray-700">Mã chi tiết sản phẩm</label>
              <input
                v-model="formData.maChiTietSanPham"
                type="text"
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 bg-gray-100"
                disabled
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700">Tên chi tiết sản phẩm</label>
              <input
                v-model="formData.tenChiTietSanPham"
                type="text"
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                required
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700">Giá</label>
              <input
                v-model.number="formData.gia"
                type="number"
                min="0"
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                required
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700">Số lượng tồn</label>
              <input
                v-model.number="formData.soLuongTon"
                type="number"
                min="0"
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                required
              />
            </div>
          </div>

          <!-- Cột phải -->
          <div class="space-y-6">
            <div>
              <label class="block text-sm font-medium text-gray-700">Trọng lượng (gram)</label>
              <input
                v-model.number="formData.trongLuong"
                type="number"
                min="0"
                step="0.01"
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                required
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700">Kích thước (cm)</label>
              <input
                v-model.number="formData.kichThuoc"
                type="number"
                min="0"
                step="0.01"
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                required
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700">Trạng thái</label>
              <select
                v-model="formData.trangThai"
                class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
              >
                <option :value="true">Hoạt động</option>
                <option :value="false">Không hoạt động</option>
              </select>
            </div>
          </div>
        </div>

        <!-- Mô tả và ghi chú -->
        <div class="space-y-6">
          <div>
            <label class="block text-sm font-medium text-gray-700">Mô tả</label>
            <textarea
              v-model="formData.moTa"
              rows="3"
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            ></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700">Ghi chú</label>
            <textarea
              v-model="formData.ghiChu"
              rows="2"
              class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            ></textarea>
          </div>
        </div>

        <!-- Buttons -->
        <div class="flex justify-end space-x-3">
          <button
            type="button"
            @click="goBack"
            class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 transition"
          >
            Hủy
          </button>
          <button
            type="submit"
            class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
          >
            Cập nhật
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import ChiTietSanPham from '@/models/ChiTietSanPham';
import ChiTietSanPhamService from '@/service/ChiTietSanPhamService';

export default {
  name: 'EditChiTietSach',

  data() {
    return {
      formData: new ChiTietSanPham(),
      loading: true,
      error: null
    };
  },

  async created() {
    await this.loadChiTietSanPham();
  },

  methods: {
    async loadChiTietSanPham() {
      try {
        this.loading = true;
        this.error = null;
        const id = this.$route.params.id;
        this.formData = await ChiTietSanPhamService.getById(id);
      } catch (error) {
        console.error('Error loading chi tiet san pham:', error);
        this.error = 'Có lỗi xảy ra khi tải thông tin chi tiết sách';
      } finally {
        this.loading = false;
      }
    },

    async handleSubmit() {
      try {
        await ChiTietSanPhamService.update(this.formData.id, this.formData);
        alert('Cập nhật chi tiết sách thành công!');
        this.goBack();
      } catch (error) {
        console.error('Error updating chi tiet san pham:', error);
        alert('Có lỗi xảy ra khi cập nhật chi tiết sách');
      }
    },

    goBack() {
      this.$router.push('/admin/sach');
    }
  }
};
</script> 