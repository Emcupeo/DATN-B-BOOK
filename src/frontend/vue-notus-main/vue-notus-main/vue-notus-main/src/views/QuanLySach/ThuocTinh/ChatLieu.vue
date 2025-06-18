<template>
  <div class="min-h-screen w-full bg-gray-100 font-roboto">
    <div class="bg-white p-4 shadow rounded w-full overflow-hidden">
      <!-- Thanh tiêu đề và công cụ -->
      <div class="flex items-center justify-between mb-4">
        <h2 class="text-lg font-bold">Danh sách chất liệu</h2>
        <div class="flex items-center justify-between gap-3">
          <input
              v-model="searchQuery"
              @input="handleSearch"
              type="text"
              placeholder="Tìm kiếm theo mã, tên chất liệu..."
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
            Thêm chất liệu
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
            <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('maChatLieu')">
              Mã chất liệu
              <span v-if="sortKey === 'maChatLieu'" class="ml-1">
                  <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortOrder === 1 ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                  </svg>
                </span>
            </th>
            <th scope="col" class="px-6 py-3 cursor-pointer" @click="sortBy('tenChatLieu')">
              Tên chất liệu
              <span v-if="sortKey === 'tenChatLieu'" class="ml-1">
                  <svg class="w-3 h-3 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="sortOrder === 1 ? 'M19 9l-7 7-7-7' : 'M5 15l7-7 7 7'"></path>
                  </svg>
                </span>
            </th>
            <th scope="col" class="px-6 py-3">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <tr class="bg-white border-b hover:bg-gray-50" v-for="(chatLieu, index) in paginatedChatLieu" :key="chatLieu.id">
            <td class="px-6 py-4">{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ chatLieu.maChatLieu }}</td>
            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">{{ chatLieu.tenChatLieu }}</td>
            <td class="px-6 py-4 flex justify-center space-x-3">
              <a href="#" @click="editChatLieu(chatLieu)" class="text-gray-600 hover:text-gray-800">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L6.832 19.82a4.5 4.5 0 01-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 011.13-1.897L16.863 4.487zm0 0L19.5 7.125" />
                </svg>
              </a>
              <a href="#" @click="deleteChatLieu(chatLieu.id)" class="text-red-600 hover:text-red-800">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                </svg>
              </a>
            </td>
          </tr>
          <tr class="bg-white border-b hover:bg-gray-50" v-if="paginatedChatLieu.length == 0">
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

    <!-- Modal thêm/sửa chất liệu -->
    <div v-if="showModal" class="fixed inset-0 bg-opacity-80 flex items-center justify-center backdrop-blur-sm z-50">
      <div class="bg-white p-6 rounded-lg w-1/2 shadow-[0_10px_30px_rgba(0,0,0,0.4)]">
        <h2 class="text-xl font-bold mb-4">{{ isEditing ? 'Sửa chất liệu' : 'Thêm chất liệu' }}</h2>
        <form @submit.prevent="validateAndSave">
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">Tên chất liệu</label>
            <input v-model="formData.tenChatLieu" type="text" class="w-full border rounded px-3 py-2" required>
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
import ChatLieuService from '@/service/ChatLieuService';
import ChatLieu from '@/models/ChatLieu';

export default {
  name: "ChatLieu",
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
      listChatLieu: [],
      searchQuery: "",
      currentPage: 1,
      itemsPerPage: 10,
      sortKey: "",
      sortOrder: 1,
      showModal: false,
      isEditing: false,
      searchTimeout: null,
      formData: new ChatLieu(),
      editingId: null
    };
  },
  computed: {
    filteredChatLieu() {
      if (!this.searchQuery) return this.listChatLieu;

      const searchLower = this.searchQuery.toLowerCase().trim();
      return this.listChatLieu.filter(item => {
        return (
            item.maChatLieu.toLowerCase().includes(searchLower) ||
            item.tenChatLieu.toLowerCase().includes(searchLower)
        );
      });
    },
    sortedAndFilteredChatLieu() {
      let result = [...this.filteredChatLieu];

      if (this.sortKey) {
        result.sort((a, b) => {
          if (a[this.sortKey] < b[this.sortKey]) return -1 * this.sortOrder;
          if (a[this.sortKey] > b[this.sortKey]) return 1 * this.sortOrder;
          return 0;
        });
      }

      return result;
    },
    paginatedChatLieu() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.sortedAndFilteredChatLieu.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.sortedAndFilteredChatLieu.length / this.itemsPerPage);
    }
  },
  methods: {
    async loadChatLieus() {
      try {
        this.listChatLieu = await ChatLieuService.getAll();
      } catch (error) {
        console.error('Error loading chat lieu:', error);
        alert('Có lỗi xảy ra khi tải danh sách chất liệu');
      }
    },

    handleSearch() {
      if (this.searchTimeout) {
        clearTimeout(this.searchTimeout);
      }

      this.searchTimeout = setTimeout(async () => {
        if (this.searchQuery.trim()) {
          try {
            this.listChatLieu = await ChatLieuService.search(this.searchQuery.trim());
          } catch (error) {
            console.error('Error searching chat lieu:', error);
            alert('Có lỗi xảy ra khi tìm kiếm chất liệu');
          }
        } else {
          await this.loadChatLieus();
        }
        this.currentPage = 1;
      }, 300);
    },

    openAddModal() {
      this.isEditing = false;
      this.formData = new ChatLieu();
      this.showModal = true;
    },

    editChatLieu(chatLieu) {
      this.isEditing = true;
      this.editingId = chatLieu.id;
      this.formData = new ChatLieu(chatLieu);
      this.showModal = true;
    },

    async saveChatLieu() {
      try {
        if (this.isEditing) {
          await ChatLieuService.update(this.editingId, this.formData);
        } else {
          await ChatLieuService.create(this.formData);
        }
        this.closeModal();
        await this.loadChatLieus();
      } catch (error) {
        console.error('Error saving chat lieu:', error);
        alert('Có lỗi xảy ra khi lưu chất liệu');
      }
    },

    async deleteChatLieu(id) {
      if (confirm('Bạn có chắc chắn muốn xóa chất liệu này?')) {
        try {
          await ChatLieuService.delete(id);
          await this.loadChatLieus();
        } catch (error) {
          console.error('Error deleting chat lieu:', error);
          alert('Có lỗi xảy ra khi xóa chất liệu');
        }
      }
    },

    closeModal() {
      this.showModal = false;
      this.formData = new ChatLieu();
      this.editingId = null;
    },

    validateAndSave() {
      this.saveChatLieu();
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
    this.loadChatLieus();
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');
.font-roboto {
  font-family: 'Roboto', sans-serif;
}
</style>