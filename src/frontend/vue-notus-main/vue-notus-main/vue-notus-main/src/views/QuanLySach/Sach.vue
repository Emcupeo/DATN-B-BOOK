<template>
  <div
      class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded"
      :class="[color === 'light' ? 'bg-white' : 'bg-emerald-900 text-white']"
  >
    <!-- Thanh tiêu đề và công cụ -->
    <div
        class="rounded-t mb-0 px-4 py-3 border-0 flex flex-wrap items-center justify-between"
    >
      <div class="flex-1">
        <h3
            class="font-semibold text-lg"
            :class="[color === 'light' ? 'text-blueGray-700' : 'text-white']"
        >
          Danh sách sản phẩm (Sách)
        </h3>
      </div>
      <div class="flex items-center space-x-2">
        <div class="relative">
          <input
              type="text"
              v-model="searchQuery"
              @input="handleSearch"
              placeholder="Tìm kiếm theo mã, tên sách..."
              class="border border-gray-300 rounded px-3 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-400 w-64"
          />
          <span class="absolute right-3 top-2.5 text-gray-400">
            <i class="fas fa-search"></i>
          </span>
        </div>
        <button
            class="bg-green-500 text-white px-3 py-2 rounded hover:bg-green-600 transition"
            @click="addProduct"
        >
          Thêm sản phẩm
        </button>
      </div>
    </div>

    <!-- Nội dung bảng -->
    <div class="block w-full overflow-x-auto">
      <table class="items-center w-full bg-transparent border-collapse">
        <thead>
        <tr>
          <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="color === 'light' ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100' : 'bg-emerald-800 text-emerald-300 border-emerald-700'"
              @click="sortBy('maSanPham')"
          >
            Mã sản phẩm
            <span>
              <i v-if="sortKey === 'maSanPham' && sortOrder === 1" class="fas fa-sort-up ml-1"></i>
              <i v-else-if="sortKey === 'maSanPham' && sortOrder === -1" class="fas fa-sort-down ml-1"></i>
              <i v-else class="fas fa-sort ml-1"></i>
            </span>
          </th>
          <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="color === 'light' ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100' : 'bg-emerald-800 text-emerald-300 border-emerald-700'"
              @click="sortBy('tenSanPham')"
          >
            Tên sách
            <span>
              <i v-if="sortKey === 'tenSanPham' && sortOrder === 1" class="fas fa-sort-up ml-1"></i>
              <i v-else-if="sortKey === 'tenSanPham' && sortOrder === -1" class="fas fa-sort-down ml-1"></i>
              <i v-else class="fas fa-sort ml-1"></i>
            </span>
          </th>
          <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="color === 'light' ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100' : 'bg-emerald-800 text-emerald-300 border-emerald-700'"
              @click="sortBy('moTa')"
          >
            Mô tả
            <span>
              <i v-if="sortKey === 'moTa' && sortOrder === 1" class="fas fa-sort-up ml-1"></i>
              <i v-else-if="sortKey === 'moTa' && sortOrder === -1" class="fas fa-sort-down ml-1"></i>
              <i v-else class="fas fa-sort ml-1"></i>
            </span>
          </th>
          <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="color === 'light' ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100' : 'bg-emerald-800 text-emerald-300 border-emerald-700'"
              @click="sortBy('tongSoLuongTon')"
          >
            Số lượng tồn
            <span>
              <i v-if="sortKey === 'tongSoLuongTon' && sortOrder === 1" class="fas fa-sort-up ml-1"></i>
              <i v-else-if="sortKey === 'tongSoLuongTon' && sortOrder === -1" class="fas fa-sort-down ml-1"></i>
              <i v-else class="fas fa-sort ml-1"></i>
            </span>
          </th>
          <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap"
              :class="color === 'light' ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100' : 'bg-emerald-800 text-emerald-300 border-emerald-700'"
          >
            Hành động
          </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="sanPham in paginatedSanPham" v-bind:key="sanPham.id">
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
            <span class="font-semibold">{{ sanPham.maSanPham }}</span>
          </td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
            <span class="font-semibold">{{ sanPham.tenSanPham }}</span>
          </td>
          <td class="border-t-0 px-6 py-4 text-xs">
            {{ sanPham.moTa }}
          </td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
            {{ sanPham.tongSoLuongTon || 0 }}
          </td>
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
            <button
                class="bg-indigo-500 text-white px-3 py-1 rounded hover:bg-indigo-600 transition mr-2"
                @click="viewDetails(sanPham.id)"
            >
              Chi tiết
            </button>
            <button
                class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition"
                @click="deleteSanPham(sanPham.id)"
            >
              Xoá
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Phân trang -->
    <div class="mt-4 px-4 py-2 flex justify-center items-center">
      <button
          class="bg-gray-300 text-gray-700 px-3 py-1 rounded-l hover:bg-gray-400 transition"
          @click="prevPage"
          :disabled="currentPage === 1"
      >
        Prev
      </button>
      <span class="mx-2 text-sm">
        Trang {{ currentPage }} / {{ totalPages }}
      </span>
      <button
          class="bg-gray-300 text-gray-700 px-3 py-1 rounded-r hover:bg-gray-400 transition"
          @click="nextPage"
          :disabled="currentPage === totalPages"
      >
        Next
      </button>
    </div>
  </div>
