<template>
  <div class="p-6 bg-gray-100 min-h-screen">
    <!-- Quản lý đơn hàng -->
    <div class="bg-white p-4 rounded-lg shadow-md">
      <h2 class="text-lg font-semibold">Quản lý đơn hàng / <span class="text-gray-400">{{ order.maHoaDon || "Không có" }}</span></h2>

<!--      Lich su -->
      <div class="mt-4">
        <h3 class="text-md font-semibold">Lịch sử đơn hàng</h3>
        <div v-if="loading" class="text-gray-500">Đang tải dữ liệu...</div>
        <div v-else-if="order.lichSuHoaDons.length" class="bg-white p-6 rounded-lg shadow-md">
          <div class="flex items-center space-x-4 overflow-x-auto">
            <div v-for="(status, index) in order.lichSuHoaDons" :key="index" class="flex items-center">
              <div class="flex flex-col items-center">
                <div class="w-12 h-12 flex items-center justify-center rounded-full text-white" :class="getStatusClass(status.trangThaiMoi)">
                  <i v-if="status.trangThaiMoi === 'Tạo đơn hàng'" class="fas fa-file text-xl"></i>
                  <i v-else-if="status.trangThaiMoi === 'Chờ xác nhận'" class="fas fa-hourglass-half text-xl"></i>
                  <i v-else-if="status.trangThaiMoi === 'Chờ giao hàng'" class="fas fa-truck text-xl"></i>
                  <i v-else-if="status.trangThaiMoi === 'Đã giao hàng'" class="fas fa-check-circle text-xl"></i>
                </div>
                <p class="mt-2 font-semibold text-center">{{ status.trangThaiMoi || "Không xác định" }}</p>
                <p class="text-sm text-gray-500">{{ formatDate(status.createdAt) }}</p>
              </div>
              <div v-if="index < order.lichSuHoaDons.length - 1" class="w-12 h-1 bg-gray-300 mx-2"></div>
            </div>
          </div>
        </div>
      </div>

      <div class="bg-white p-6 rounded-lg shadow-md d-flex mt-4">
        <button class="px-4 py-2 border border-orange-400 text-orange-400 rounded-lg">Xác nhận đơn hàng</button>
        <button class="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 ml-2">Hủy đơn</button>
      </div>

      <!-- Thông tin đơn hàng-->
      <div class="mt-4">
        <h3 class="text-md font-semibold">Thông tin đơn hàng - Đơn tại quầy</h3>
        <div class="bg-white p-6 rounded-lg shadow-md flex justify-between items-center">
          <div class="text-sm">
            <p class="mb-3"><strong>Mã:</strong> {{ order.maHoaDon || "Không có" }}</p>
            <p><strong>Số điện thoại:</strong> {{ order.soDienThoaiNguoiNhan || "Không có" }}</p>
          </div>
          <div class="text-sm">
            <p class="mb-3"><strong>Têsn khách hàng:</strong> {{ order.tenNguoiNhan || "Khách lẻ" }}</p>
            <p><strong>Tên người nhận:</strong> {{ order.diaChi || "Không có" }}</p>
          </div>
          <div class="text-sm">
            <p class="mb-3"><strong>Loại:</strong> <span class="px-2 py-1 rounded bg-blue-100 text-blue-600">{{ order.loaiHoaDon || "Không xác định" }}</span></p>
            <p><strong>Trạng thái:</strong> <span class="px-2 py-1 rounded" :class="getStatusClass(order.trangThai)">{{ order.trangThai || "Không xác định" }}</span></p>
          </div>
          <button class="px-4 py-2 border border-orange-400 text-orange-400 rounded-lg">Cập nhật</button>
        </div>
      </div>

      <!-- Lịch sử thanh toán -->
      <div class="mt-4">
        <h3 class="text-md font-semibold">Lịch sử thanh toán</h3>
        <div class="bg-white p-6 rounded-lg shadow-md">
          <table class="w-full text-sm text-left text-gray-500">
            <thead class="text-xs text-gray-700 uppercase bg-gray-50">
            <tr>
              <th class="px-4 py-3">Số tiền</th>
              <th class="px-4 py-3">Thời gian</th>
              <th class="px-4 py-3">PTTT</th>
              <th class="px-4 py-3">Trạng thái</th>
              <th class="px-4 py-3">Nhân viên xác nhận</th>
            </tr>
            </thead>
            <tbody>
            <tr class="bg-white border-b hover:bg-gray-50">
              <td class="px-4 py-3">{{ order.tongTien }} đ</td>
<!--              <td class="px-4 py-3">{{ formatDate(order.lichSuHoaDons.updatedAt) }}</td>-->
<!--              <td class="px-4 py-3">{{ order.phuongThucThanhToan. }}</td>-->
<!--              <td class="px-4 py-3">{{ payment. }}</td>-->
<!--              <td class="px-4 py-3">{{ order.idNhanVien }}</td>-->
            </tr>
            </tbody>
          </table>
        </div>
      </div>

<!--      Danh sach san pham-->
      <div class="mt-4">
        <h3 class="text-md font-semibold">Danh sách sản phẩm</h3>
        <div class="bg-white p-6 rounded-lg shadow-md">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import HoaDonService from "@/service/hoaDonService";

export default {
  data() {
    return {
      order: {
        lichSuHoaDons: [],
        hoaDonChiTiets: [],
      },
      loading: true
    };
  },
  methods: {
    async fetchOrder() {
      try {
        const orderId = this.$route.params.id;
        if (!orderId) {
          console.error("Thiếu ID hóa đơn");
          this.loading = false;
          return;
        }
        console.log("Fetching order with ID:", orderId);
        const response = await HoaDonService.getListHoaDonById(orderId);
        if (response.data) {
          this.order = {
            ...response.data,
            lichSuHoaDons: Array.isArray(response.data.lichSuHoaDons) ? response.data.lichSuHoaDons : [],
            hoaDonChiTiets: Array.isArray(response.data.hoaDonChiTiets) ? response.data.hoaDonChiTiets : [],
          };
          console.log("Order data received:", this.order);
        } else {
          console.error("Dữ liệu trả về không hợp lệ:", response);
          this.order = { lichSuHoaDons: [] };
        }
      } catch (error) {
        console.error("Lỗi khi tải hóa đơn:", error);
        if (error.response) {
          console.error("Chi tiết lỗi từ server:", error.response.data);
        }
      } finally {
        this.loading = false;
      }
    },

    getStatusClass(status) {
      return {
        "Tạo đơn hàng": "bg-green-500 text-white",
        "Chờ xác nhận": "bg-yellow-500 text-white",
        "Chờ giao hàng": "bg-yellow-400 text-white",
        "Đã giao hàng": "bg-green-500 text-white"
      }[status] || "bg-gray-500 text-white";
    },

    formatDate(dateString) {
      if (!dateString) return "Không có dữ liệu";
      return new Date(dateString).toLocaleString("vi-VN");
    },

    formatCurrency(value) {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(value);
    },
  },
  mounted() {
    this.fetchOrder();
  }
};
</script>

<style scoped>
.bg-green-500 { background-color: #22c55e; }
.bg-yellow-500 { background-color: #facc15; }
.bg-yellow-400 { background-color: #fbbf24; }
.bg-gray-500 { background-color: #6b7280; }
</style>
