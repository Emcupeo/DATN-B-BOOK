<template>
  <div class="bg-white rounded-2xl shadow-lg border border-gray-100 overflow-hidden">
    <div class="bg-gradient-to-r from-blue-50 to-indigo-50 px-6 py-4 border-b border-gray-100">
      <h3 class="text-xl font-bold text-gray-900 flex items-center">
        <i class="fas fa-calendar-alt mr-3 text-blue-600"></i>
        Bộ lọc thời gian
      </h3>
    </div>
    <div class="p-6">
      <div class="flex flex-wrap items-center justify-between">
        <div class="flex items-center space-x-4 mb-4 lg:mb-0">
          <p class="text-gray-600">Chọn khoảng thời gian để xem thống kê:</p>
        </div>
        <div class="flex items-center space-x-3">
          <button
            v-for="option in timeOptions"
            :key="option.value"
            @click="selectTimeRange(option.value)"
            class="px-6 py-3 rounded-xl font-semibold text-sm transition-all duration-300 transform hover:-translate-y-0.5 shadow-md hover:shadow-lg"
            :class="selectedTimeRange === option.value 
              ? 'bg-gradient-to-r from-blue-500 to-indigo-500 text-white shadow-lg' 
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200 border border-gray-200'"
          >
            <i :class="option.icon" class="mr-2"></i>
            {{ option.label }}
          </button>
        </div>
      </div>
    </div>
    
    <!-- So sánh thời gian -->
    <div v-if="comparisonData" class="mt-6 pt-6 border-t border-gray-100">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div class="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl p-4">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-semibold text-gray-600">Tháng này</p>
              <p class="text-2xl font-bold text-blue-600">{{ formatCurrency(comparisonData.thangNay) }}</p>
            </div>
            <div class="w-12 h-12 bg-blue-500 rounded-full flex items-center justify-center">
              <i class="fas fa-calendar text-white"></i>
            </div>
          </div>
        </div>
        
        <div class="bg-gradient-to-r from-gray-50 to-gray-100 rounded-xl p-4">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-semibold text-gray-600">Tháng trước</p>
              <p class="text-2xl font-bold text-gray-600">{{ formatCurrency(comparisonData.thangTruoc) }}</p>
            </div>
            <div class="w-12 h-12 bg-gray-500 rounded-full flex items-center justify-center">
              <i class="fas fa-history text-white"></i>
            </div>
          </div>
        </div>
        
        <div class="bg-gradient-to-r from-green-50 to-emerald-50 rounded-xl p-4">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-semibold text-gray-600">Thay đổi</p>
              <p class="text-2xl font-bold" :class="getChangeColor(comparisonData.phanTramThayDoi)">
                {{ formatPercentage(comparisonData.phanTramThayDoi) }}
              </p>
            </div>
            <div class="w-12 h-12 rounded-full flex items-center justify-center" :class="getChangeBgColor(comparisonData.phanTramThayDoi)">
              <i :class="getChangeIcon(comparisonData.phanTramThayDoi)" class="text-white"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "TimeFilterCard",
  props: {
    selectedTimeRange: {
      type: String,
      default: 'ngay'
    },
    comparisonData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      timeOptions: [
        { value: 'ngay', label: 'Theo ngày', icon: 'fas fa-calendar-day' },
        { value: 'tuan', label: 'Theo tuần', icon: 'fas fa-calendar-week' },
        { value: 'thang', label: 'Theo tháng', icon: 'fas fa-calendar-alt' },
        { value: 'nam', label: 'Theo năm', icon: 'fas fa-calendar' }
      ]
    };
  },
  methods: {
    selectTimeRange(range) {
      this.$emit('time-range-changed', range);
    },
    formatCurrency(amount) {
      if (!amount) return '0 ₫';
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount);
    },
    formatPercentage(value) {
      if (!value) return '0%';
      const num = parseFloat(value);
      return (num >= 0 ? '+' : '') + num.toFixed(1) + '%';
    },
    getChangeColor(value) {
      const num = parseFloat(value);
      if (num > 0) return 'text-green-600';
      if (num < 0) return 'text-red-600';
      return 'text-gray-600';
    },
    getChangeBgColor(value) {
      const num = parseFloat(value);
      if (num > 0) return 'bg-green-500';
      if (num < 0) return 'bg-red-500';
      return 'bg-gray-500';
    },
    getChangeIcon(value) {
      const num = parseFloat(value);
      if (num > 0) return 'fas fa-arrow-up';
      if (num < 0) return 'fas fa-arrow-down';
      return 'fas fa-minus';
    }
  }
};
</script>
