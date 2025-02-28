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
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Tìm kiếm sản phẩm..."
          class="border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
        />
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
        <!-- Tiêu đề cột -->
        <thead>
          <tr>
            <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap"
              :class="
                color === 'light'
                  ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                  : 'bg-emerald-800 text-emerald-300 border-emerald-700'
              "
            >
              Ảnh bìa
            </th>
            <!-- Tên sách có sort -->
            <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="
                color === 'light'
                  ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                  : 'bg-emerald-800 text-emerald-300 border-emerald-700'
              "
              @click="sortBy('title')"
            >
              Tên sách
              <span>
                <i
                  v-if="sortKey === 'title' && sortOrder === 1"
                  class="fas fa-sort-up ml-1"
                ></i>
                <i
                  v-else-if="sortKey === 'title' && sortOrder === -1"
                  class="fas fa-sort-down ml-1"
                ></i>
                <i v-else class="fas fa-sort ml-1"></i>
              </span>
            </th>
            <!-- Giá có sort -->
            <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="
                color === 'light'
                  ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                  : 'bg-emerald-800 text-emerald-300 border-emerald-700'
              "
              @click="sortBy('price')"
            >
              Giá
              <span>
                <i
                  v-if="sortKey === 'price' && sortOrder === 1"
                  class="fas fa-sort-up ml-1"
                ></i>
                <i
                  v-else-if="sortKey === 'price' && sortOrder === -1"
                  class="fas fa-sort-down ml-1"
                ></i>
                <i v-else class="fas fa-sort ml-1"></i>
              </span>
            </th>
            <!-- Trạng thái có sort -->
            <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="
                color === 'light'
                  ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                  : 'bg-emerald-800 text-emerald-300 border-emerald-700'
              "
              @click="sortBy('status')"
            >
              Trạng thái
              <span>
                <i
                  v-if="sortKey === 'status' && sortOrder === 1"
                  class="fas fa-sort-up ml-1"
                ></i>
                <i
                  v-else-if="sortKey === 'status' && sortOrder === -1"
                  class="fas fa-sort-down ml-1"
                ></i>
                <i v-else class="fas fa-sort ml-1"></i>
              </span>
            </th>
            <!-- Thể loại có sort -->
            <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="
                color === 'light'
                  ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                  : 'bg-emerald-800 text-emerald-300 border-emerald-700'
              "
              @click="sortBy('category')"
            >
              Thể loại
              <span>
                <i
                  v-if="sortKey === 'category' && sortOrder === 1"
                  class="fas fa-sort-up ml-1"
                ></i>
                <i
                  v-else-if="sortKey === 'category' && sortOrder === -1"
                  class="fas fa-sort-down ml-1"
                ></i>
                <i v-else class="fas fa-sort ml-1"></i>
              </span>
            </th>
            <!-- Số lượng có sort -->
            <th
              class="px-6 py-3 text-xs uppercase font-semibold text-left border border-solid whitespace-nowrap cursor-pointer"
              :class="
                color === 'light'
                  ? 'bg-blueGray-50 text-blueGray-500 border-blueGray-100'
                  : 'bg-emerald-800 text-emerald-300 border-emerald-700'
              "
              @click="sortBy('quantity')"
            >
              Số lượng
              <span>
                <i
                  v-if="sortKey === 'quantity' && sortOrder === 1"
                  class="fas fa-sort-up ml-1"
                ></i>
                <i
                  v-else-if="sortKey === 'quantity' && sortOrder === -1"
                  class="fas fa-sort-down ml-1"
                ></i>
                <i v-else class="fas fa-sort ml-1"></i>
              </span>
            </th>
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
          <tr v-for="sanPham in listSanPham" v-bind:key="sanPham.id">
            <!-- Ảnh bìa -->
            <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
              <img class="h-16 w-16 object-cover rounded" alt="Bìa sách" />
            </td>
            <!-- Tên sách -->
            <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
              <span
                :class="[
                  color === 'light' ? 'text-blueGray-600' : 'text-white',
                  'font-semibold',
                ]"
              >
                {{ sanPham.tenSanPham }}
              </span>
            </td>
            <!-- Giá -->
            <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
              <!-- <div v-if="book.oldPrice">
                <span class="line-through text-gray-400 mr-2">
                  {{ formatPrice(book.oldPrice) }}
                </span>
                <span>{{ formatPrice(book.price) }}</span>
              </div>
              <div v-else>
                {{ formatPrice(book.price) }}
              </div> -->
            </td>
            <!-- Trạng thái -->
            <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
              <!-- <span
                :class="
                  book.status === 'Còn hàng'
                    ? 'text-emerald-500'
                    : 'text-red-500'
                "
              >
                {{ book.status }}
              </span> -->
            </td>
            <!-- Thể loại -->
            <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
              <!-- {{ book.category }} -->
            </td>
            <!-- Số lượng -->
            <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
              <!-- {{ book.quantity }} -->
            </td>
            <!-- Hành động -->
            <td class="border-t-0 px-6 py-4 text-xs whitespace-nowrap">
              <!-- <button
                class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 transition ml-2"
                @click="editBook(book)"
              >
                Sửa
              </button> -->
              <!-- <button
                class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition ml-2"
                @click="deleteBook(book)"
              >
                Xoá
              </button> -->
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
import SanPhamService from "../../service/SanPhamService";

