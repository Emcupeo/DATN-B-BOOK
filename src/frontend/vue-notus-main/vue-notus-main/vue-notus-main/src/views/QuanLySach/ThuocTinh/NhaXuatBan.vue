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
          Danh sách nhà xuất bản
        </h3>
      </div>
      <div class="flex items-center space-x-2">
        <div class="relative">
          <input
              type="text"
              v-model="searchQuery"
              @input="handleSearch"
              placeholder="Tìm kiếm theo mã, tên nhà xuất bản ..."
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
          Thêm nhà xuất bản
        </button>
      </div>
    </div>

    <!-- Nội dung bảng -->
    <div class="block w-full overflow-x-auto">
      <table class="items-center w-full bg-transparent border-collapse">
        <!-- Tiêu đề cột -->
        <thead>
        <tr>
          <!-- Mã nhà xuất bản  -->
          <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="
                color === 'light'
                  ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                  : 'bg-emerald-800 text-emerald-300 border-emerald-700'
              "
              @click="sortBy('maNhaXuatBan')"
          >
            Mã nhà xuất bản
            <span>
                <i
                    v-if="sortKey === 'maNhaXuatBan' && sortOrder === 1"
                    class="fas fa-sort-up ml-1"
                ></i>
                <i
                    v-else-if="sortKey === 'maNhaXuatBan' && sortOrder === -1"
                    class="fas fa-sort-down ml-1"
                ></i>
                <i v-else class="fas fa-sort ml-1"></i>
              </span>
          </th>
          <!-- Tên nhà xuất bản  có sort -->
          <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="
                color === 'light'
                  ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                  : 'bg-emerald-800 text-emerald-300 border-emerald-700'
              "
              @click="sortBy('tenNhaXuatBan')"
          >
            Tên nhà xuất bản
            <span>
                <i
                    v-if="sortKey === 'tenNhaXuatBan' && sortOrder === 1"
                    class="fas fa-sort-up ml-1"
                ></i>
                <i
                    v-else-if="sortKey === 'tenNhaXuatBan' && sortOrder === -1"
                    class="fas fa-sort-down ml-1"
                ></i>
                <i v-else class="fas fa-sort ml-1"></i>
              </span>
          </th>
          <!-- Mô tả -->

          <!-- Hành động -->
          <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap"
              :class="
                color === 'light'
                  ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                  : 'bg-emerald-800 text-emerald-300 border-emerald-700'
              "
          >
            Hành động
          </th>
        </tr>
        </thead>

        <!-- Dữ liệu bảng (tbody) -->
        <tbody>
        <tr v-for="nhaXuatBan in paginatedNhaXuatBan" v-bind:key="nhaXuatBan.id">
          <!-- Mã nhà xuất bản  -->
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
              <span class="font-semibold">
                {{ nhaXuatBan.maNhaXuatBan }}
              </span>
          </td>
          <!-- Tên nhà xuất bản  -->
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
              <span class="font-semibold">
                {{ nhaXuatBan.tenNhaXuatBan }}
              </span>
          </td>
          <!-- Mô tả -->

          <!-- Hành động -->
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
            <button
                class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 transition mr-2"
                @click="editNhaXuatBan(nhaXuatBan)"
            >
              Sửa
            </button>
            <button
                class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition"
                @click="deleteNhaXuatBan(nhaXuatBan.id)"
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

    <!-- Modal thêm/sửa nhà xuất bản  -->
    <div v-if="showModal" class="fixed inset-0 bg-opacity-80 flex items-center justify-center backdrop-blur-sm z-50">
      <div class="bg-white p-6 rounded-lg w-1/2 shadow-[0_10px_30px_rgba(0,0,0,0.4)] ">
        <h2 class="text-xl font-bold mb-4">{{ isEditing ? 'Sửa nhà xuất bản ' : 'Thêm nhà xuất bản ' }}</h2>
        <form @submit.prevent="validateAndSave">
          <!-- Bỏ trường Mã nhà xuất bản  -->
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">Tên nhà xuất bản </label>
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
        console.error('Error loading nha xuat ban:', error);
        alert('Có lỗi xảy ra khi tải danh sách nhà xuất bản ');
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
            console.error('Error searching nha xuat ban:', error);
            alert('Có lỗi xảy ra khi tìm kiếm nhà xuất bản ');
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
          // Khi tạo mới, không cần gửi maNhaXuatBan, backend sẽ tự xử lý
          await NhaXuatBanService.create(this.formData);
        }
        this.closeModal();
        await this.loadNhaXuatBans();
      } catch (error) {
        console.error('Error saving nha xuat ban:', error);
        alert('Có lỗi xảy ra khi lưu nhà xuất bản ');
      }
    },

    async deleteNhaXuatBan(id) {
      if (confirm('Bạn có chắc chắn muốn xóa nhà xuất bản này?')) {
        try {
          await NhaXuatBanService.delete(id);
          await this.loadNhaXuatBans();
        } catch (error) {
          console.error('Error deleting nha xuat ban:', error);
          alert('Có lỗi xảy ra khi xóa nhà xuất bản ');
        }
      }
    },

    closeModal() {
      this.showModal = false;
      this.formData = new NhaXuatBan();
      this.editingId = null;
    },

    async validateAndSave() {
      // Bỏ logic kiểm tra maNhaXuatBan vì backend sẽ tự sinh
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
/* CSS tùy chỉnh nếu cần */
</style>
