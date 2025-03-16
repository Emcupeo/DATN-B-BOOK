<template>
  <div class="p-4 space-y-4">
    <h3 class="text-2xl font-semibold">Quản lý hóa đơn</h3>

    <!-- Thanh tiêu đề + Bộ lọc -->
    <div class="bg-white shadow sm:rounded-lg p-4 space-y-4">
      <!-- Hàng tìm kiếm -->
      <div class="flex items-center gap-2">
        <input
            type="text"
            v-model="searchName"
            placeholder="🔍 Tìm kiếm hóa đơn"
            class="border border-gray-300 rounded px-4 py-2 text-sm w-[700px] focus:outline-none focus:ring"
        />
        <div class="flex gap-2 ml-auto">
          <button class="bg-orange-500 text-white px-4 py-2 rounded text-sm hover:bg-orange-600 flex items-center">
            📷 Quét mã
          </button>
          <button class="bg-green-500 text-white px-4 py-2 rounded text-sm hover:bg-green-600 flex items-center" @click="viewBanHang">
            ➕ Tạo hóa đơn
          </button>
        </div>
      </div>

      <!-- Hàng lọc -->
      <div class="flex flex-wrap items-center gap-4 justify-between">
        <div class="flex-1 flex items-center gap-2">
          <input
              type="date"
              v-model="dateFrom"
              class="border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring w-[350px]"
          />
          <input
              type="date"
              v-model="dateTo"
              class="border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring w-[350px]"
          />

          <!-- Lọc theo khoảng giá -->
          <div class="flex flex-col space-y-2 w-60">
            <label class="text-sm font-medium">
              Khoảng giá: {{ minPrice.toLocaleString() }} - {{ maxPrice.toLocaleString() }} ₫
            </label>

            <input
                type="range"
                v-model="minPrice"
                :min="priceRange.min"
                :max="priceRange.max"
                :step="10000"
                class="w-full"
            />

            <input
                type="range"
                v-model="maxPrice"
                :min="priceRange.min"
                :max="priceRange.max"
                :step="10000"
                class="w-full"
            />
          </div>

          <!-- Lọc theo loại hóa đơn -->
          <div class="flex items-center space-x-2">
            <label class="text-sm font-medium">Loại:</label>

            <label class="flex items-center space-x-1">
              <input type="radio" v-model="selectedType" value="" />
              <span>Tất cả</span>
            </label>

            <label class="flex items-center space-x-1">
              <input type="radio" v-model="selectedType" value="Tại quầy" />
              <span>Tại quầy</span>
            </label>

            <label class="flex items-center space-x-1">
              <input type="radio" v-model="selectedType" value="Trực tuyến" />
              <span>Trực tuyến</span>
            </label>
          </div>
        </div>

        <div class="flex items-center gap-2 mr-12">
          <button class="bg-blue-500 text-white px-4 py-2 rounded text-sm hover:bg-blue-600 flex items-center">
            📥 Export Excel
          </button>
        </div>

      </div>
    </div>

    <!-- Bảng Hóa đơn với Tabs -->
    <div class="relative overflow-x-auto shadow-md sm:rounded-lg bg-white">
      <!-- Tabs -->
      <div class="flex border-b bg-gray-50 px-4 py-2">
        <button v-for="tab in tabs" :key="tab.value"
                @click="selectedTab = tab.value"
                class="relative px-4 py-2 flex items-center gap-2 transition-all duration-200
                 outline-none focus:outline-none focus:ring-0 focus-visible:outline-none"
                :class="selectedTab === tab.value ? 'after:absolute after:left-0 after:bottom-0 after:w-full after:h-0.5 after:bg-blue-500 text-blue-500 font-semibold' : 'text-gray-500'">
          {{ tab.label }}
          <!-- Hiển thị số mũ nếu có hóa đơn -->
          <span v-if="invoiceCounts[tab.value] > 0"
                class="absolute -top-2 -right-2 bg-red-500 text-white text-xs font-semibold px-2 py-0.5 rounded-full min-w-[22px] text-center shadow-md">
          {{ invoiceCounts[tab.value] }}
          </span>
        </button>
      </div>

      <table class="w-full text-sm text-left text-gray-500">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50">
        <tr>
          <th class="px-4 py-3">#</th>
          <th class="px-6 py-3">Mã HĐ</th>
          <th class="px-6 py-3">Tổng SP</th>
          <th class="px-6 py-3">Tổng tiền</th>
          <th class="px-6 py-3">Nhân viên</th>
          <th class="px-6 py-3">Khách hàng</th>
          <th class="px-6 py-3">Số điện thoại</th>
          <th class="px-6 py-3">Ngày tạo</th>
          <th class="px-6 py-3">Loại hóa đơn</th>
          <th class="px-6 py-3">Trạng thái</th>
          <th class="px-6 py-3">Hành động</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(invoice, index) in paginatedInvoices" :key="invoice.id" class="bg-white border-b hover:bg-gray-50">
          <td class="px-4 py-3">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
          <td class="px-6 py-4 font-medium text-gray-900">{{ invoice.maHoaDon }}</td>
          <td class="px-6 py-4">{{ getTotalProducts(invoice.hoaDonChiTiets) }}</td>
          <td class="px-6 py-4">{{ formatCurrency(invoice.tongTien) }}</td>
          <td class="px-6 py-4">{{ invoice.nhanVien.maNhanVien }}</td>
          <td class="px-6 py-4">{{ invoice.tenNguoiNhan }}</td>
          <td class="px-6 py-4">{{ invoice.soDienThoaiNguoiNhan }}</td>
          <td class="px-6 py-4">{{ formatDate(invoice.ngayTao) }}</td>
          <td class="px-6 py-4">
            <span :class="getInvoiceTypeClass(invoice.loaiHoaDon)">
              {{ invoice.loaiHoaDon }}
            </span>
          </td>
          <td class="px-6 py-4">
            <span :class="getStatusClass(invoice.trangThai)">
              {{ invoice.trangThai }}
            </span>
          </td>
          <td class="px-6 py-4 text-center">
            <div class="flex justify-center items-center w-20">
              <i
                  class="fas fa-eye text-gray-500 cursor-pointer hover:text-blue-500 text-lg"
                  @click="viewDetails(invoice.id)"
              ></i>
            </div>
          </td>
        </tr>
        </tbody>
      </table>

      <!-- Phân trang -->
      <div class="flex justify-between items-center p-4 bg-white border-t">
        <div class="flex items-center">
          <span>Xem</span>
          <select v-model="itemsPerPage" class="mx-2 border rounded px-2 py-1">
            <option v-for="size in [5, 10, 20, 50]" :key="size" :value="size">{{ size }}</option>
          </select>
          <span>sản phẩm</span>
        </div>

        <div class="flex items-center gap-2">
          <button @click="prevPage" :disabled="currentPage === 1" class="px-3 py-1 border rounded bg-gray-200 hover:bg-gray-300 disabled:opacity-50">
            <i class="fa-solid fa-chevron-left"></i>
          </button>
          <span>{{ currentPage }}</span>
          <button @click="nextPage" :disabled="currentPage === totalPages" class="px-3 py-1 border rounded bg-gray-200 hover:bg-gray-300 disabled:opacity-50">
            <i class="fa-solid fa-chevron-right"></i>
          </button>
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
      selectedType: "",
      minPrice: 0,
      maxPrice: 5000000,
      priceRange: {
        min: 0,
        max: 5000000,
      },
      selectedTab: 'all',
      searchName: "",
      dateFrom: "",
      dateTo: "",
      invoices: [],
      tabs: [
        {label: 'TẤT CẢ', value: 'all'},
        {label: 'ĐÃ HỦY', value: 'cancelled'},
        {label: 'CHỜ XÁC NHẬN', value: 'pending'},
        {label: 'CHỜ GIAO HÀNG', value: 'shipping'},
        {label: 'ĐÃ GIAO HÀNG', value: 'delivered'},
        {label: 'ĐÃ THANH TOÁN', value: 'paid'},
        {label: 'HOÀN THÀNH', value: 'completed'}
      ],
      currentPage: 1,
      itemsPerPage: 10,
    };
  },
  computed: {
    filteredInvoices() {
      const tabMapping = {
        all: '',
        cancelled: 'Đã hủy',
        pending: 'Chờ xác nhận',
        shipping: 'Chờ giao hàng',
        delivered: 'Đã giao hàng',
        paid: 'Đã thanh toán',
        completed: 'Hoàn thành'
      };

      let result = this.invoices;

      // Lọc theo tab trạng thái
      if (this.selectedTab !== 'all') {
        result = result.filter(inv => inv.trangThai === tabMapping[this.selectedTab]);
      }

      // Lọc theo loại hóa đơn
      if (this.selectedType) {
        result = result.filter(inv => inv.loaiHoaDon === this.selectedType);
      }

      // Lọc theo tìm kiếm (mã, tên, sđt, mã nhân viên)
      if (this.searchName) {
        const searchLower = this.searchName.toLowerCase();
        result = result.filter(inv =>
            inv.maHoaDon.toLowerCase().includes(searchLower) ||
            inv.tenNguoiNhan.toLowerCase().includes(searchLower) ||
            inv.soDienThoaiNguoiNhan.includes(searchLower) ||
            inv.nhanVien.maNhanVien.toLowerCase().includes(searchLower)
        );
      }

      // Lọc theo ngày
      if (this.dateFrom) {
        const fromDate = new Date(this.dateFrom);
        fromDate.setHours(0, 0, 0, 0);
        result = result.filter(inv => new Date(inv.ngayTao) >= fromDate);
      }

      if (this.dateTo) {
        const toDate = new Date(this.dateTo);
        toDate.setHours(23, 59, 59, 999);
        result = result.filter(inv => new Date(inv.ngayTao) <= toDate);
      }

      // Lọc theo khoảng giá
      result = result.filter(inv => inv.tongTien >= this.minPrice && inv.tongTien <= this.maxPrice);

      return result;
    },

    invoiceCounts() {
      return this.filteredInvoices.reduce((counts, invoice) => {
        const statusMapping = {
          "Đã hủy": "cancelled",
          "Chờ xác nhận": "pending",
          "Chờ giao hàng": "shipping",
          "Đã giao hàng": "delivered",
          "Đã thanh toán": "paid",
          "Hoàn thành": "completed"
        };

        const mappedStatus = statusMapping[invoice.trangThai];
        if (mappedStatus) {
          counts[mappedStatus] = (counts[mappedStatus] || 0) + 1;
        }
        counts.all = (counts.all || 0) + 1;

        return counts;
      }, {
        all: 0, cancelled: 0, pending: 0, shipping: 0, delivered: 0, paid: 0, completed: 0
      });
    },

    paginatedInvoices() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredInvoices.slice(start, end);
    },

    totalPages() {
      return Math.ceil(this.filteredInvoices.length / this.itemsPerPage);
    }

  },
  methods: {
    formatCurrency(value) {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(value);
    },

    viewDetails(id) {
      this.$router.push({path: `/admin/hoa-don-chi-tiet/${id}`});
    },

    viewBanHang(){
      this.$router.push('/admin/ban-hang-tai-quay');
    },

    getListHoaDon() {
      HoaDonService.getListHoaDon().then((response) => {
        this.invoices = response.data;
        this.currentPage = 1;
      });
    },
    getTotalProducts(hoaDonChiTiets) {
      if (!Array.isArray(hoaDonChiTiets)) return 0;
      return hoaDonChiTiets.reduce((total, item) => total + (item.soLuong || 0), 0);
    },
    getStatusClass(status) {
      const statusClasses = {
        'Đã thanh toán': 'text-green-600 bg-green-100 px-2 py-1 rounded',
        'Hoàn thành': 'text-green-600 bg-green-100 px-2 py-1 rounded',
        'Đã hủy': 'text-red-600 bg-red-100 px-2 py-1 rounded',
        'Chờ xác nhận': 'text-yellow-600 bg-yellow-100 px-2 py-1 rounded',
        'Chờ giao hàng': 'text-yellow-600 bg-yellow-100 px-2 py-1 rounded',
        'Đã giao hàng': 'text-blue-600 bg-blue-100 px-2 py-1 rounded'
      };
      return statusClasses[status] || 'text-gray-600 bg-gray-100 px-2 py-1 rounded';
    },
    formatDate(dateString) {
      if (!dateString) return "Không có dữ liệu";
      return new Date(dateString).toLocaleString("vi-VN");
    },
    getInvoiceTypeClass(type) {
      const typeClasses = {
        'Tại quầy': 'text-purple-600 bg-purple-100 px-2 py-1 rounded',
        'Trực tuyến': 'text-indigo-600 bg-indigo-100 px-2 py-1 rounded'
      };
      return typeClasses[type] || 'text-gray-600 bg-gray-100 px-2 py-1 rounded';
    },
    prevPage() {
      if (this.currentPage > 1) this.currentPage--;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) this.currentPage++;
    },
  },
  created() {
    this.getListHoaDon();
  }
};
</script>
