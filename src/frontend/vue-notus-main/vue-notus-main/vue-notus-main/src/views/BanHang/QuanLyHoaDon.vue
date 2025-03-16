<template>
  <div class="p-6 bg-gray-100 min-h-screen">
    <!-- Quản lý đơn hàng -->
    <div class="bg-white p-4 rounded-lg shadow-md">
      <h2 class="text-lg font-semibold">Quản lý hóa đơn / <span class="text-gray-400">{{ order.maHoaDon || "Không có" }}</span></h2>

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
            <p class="mb-3"><strong>Loại:</strong> <span :class="getInvoiceTypeClass(order.loaiHoaDon)">{{ order.loaiHoaDon || "Không xác định" }}</span></p>
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
              <td class="px-4 py-3">{{ formatDate(order.lichSuHoaDons[0]?.updatedAt) }}</td>
              <td class="px-4 py-3">{{order.hinhThucThanhToan?.phuongThucThanhToan?.kieuThanhToan}}</td>
              <td class="px-4 py-3">{{ order.lichSuHoaDons[0]?.trangThaiMoi }}</td>
              <td class="px-4 py-3">{{ order.nhanVien.maNhanVien }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Danh sách sản phẩm -->
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
                  <button @click="decreaseQuantity(item)" class="px-3 py-1 border-r">-</button>
                  <span class="px-4">{{ item.soLuong }}</span>
                  <button @click="increaseQuantity(item)" class="px-3 py-1 border-l">+</button>
                </div>
              </div>

              <!-- Tổng giá -->
              <p class="text-red-500 font-semibold text-center">
                {{ formatCurrency(tongTienHang) }}
              </p>

              <!-- Nút xóa -->
              <div class="flex justify-center">
                <button @click="removeItem(item.id)" class="text-gray-500 hover:text-red-500">
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

    // Tính giảm giá
    tienGiamGia() {
      const phanTramGiam = this.order.hoaDonChiTiets?.phieuGiamGia?.soPhanTramGiam || 0; // Nếu không có, mặc định là 0%
      return (this.tongTienHang * phanTramGiam) / 100; // Áp dụng phần trăm giảm giá
    },

    // Tính tổng tiền thanh toán
    thanhTien() {
      const phiShip = this.order.phiShip || 0; // Nếu phí ship không có thì mặc định là 0
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
