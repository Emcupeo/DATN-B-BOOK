<template>
    <div class="p-4 space-y-4">
      <!-- Thanh tiêu đề + Bộ lọc (tất cả ép sang trái) -->
      <div class="bg-white shadow sm:rounded-lg p-4">
        <h2 class="text-xl font-semibold text-gray-800 ml-4">
          Quản lý Hóa đơn
        </h2>
        <!-- Tên (placeholder "Tìm tên"...) -->
        <div class="bg-white sm:rounded-lg p-4 flex flex-wrap items-center gap-2">
            <input
          type="text"
          v-model="searchName"
          placeholder="Tìm tên, mã hoá đơn..."
          class="border border-gray-300 rounded px-3 py-1 text-sm focus:outline-none focus:ring"
        />
        <!-- Lọc khoảng ngày -->
        <input
          type="date"
          v-model="dateFrom"
          class="border border-gray-300 rounded px-3 py-1 text-sm focus:outline-none focus:ring"
        />
        <input
          type="date"
          v-model="dateTo"
          class="border border-gray-300 rounded px-3 py-1 text-sm focus:outline-none focus:ring"
        />
        <!-- Hình thức nhận hàng -->
        <select
          v-model="deliveryMethod"
          class="border border-gray-300 rounded px-3 py-1 text-sm focus:outline-none focus:ring"
        >
          <option value="">Hình thức nhận hàng</option>
          <option value="quầy">Tại quầy</option>
          <option value="ship">Giao hàng</option>
        </select>
        <!-- Hình thức thanh toán -->
        <select
          v-model="paymentMethod"
          class="border border-gray-300 rounded px-3 py-1 text-sm focus:outline-none focus:ring"
        >
          <option value="">Hình thức thanh toán</option>
          <option value="tienmat">Tiền mặt</option>
          <option value="chuyenkhoan">Chuyển khoản</option>
        </select>
        <!-- Đã bỏ nút Tìm kiếm, tất cả ép sang trái -->
      </div>
        </div>
  
      <!-- Bảng Hóa đơn -->
      <div class="relative overflow-x-auto shadow-md sm:rounded-lg bg-white">
        <table class="w-full text-sm text-left text-gray-500">
          <!-- Tiêu đề cột -->
          <thead class="text-xs text-gray-700 uppercase bg-gray-50">
            <tr>
              <th scope="col" class="p-4">
                <div class="flex items-center">
                  <input
                    id="checkbox-all-search"
                    type="checkbox"
                    class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded-sm focus:ring-blue-500 focus:ring-2"
                  />
                  <label for="checkbox-all-search" class="sr-only">checkbox</label>
                </div>
              </th>
              <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('maHD')">
                Mã HĐ
                <i :class="getSortIcon('maHD')" class="ml-1"></i>
              </th>
              <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('khachHang')">
                Khách hàng
                <i :class="getSortIcon('khachHang')" class="ml-1"></i>
              </th>
              <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('ngayTao')">
                Ngày tạo
                <i :class="getSortIcon('ngayTao')" class="ml-1"></i>
              </th>
              <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('tongTien')">
                Tổng tiền
                <i :class="getSortIcon('tongTien')" class="ml-1"></i>
              </th>
              <th scope="col" class="px-6 py-3">
                Trạng thái
              </th>
              <th scope="col" class="px-6 py-3">
                Nhận hàng
              </th>
              <th scope="col" class="px-6 py-3">
                Thanh toán
              </th>
              <th scope="col" class="px-6 py-3">
                Hành động
              </th>
            </tr>
          </thead>
          <!-- Dữ liệu bảng -->
          <tbody>
            <tr
              v-for="(invoice, index) in filteredAndSorted"
              :key="invoice.id"
              class="bg-white border-b hover:bg-gray-50"
            >
              <!-- checkbox -->
              <td class="w-4 p-4">
                <div class="flex items-center">
                  <input
                    :id="'checkbox-table-search-' + index"
                    type="checkbox"
                    class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded-sm focus:ring-blue-500 focus:ring-2"
                  />
                  <label :for="'checkbox-table-search-' + index" class="sr-only">checkbox</label>
                </div>
              </td>
              <!-- Mã hóa đơn -->
              <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
                {{ invoice.maHD }}
              </th>
              <!-- Khách hàng -->
              <td class="px-6 py-4">
                {{ invoice.khachHang }}
              </td>
              <!-- Ngày tạo -->
              <td class="px-6 py-4">
                {{ invoice.ngayTao }}
              </td>
              <!-- Tổng tiền -->
              <td class="px-6 py-4">
                {{ formatCurrency(invoice.tongTien) }}
              </td>
              <!-- Trạng thái -->
              <td class="px-6 py-4">
                <span
                  :class="invoice.trangThai === 'Hoàn thành'
                    ? 'text-green-600'
                    : invoice.trangThai === 'Đang xử lý'
                      ? 'text-yellow-600'
                      : 'text-red-600'"
                >
                  {{ invoice.trangThai }}
                </span>
              </td>
              <!-- Nhận hàng -->
              <td class="px-6 py-4">
                {{ invoice.hinhThucNhanHang }}
              </td>
              <!-- Thanh toán -->
              <td class="px-6 py-4">
                {{ invoice.hinhThucThanhToan }}
              </td>
              <!-- Hành động -->
              <td class="flex items-center px-6 py-4 space-x-3">
                <a
                  href="#"
                  class="font-medium text-blue-600 hover:underline text-xs"
                  @click.prevent="onEdit(invoice)"
                >
                  Sửa
                </a>
                <a
                  href="#"
                  class="font-medium text-red-600 hover:underline text-xs"
                  @click.prevent="onDelete(invoice)"
                >
                  Xoá
                </a>
              </td>
            </tr>
            <!-- Nếu không có hoá đơn -->
            <tr v-if="filteredAndSorted.length === 0">
              <td colspan="9" class="px-6 py-4 text-center text-gray-500">
                Không có hoá đơn
              </td>
            </tr>
          </tbody>
        </table>
      </div>
  
    </div>
  </template>
  
  <script>
  export default {
    name: "QuanLyHoaDon",
    data() {
      return {
        // Các bộ lọc
        searchName: "",
        dateFrom: "",
        dateTo: "",
        deliveryMethod: "",
        paymentMethod: "",
        // Dữ liệu mẫu hoá đơn
        invoices: [
          {
            id: 1,
            maHD: "HD001",
            khachHang: "Nguyễn Văn A",
            ngayTao: "2023-01-01",
            tongTien: 500000,
            trangThai: "Hoàn thành",
            hinhThucNhanHang: "Tại quầy",
            hinhThucThanhToan: "Tiền mặt",
          },
          {
            id: 2,
            maHD: "HD002",
            khachHang: "Trần Thị B",
            ngayTao: "2023-02-10",
            tongTien: 750000,
            trangThai: "Đang xử lý",
            hinhThucNhanHang: "Giao hàng",
            hinhThucThanhToan: "Chuyển khoản",
          },
          {
            id: 3,
            maHD: "HD003",
            khachHang: "Hoàng Văn C",
            ngayTao: "2023-02-15",
            tongTien: 1200000,
            trangThai: "Hủy",
            hinhThucNhanHang: "Tại quầy",
            hinhThucThanhToan: "Tiền mặt",
          },
        ],
        // Biến sort
        sortKey: "",
        sortOrder: 1, // 1: tăng dần, -1: giảm dần
      };
    },
    computed: {
      filteredAndSorted() {
        // 1) Lọc
        let result = this.invoices.filter((inv) => {
          // Tên/mã
          if (
            this.searchName &&
            !(
              inv.maHD.toLowerCase().includes(this.searchName.toLowerCase()) ||
              inv.khachHang.toLowerCase().includes(this.searchName.toLowerCase())
            )
          ) {
            return false;
          }
          // Lọc ngày từ
          if (this.dateFrom && inv.ngayTao < this.dateFrom) {
            return false;
          }
          // Lọc ngày đến
          if (this.dateTo && inv.ngayTao > this.dateTo) {
            return false;
          }
          // Hình thức nhận hàng
          if (this.deliveryMethod && inv.hinhThucNhanHang.toLowerCase() !== this.deliveryMethod) {
            return false;
          }
          // Hình thức thanh toán
          if (this.paymentMethod && inv.hinhThucThanhToan.toLowerCase() !== this.paymentMethod) {
            return false;
          }
          return true;
        });
        // 2) Sort
        if (this.sortKey) {
          result.sort((a, b) => {
            if (a[this.sortKey] < b[this.sortKey]) return -1 * this.sortOrder;
            if (a[this.sortKey] > b[this.sortKey]) return 1 * this.sortOrder;
            return 0;
          });
        }
        return result;
      },
    },
    methods: {
      sortBy(key) {
        if (this.sortKey === key) {
          // Đảo chiều sort
          this.sortOrder = -this.sortOrder;
        } else {
          // Gán sortKey mới, reset sortOrder = 1
          this.sortKey = key;
          this.sortOrder = 1;
        }
      },
      getSortIcon(key) {
        // Trả về icon sort tương ứng
        if (this.sortKey !== key) {
          return "fas fa-sort text-gray-400"; // icon sort mặc định
        } else {
          // Đang sort cột này
          return this.sortOrder === 1 ? "fas fa-sort-up" : "fas fa-sort-down";
        }
      },
      onEdit(invoice) {
        alert("Sửa hoá đơn: " + invoice.maHD);
      },
      onDelete(invoice) {
        alert("Xoá hoá đơn: " + invoice.maHD);
      },
      formatCurrency(value) {
        return new Intl.NumberFormat("vi-VN", {
          style: "currency",
          currency: "VND",
        }).format(value);
      },
    },
  };
  </script>
  
  <style scoped>
  /* Tuỳ chỉnh CSS nếu cần */
  </style>
  