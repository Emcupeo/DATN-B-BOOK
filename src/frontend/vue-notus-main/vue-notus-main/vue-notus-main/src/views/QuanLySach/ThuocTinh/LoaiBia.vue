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
          Danh sách loại bìa
        </h3>
      </div>
      <div class="flex items-center space-x-2">
        <div class="relative">
          <input
              type="text"
              v-model="searchQuery"
              @input="handleSearch"
              placeholder="Tìm kiếm theo mã, tên loại bìa ..."
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
          Thêm loại bìa
        </button>
      </div>
    </div>

    <!-- Nội dung bảng -->
    <div class="block w-full overflow-x-auto">
      <table class="items-center w-full bg-transparent border-collapse">
        <!-- Tiêu đề cột -->
        <thead>
        <tr>
          <!-- Mã loại bìa  -->
          <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="
                color === 'light'
                  ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                  : 'bg-emerald-800 text-emerald-300 border-emerald-700'
              "
              @click="sortBy('maLoaiBia')"
          >
            Mã loại bìa
            <span>
                <i
                    v-if="sortKey === 'maLoaiBia' && sortOrder === 1"
                    class="fas fa-sort-up ml-1"
                ></i>
                <i
                    v-else-if="sortKey === 'maLoaiBia' && sortOrder === -1"
                    class="fas fa-sort-down ml-1"
                ></i>
                <i v-else class="fas fa-sort ml-1"></i>
              </span>
          </th>
          <!-- Tên loại bìa  có sort -->
          <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="
                color === 'light'
                  ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                  : 'bg-emerald-800 text-emerald-300 border-emerald-700'
              "
              @click="sortBy('tenLoaiBia')"
          >
            Tên loại bìa
            <span>
                <i
                    v-if="sortKey === 'tenLoaiBia' && sortOrder === 1"
                    class="fas fa-sort-up ml-1"
                ></i>
                <i
                    v-else-if="sortKey === 'tenLoaiBia' && sortOrder === -1"
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
        <tr v-for="loaiBia in paginatedLoaiBia" v-bind:key="loaiBia.id">
          <!-- Mã loại bìa  -->
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
              <span class="font-semibold">
                {{ loaiBia.maLoaiBia }}
              </span>
          </td>
          <!-- Tên loại bìa  -->
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
              <span class="font-semibold">
                {{ loaiBia.tenLoaiBia }}
              </span>
          </td>
          <!-- Mô tả -->

          <!-- Hành động -->
          <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
            <button
                class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 transition mr-2"
                @click="editLoaiBia(loaiBia)"
            >
              Sửa
            </button>
            <button
                class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition"
                @click="deleteLoaiBia(loaiBia.id)"
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

    <!-- Modal thêm/sửa loại bìa  -->
    <div v-if="showModal" class="fixed inset-0 bg-opacity-80 flex items-center justify-center backdrop-blur-sm z-50">
      <div class="bg-white p-6 rounded-lg w-1/2 shadow-[0_10px_30px_rgba(0,0,0,0.4)] ">
        <h2 class="text-xl font-bold mb-4">{{ isEditing ? 'Sửa loại bìa ' : 'Thêm loại bìa ' }}</h2>
        <form @submit.prevent="validateAndSave">
          <!-- Bỏ trường Mã loại bìa  -->
          <div class="mb-4">
            <label class="block text-sm font-medium mb-1">Tên loại bìa </label>
            <input v-model="formData.tenLoaiBia" type="text" class="w-full border rounded px-3 py-2" required>
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
import LoaiBiaService from '@/service/LoaiBiaService';
import LoaiBia from '@/models/LoaiBia';

export default {
  name: "LoaiBia",
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
      listLoaiBia: [],
      searchQuery: "",
      currentPage: 1,
      itemsPerPage: 10,
      sortKey: "",
      sortOrder: 1,
      showModal: false,
      isEditing: false,
      searchTimeout: null,
      formData: new LoaiBia(),
      editingId: null
    };
  },
  computed: {
    filteredLoaiBia() {
      if (!this.searchQuery) return this.listLoaiBia;

      const searchLower = this.searchQuery.toLowerCase().trim();
      return this.listLoaiBia.filter(item => {
        return (
            item.maLoaiBia.toLowerCase().includes(searchLower) ||
            item.tenLoaiBia.toLowerCase().includes(searchLower)
        );
      });
    },
    sortedAndFilteredLoaiBia() {
      let result = [...this.filteredLoaiBia];

      if (this.sortKey) {
        result.sort((a, b) => {
          if (a[this.sortKey] < b[this.sortKey]) return -1 * this.sortOrder;
          if (a[this.sortKey] > b[this.sortKey]) return 1 * this.sortOrder;
          return 0;
        });
      }

      return result;
    },
    paginatedLoaiBia() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.sortedAndFilteredLoaiBia.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.sortedAndFilteredLoaiBia.length / this.itemsPerPage);
    }
  },
  methods: {
    async loadLoaiBias() {
      try {
        this.listLoaiBia = await LoaiBiaService.getAll();
      } catch (error) {
        console.error('Error loading loai bia:', error);
        alert('Có lỗi xảy ra khi tải danh sách loại bìa ');
      }
    },

    handleSearch() {
      if (this.searchTimeout) {
        clearTimeout(this.searchTimeout);
      }

      this.searchTimeout = setTimeout(async () => {
        if (this.searchQuery.trim()) {
          try {
            this.listLoaiBia = await LoaiBiaService.search(this.searchQuery.trim());
          } catch (error) {
            console.error('Error searching loai bia:', error);
            alert('Có lỗi xảy ra khi tìm kiếm loại bìa ');
          }
        } else {
          await this.loadLoaiBias();
        }
        this.currentPage = 1;
      }, 300);
    },

    openAddModal() {
      this.isEditing = false;
      this.formData = new LoaiBia();
      this.showModal = true;
    },

    editLoaiBia(loaiBia) {
      this.isEditing = true;
      this.editingId = loaiBia.id;
      this.formData = new LoaiBia(loaiBia);
      this.showModal = true;
    },

    async saveLoaiBia() {
      try {
        if (this.isEditing) {
          await LoaiBiaService.update(this.editingId, this.formData);
        } else {
          // Khi tạo mới, không cần gửi maLoaiBia, backend sẽ tự xử lý
          await LoaiBiaService.create(this.formData);
        }
        this.closeModal();
        await this.loadLoaiBias();
      } catch (error) {
        console.error('Error saving loai bia:', error);
        alert('Có lỗi xảy ra khi lưu loại bìa ');
      }
    },

    async deleteLoaiBia(id) {
      if (confirm('Bạn có chắc chắn muốn xóa loại bìa  này?')) {
        try {
          await LoaiBiaService.delete(id);
          await this.loadLoaiBias();
        } catch (error) {
          console.error('Error deleting loai bia:', error);
          alert('Có lỗi xảy ra khi xóa loại bìa ');
        }
      }
    },

    closeModal() {
      this.showModal = false;
      this.formData = new LoaiBia();
      this.editingId = null;
    },

    async validateAndSave() {
      // Bỏ logic kiểm tra maLoaiBia vì backend sẽ tự sinh
      this.saveLoaiBia();
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
    this.loadLoaiBias();
  },
};
</script>

<style scoped>
/* CSS tùy chỉnh nếu cần */
</style>
