<template>
  <div class="relative">
    <div>
      <div class="flex flex-col md:flex-row gap-4">
        <!-- Div chứa form (bên trái) với chiều rộng cố định -->
        <div class="relative md:w-[500px]">
          <div class="w-full md:w-[300px] shadow-lg rounded p-4 bg-white">
            <h2 class="text-xl font-semibold mb-4" v-if="selectedCategory">
              Chỉnh sửa danh mục
            </h2>
            <h2 class="text-xl font-semibold mb-4" v-else>
              Thêm danh mục
            </h2>
            <form @submit.prevent="handleSubmit">
              <div class="mb-4">
                <label class="block text-gray-700 mb-1">Tên danh mục</label>
                <input type="text" v-model="form.name" placeholder="Nhập tên danh mục"
                  class="block w-full p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" required />
              </div>
              <div class="mb-4">
                <label class="block text-gray-700 mb-1">Mô tả</label>
                <textarea v-model="form.description" placeholder="Nhập mô tả danh mục"
                  class="block w-full p-4 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" rows="3" required></textarea>
              </div>
              <div class="flex justify-end space-x-2">
                <button type="button" class="text-white text-xs bg-gradient-to-r from-teal-400 via-teal-500 to-teal-600 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-teal-300 dark:focus:ring-teal-800 shadow-lg shadow-teal-500/50 dark:shadow-lg dark:shadow-teal-800/80 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2"
                  @click="cancel">
                  Hủy
                </button>
                <button type="submit" class="text-white text-xs bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 shadow-lg shadow-blue-500/50 dark:shadow-lg dark:shadow-blue-800/80 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2 ">
                  {{ selectedCategory ? "Cập nhật" : "Thêm mới" }}
                </button>
              </div>
            </form>
          </div>
        </div>


        <!-- Div chứa bảng danh mục (bên phải) với full width -->
        <div class="w-full shadow-lg rounded p-4 bg-white">
          <!-- Thanh tiêu đề và tìm kiếm -->
          <div class="rounded-t mb-0 px-4 py-3 border-0 flex flex-wrap items-center justify-between">
            <div class="flex-1">
              <h3 class="font-semibold text-lg text-blueGray-700">
                Danh mục sản phẩm
              </h3>
            </div>
            <div class="flex items-center">
              <input type="text" v-model="searchQuery" placeholder="Tìm kiếm danh mục..."
                class="block w-full p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" />
            </div>
          </div>

          <!-- Nội dung bảng -->
          <div class="block w-full overflow-x-auto">
            <table class="min-w-full bg-transparent border-collapse">
              <thead>
                <tr>
                  <th
                    class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
                    :class="color === 'light'
                      ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                      : 'bg-emerald-800 text-emerald-300 border-emerald-700'" @click="sortBy('name')">
                    Tên danh mục
                    <span>
                      <i v-if="sortKey === 'name' && sortOrder === 1" class="fas fa-sort-up ml-1"></i>
                      <i v-else-if="sortKey === 'name' && sortOrder === -1" class="fas fa-sort-down ml-1"></i>
                      <i v-else class="fas fa-sort ml-1"></i>
                    </span>
                  </th>
                  <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap"
                    :class="color === 'light'
                      ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                      : 'bg-emerald-800 text-emerald-300 border-emerald-700'">
                    Mô tả
                  </th>
                  <th
                    class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
                    :class="color === 'light'
                      ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                      : 'bg-emerald-800 text-emerald-300 border-emerald-700'" @click="sortBy('productCount')">
                    Số sản phẩm
                    <span>
                      <i v-if="sortKey === 'productCount' && sortOrder === 1" class="fas fa-sort-up ml-1"></i>
                      <i v-else-if="sortKey === 'productCount' && sortOrder === -1" class="fas fa-sort-down ml-1"></i>
                      <i v-else class="fas fa-sort ml-1"></i>
                    </span>
                  </th>
                  <th class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap"
                    :class="color === 'light'
                      ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                      : 'bg-emerald-800 text-emerald-300 border-emerald-700'">
                    Hành động
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(category, index) in paginatedCategories" :key="index">
                  <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
                    <span :class="[color === 'light' ? 'text-blueGray-600' : 'text-white', 'font-semibold']">
                      {{ category.name }}
                    </span>
                  </td>
                  <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
                    {{ category.description }}
                  </td>
                  <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
                    {{ category.productCount }}
                  </td>
                  <td class="border-t-0 px-1 py-4 text-xs whitespace-nowrap">
                    <button class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 transition ml-2 text-white bg-gradient-to-r from-purple-500 via-purple-600 to-purple-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-purple-300 dark:focus:ring-purple-800 shadow-lg shadow-purple-500/50 dark:shadow-lg dark:shadow-purple-800/80 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2"
                      @click="onEditCategory(category)">
                      Sửa
                    </button>
                    <button class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition ml-2 text-white bg-gradient-to-r from-red-400 via-red-500 to-red-600 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-red-300 dark:focus:ring-red-800 shadow-lg shadow-red-500/50 dark:shadow-lg dark:shadow-red-800/80 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2"
                      @click="deleteCategory(category)">
                      Xoá
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Phân trang -->
          <div class="mt-4 px-4 py-2 flex justify-center items-center">
            <button class="bg-gray-300 text-gray-700 px-3 py-1 rounded-l hover:bg-gray-400 transition" @click="prevPage"
              :disabled="currentPage === 1">
              Prev
            </button>
            <span class="mx-2 text-sm">
              Trang {{ currentPage }} / {{ totalPages }}
            </span>
            <button class="bg-gray-300 text-gray-700 px-3 py-1 rounded-r hover:bg-gray-400 transition" @click="nextPage"
              :disabled="currentPage === totalPages">
              Next
            </button>
          </div>
        </div>
      </div>
    </div>

  </div>

