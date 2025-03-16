<template>
  <div class="p-6 bg-gray-100 min-h-screen">
    <!-- Quản lý đơn hàng -->
    <div class="bg-white p-4 rounded-lg shadow-md">
      <h2 class="text-lg font-semibold">Quản lý hóa đơn / <span class="text-gray-400">{{ order.maHoaDon || "Không có" }}</span></h2>

      <!-- Lịch sử đơn hàng dạng timeline -->
      <div v-if="order.lichSuHoaDons.length" class="bg-white p-6 rounded-lg shadow-md">
        <div class="flex items-center overflow-x-auto space-x-4 py-4">
          <div v-for="(status, index) in sortedOrderHistory" :key="index" class="flex items-center">
            <div class="flex flex-col items-center">
              <!-- Icon trạng thái -->
              <div class="icon-wrapper" :class="getStatusClass(status.trangThaiMoi)">
                <i v-if="status.trangThaiMoi === 'Tạo hóa đơn'" class="fas fa-file text-xl"></i>
                <i v-else-if="status.trangThaiMoi === 'Chờ xác nhận'" class="fas fa-check-circle text-xl"></i>
                <i v-else-if="status.trangThaiMoi === 'Chờ giao hàng'" class="fas fa-truck-loading text-xl"></i>
                <i v-else-if="status.trangThaiMoi === 'Đang vận chuyển'" class="fas fa-shipping-fast text-xl"></i>
                <i v-else-if="status.trangThaiMoi === 'Hoàn thành'" class="fas fa-check-circle text-xl"></i>
              </div>
              <p class="mt-2 font-semibold text-center">{{ status.trangThaiMoi || "Không xác định" }}</p>
              <p class="text-sm text-gray-500">{{ formatDate(status.createdAt) }}</p>
            </div>

            <!-- Đường dẫn nối các bước -->
            <div v-if="index < sortedOrderHistory.length - 1" class="progress-line"></div>
          </div>
        </div>
      </div>


      <div class="bg-white p-6 rounded-lg shadow-md d-flex mt-4">
        <button v-if="order.trangThai === 'Chờ xác nhận'" @click="xacNhanHoaDon" class="px-4 py-2 border border-green-500 text-green-500 rounded-lg">Xác nhận hóa đơn</button>
        <button v-if="order.trangThai === 'Chờ xác nhận' || order.trangThai === 'Chờ giao hàng' || order.trangThai === 'Đang vận chuyển'"
                @click="huyDon" class="px-4 py-2 border border-red-500 text-red-500 rounded-lg ml-2">Hủy đơn</button>

        <button v-if="order.trangThai === 'Chờ giao hàng'" @click="xacNhanGiaoHang" class="px-4 py-2 border border-blue-500 text-blue-500 rounded-lg">Xác nhận giao hàng</button>

        <button v-if="order.trangThai === 'Đang vận chuyển'" @click="xacNhanLayHang" class="px-4 py-2 border border-yellow-500 text-yellow-500 rounded-lg">Xác nhận lấy hàng</button>

        <button v-if="order.trangThai === 'Hoàn thành'" class="px-4 py-2 border border-orange-400 text-orange-400 rounded-lg">Chi tiết</button>
        <button v-if="order.trangThai === 'Hoàn thành'" class="px-4 py-2 border border-orange-400 text-orange-400 rounded-lg ml-2">In hóa đơn</button>
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
            <p class="mb-3"><strong>Loại:</strong> <span :class="getInvoiceTypeClass(order.loaiHoaDon)">{{ order.loaiHoaDon || "Không xác định" }}</span></p>
            <p><strong>Trạng thái:</strong> <span class="px-2 py-1 rounded" :class="getStatusClass(order.trangThai)">{{ order.trangThai || "Không xác định" }}</span></p>
          </div>
