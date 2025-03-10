<template>
  <div
    class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded bg-white"
  >
    <!-- Header với nút back -->
    <div class="rounded-t mb-0 px-4 py-3 border-0">
      <div class="flex flex-wrap items-center justify-between">
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
            Chi tiết sách
          </h3>
        </div>
        <div
          class="relative w-full px-4 max-w-full flex-grow flex-1 text-right"
        >
          <button
            class="bg-green-500 text-white active:bg-green-600 text-xs font-bold uppercase px-3 py-1 rounded outline-none focus:outline-none mr-1 mb-1 ease-linear transition-all duration-150"
            type="button"
            @click="addChiTietSach"
          >
            <i class="fas fa-plus mr-2"></i>
            Tạo chi tiết sách
          </button>
        </div>
      </div>
    </div>

    <!-- Loading state -->
    <div v-if="loading" class="p-4 text-center">
      <i class="fas fa-spinner fa-spin mr-2"></i>
      Đang tải...
    </div>

    <!-- Error state - Not Found -->
    <div
      v-else-if="error && error.includes('không tìm thấy')"
      class="p-8 text-center"
    >
      <div class="mb-4">
        <i class="fas fa-exclamation-circle text-4xl text-yellow-500 mb-4"></i>
        <p class="text-lg text-gray-700 mb-6">
          Chưa có chi tiết sản phẩm cho sách này
        </p>
        <button
          @click="addChiTietSach"
          class="bg-green-500 text-white px-6 py-3 rounded-lg hover:bg-green-600 transition-colors duration-200"
        >
          <i class="fas fa-plus mr-2"></i>
          Thêm chi tiết sản phẩm
        </button>
      </div>
    </div>

    <!-- Other Error state -->
    <div v-else-if="error" class="p-4 text-red-500">
      <i class="fas fa-exclamation-circle mr-2"></i>
      {{ error }}
    </div>

    <!-- Content -->

    <div v-else-if="chiTietSanPham" class="block w-full overflow-x-auto p-4">
      <!-- <template v-for="chiTietSanPham in paginatedSanPham" v-bind:key="chiTietSanPham.id"> -->
      <div class="grid grid-cols-2 gap-4">
        <!-- Thông tin cơ bản -->
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700"
              >Mã chi tiết sản phẩm</label
            >
            <div class="mt-1 text-sm">
              {{ chiTietSanPham.maChiTietSanPham }}
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700"
              >Tên sách</label
            >
            <div class="mt-1 text-sm">
              {{ chiTietSanPham.tenChiTietSanPham }}
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700">Giá</label>
            <div class="mt-1 text-sm">
              {{ formatCurrency(chiTietSanPham.gia) }}
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700"
              >Số lượng tồn</label
            >
            <div class="mt-1 text-sm">{{ chiTietSanPham.soLuongTon }}</div>
          </div>
        </div>

        <!-- Thông tin chi tiết -->
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700"
              >Trọng lượng</label
            >
            <div class="mt-1 text-sm">{{ chiTietSanPham.trongLuong }} gram</div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700"
              >Kích thước</label
            >
            <div class="mt-1 text-sm">{{ chiTietSanPham.kichThuoc }} cm</div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700"
              >Trạng thái</label
            >
            <div class="mt-1">
              <span
                :class="[
                  'px-2 inline-flex text-xs leading-5 font-semibold rounded-full',
                  chiTietSanPham.trangThai
                    ? 'bg-green-100 text-green-800'
                    : 'bg-red-100 text-red-800',
                ]"
              >
                {{ chiTietSanPham.trangThai ? "Hoạt động" : "Không hoạt động" }}
              </span>
            </div>
          </div>
        </div>

        <!-- Mô tả -->
        <div class="col-span-2">
          <label class="block text-sm font-medium text-gray-700">Mô tả</label>
          <div class="mt-1 text-sm">
            {{ chiTietSanPham.moTa || "Không có mô tả" }}
          </div>
        </div>

        <!-- Ghi chú -->
        <div class="col-span-2">
          <label class="block text-sm font-medium text-gray-700">Ghi chú</label>
          <div class="mt-1 text-sm">
            {{ chiTietSanPham.ghiChu || "Không có ghi chú" }}
          </div>
        </div>

        <!-- Thông tin thời gian -->
        <div class="col-span-2 border-t pt-4 mt-4">
          <div class="grid grid-cols-2 gap-4 text-sm text-gray-500">
            <div>
              <p>Ngày tạo: {{ formatDate(chiTietSanPham.createdAt) }}</p>
              <p>Người tạo: {{ chiTietSanPham.createdBy || "N/A" }}</p>
            </div>
            <div>
              <p>
                Cập nhật lần cuối: {{ formatDate(chiTietSanPham.updatedAt) }}
              </p>
              <p>Người cập nhật: {{ chiTietSanPham.updatedBy || "N/A" }}</p>
            </div>
          </div>
        </div>

        <!-- Thêm nút Sửa và Xóa -->
        <div class="col-span-2 flex justify-end space-x-3 mt-6">
          <button
            @click="editChiTietSach"
            class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
          >
            <i class="fas fa-edit mr-2"></i>
            Sửa
          </button>
          <button
            @click="deleteChiTietSach"
            class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition"
          >
            <i class="fas fa-trash mr-2"></i>
            Xóa
          </button>
        </div>
      </div>
      <!-- </template> -->
    </div>
  </div>
</template>

<script>
import ChiTietSanPhamService from "@/service/ChiTietSanPhamService";

export default {
  name: "ChiTietSach",

  data() {
    return {
      chiTietSanPham: null,
      loading: true,
      error: null,
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
        this.chiTietSanPham = await ChiTietSanPhamService.getById(id);
      } catch (error) {
        console.error("Error loading chi tiet san pham:", error);
        this.error =
          error.response?.data?.message ||
          "Có lỗi xảy ra khi tải thông tin chi tiết sản phẩm";
      } finally {
        this.loading = false;
      }
    },

    formatCurrency(value) {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(value);
    },

    formatDate(date) {
      if (!date) return "N/A";
      return new Date(date).toLocaleString("vi-VN");
    },

    goBack() {
      this.$router.push("/admin/sach");
    },

    editChiTietSach() {
      this.$router.push(`/admin/sach/edit/${this.chiTietSanPham.id}`);
    },

    addChiTietSach() {
      this.$router.push(`/admin/sach/add/${this.chiTietSanPham.id}`);
    },

    async deleteChiTietSach() {
      if (confirm("Bạn có chắc chắn muốn xóa chi tiết sách này?")) {
        try {
          await ChiTietSanPhamService.delete(this.chiTietSanPham.id);
          alert("Xóa chi tiết sách thành công!");
          this.goBack();
        } catch (error) {
          console.error("Error deleting chi tiet san pham:", error);
          alert("Có lỗi xảy ra khi xóa chi tiết sách");
        }
      }
    },
  },
};
</script>