export default {
  name: "ListSach",
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
      // searchQuery: "",
      // currentPage: 1,
      // itemsPerPage: 10,
      // sortKey: "", // field đang được sort
      // sortOrder: 1, // 1: tăng dần, -1: giảm dần
    };
  },
  // computed: {
  //   // Lọc theo search rồi sắp xếp
  //   sortedBooks() {
  //     let filtered = this.sanPhams.filter((book) =>
  //       book.title.toLowerCase().includes(this.searchQuery.trim().toLowerCase())
  //     );
  //     if (this.sortKey) {
  //       filtered.sort((a, b) => {
  //         if (a[this.sortKey] < b[this.sortKey]) return -1 * this.sortOrder;
  //         if (a[this.sortKey] > b[this.sortKey]) return 1 * this.sortOrder;
  //         return 0;
  //       });
  //     }
  //     return filtered;
  //   },
  //   totalPages() {
  //     return Math.ceil(this.sortedBooks.length / this.itemsPerPage);
  //   },
  //   paginatedBooks() {
  //     const start = (this.currentPage - 1) * this.itemsPerPage;
  //     return this.sortedBooks.slice(start, start + this.itemsPerPage);
  //   },
  // },
  methods: {
    // formatPrice(value) {
    //   return new Intl.NumberFormat("vi-VN").format(value) + "đ";
    // },
    // editBook() {
    //   // Chuyển hướng sang trang chỉnh sửa
    //   this.$router.push("/admin/sach/edit");
    // },

    // deleteBook(book) {
    //   alert(`Xoá: ${book.title}`);
    // },
    // addProduct() {
    //   // Chuyển hướng sang trang thêm sản phẩm
    //   this.$router.push("/admin/sach/add");
    // },
    // prevPage() {
    //   if (this.currentPage > 1) this.currentPage--;
    // },
    // nextPage() {
    //   if (this.currentPage < this.totalPages) this.currentPage++;
    // },
    // sortBy(key) {
    //   if (this.sortKey === key) {
    //     this.sortOrder = -this.sortOrder;
    //   } else {
    //     this.sortKey = key;
    //     this.sortOrder = 1;
    //   }
    //   this.currentPage = 1;
    // },
    getListSanPham() {
      SanPhamService.getListSanPham().then((response) => {
        this.listSanPham = response.data;
      });
    },
  },
  created() {
    this.getListSanPham();
  },
};
</script>

<style scoped>
/* CSS tùy chỉnh nếu cần */
</style>