<!--          <button class="px-4 py-2 border border-orange-400 text-orange-400 rounded-lg">Cập nhật</button>-->
        </div>
      </div>

      <!-- Lịch sử thanh toán -->
      <div class="mt-4">
        <h3 class="text-md font-semibold">Lịch sử thanh toán</h3>
        <div class="bg-white p-6 rounded-lg shadow-md">
          <template v-if="order.trangThai === 'Hoàn thành'">
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
                <td class="px-4 py-3">{{ formatDate(order.lichSuHoaDons[0]?.updatedAt) }}</td>
                <td class="px-4 py-3">
              <span :class="getPaymentClass(order.hinhThucThanhToan?.phuongThucThanhToan?.kieuThanhToan)">
                {{ order.hinhThucThanhToan?.phuongThucThanhToan?.kieuThanhToan || 'Không xác định' }}
              </span>
                </td>
                <td class="px-4 py-3">
              <span :class="getStatusClass(order.trangThai)">
                {{ order.trangThai }}
              </span>
                </td>
                <td class="px-4 py-3">{{ order.nhanVien.maNhanVien }}</td>
              </tr>
              </tbody>
            </table>
          </template>

          <!-- Nếu trạng thái không phải "Hoàn thành" -->
          <p v-else class="text-center text-gray-500 py-4">Không có dữ liệu</p>
        </div>
      </div>


      <div class="mt-4">
        <h3 class="text-md font-semibold">Danh sách sản phẩm</h3>
        <div class="bg-white p-6 rounded-lg shadow-md">
          <div v-if="order.hoaDonChiTiets.length">
            <div v-for="item in order.hoaDonChiTiets" :key="item.id"
                 class="grid grid-cols-5 gap-4 items-center border-b py-4">

              <!-- Ảnh sản phẩm -->
              <div class="flex justify-center">
                <img :src="item.chiTietSanPham?.anh || 'default-image.jpg'"
                     alt="Sản phẩm"
                     class="w-16 h-16 object-cover rounded-md">
              </div>

              <!-- Thông tin sản phẩm -->
              <div>
                <p class="font-medium">{{ item.chiTietSanPham?.tenChiTietSanPham }}</p>
                <p class="text-red-500">{{ formatCurrency(item.giaSanPham) }}</p>
                <p class="text-gray-500">Tác giả: {{ item.chiTietSanPham?.tacGia?.tenTacGia }}</p>
                <p>x{{ item.soLuong }}</p>
              </div>

              <!-- Điều chỉnh số lượng -->
              <div class="flex justify-center">
                <div class="flex items-center border rounded-lg">
                  <button @click="decreaseQuantity(item)"
                          class="px-3 py-1 border-r"
                          :disabled="order.trangThai === 'Hoàn thành'">
                    -
                  </button>
                  <input type="text"
                         v-model="item.soLuong"
                         class="w-12 text-center border-none outline-none"
                         :disabled="order.trangThai === 'Hoàn thành'"
                         @input="validateQuantity(item)">
                  <button @click="increaseQuantity(item)"
                          class="px-3 py-1 border-l"
                          :disabled="order.trangThai === 'Hoàn thành'">
                    +
                  </button>
                </div>
              </div>

              <!-- Tổng giá -->
              <p class="text-red-500 font-semibold text-center">
                {{ formatCurrency(tongTienHang) }}
              </p>

              <div class="flex justify-center">
                <button v-if="order.trangThai !== 'Hoàn thành'" @click="removeItem(item.id)" class="text-gray-500 hover:text-red-500">
                  <i class="fas fa-trash text-xl"></i>
                </button>
              </div>
            </div>
          </div>
          <p v-else class="text-gray-500 text-center">Không có sản phẩm nào trong đơn hàng.</p>
        </div>
      </div>



      <!-- Phiếu giảm giá -->
      <div class="mt-4">
        <h3 class="text-md font-semibold">Phiếu giảm giá</h3>
        <div class="bg-white p-6 rounded-lg shadow-md flex justify-between items-center">
          <div class="text-sm">
            <p class="mb-3">Phiếu giảm giá: <strong>{{ order.phieuGiamGia?.maPhieuGiamGia || "Không có" }}</strong></p>
            <p class="mb-3">Giảm giá từ cửa hàng: <strong>0%</strong></p>
          </div>
          <div class="text-sm">
          </div>
          <div class="text-sm">
            <p class="mb-3">Tổng tiền hàng: <strong>{{ formatCurrency(tongTienHang) }}</strong></p>
            <p class="mb-3">Giảm giá: <strong>{{ formatCurrency(tienGiamGia) }}</strong></p>
            <p class="mb-3">Phí vận chuyển: <strong>{{ formatCurrency(order.phiShip) }}</strong></p>
            <hr>
            <p class="mb-3">Tổng tiền: <strong class="text-red-500">{{ formatCurrency(thanhTien)  }}</strong></p>
          </div>
        </div>
      </div>



    </div>
  </div>
