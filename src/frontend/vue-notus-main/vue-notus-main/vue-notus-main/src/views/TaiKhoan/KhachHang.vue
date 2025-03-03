<template>
    <div class="flex space-x-6">
      <!-- Form nhập khách hàng (Bên trái) -->
      <div class="w-1/3 bg-white p-4 shadow rounded">
        <h2 class="text-lg font-bold mb-4">Thêm Khách Hàng</h2>
        <form @submit.prevent="addKhachHang">
          <input v-model="khachHang.maKhachHang" type="text" placeholder="Mã khách hàng" class="input-style" required disabled/>

          <input v-model="khachHang.hoTen" type="text" placeholder="Họ và tên" class="input-style" required/>
          <input v-model="khachHang.soDienThoai" type="text" placeholder="Số điện thoại" class="input-style" required/>
          <label>
            <input v-model="khachHang.gioiTinh" type="radio" value="Nam" required /> Nam
          </label>
          <label>
            <input v-model="khachHang.gioiTinh" type="radio" value="Nữ" required /> Nữ
          </label> <br>
          <button type="submit" class="btn-green">Thêm Khách Hàng</button>
        </form>
      </div>
  
      <!-- Bảng danh sách khách hàng (Bên phải) -->
      <div class="w-2/3 bg-white p-4 shadow rounded">
        <h2 class="text-lg font-bold mb-4">Danh Sách Khách Hàng</h2>
        <input type="text" v-model="searchQuery" placeholder="Tìm kiếm khách hàng..." class="input-style" />
  
        <div class="relative overflow-x-auto">
          <table class="w-full text-sm text-left">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3">Mã KH</th>
                <th class="px-6 py-3">Họ và tên</th>
                <th class="px-6 py-3">Số điện thoại</th>
                <th class="px-6 py-3">Giới tính</th>
                <th class="px-6 py-3">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="khachHang in listKhachHang" v-bind:key="khachHang.id" class="hover:bg-gray-50">
                <td class="px-6 py-4">{{ khachHang.maKhachHang }}</td>
                <td class="px-6 py-4">{{ khachHang.hoTen }}</td>
                <td class="px-6 py-4">{{ khachHang.soDienThoai }}</td>
                <td class="px-6 py-4">{{ khachHang.gioiTinh ? 'Nam' : 'Nữ' }}</td>
                <td class="px-6 py-4">
                  <a href="#" @click.prevent="editCustomer(index)" class="text-blue-600 hover:underline">Edit</a>
                  <a href="#" @click.prevent="removeCustomer(index)" class="text-red-600 hover:underline ml-3">Remove</a>
                </td>
              </tr>
              <tr v-if="khachHang.length === 0">
                <td class="px-6 py-4 text-center" colspan="5">Không có dữ liệu</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import KhachHangService from "../../service/KhachHangService";
  export default {
    name: "ListKhachHang",
    data() {
      return {
        khachHang:{id:"",hoTen:""},
        listKhachHang: [],
        searchQuery: "",
      }

      //   newCustomer: { id: "", name: "", phone: "", email: "" },
      //   customers: [
      //     { id: "KH001", name: "Nguyễn Văn An", phone: "0987654321", email: "an@example.com" },
      //     { id: "KH002", name: "Trần Thị Bích", phone: "0977123456", email: "bich@example.com" }
      //   ]
      // };
    },
    computed: {
      // filteredCustomers() {
      //   return this.customers.filter(cus =>
      //     cus.name.toLowerCase().includes(this.searchQuery.toLowerCase())
      //   );
      // }
    },
    methods: {

      // addCustomer() {
      //   this.customers.push({ ...this.newCustomer });
      //   this.newCustomer = { id: "", name: "", phone: "", email: "" };
      // },
      // removeCustomer(index) {
      //   this.customers.splice(index, 1);
      // },
      // editCustomer(index) {
      //   this.newCustomer = { ...this.customers[index] };
      //   this.customers.splice(index, 1);
      // }
      getListKhachHang(){
        KhachHangService.getListKhachHang().then((response)=>{
          this.listKhachHang = response.data;
        });
      },
    },
    created() {
      this.getListKhachHang();
    },
    async addKhachHang() {
      if (!this.khachHang.hoTen || !this.khachHang.soDienThoai || this.khachHang.gioiTinh === "") {
        alert("Vui lòng điền đầy đủ thông tin khách hàng!");
        return;
      }

      // Kiểm tra số điện thoại chỉ chứa số
      const phoneRegex = /^[0-9]{10,11}$/;
      if (!phoneRegex.test(this.khachHang.soDienThoai)) {
        alert("Số điện thoại không hợp lệ! Vui lòng nhập 10-11 chữ số.");
        return;
      }

      try {
        // Chuyển giới tính thành Boolean
        this.khachHang.gioiTinh = this.khachHang.gioiTinh === "Nam" ? true : false;

        await KhachHangService.createKhachHang(this.khachHang);
        alert("Thêm khách hàng thành công!");

        // Load lại danh sách trước khi reset form
        await this.getListKhachHang();

        // Reset form
        this.khachHang = { hoTen: "", soDienThoai: "", gioiTinh: "" };
      } catch (error) {
        console.error("Lỗi khi thêm khách hàng:", error);
        alert("Thêm khách hàng thất bại!");
      }
    }
  };
  </script>
  
  <style scoped>
  .input-style {
    display: block;
    width: 100%;
    padding: 8px;
    margin-bottom: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
  }
  
  .btn-green {
    background-color: #28a745;
    color: white;
    padding: 8px 12px;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .btn-green:hover {
    background-color: #218838;
  }
  </style>
  