<template>
  <div class="min-h-screen bg-gray-50 flex items-center justify-center px-4">
    <div class="max-w-md mx-auto text-center">
      <!-- 404 Icon -->
      <div class="mb-8">
        <div class="text-6xl font-bold text-gray-300 mb-4">404</div>
        <div class="w-16 h-16 mx-auto bg-blue-100 rounded-full flex items-center justify-center">
          <i class="fas fa-search text-2xl text-blue-500"></i>
        </div>
      </div>

      <!-- Main Content -->
      <div class="bg-white rounded-lg shadow-lg p-8 mb-8">
        <h1 class="text-2xl font-bold text-gray-900 mb-4">Trang không tìm thấy</h1>
        <p class="text-gray-600 mb-6">Xin lỗi, trang bạn đang tìm kiếm không tồn tại.</p>
        
        <!-- Current URL -->
        <div class="bg-gray-50 rounded-lg p-4 mb-6">
          <div class="text-sm text-gray-500 mb-1">URL hiện tại:</div>
          <div class="text-sm font-mono text-gray-800 break-all">{{ currentPath }}</div>
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="space-y-3">
        <button 
          @click="goBack"
          class="w-full inline-flex items-center justify-center px-6 py-3 border border-transparent text-base font-medium rounded-lg text-white bg-blue-600 hover:bg-blue-700 transition-colors duration-200"
        >
          <i class="fas fa-arrow-left mr-2"></i>
          Quay lại
        </button>
        
        <router-link 
          to="/" 
          class="w-full inline-flex items-center justify-center px-6 py-3 border border-gray-300 text-base font-medium rounded-lg text-gray-700 bg-white hover:bg-gray-50 transition-colors duration-200"
        >
          <i class="fas fa-home mr-2"></i>
          Về trang chủ
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

export default {
  name: 'NotFound',
  setup() {
    const route = useRoute()
    const router = useRouter()
    
    const currentPath = computed(() => {
      return route.fullPath
    })
    
    const goBack = () => {
      if (window.history.length > 1) {
        router.go(-1)
      } else {
        router.push('/')
      }
    }
    
    return {
      currentPath,
      goBack
    }
  }
}
</script>

<style scoped>
/* Simple 404 page styles */
</style>