</template>

<script>
import HoaDonService from "@/service/hoaDonService";

export default {
  computed: {
    tongTienHang() {
      return this.order.hoaDonChiTiets.reduce((total, item) => {
        return total + item.soLuong * item.giaSanPham;
      }, 0);
    },

    sortedOrderHistory() {
      return [...this.order.lichSuHoaDons].sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));
    },

    // Tính giảm giá
    tienGiamGia() {
      const phanTramGiam = this.order.hoaDonChiTiets?.phieuGiamGia?.soPhanTramGiam || 0;
      return (this.tongTienHang * phanTramGiam) / 100;
    },

    // Tính tổng tiền thanh toán
    thanhTien() {
      const phiShip = this.order.phiShip || 0;
      return this.tongTienHang - this.tienGiamGia + phiShip;
    }
  },


  data() {
    return {
      order: {
        phieuGiamGia: {},
        nhanVien: {},
        hinhThucThanhToan: {},
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
          console.log(response.data);
          this.order = {
            ...response.data,
            lichSuHoaDons: Array.isArray(response.data.lichSuHoaDons) ? response.data.lichSuHoaDons : [],
            hoaDonChiTiets: Array.isArray(response.data.hoaDonChiTiets) ? response.data.hoaDonChiTiets : [],
          };
        }
      } catch (error) {
        console.error("Lỗi khi tải hóa đơn:", error);
      } finally {
        this.loading = false;
      }
    },

    getStatusClass(status) {
      return {
        "Tạo đơn hàng": "bg-green-500 text-white",
        "Chờ xác nhận": "bg-yellow-500 text-white",
        "Chờ giao hàng": "bg-yellow-400 text-white",
        "Đã giao hàng": "bg-green-500 text-white",
        "Hoàn thành": 'text-green-600 bg-green-100 px-2 py-1 rounded'
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

    getInvoiceTypeClass(type) {
      const typeClasses = {
        'Tại quầy': 'text-purple-600 bg-purple-100 px-2 py-1 rounded',
        'Trực tuyến': 'text-indigo-600 bg-indigo-100 px-2 py-1 rounded'
      };
      return typeClasses[type] || 'text-gray-600 bg-gray-100 px-2 py-1 rounded';
    },

    getPaymentClass(pttt) {
      switch (pttt) {
        case "Tiền mặt":
          return "bg-green-200 text-green-800 px-2 py-1 rounded";
        case "Chuyển khoản":
          return "bg-blue-200 text-blue-800 px-2 py-1 rounded";
        case "Tiền mặt và Chuyển khoản":
          return "bg-orange-200 text-orange-800 px-2 py-1 rounded";
        default:
          return "bg-gray-200 text-gray-800 px-2 py-1 rounded";
      }
    },

    increaseQuantity(item) {
      if (this.order.trangThai === 'Hoàn thành') return;
      item.soLuong = Number(item.soLuong) + 1;
    },
    decreaseQuantity(item) {
      if (this.order.trangThai === 'Hoàn thành') return;
      if (item.soLuong > 1) {
        item.soLuong = Number(item.soLuong) - 1;
      }
    },
    validateQuantity(item) {
      // Chỉ cho phép nhập số, nếu không sẽ đặt lại thành 1
      if (!/^\d+$/.test(item.soLuong) || Number(item.soLuong) < 1) {
        item.soLuong = 1;
      }
    }
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
/* Căn chỉnh icon trạng thái */
.icon-wrapper {
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  font-size: 20px;
  color: white;
}

/* Đường nối trạng thái */
.progress-line {
  width: 80px;
  height: 8px;
  background-color: #d1d5db;
  border-radius: 5px;
  margin: 0 10px;
}
</style>
