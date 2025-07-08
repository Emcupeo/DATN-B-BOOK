<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-100 to-gray-300">
    <div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-md border-t-4 border-blue-700">
      <h2 class="text-2xl font-bold text-center mb-6 text-blue-700">Đăng nhập quản trị</h2>
      <form @submit.prevent="onLogin">
        <div class="mb-4">
          <label class="block mb-1 font-medium">Tên đăng nhập</label>
          <input v-model="username" type="text" required class="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-700" placeholder="Nhập tên đăng nhập" />
        </div>
        <div class="mb-4">
          <label class="block mb-1 font-medium">Mật khẩu</label>
          <input v-model="password" type="password" required class="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-700" placeholder="Nhập mật khẩu" />
        </div>
        <button type="submit" class="w-full bg-blue-700 text-white py-2 rounded hover:bg-blue-800 font-semibold">Đăng nhập</button>
      </form>
      <div class="mt-4 text-center text-sm">
        Chưa có tài khoản quản trị?
        <router-link to="/admin/register" class="text-blue-700 hover:underline">Đăng ký quản trị</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
    };
  },
  methods: {
    onLogin() {
      // Xử lý đăng nhập admin ở đây
      alert('Đăng nhập quản trị thành công (demo)!');
    },
  },
};
</script> 