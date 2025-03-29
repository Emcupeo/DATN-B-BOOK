<template>
  <div class="min-h-screen w-full bg-gray-100 font-roboto">
    <div class="bg-white p-4 shadow rounded w-full">
      <!-- Thanh tiêu đề và công cụ -->
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-lg font-bold">Danh sách nhà xuất bản</h2>
        <div class="flex items-center justify-between gap-3">
          <input
              v-model="searchQuery"
              @input="handleSearch"
              type="text"
              placeholder="Tìm kiếm theo mã, tên nhà xuất bản..."
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
            Thêm nhà xuất bản
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
            <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('maNhaXuatBan')">
              Mã nhà xuất bản
              <span v-if="sortKey === 'maNhaXuatBan'" class="ml-1">
                  <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortOrder === 1 ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                  </svg>
                </span>
            </th>
            <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('tenNhaXuatBan')">
              Tên nhà xuất bản
              <span v-if="sortKey === 'tenNhaXuatBan'" class="ml-1">
                  <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortOrder === 1 ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                  </svg>
                </span>
            </th>
            <th scope="col" class="px-6 py-3">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <tr class="bg-white border-b hover:bg-gray-50" v-for="(nhaXuatBan, index) in paginatedNhaXuatBan" :key="nhaXuatBan.id">
            <td class="px-6 py-4">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ nhaXuatBan.maNhaXuatBan }}</td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ nhaXuatBan.tenNhaXuatBan }}</td>
            <td class="px-6 py-4 flex justify-center space-x-3">
              <a href="#" @click="editNhaXuatBan(nhaXuatBan)" class="text-gray-600 hover:text-gray-800">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L6.832 19.82a4.5 4.5 0 01-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 011.13-1.897L16.863 4.487zm0 0L19.5 7.125" />
                </svg>
              </a>
              <a href="#" @click="deleteNhaXuatBan(nhaXuatBan.id)" class="text-red-600 hover:text-red-800">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                </svg>
              </a>
            </td>
          </tr>
          <tr class="bg-white border-b hover:bg-gray-50" v-if="paginatedNhaXuatBan.length == 0">
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

    <!-- Modal thêm/sửa nhà xuất bản -->
    <div v-if="showModal" class="fixed inset-0 bg-opacity-80 flex items-center justify-center backdrop-blur-sm z-50">
      <div class="bg-white p-6 rounded-lg w-1/2 shadow-[0_10px_30px_rgba(0,0,0,0.4)]">
        <h2 class="text-xl font-bold mb-4">{{ isEditing ? 'Sửa nhà xuất bản' : 'Thêm nhà xuất bản' }}</h2>
        <form @submit.prevent="validateAndSave">
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">Tên nhà xuất bản</label>
            <input v-model="formData.tenNhaXuatBan" type="text" class="w-full border rounded px-3 py-2" required>
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
import NhaXuatBanService from '@/service/NhaXuatBanService';
import NhaXuatBan from '@/models/NhaXuatBan';

export default {
  name: "NhaXuatBan",
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
      listNhaXuatBan: [],
      searchQuery: "",
      currentPage: 1,
      itemsPerPage: 10,
      sortKey: "",
      sortOrder: 1,
      showModal: false,
      isEditing: false,
      searchTimeout: null,
      formData: new NhaXuatBan(),
      editingId: null
    };
  },
  computed: {
    filteredNhaXuatBan() {
      if (!this.searchQuery) return this.listNhaXuatBan;

      const searchLower = this.searchQuery.toLowerCase().trim();
      return this.listNhaXuatBan.filter(item => {
        return (
            item.maNhaXuatBan.toLowerCase().includes(searchLower) ||
            item.tenNhaXuatBan.toLowerCase().includes(searchLower)
        );
      });
    },
    sortedAndFilteredNhaXuatBan() {
      let result = [...this.filteredNhaXuatBan];

      if (this.sortKey) {
        result.sort((a, b) => {
          if (a[this.sortKey] < b[this.sortKey]) return -1 * this.sortOrder;
          if (a[this.sortKey] > b[this.sortKey]) return 1 * this.sortOrder;
          return 0;
        });
      }

      return result;
    },
    paginatedNhaXuatBan() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.sortedAndFilteredNhaXuatBan.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.sortedAndFilteredNhaXuatBan.length / this.itemsPerPage);
    }
  },
  methods: {
    async loadNhaXuatBans() {
      try {
        this.listNhaXuatBan = await NhaXuatBanService.getAll();
      } catch (error) {
        console.error('Error loading chat lieu:', error);
        alert('Có lỗi xảy ra khi tải danh sách nhà xuất bản');
      }
    },

    handleSearch() {
      if (this.searchTimeout) {
        clearTimeout(this.searchTimeout);
      }

      this.searchTimeout = setTimeout(async () => {
        if (this.searchQuery.trim()) {
          try {
            this.listNhaXuatBan = await NhaXuatBanService.search(this.searchQuery.trim());
          } catch (error) {
            console.error('Error searching chat lieu:', error);
            alert('Có lỗi xảy ra khi tìm kiếm nhà xuất bản');
          }
        } else {
          await this.loadNhaXuatBans();
        }
        this.currentPage = 1;
      }, 300);
    },

    openAddModal() {
      this.isEditing = false;
      this.formData = new NhaXuatBan();
      this.showModal = true;
    },

    editNhaXuatBan(nhaXuatBan) {
      this.isEditing = true;
      this.editingId = nhaXuatBan.id;
      this.formData = new NhaXuatBan(nhaXuatBan);
      this.showModal = true;
    },

    async saveNhaXuatBan() {
      try {
        if (this.isEditing) {
          await NhaXuatBanService.update(this.editingId, this.formData);
        } else {
          await NhaXuatBanService.create(this.formData);
        }
        this.closeModal();
        await this.loadNhaXuatBans();
      } catch (error) {
        console.error('Error saving chat lieu:', error);
        alert('Có lỗi xảy ra khi lưu nhà xuất bản');
      }
    },

    async deleteNhaXuatBan(id) {
      if (confirm('Bạn có chắc chắn muốn xóa nhà xuất bản này?')) {
        try {
          await NhaXuatBanService.delete(id);
          await this.loadNhaXuatBans();
        } catch (error) {
          console.error('Error deleting chat lieu:', error);
          alert('Có lỗi xảy ra khi xóa nhà xuất bản');
        }
      }
    },

    closeModal() {
      this.showModal = false;
      this.formData = new NhaXuatBan();
      this.editingId = null;
    },

    validateAndSave() {
      this.saveNhaXuatBan();
    },

    addProduct() {
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
    this.loadNhaXuatBans();
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');
.font-roboto {
  font-family: 'Roboto', sans-serif;
}
</style>