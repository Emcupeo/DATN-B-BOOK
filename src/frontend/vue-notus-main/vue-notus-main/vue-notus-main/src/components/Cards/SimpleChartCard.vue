<template>
  <div class="w-full">
    <div v-if="!hasData" class="text-center py-12">
      <div class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
        <i class="fas fa-chart-line text-gray-400 text-2xl"></i>
      </div>
      <p class="text-gray-500 font-medium">Không có dữ liệu để hiển thị</p>
      <p class="text-gray-400 text-sm mt-1">Dữ liệu sẽ được cập nhật khi có hoạt động</p>
    </div>
    <div v-else>
      <!-- Simple bar chart using CSS -->
      <div v-if="chartType === 'bar'" class="space-y-4">
        <div v-for="(item, index) in chartData" :key="index" class="group">
          <div class="flex items-center justify-between mb-2">
            <span class="text-sm font-medium text-gray-700">{{ item.label }}</span>
            <span class="text-sm font-bold text-gray-900">{{ formatValue(item.value) }}</span>
          </div>
          <div class="bg-gray-200 rounded-full h-3 overflow-hidden">
            <div 
              class="bg-gradient-to-r from-blue-500 to-indigo-500 h-3 rounded-full transition-all duration-700 ease-out group-hover:from-blue-600 group-hover:to-indigo-600"
              :style="{ width: getPercentage(item.value) + '%' }"
            ></div>
          </div>
        </div>
      </div>
      
      <!-- Simple line chart using CSS -->
      <div v-else-if="chartType === 'line'" class="space-y-4">
        <div v-for="(item, index) in chartData" :key="index" class="group">
          <div class="flex items-center justify-between mb-2">
            <span class="text-sm font-medium text-gray-700">{{ item.label }}</span>
            <span class="text-sm font-bold text-gray-900">{{ formatValue(item.value) }}</span>
          </div>
          <div class="bg-gray-200 rounded-full h-3 overflow-hidden">
            <div 
              class="bg-gradient-to-r from-blue-500 via-purple-500 to-pink-500 h-3 rounded-full transition-all duration-700 ease-out group-hover:from-blue-600 group-hover:via-purple-600 group-hover:to-pink-600"
              :style="{ width: getPercentage(item.value) + '%' }"
            ></div>
          </div>
        </div>
      </div>
      
      <!-- Simple doughnut chart using CSS circles -->
      <div v-else-if="chartType === 'doughnut'" class="flex flex-wrap justify-center gap-6">
        <div v-for="(item, index) in chartData" :key="index" class="text-center group">
          <div class="relative">
            <div 
              class="w-20 h-20 rounded-full border-4 border-white shadow-lg flex items-center justify-center text-white font-bold text-sm transition-all duration-300 group-hover:scale-110 group-hover:shadow-xl"
              :style="{ backgroundColor: getColor(index) }"
            >
              {{ Math.round(getPercentage(item.value)) }}%
            </div>
            <div class="absolute -top-1 -right-1 w-6 h-6 bg-white rounded-full shadow-md flex items-center justify-center">
              <span class="text-xs font-bold text-gray-800">{{ item.value }}</span>
            </div>
          </div>
          <div class="mt-3">
            <p class="text-sm font-medium text-gray-700">{{ item.label }}</p>
            <p class="text-xs text-gray-500">{{ formatValue(item.value) }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "SimpleChartCard",
  props: {
    title: {
      type: String,
      required: true
    },
    chartData: {
      type: Array,
      default: () => []
    },
    chartType: {
      type: String,
      default: 'bar',
      validator: (value) => ['bar', 'line', 'doughnut', 'pie'].includes(value)
    }
  },
  computed: {
    hasData() {
      return this.chartData && this.chartData.length > 0;
    },
    maxValue() {
      if (!this.hasData) return 1;
      return Math.max(...this.chartData.map(item => item.value || 0));
    }
  },
  methods: {
    getPercentage(value) {
      if (!this.maxValue || this.maxValue === 0) return 0;
      return (value / this.maxValue) * 100;
    },
    formatValue(value) {
      if (typeof value === 'number') {
        if (value >= 1000000) {
          return (value / 1000000).toFixed(1) + 'M';
        } else if (value >= 1000) {
          return (value / 1000).toFixed(1) + 'K';
        }
        return value.toLocaleString();
      }
      return value;
    },
    getColor(index) {
      const colors = [
        '#F59E0B',
        '#3B82F6', 
        '#8B5CF6',
        '#10B981',
        '#EF4444',
        '#F97316',
        '#06B6D4',
        '#84CC16'
      ];
      return colors[index % colors.length];
    }
  }
};
</script>
