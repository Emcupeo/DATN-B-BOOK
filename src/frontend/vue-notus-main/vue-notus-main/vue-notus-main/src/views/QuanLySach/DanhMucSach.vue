<template>
  <div class="min-h-screen w-full bg-gray-100 font-roboto">
    <div class="bg-white p-4 shadow rounded w-full overflow-hidden">
      <!-- Thanh tiêu đề và công cụ -->
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-lg font-bold">Danh sách danh mục</h2>
        <div class="flex items-center justify-between gap-3">
          <input
              v-model="searchQuery"
              @input="handleSearch"
              type="text"
              placeholder="Tìm kiếm theo mã, tên danh mục..."
              class="block w-[300px] h-[40px] p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500 mb-2"
          />
          <button
              @click="addDanhMuc"
              type="button"
              class="flex items-center gap-2 text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg shadow-blue-500/50 dark:shadow-lg dark:shadow-blue-800/80 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2"
          >
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" />
            </svg>
            Thêm danh mục
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

            <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('tenDanhMuc')">
              Tên danh mục
              <span v-if="sortKey === 'tenDanhMuc'" class="ml-1">
                  <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortOrder === 1 ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                  </svg>
                </span>
            </th>
            <th scope="col" class="px-6 py-3">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <tr class="bg-white border-b hover:bg-gray-50" v-for="(danhMuc, index) in paginatedDanhMuc" :key="danhMuc.id">
            <td class="px-6 py-4">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ danhMuc.tenDanhMuc }}</td>
            <td class="px-6 py-4 flex justify-center space-x-3">
              <a href="#" @click="editDanhMuc(danhMuc)" class="text-gray-600 hover:text-gray-800">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L6.832 19.82a4.5 4.5 0 01-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 011.13-1.897L16.863 4.487zm0 0L19.5 7.125" />
                </svg>
              </a>
              <a href="#" @click="deleteDanhMuc(danhMuc.id)" class="text-red-600 hover:text-red-800">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                </svg>
              </a>
            </td>
          </tr>
          <tr class="bg-white border-b hover:bg-gray-50" v-if="paginatedDanhMuc.length == 0">
            <td class="px-6 py-4" colspan="4" align="center">Không có dữ liệu</td>
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

    <!-- Modal thêm/sửa danh mục -->
    <div v-if="showModal" class="fixed inset-0 bg-opacity-80 flex items-center justify-center backdrop-blur-sm z-50">
      <div class="bg-white p-6 rounded-lg w-1/2 shadow-[0_10px_30px_rgba(0,0,0,0.4)]">
        <h2 class="text-xl font-bold mb-4">{{ isEditing ? 'Sửa danh mục' : 'Thêm danh mục' }}</h2>
        <form @submit.prevent="validateAndSave">
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">Tên danh mục</label>
            <input v-model="formData.tenDanhMuc" type="text" class="w-full border rounded px-3 py-2" required>
          </div>
          <div class="flex justify-end">
            <button type="button" @click="closeModal" class="bg-gray-500 text-white px-4 py-2 rounded mr-2">Hủy</button>
            <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">Lưu</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import DanhMucService from '../../service/DanhMucService';
import DanhMuc from '../../models/DanhMuc';

export default {
  name: "DanhMucSach",
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
      listDanhMuc: [],
      searchQuery: "",
      currentPage: 1,
      itemsPerPage: 10,
      sortKey: "",
      sortOrder: 1,
      showModal: false,
      isEditing: false,
      searchTimeout: null,
      formData: new DanhMuc(),
      editingId: null
    };
  },
  computed: {
    filteredDanhMuc() {
      if (!this.searchQuery) return this.listDanhMuc;

      const searchLower = this.searchQuery.toLowerCase().trim();
      return this.listDanhMuc.filter(item => {
        return (
            item.maDanhMuc.toLowerCase().includes(searchLower) ||
            item.tenDanhMuc.toLowerCase().includes(searchLower)
        );
      });
    },
    sortedAndFilteredDanhMuc() {
      let result = [...this.filteredDanhMuc];

      if (this.sortKey) {
        result.sort((a, b) => {
          if (a[this.sortKey] < b[this.sortKey]) return -1 * this.sortOrder;
          if (a[this.sortKey] > b[this.sortKey]) return 1 * this.sortOrder;
          return 0;
        });
      }

      return result;
    },
    paginatedDanhMuc() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.sortedAndFilteredDanhMuc.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.sortedAndFilteredDanhMuc.length / this.itemsPerPage);
    }
  },
  methods: {
    async loadDanhMucs() {
      try {
        this.listDanhMuc = await DanhMucService.getAll();
      } catch (error) {
        console.error('Error loading danh muc:', error);
        alert('Có lỗi xảy ra khi tải danh sách danh mục');
      }
    },

    handleSearch() {
      if (this.searchTimeout) {
        clearTimeout(this.searchTimeout);
      }

      this.searchTimeout = setTimeout(async () => {
        if (this.searchQuery.trim()) {
          try {
            this.listDanhMuc = await DanhMucService.search(this.searchQuery.trim());
          } catch (error) {
            console.error('Error searching danh muc:', error);
            alert('Có lỗi xảy ra khi tìm kiếm danh mục');
          }
        } else {
          await this.loadDanhMucs();
        }
        this.currentPage = 1;
      }, 300);
    },

    openAddModal() {
      this.isEditing = false;
      this.formData = new DanhMuc();
      this.showModal = true;
    },

    editDanhMuc(danhMuc) {
      this.isEditing = true;
      this.editingId = danhMuc.id;
      this.formData = new DanhMuc(danhMuc);
      this.showModal = true;
    },

    async saveDanhMuc() {
      try {
        if (this.isEditing) {
          await DanhMucService.update(this.editingId, this.formData);
        } else {
          await DanhMucService.create(this.formData);
        }
        this.closeModal();
        await this.loadDanhMucs();
      } catch (error) {
        console.error('Error saving danh muc:', error);
        alert('Có lỗi xảy ra khi lưu danh mục');
      }
    },

    async deleteDanhMuc(id) {
      if (confirm('Bạn có chắc chắn muốn xóa danh mục này?')) {
        try {
          await DanhMucService.delete(id);
          await this.loadDanhMucs();
        } catch (error) {
          console.error('Error deleting danh muc:', error);
          alert('Có lỗi xảy ra khi xóa danh mục');
        }
      }
    },

    closeModal() {
      this.showModal = false;
      this.formData = new DanhMuc();
      this.editingId = null;
    },

    validateAndSave() {
      this.saveDanhMuc();
    },

    addDanhMuc() {
      this.openAddModal();
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
  },
  created() {
    this.loadDanhMucs();
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');
.font-roboto {
  font-family: 'Roboto', sans-serif;
}
</style>