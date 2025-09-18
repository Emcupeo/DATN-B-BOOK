<template>
  <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow duration-200">
    <div class="flex items-center justify-between">
      <div>
        <h3 class="text-lg font-semibold text-gray-800">{{ title }}</h3>
        <p class="text-3xl font-bold text-gray-900 mt-2">{{ value }}</p>
        <p class="text-sm text-gray-500 mt-1">{{ subtitle }}</p>
      </div>
      <div class="flex-shrink-0">
        <div class="w-12 h-12 rounded-full flex items-center justify-center" :class="iconBgClass">
          <i :class="iconClass" class="text-white text-xl"></i>
        </div>
      </div>
    </div>
    <div class="mt-4 flex items-center" v-if="showTrend">
      <span class="text-sm font-medium" :class="trendColor">
        <i :class="trendIcon"></i>
        {{ trendText }}
      </span>
      <span class="text-sm text-gray-500 ml-2">{{ trendPeriod }}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "QuickStatsCard",
  props: {
    title: {
      type: String,
      required: true
    },
    value: {
      type: [String, Number],
      required: true
    },
    subtitle: {
      type: String,
      default: ""
    },
    iconClass: {
      type: String,
      required: true
    },
    iconBgClass: {
      type: String,
      default: "bg-blue-500"
    },
    showTrend: {
      type: Boolean,
      default: false
    },
    trend: {
      type: String,
      default: "neutral", // up, down, neutral
      validator: (value) => ['up', 'down', 'neutral'].includes(value)
    },
    trendText: {
      type: String,
      default: ""
    },
    trendPeriod: {
      type: String,
      default: "so với tháng trước"
    }
  },
  computed: {
    trendColor() {
      switch (this.trend) {
        case 'up':
          return 'text-green-600';
        case 'down':
          return 'text-red-600';
        default:
          return 'text-gray-600';
      }
    },
    trendIcon() {
      switch (this.trend) {
        case 'up':
          return 'fas fa-arrow-up';
        case 'down':
          return 'fas fa-arrow-down';
        default:
          return 'fas fa-minus';
      }
    }
  }
};
</script>