</template>

<script>
import SanPhamService from '@/service/SanPhamService';

export default {
  name: "Sach",
  props: {
    color: {
      default: "light",
      validator(value) {
        return ["light", "dark"].includes(value);
      },
    },
  },
  data() {
    return {
      listSanPham: [],
      searchQuery: "",
      currentPage: 1,
      itemsPerPage: 10,
      sortKey: "",
      sortOrder: 1,
      searchTimeout: null,
    };
  },
  computed: {
    filteredSanPham() {
      if (!this.searchQuery) return this.listSanPham;

      const searchLower = this.searchQuery.toLowerCase().trim();
      return this.listSanPham.filter(item => {
        return (
            item.maSanPham.toLowerCase().includes(searchLower) ||
            item.tenSanPham.toLowerCase().includes(searchLower) ||
            item.moTa.toLowerCase().includes(searchLower)
        );
      });
    },
    sortedAndFilteredSanPham() {
      let result = [...this.filteredSanPham];

      if (this.sortKey) {
        result.sort((a, b) => {
          if (this.sortKey === 'tongSoLuongTon') {
            // Sắp xếp số lượng tồn (number)
            return (a.tongSoLuongTon - b.tongSoLuongTon) * this.sortOrder;
          }
          // Sắp xếp các trường chuỗi
          if (a[this.sortKey] < b[this.sortKey]) return -1 * this.sortOrder;
          if (a[this.sortKey] > b[this.sortKey]) return 1 * this.sortOrder;
          return 0;
        });
      }

      return result;
    },
    paginatedSanPham() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.sortedAndFilteredSanPham.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.sortedAndFilteredSanPham.length / this.itemsPerPage);
    }
  },
  methods: {
    async loadSanPhams() {
      try {
        this.listSanPham = await SanPhamService.getAll();
        console.log('Dữ liệu từ API:', this.listSanPham); // Debug dữ liệu
      } catch (error) {
        console.error('Error loading san pham:', error);
        alert('Có lỗi xảy ra khi tải danh sách sản phẩm');
      }
    },

    handleSearch() {
      if (this.searchTimeout) {
        clearTimeout(this.searchTimeout);
      }

      this.searchTimeout = setTimeout(async () => {
        if (this.searchQuery.trim()) {
          try {
            this.listSanPham = await SanPhamService.search(this.searchQuery.trim());
            console.log('Dữ liệu tìm kiếm:', this.listSanPham); // Debug dữ liệu tìm kiếm
          } catch (error) {
            console.error('Error searching san pham:', error);
            alert('Có lỗi xảy ra khi tìm kiếm sản phẩm');
          }
        } else {
          await this.loadSanPhams();
        }
        this.currentPage = 1;
      }, 300);
    },

    addProduct() {
      this.$router.push('/admin/sach/add');
    },

    async deleteSanPham(id) {
      if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
        try {
          await SanPhamService.delete(id);
          await this.loadSanPhams();
        } catch (error) {
          console.error('Error deleting san pham:', error);
          alert('Có lỗi xảy ra khi xóa sản phẩm');
        }
      }
    },

    sortBy(key) {
      if (this.sortKey === key) {
        this.sortOrder = -this.sortOrder;
      } else {
        this.sortKey = key;
        this.sortOrder = 1;
      }
      this.currentPage = 1;
    },

    prevPage() {
      if (this.currentPage > 1) this.currentPage--;
    },

    nextPage() {
      if (this.currentPage < this.totalPages) this.currentPage++;
    },

    viewDetails(id) {
      this.$router.push(`/admin/sach/${id}`);
    },
  },
  created() {
    this.loadSanPhams();
  },
};
</script>

<style scoped>
/* CSS tùy chỉnh nếu cần */
</style>