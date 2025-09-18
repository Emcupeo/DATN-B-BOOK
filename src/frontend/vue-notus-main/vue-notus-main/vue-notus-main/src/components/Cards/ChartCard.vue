<template>
  <div class="relative flex flex-col min-w-0 break-words bg-white w-full mb-6 shadow-xl rounded-2xl border border-gray-100">
    <div class="rounded-t-2xl mb-0 px-6 py-4 border-0 bg-gradient-to-r from-gray-50 to-gray-100">
      <div class="flex flex-wrap items-center">
        <div class="relative w-full px-4 max-w-full flex-grow flex-1">
          <h3 class="font-bold text-lg text-gray-800 flex items-center">
            <i class="fas fa-chart-pie mr-3 text-indigo-600"></i>
            {{ title }}
          </h3>
        </div>
      </div>
    </div>
    <div class="block w-full overflow-x-auto">
      <div class="p-6">
        <canvas :id="chartId" width="400" height="300"></canvas>
      </div>
    </div>
  </div>
</template>

<script>
import Chart from 'chart.js';

export default {
  name: "ChartCard",
  props: {
    title: {
      type: String,
      required: true
    },
    chartId: {
      type: String,
      required: true
    },
    chartData: {
      type: Object,
      required: true
    },
    chartType: {
      type: String,
      default: 'bar',
      validator: (value) => ['bar', 'line', 'doughnut', 'pie'].includes(value)
    }
  },
  data() {
    return {
      chart: null
    };
  },
  mounted() {
    this.createChart();
  },
  watch: {
    chartData: {
      handler() {
        if (this.chart) {
          this.chart.destroy();
          this.createChart();
        }
      },
      deep: true
    }
  },
  beforeUnmount() {
    if (this.chart) {
      this.chart.destroy();
    }
  },
  methods: {
    createChart() {
      const ctx = document.getElementById(this.chartId);
      if (!ctx) return;

      // Tạo dữ liệu đơn giản và an toàn
      const labels = this.chartData.labels || [];
      const datasets = this.chartData.datasets || [];
      
      if (labels.length === 0 || datasets.length === 0) {
        console.warn('Chart data is empty');
        return;
      }

      // Tạo dataset đơn giản
      const simpleDataset = {
        label: datasets[0].label || 'Data',
        data: datasets[0].data || [],
        borderWidth: 2
      };

      // Thêm màu sắc tùy theo loại chart
      if (this.chartType === 'line') {
        simpleDataset.borderColor = '#3B82F6';
        simpleDataset.backgroundColor = 'rgba(59, 130, 246, 0.1)';
        simpleDataset.fill = true;
        simpleDataset.tension = 0.4;
      } else if (this.chartType === 'doughnut' || this.chartType === 'pie') {
        simpleDataset.backgroundColor = [
          '#F59E0B',
          '#3B82F6', 
          '#8B5CF6',
          '#10B981',
          '#EF4444'
        ];
        simpleDataset.borderWidth = 2;
        simpleDataset.borderColor = '#ffffff';
      } else {
        simpleDataset.backgroundColor = '#3B82F6';
        simpleDataset.borderColor = '#3B82F6';
      }

      // Cấu hình options đơn giản
      const options = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom'
          }
        }
      };

      // Thêm scales cho line và bar charts
      if (this.chartType === 'line' || this.chartType === 'bar') {
        options.scales = {
          y: {
            beginAtZero: true
          }
        };
      }

      try {
        this.chart = new Chart(ctx, {
          type: this.chartType,
          data: {
            labels: labels,
            datasets: [simpleDataset]
          },
          options: options
        });
      } catch (error) {
        console.error('Error creating chart:', error);
      }
    }
  }
};
</script>
