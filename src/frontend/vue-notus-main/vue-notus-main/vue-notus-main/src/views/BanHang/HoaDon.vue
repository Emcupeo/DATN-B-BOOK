<template>
  <div class="p-4 space-y-4">
    <h3 class="text-md font-semibold">Quản lý đơn hàng</h3>
    <!-- Thanh tiêu đề + Bộ lọc -->
    <div class="bg-white shadow sm:rounded-lg p-4 space-y-4">
      <!-- Hàng tìm kiếm -->
      <div class="flex items-center gap-2">
        <input
            type="text"
            v-model="searchName"
            placeholder="🔍 Tìm kiếm hóa đơn"
            class="border border-gray-300 rounded px-4 py-2 text-sm w-80 focus:outline-none focus:ring"
        />
      </div>

      <!-- Hàng lọc -->
      <div class="flex flex-wrap items-center gap-4 justify-between">
        <div class="flex-1 flex items-center gap-2">
          <input
              type="date"
              v-model="dateFrom"
              class="border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring w-40"
          />
          <input
              type="date"
              v-model="dateTo"
              class="border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring w-40"
          />

          <!-- Lọc theo khoảng giá -->
          <input
              type="number"
              v-model="minPrice"
              placeholder="Giá tối thiểu"
              class="border border-gray-300 rounded px-3 py-2 text-sm w-28 focus:outline-none focus:ring w-60"
          />
          <input
              type="number"
              v-model="maxPrice"
              placeholder="Giá tối đa"
              class="border border-gray-300 rounded px-3 py-2 text-sm w-28 focus:outline-none focus:ring w-60"
          />

          <!-- Lọc theo loại hóa đơn -->
          <div class="flex items-center space-x-2">
            <label class="text-sm font-medium">Loại:</label>
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

        <div class="flex items-center gap-2">
          <button class="bg-orange-500 text-white px-4 py-2 rounded text-sm hover:bg-orange-600 flex items-center">
            📷 Quét mã
          </button>
          <button class="bg-green-500 text-white px-4 py-2 rounded text-sm hover:bg-green-600 flex items-center">
            ➕ Tạo hóa đơn
          </button>
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
        <button v-for="tab in tabs" :key="tab.value" @click="selectedTab = tab.value"
                :class="['px-4 py-2', selectedTab === tab.value ? 'border-b-2 border-blue-500 text-blue-500' : 'text-gray-500']">
          {{ tab.label }}
        </button>
      </div>

      <table class="w-full text-sm text-left text-gray-500">
        <thead class="text-xs text-gray-700 uppercase bg-gray-50">
        <tr>
          <th class="px-4 py-3">#</th>
          <th class="px-6 py-3">Mã HĐ</th>
          <th class="px-6 py-3">Tổng SP</th>
          <th class="px-6 py-3">Tổng tiền</th>
          <th class="px-6 py-3">Khách hàng</th>
          <th class="px-6 py-3">Ngày tạo</th>
          <th class="px-6 py-3">Loại hóa đơn</th>
          <th class="px-6 py-3">Trạng thái</th>
          <th class="px-6 py-3">Hành động</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(invoice, index) in filteredInvoices" :key="invoice.id" class="bg-white border-b hover:bg-gray-50">
          <td class="px-4 py-3">{{ index + 1 }}</td>
          <td class="px-6 py-4 font-medium text-gray-900">{{ invoice.maHoaDon }}</td>
          <td class="px-6 py-4">{{ getTotalProducts(invoice.hoaDonChiTiets) }}</td>
          <td class="px-6 py-4">{{ formatCurrency(invoice.tongTien) }}</td>
          <td class="px-6 py-4">{{ invoice.tenNguoiNhan }}</td>
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
    </div>
  </div>
</template>

<script>
import HoaDonService from "@/service/hoaDonService";

export default {
  data() {
    return {
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
      ]
    };
  },
  computed: {
    filteredInvoices() {
      let result = this.invoices;
      if (this.selectedTab !== 'all') {
        result = result.filter(inv => inv.trangThai.toLowerCase().includes(this.selectedTab));
      }
      if (this.searchName) {
        result = result.filter(inv => inv.tenNguoiNhan.toLowerCase().includes(this.searchName.toLowerCase()));
      }
      return result;
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
    getListHoaDon() {
      HoaDonService.getListHoaDon().then((response) => {
        this.invoices = response.data;
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
  },
  created() {
    this.getListHoaDon();
  }
};
</script>
