<template>
  <div>
    <a
      class="text-gray-600 block hover:text-gray-800 transition-colors"
      href="#pablo"
      ref="btnDropdownRef"
      v-on:click="toggleDropdown($event)"
    >
      <div class="items-center flex">
        <span
          class="w-10 h-10 text-sm text-white bg-gradient-to-r from-blue-500 to-purple-500 inline-flex items-center justify-center rounded-full mr-3 shadow-sm"
        >
          <span class="font-semibold">{{ userInitial }}</span>
        </span>
        <div class="text-gray-700 text-sm hidden lg:block">
          <div class="font-semibold">{{ userDisplayName }}</div>
          <div class="text-xs text-gray-500">{{ roleDisplayName }}</div>
        </div>
        <i class="fas fa-chevron-down text-xs ml-2 text-gray-400"></i>
      </div>
    </a>
    <div
      ref="popoverDropdownRef"
      class="bg-white text-base z-50 float-right py-2 list-none text-left rounded-lg shadow-xl min-w-56 border border-gray-100 mt-2"
      v-bind:class="{
        hidden: !dropdownPopoverShow,
        block: dropdownPopoverShow,
      }"
    >
      <!-- User Info Header -->
      <div class="px-4 py-3 border-b border-gray-100 bg-gray-50 rounded-t-lg">
        <div class="flex items-center">
          <span class="w-8 h-8 text-xs text-white bg-gradient-to-r from-blue-500 to-purple-500 inline-flex items-center justify-center rounded-full mr-3">
            <span class="font-semibold">{{ userInitial }}</span>
          </span>
          <div>
            <div class="text-sm font-semibold text-gray-800">{{ userDisplayName }}</div>
            <div class="text-xs text-gray-500">{{ roleDisplayName }}</div>
          </div>
        </div>
      </div>
      
      <!-- Menu Items -->
      <div class="py-1">
        <button
          @click="openProfileModal"
          class="flex items-center w-full px-4 py-2 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-600 transition-colors text-left"
        >
          <i class="fas fa-user w-4 mr-3 text-gray-400"></i>
          Thông tin cá nhân
        </button>
        
        <button
          @click="openChangePasswordModal"
          class="flex items-center w-full px-4 py-2 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-600 transition-colors text-left"
        >
          <i class="fas fa-key w-4 mr-3 text-gray-400"></i>
          Đổi mật khẩu
        </button>
        
        <a
          href="javascript:void(0);"
          class="flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-600 transition-colors"
        >
          <i class="fas fa-bell w-4 mr-3 text-gray-400"></i>
          Thông báo
        </a>
      </div>
      
      <div class="border-t border-gray-100"></div>
      
      <!-- Logout -->
      <div class="py-1">
        <button
          @click="handleLogout"
          class="flex items-center w-full px-4 py-2 text-sm text-red-600 hover:bg-red-50 transition-colors text-left"
        >
          <i class="fas fa-sign-out-alt w-4 mr-3"></i>
          Đăng xuất
        </button>
      </div>
    </div>

    <!-- Profile Modal -->
    <ProfileModal 
      :isOpen="showProfileModal" 
      @close="showProfileModal = false" 
    />

    <!-- Change Password Modal -->
    <ChangePasswordModal 
      :isOpen="showChangePasswordModal" 
      @close="showChangePasswordModal = false" 
    />
  </div>
</template>

<script>
import { createPopper } from "@popperjs/core";
import { useAuthStore } from "@/store/auth";
import { useRouter } from "vue-router";
import ProfileModal from "@/components/Modals/ProfileModal.vue";
import ChangePasswordModal from "@/components/Modals/ChangePasswordModal.vue";

import image from "@/assets/img/team-1-800x800.jpg";

export default {
  components: {
    ProfileModal,
    ChangePasswordModal
  },
  setup() {
    const authStore = useAuthStore();
    const router = useRouter();

    const handleLogout = async () => {
      try {
        authStore.logout();
        await router.push('/auth/login');
      } catch (error) {
        console.error('Logout error:', error);
      }
    };

    return {
      authStore,
      handleLogout
    };
  },
  data() {
    return {
      dropdownPopoverShow: false,
      image: image,
      showProfileModal: false,
      showChangePasswordModal: false
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
      return this.authStore.userName || 'User';
    },
    
    userInitial() {
      return (this.authStore.userName || 'U').charAt(0).toUpperCase();
    }
  },
  methods: {
    toggleDropdown: function (event) {
      event.preventDefault();
      if (this.dropdownPopoverShow) {
        this.dropdownPopoverShow = false;
      } else {
        this.dropdownPopoverShow = true;
        createPopper(this.$refs.btnDropdownRef, this.$refs.popoverDropdownRef, {
          placement: "bottom-start",
        });
      }
    },

    openProfileModal() {
      this.showProfileModal = true;
      this.dropdownPopoverShow = false; // Close dropdown
    },

    openChangePasswordModal() {
      this.showChangePasswordModal = true;
      this.dropdownPopoverShow = false; // Close dropdown
    }
  },
};
</script>
