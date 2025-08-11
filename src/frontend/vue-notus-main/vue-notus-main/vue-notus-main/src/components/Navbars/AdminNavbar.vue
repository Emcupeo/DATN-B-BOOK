<template>
  <!-- Thanh điều hướng -->
  <nav
    class="sticky top-0 left-0 w-full z-10 bg-white shadow-md md:flex-row md:flex-nowrap md:justify-start flex items-center p-4 border-b border-gray-200"
  >
    <div
      class="w-full mx-auto items-center flex justify-between md:flex-nowrap flex-wrap md:px-10 px-4"
    >
      <!-- Thương hiệu -->
      <div class="flex items-center">
        <a
          class="text-gray-800 text-lg font-bold hidden lg:inline-block"
          href="javascript:void(0)"
        >
          <i class="fas fa-tachometer-alt text-blue-600 mr-2"></i>
          Bảng điều khiển
        </a>
      </div>
      
      <!-- Thông tin người dùng -->
      <div class="flex items-center space-x-4">
        <!-- Welcome message -->
        <div class="hidden md:block text-gray-600">
          <span class="text-sm">Xin chào, </span>
          <span class="font-semibold text-gray-800">{{ userDisplayName }}</span>
          <span class="text-xs text-gray-500 ml-2">({{ roleDisplayName }})</span>
        </div>
        
        <!-- Người dùng -->
        <ul class="flex-col md:flex-row list-none items-center flex">
          <user-dropdown />
        </ul>
      </div>
    </div>
  </nav>
  <!-- Kết thúc Thanh điều hướng -->
</template>

<script>
import UserDropdown from "@/components/Dropdowns/UserDropdown.vue";
import { useAuthStore } from "@/store/auth";

export default {
  components: {
    UserDropdown,
  },
  setup() {
    const authStore = useAuthStore();
    
    return {
      authStore
    };
  },
  computed: {
    roleDisplayName() {
      const roleMap = {
        'ADMIN': 'Quản trị viên',
        'NHAN_VIEN': 'Nhân viên',
        'KHACH_HANG': 'Khách hàng'
      };
      return roleMap[this.authStore.userRole] || 'Người dùng';
    },
    
    userDisplayName() {
      return this.authStore.userName || 'Admin';
    }
  }
};
</script>
