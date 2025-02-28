<template>
    <div class="flex space-x-6">
      <!-- Form nhập nhân viên (Bên trái) -->
      <div class="w-1/3 bg-white p-4 shadow rounded">
        <h2 class="text-lg font-bold mb-4">Thêm Nhân Viên</h2>
        <form @submit.prevent="addEmployee">
          <input v-model="newEmployee.id" type="text" placeholder="Mã nhân viên" class="input-style" required />
          <input v-model="newEmployee.name" type="text" placeholder="Họ và tên" class="input-style" required />
          <input v-model="newEmployee.position" type="text" placeholder="Chức vụ" class="input-style" required />
          <input v-model="newEmployee.email" type="email" placeholder="Email" class="input-style" required />
          <button type="submit" class="btn-blue">Thêm Nhân Viên</button>
        </form>
      </div>
  
      <!-- Bảng danh sách nhân viên (Bên phải) -->
      <div class="w-2/3 bg-white p-4 shadow rounded">
        <h2 class="text-lg font-bold mb-4">Danh Sách Nhân Viên</h2>
        <input type="text" v-model="searchQuery" placeholder="Tìm kiếm..." class="input-style" />
  
        <div class="relative overflow-x-auto">
          <table class="w-full text-sm text-left">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3">Mã NV</th>
                <th class="px-6 py-3">Họ và tên</th>
                <th class="px-6 py-3">Chức vụ</th>
                <th class="px-6 py-3">Email</th>
                <th class="px-6 py-3">Hành động</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(employee, index) in filteredEmployees" :key="index" class="hover:bg-gray-50">
                <td class="px-6 py-4">{{ employee.id }}</td>
                <td class="px-6 py-4">{{ employee.name }}</td>
                <td class="px-6 py-4">{{ employee.position }}</td>
                <td class="px-6 py-4">{{ employee.email }}</td>
                <td class="px-6 py-4">
                  <a href="#" @click.prevent="editEmployee(index)" class="text-blue-600 hover:underline">Edit</a>
                  <a href="#" @click.prevent="removeEmployee(index)" class="text-red-600 hover:underline ml-3">Remove</a>
                </td>
              </tr>
              <tr v-if="!employees.length">
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
        newEmployee: { id: "", name: "", position: "", email: "" },
        employees: [
          { id: "NV001", name: "Nguyễn Văn A", position: "Quản lý", email: "a@example.com" },
          { id: "NV002", name: "Trần Thị B", position: "Nhân viên", email: "b@example.com" }
        ]
      };
    },
    computed: {
      filteredEmployees() {
        return this.employees.filter(emp =>
          emp.name.toLowerCase().includes(this.searchQuery.toLowerCase())
        );
      }
    },
    methods: {
      addEmployee() {
        this.employees.push({ ...this.newEmployee });
        this.newEmployee = { id: "", name: "", position: "", email: "" };
      },
      removeEmployee(index) {
        this.employees.splice(index, 1);
      },
      editEmployee(index) {
        this.newEmployee = { ...this.employees[index] };
        this.employees.splice(index, 1);
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
  
  .btn-blue {
    background-color: #007bff;
    color: white;
    padding: 8px 12px;
    border-radius: 5px;
    cursor: pointer;
  }
  
  .btn-blue:hover {
    background-color: #0056b3;
  }
  </style>
  