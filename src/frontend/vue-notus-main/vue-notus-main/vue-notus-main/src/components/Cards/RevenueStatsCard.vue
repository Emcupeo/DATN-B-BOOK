<template>
  <div class="relative flex flex-col min-w-0 break-words bg-white rounded-2xl mb-6 xl:mb-0 shadow-xl hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-2 border border-gray-100">
    <div class="flex-auto p-6">
      <div class="flex flex-wrap items-center justify-between">
        <div class="relative w-full pr-4 max-w-full flex-grow flex-1">
          <h5 class="text-gray-500 uppercase font-bold text-sm tracking-wide">
            {{ title }}
          </h5>
          <span class="font-bold text-3xl text-gray-800 mt-2 block">
            {{ formattedValue }}
          </span>
        </div>
        <div class="relative w-auto pl-4 flex-initial">
          <div class="text-white p-4 text-center inline-flex items-center justify-center w-16 h-16 shadow-lg rounded-2xl transform rotate-3 hover:rotate-0 transition-transform duration-300"
               :class="iconBgClass">
            <i :class="iconClass" class="text-xl"></i>
          </div>
        </div>
      </div>
      <div class="mt-6 pt-4 border-t border-gray-100" v-if="description">
        <div class="flex items-center justify-between">
          <span class="text-sm text-gray-600">{{ description }}</span>
          <div class="flex items-center" v-if="showTrend">
            <span class="text-sm font-semibold mr-2" :class="trendColor">
              <i :class="trendIcon" class="mr-1"></i>
              {{ trendValue }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "RevenueStatsCard",
  props: {
    title: {
      type: String,
      required: true
    },
    value: {
      type: [String, Number],
      required: true
    },
    iconClass: {
      type: String,
      required: true
    },
    iconBgClass: {
      type: String,
      default: "bg-blue-500"
    },
    description: {
      type: String,
      default: ""
    },
    showTrend: {
      type: Boolean,
      default: false
    },
    trend: {
      type: String,
      default: "neutral",
      validator: (value) => ['up', 'down', 'neutral'].includes(value)
    },
    trendValue: {
      type: String,
      default: ""
    },
    isCurrency: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    formattedValue() {
      if (this.isCurrency) {
        return this.formatCurrency(this.value);
      }
      return this.value;
    },
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
  },
  methods: {
    formatCurrency(amount) {
      if (!amount) return '0 ₫';
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount);
    }
  }
};
</script>

