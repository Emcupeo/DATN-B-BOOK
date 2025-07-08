<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-300 via-blue-100 to-blue-400 relative overflow-hidden animate-bg-move">
    <!-- Hiệu ứng nền động -->
    <div class="absolute inset-0 z-0">
      <div class="absolute top-0 left-0 w-72 h-72 bg-blue-400 opacity-30 rounded-full blur-3xl animate-float-slow"></div>
      <div class="absolute bottom-0 right-0 w-96 h-96 bg-blue-200 opacity-40 rounded-full blur-2xl animate-float"></div>
      <div class="absolute top-1/2 left-1/2 w-40 h-40 bg-blue-500 opacity-20 rounded-full blur-2xl animate-pulse"></div>
    </div>
    <transition name="fade-slide">
      <div class="bg-white p-10 rounded-2xl shadow-2xl w-full max-w-md z-20 relative animate-fade-in">
        <h2 class="text-3xl font-extrabold text-center mb-2 text-blue-700 tracking-wide drop-shadow animate-pop">Chào mừng bạn trở lại!</h2>
        <p class="text-center text-gray-500 mb-6">Đăng nhập để tiếp tục mua sắm tại <span class="font-bold text-blue-500">BBOOK</span></p>
        <form @submit.prevent="onLogin" autocomplete="off">
          <div class="mb-4 relative">
            <label class="block mb-1 font-medium text-gray-700">Email</label>
            <div class="flex items-center border rounded focus-within:ring-2 focus-within:ring-blue-400 transition-all duration-200">
              <span class="px-3 text-blue-400 animate-bounce-x">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 12H8m8 0a4 4 0 11-8 0 4 4 0 018 0zm0 0v1a4 4 0 01-8 0v-1"></path></svg>
              </span>
              <input v-model="email" type="email" required class="w-full px-3 py-2 outline-none bg-transparent focus:bg-blue-50 transition-all duration-200" placeholder="Nhập email" />
            </div>
          </div>
          <div class="mb-4 relative">
            <label class="block mb-1 font-medium text-gray-700">Mật khẩu</label>
            <div class="flex items-center border rounded focus-within:ring-2 focus-within:ring-blue-400 transition-all duration-200">
              <span class="px-3 text-blue-400 animate-bounce-x-delay">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 11c1.104 0 2-.896 2-2s-.896-2-2-2-2 .896-2 2 .896 2 2 2zm0 0v2m0 4h.01M12 3a9 9 0 110 18 9 9 0 010-18z"></path></svg>
              </span>
              <input :type="showPassword ? 'text' : 'password'" v-model="password" required class="w-full px-3 py-2 outline-none bg-transparent focus:bg-blue-50 transition-all duration-200" placeholder="Nhập mật khẩu" />
              <button type="button" @click="showPassword = !showPassword" class="px-3 focus:outline-none group">
                <svg v-if="showPassword" class="w-5 h-5 text-blue-500 group-hover:scale-125 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0zm-6 0a9 9 0 0118 0 9 9 0 01-18 0z"></path></svg>
                <svg v-else class="w-5 h-5 text-gray-400 group-hover:scale-125 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-5.523 0-10-4.477-10-10 0-1.657.336-3.234.938-4.675M15 12a3 3 0 11-6 0 3 3 0 016 0zm6.364-6.364A9.956 9.956 0 0122 12c0 5.523-4.477 10-10 10a9.956 9.956 0 01-6.364-2.364"></path></svg>
              </button>
            </div>
          </div>
          <transition name="fade">
            <div v-if="errorMessage" class="mb-4 text-red-600 bg-red-50 border border-red-200 rounded px-3 py-2 text-sm flex items-center gap-2 animate-shake">
              <svg class="w-4 h-4 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
              {{ errorMessage }}
            </div>
          </transition>
          <button type="submit" class="w-full bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 text-white py-2 rounded-lg font-bold text-lg shadow hover:scale-105 hover:shadow-xl transition-transform duration-200 active:scale-95 animate-pop">Đăng nhập</button>
        </form>
        <div class="mt-6 text-center text-sm animate-fade-in">
          <span class="text-gray-500">Chưa có tài khoản?</span>
          <router-link to="/register" class="text-blue-600 hover:underline font-semibold transition-colors">Đăng ký ngay</router-link>
        </div>
        <div class="mt-2 text-center">
          <router-link to="/" class="text-xs text-gray-400 hover:text-blue-500 transition-colors">← Về trang chủ</router-link>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  data() {
    return {
      email: '',
      password: '',
      showPassword: false,
      errorMessage: '',
    };
  },
  methods: {
    onLogin() {
      // Demo: kiểm tra email và password đơn giản
      if (!this.email || !this.password) {
        this.errorMessage = 'Vui lòng nhập đầy đủ thông tin!';
        return;
      }
      if (!this.email.match(/^[^@\s]+@[^@\s]+\.[^@\s]+$/)) {
        this.errorMessage = 'Email không hợp lệ!';
        return;
      }
      if (this.password.length < 6) {
        this.errorMessage = 'Mật khẩu phải từ 6 ký tự trở lên!';
        return;
      }
      this.errorMessage = '';
      // Xử lý đăng nhập thực tế ở đây
      alert('Đăng nhập thành công (demo)!');
    },
  },
};
</script>

<style scoped>
@keyframes fade-in {
  from { opacity: 0; transform: translateY(40px); }
  to { opacity: 1; transform: none; }
}
.animate-fade-in {
  animation: fade-in 0.7s cubic-bezier(0.4,0,0.2,1);
}
@keyframes pop {
  0% { transform: scale(0.8); opacity: 0; }
  80% { transform: scale(1.05); opacity: 1; }
  100% { transform: scale(1); }
}
.animate-pop {
  animation: pop 0.5s cubic-bezier(.36,.07,.19,.97) both;
}
@keyframes shake {
  10%, 90% { transform: translateX(-2px); }
  20%, 80% { transform: translateX(4px); }
  30%, 50%, 70% { transform: translateX(-8px); }
  40%, 60% { transform: translateX(8px); }
}
.animate-shake {
  animation: shake 0.4s cubic-bezier(.36,.07,.19,.97) both;
}
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-30px); }
}
.animate-float {
  animation: float 6s ease-in-out infinite;
}
@keyframes float-slow {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(40px) scale(1.1); }
}
.animate-float-slow {
  animation: float-slow 10s ease-in-out infinite;
}
@keyframes bg-move {
  0% { background-position: 0% 50%; }
  100% { background-position: 100% 50%; }
}
.animate-bg-move {
  animation: bg-move 20s linear infinite alternate;
}
@keyframes bounce-x {
  0%, 100% { transform: translateX(0); }
  50% { transform: translateX(8px); }
}
.animate-bounce-x {
  animation: bounce-x 1.2s infinite;
}
@keyframes bounce-x-delay {
  0%, 100% { transform: translateX(0); }
  50% { transform: translateX(-8px); }
}
.animate-bounce-x-delay {
  animation: bounce-x-delay 1.2s infinite;
}
.fade-slide-enter-active, .fade-slide-leave-active {
  transition: all 0.5s cubic-bezier(.36,.07,.19,.97);
}
.fade-slide-enter, .fade-slide-leave-to {
  opacity: 0;
  transform: translateY(40px);
}
</style> 