</template>

<script>
export default {
  name: "BangDanhMuc",
  props: {
    color: {
      type: String,
      default: "light",
      validator(value) {
        return ["light", "dark"].includes(value);
      },
    },
  },
  data() {
    return {
      searchQuery: "",
      currentPage: 1,
      itemsPerPage: 10,
      sortKey: "",
      sortOrder: 1,
      categories: [
        {
          id: 1,
          name: "Công nghệ",
          description: "Các sản phẩm công nghệ tiên tiến",
          productCount: 20,
        },
        {
          id: 2,
          name: "Lập trình",
          description: "Sách về các ngôn ngữ và framework",
          productCount: 35,
        },
        {
          id: 3,
          name: "Thiết kế",
          description: "Hướng dẫn thiết kế UI/UX và đồ họa",
          productCount: 15,
        },
        {
          id: 4,
          name: "Kinh doanh",
          description: "Các sách về quản trị và marketing",
          productCount: 10,
        },
        {
          id: 4,
          name: "Kinh doanh",
          description: "Các sách về quản trị và marketing",
          productCount: 10,
        },
        {
          id: 4,
          name: "Kinh doanh",
          description: "Các sách về quản trị và marketing",
          productCount: 10,
        },
        {
          id: 4,
          name: "Kinh doanh",
          description: "Các sách về quản trị và marketing",
          productCount: 10,
        },
        {
          id: 4,
          name: "Kinh doanh",
          description: "Các sách về quản trị và marketing",
          productCount: 10,
        },

        {
          id: 4,
          name: "Kinh doanh",
          description: "Các sách về quản trị và marketing",
          productCount: 10,
        },
      ],
      selectedCategory: null,
      form: {
        id: null,
        name: "",
        description: ""
      }
    };
  },
  computed: {
    sortedCategories() {
      let filtered = this.categories.filter((cat) =>
        cat.name.toLowerCase().includes(this.searchQuery.trim().toLowerCase())
      );
      if (this.sortKey) {
        filtered.sort((a, b) => {
          if (a[this.sortKey] < b[this.sortKey]) return -1 * this.sortOrder;
          if (a[this.sortKey] > b[this.sortKey]) return 1 * this.sortOrder;
          return 0;
        });
      }
      return filtered;
    },
    totalPages() {
      return Math.ceil(this.sortedCategories.length / this.itemsPerPage);
    },
    paginatedCategories() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      return this.sortedCategories.slice(start, start + this.itemsPerPage);
    },
  },
  methods: {
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
    deleteCategory(category) {
      alert(`Xoá danh mục: ${category.name}`);
    },
    onEditCategory(category) {
      this.selectedCategory = category;
      this.form = { ...category };
    },
    cancel() {
      this.selectedCategory = null;
      this.form = { id: null, name: "", description: "" };
    },
    handleSubmit() {
      if (this.selectedCategory) {
        const index = this.categories.findIndex((cat) => cat.id === this.form.id);
        if (index !== -1) {
          this.categories.splice(index, 1, { ...this.form });
        }
      } else {
        this.form.id = Date.now();
        this.form.productCount = 0;
        this.categories.push({ ...this.form });
      }
      this.cancel();
    },
  },
};
</script>

<style scoped>
/* CSS tùy chỉnh nếu cần */
</style>
