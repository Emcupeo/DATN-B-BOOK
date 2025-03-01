<template>
  <div class="order-info">
    <h2>Thông tin đơn hàng</h2>
    <div v-if="loading">
      <p>Đang tải dữ liệu...</p>
    </div>
    <div v-else-if="order">
      <p><strong>Mã đơn hàng:</strong> {{ order.maHoaDon }}</p>
      <p><strong>Tên khách hàng:</strong> {{ order.tenNguoiNhan }}</p>
      <p><strong>Số điện thoại:</strong> {{ order.soDienThoaiNguoiNhan }}</p>
      <p><strong>Loại:</strong> {{ order.loaiHoaDon }}</p>
      <p>
        <strong>Trạng thái:</strong>
        <span :class="getStatusClass(order.trangThai)">
          {{ getStatusText(order.trangThai) }}
        </span>
      </p>
      <p><strong>Địa chỉ nhận:</strong> {{ order.diaChi }}</p>
    </div>
    <div v-else>
      <p class="error-message">Không tìm thấy đơn hàng!</p>
    </div>
  </div>
</template>

<script>
import HoaDonService from "@/service/hoaDonService";

export default {
  data() {
    return {
      order: null,
      loading: true
    };
  },
  methods: {
    async fetchOrder() {
      const orderId = this.$route.params.id; // Lấy ID từ route
      if (!orderId) {
        console.error("Thiếu ID hóa đơn");
        this.loading = false;
        return;
      }
      try {
        const response = await HoaDonService.getListHoaDonById(orderId);
        this.order = response.data;
      } catch (error) {
        console.error("Lỗi khi tải hóa đơn:", error);
      } finally {
        this.loading = false;
      }
    },
    getStatusClass(status) {
      return {
        pending: "text-yellow-500",
        confirmed: "text-green-500",
        canceled: "text-red-500",
        delivered: "text-blue-500"
      }[status] || "text-gray-500";
    },
    getStatusText(status) {
      return {
        pending: "Chờ xác nhận",
        confirmed: "Đã xác nhận",
        canceled: "Đã hủy",
        delivered: "Đã giao hàng"
      }[status] || "Không xác định";
    }
  },
  mounted() {
    this.fetchOrder();
  }
};
</script>

<style scoped>
.order-info {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #fff;
}
.text-yellow-500 { color: #facc15; }
.text-green-500 { color: #22c55e; }
.text-red-500 { color: #ef4444; }
.text-blue-500 { color: #3b82f6; }
.text-gray-500 { color: #6b7280; }
.error-message {
  color: red;
  font-weight: bold;
}
</style>
