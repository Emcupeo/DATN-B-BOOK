<template>
  <div class="min-h-screen w-full bg-gray-100 font-roboto">
    <div class="bg-white p-4 shadow rounded w-full overflow-hidden">
      <!-- Thanh tiêu đề và công cụ -->
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-lg font-bold">Danh sách sản phẩm (Sách)</h2>
        <div class="flex items-center justify-between gap-3">
          <input
              v-model="searchQuery"
              @input="handleSearch"
              type="text"
              placeholder="Tìm kiếm theo mã, tên sách..."
              class="block w-[300px] h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500 mb-2"
          />
          <button
              @click="addProduct"
              type="button"
              class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg shadow-blue-500/50 dark:shadow-lg dark:shadow-blue-800/80 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            Thêm sản phẩm
          </button>
        </div>
      </div>

      <!-- Nội dung bảng -->
      <div class="relative overflow-x-auto sm:rounded-lg w-full h-[calc(100vh-120px)]">
        <table class="w-full text-sm text-center text-gray-500">
          <thead class="text-xs text-gray-700 uppercase bg-gray-50">
          <tr>
            <th scope="col" class="px-6 py-3">
              STT
            </th>
            <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('maSanPham')">
              Mã sản phẩm
              <span v-if="sortKey === 'maSanPham'" class="ml-1">
                  <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortOrder === 1 ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                  </svg>
                </span>
            </th>
            <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('tenSanPham')">
              Tên sách
              <span v-if="sortKey === 'tenSanPham'" class="ml-1">
                  <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortOrder === 1 ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                  </svg>
                </span>
            </th>
            <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('moTa')">
              Mô tả
              <span v-if="sortKey === 'moTa'" class="ml-1">
                  <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortOrder === 1 ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                  </svg>
                </span>
            </th>
            <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('tongSoLuongTon')">
              Số lượng tồn
              <span v-if="sortKey === 'tongSoLuongTon'" class="ml-1">
                  <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortOrder === 1 ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                  </svg>
                </span>
            </th>
            <th scope="col" class="px-6 py-3">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <tr class="bg-white border-b hover:bg-gray-50" v-for="(sanPham, index) in paginatedSanPham" :key="sanPham.id">
            <td class="px-6 py-4">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ sanPham.maSanPham }}</td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ sanPham.tenSanPham }}</td>
            <td class="px-6 py-4">{{ sanPham.moTa }}</td>
            <td class="px-6 py-4">{{ sanPham.tongSoLuongTon || 0 }}</td>
            <td class="px-6 py-4 flex justify-center space-x-3">
              <a href="#" @click="viewDetails(sanPham.id)" class="text-gray-600 hover:text-gray-800">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 5c7 0 10 7 10 7s-3 7-10 7-10-7-10-7 3-7 10-7z" />
                  <path stroke-linecap="round" stroke-linejoin="round" d="M12 9c1.657 0 3 1.343 3 3s-1.343 3-3 3-3-1.343-3-3 1.343-3 3-3z" />
                </svg>
              </a>
              <a href="#" @click="deleteSanPham(sanPham.id)" class="text-red-600 hover:text-red-800">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                </svg>
              </a>
            </td>
          </tr>
          <tr class="bg-white border-b hover:bg-gray-50" v-if="paginatedSanPham.length == 0">
            <td class="px-6 py-4" colspan="6" align="center">Không có dữ liệu</td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- Phân trang -->
      <div class="flex justify-center items-center mt-4 space-x-2">
        <button
            @click="prevPage"
            :disabled="currentPage === 1"
            class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg shadow-blue-500/50 dark:shadow-lg dark:shadow-blue-800/80 font-medium rounded-lg text-sm p-2 text-center flex items-center justify-center"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
            <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5" />
          </svg>
        </button>
        <span class="text-xs font-semibold text-gray-700">
          Trang {{ currentPage }} / {{ totalPages }}
        </span>
        <button
            @click="nextPage"
            :disabled="currentPage === totalPages"
            class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg shadow-blue-500/50 dark:shadow-lg dark:shadow-blue-800/80 font-medium rounded-lg text-sm p-2 text-center flex items-center justify-center"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
            <path stroke-linecap="round" stroke-linejoin="round" d="m8.25 4.5 7.5 7.5-7.5 7.5" />
          </svg>
        </button>
      </div>
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
            return (a.tongSoLuongTon - b.tongSoLuongTon) * this.sortOrder;
          }
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
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');
.font-roboto {
  font-family: 'Roboto', sans-serif;
}
</style>