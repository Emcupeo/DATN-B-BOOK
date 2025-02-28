<template>
    <div class="flex space-x-6">
      <!-- Form nhập khách hàng (Bên trái) -->
      <div class="w-1/3 bg-white p-4 shadow rounded">
        <h2 class="text-lg font-bold mb-4">Thêm Khách Hàng</h2>
        <form @submit.prevent="addCustomer">
          <input v-model="newCustomer.id" type="text" placeholder="Mã khách hàng" class="input-style" required />
          <input v-model="newCustomer.name" type="text" placeholder="Họ và tên" class="input-style" required />
          <input v-model="newCustomer.phone" type="text" placeholder="Số điện thoại" class="input-style" required />
          <input v-model="newCustomer.email" type="email" placeholder="Email" class="input-style" required />
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
                <th class="px-6 py-3">Email</th>
                <th class="px-6 py-3">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(customer, index) in filteredCustomers" :key="index" class="hover:bg-gray-50">
                <td class="px-6 py-4">{{ customer.id }}</td>
                <td class="px-6 py-4">{{ customer.name }}</td>
                <td class="px-6 py-4">{{ customer.phone }}</td>
                <td class="px-6 py-4">{{ customer.email }}</td>
                <td class="px-6 py-4">
                  <a href="#" @click.prevent="editCustomer(index)" class="text-blue-600 hover:underline">Edit</a>
                  <a href="#" @click.prevent="removeCustomer(index)" class="text-red-600 hover:underline ml-3">Remove</a>
                </td>
              </tr>
              <tr v-if="!customers.length">
                <td class="px-6 py-4 text-center" colspan="5">Không có dữ liệu</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        searchQuery: "",
        newCustomer: { id: "", name: "", phone: "", email: "" },
        customers: [
          { id: "KH001", name: "Nguyễn Văn An", phone: "0987654321", email: "an@example.com" },
          { id: "KH002", name: "Trần Thị Bích", phone: "0977123456", email: "bich@example.com" }
        ]
      };
    },
    computed: {
      filteredCustomers() {
        return this.customers.filter(cus =>
          cus.name.toLowerCase().includes(this.searchQuery.toLowerCase())
        );
      }
    },
    methods: {
      addCustomer() {
        this.customers.push({ ...this.newCustomer });
        this.newCustomer = { id: "", name: "", phone: "", email: "" };
      },
      removeCustomer(index) {
        this.customers.splice(index, 1);
      },
      editCustomer(index) {
        this.newCustomer = { ...this.customers[index] };
        this.customers.splice(index, 1);
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
  