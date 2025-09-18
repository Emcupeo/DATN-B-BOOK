<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <div class="bg-white shadow-sm border-b border-gray-200">
      <div class="px-6 py-4">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-2xl font-semibold text-gray-900">Dashboard B-BOOK</h1>
            <p class="text-gray-600 text-sm">Hệ thống quản lý sách</p>
          </div>
          <div class="flex items-center space-x-4">
            <div class="text-right text-sm text-gray-500">
              <div>{{ currentTime }}</div>
              <div>{{ currentDate }}</div>
            </div>
            <button 
              @click="refreshData"
              :disabled="loading"
              class="bg-gray-800 hover:bg-gray-900 disabled:bg-gray-400 text-white px-4 py-2 rounded-lg flex items-center space-x-2 transition-colors"
            >
              <i :class="loading ? 'fas fa-spinner fa-spin' : 'fas fa-sync-alt'"></i>
              <span>{{ loading ? 'Đang tải...' : 'Làm mới' }}</span>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="p-6">
      <!-- Loading State -->
      <div v-if="loading" class="flex items-center justify-center py-20">
        <div class="text-center">
          <i class="fas fa-spinner fa-spin text-3xl text-gray-600 mb-4"></i>
          <p class="text-gray-600">Đang tải dữ liệu...</p>
        </div>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="flex items-center justify-center py-20">
        <div class="text-center">
          <i class="fas fa-exclamation-triangle text-3xl text-red-500 mb-4"></i>
          <p class="text-red-600 mb-4">{{ error }}</p>
          <button @click="refreshData" class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg">
            Thử lại
          </button>
        </div>
      </div>

      <!-- Dashboard Content -->
      <div v-else class="space-y-6">
        <!-- Hero Section - Tổng doanh thu -->
        <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-8">
          <div class="flex items-center justify-between">
            <div>
              <h2 class="text-3xl font-semibold text-gray-900 mb-2">Tổng Doanh Thu</h2>
              <p class="text-gray-600 mb-6">Tổng kết doanh thu toàn hệ thống</p>
              <div class="text-4xl font-bold text-gray-900 mb-2">
                {{ formatCurrency(stats.tongQuan?.tongDoanhThu || 0) }}
              </div>
              <div class="flex items-center text-gray-600">
                <i class="fas fa-arrow-up mr-2"></i>
                <span>Tăng trưởng tích cực</span>
              </div>
            </div>
            <div class="w-16 h-16 bg-gray-100 rounded-lg flex items-center justify-center">
              <i class="fas fa-chart-line text-2xl text-gray-600"></i>
            </div>
          </div>
        </div>

        <!-- Quick Stats Section -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div class="flex items-center">
              <div class="w-12 h-12 bg-gray-100 rounded-lg flex items-center justify-center mr-4">
                <i class="fas fa-calendar-day text-gray-600"></i>
              </div>
              <div>
                <p class="text-sm text-gray-600">Hôm nay</p>
                <p class="text-2xl font-semibold text-gray-900">{{ formatCurrency(stats.tongQuan?.doanhThuHomNay || 0) }}</p>
              </div>
            </div>
          </div>

          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div class="flex items-center">
              <div class="w-12 h-12 bg-gray-100 rounded-lg flex items-center justify-center mr-4">
                <i class="fas fa-calendar-alt text-gray-600"></i>
              </div>
              <div>
                <p class="text-sm text-gray-600">Tháng này</p>
                <p class="text-2xl font-semibold text-gray-900">{{ formatCurrency(getThangNayDoanhThu()) }}</p>
              </div>
            </div>
          </div>

          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div class="flex items-center">
              <div class="w-12 h-12 bg-gray-100 rounded-lg flex items-center justify-center mr-4">
                <i class="fas fa-calendar text-gray-600"></i>
              </div>
              <div>
                <p class="text-sm text-gray-600">Năm nay</p>
                <p class="text-2xl font-semibold text-gray-900">{{ formatCurrency(getNamNayDoanhThu()) }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm text-gray-600">Sản Phẩm Chính</p>
                <p class="text-3xl font-semibold text-gray-900 mt-1">{{ stats.tongQuan?.tongSanPhamChinh || 0 }}</p>
                <p class="text-sm text-gray-500 mt-1">Sản phẩm gốc</p>
              </div>
              <div class="w-12 h-12 bg-gray-100 rounded-lg flex items-center justify-center">
                <i class="fas fa-book text-gray-600"></i>
              </div>
            </div>
          </div>

          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm text-gray-600">Chi Tiết Sản Phẩm</p>
                <p class="text-3xl font-semibold text-gray-900 mt-1">{{ stats.tongQuan?.tongChiTietSanPham || 0 }}</p>
                <p class="text-sm text-gray-500 mt-1">Phiên bản sản phẩm</p>
              </div>
              <div class="w-12 h-12 bg-gray-100 rounded-lg flex items-center justify-center">
                <i class="fas fa-list text-gray-600"></i>
              </div>
            </div>
          </div>

          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm text-gray-600">Bộ Sách</p>
                <p class="text-3xl font-semibold text-gray-900 mt-1">{{ stats.tongQuan?.tongBoSach || 0 }}</p>
                <p class="text-sm text-gray-500 mt-1">Bộ sách combo</p>
              </div>
              <div class="w-12 h-12 bg-gray-100 rounded-lg flex items-center justify-center">
                <i class="fas fa-layer-group text-gray-600"></i>
              </div>
            </div>
          </div>
        </div>

        <!-- Order Status Cards -->
        <div class="grid grid-cols-1 md:grid-cols-4 gap-6">
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm text-gray-600">Chờ Xác Nhận</p>
                <p class="text-3xl font-semibold text-gray-900 mt-1">{{ stats.donHangTrangThai?.choXacNhan || 0 }}</p>
                <p class="text-sm text-gray-500 mt-1">Đơn hàng chờ xử lý</p>
              </div>
              <div class="w-12 h-12 bg-gray-100 rounded-lg flex items-center justify-center">
                <i class="fas fa-clock text-gray-600"></i>
              </div>
            </div>
          </div>

          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm text-gray-600">Chờ Giao</p>
                <p class="text-3xl font-semibold text-gray-900 mt-1">{{ stats.donHangTrangThai?.dangGiao || 0 }}</p>
                <p class="text-sm text-gray-500 mt-1">Đơn hàng đang giao</p>
              </div>
              <div class="w-12 h-12 bg-gray-100 rounded-lg flex items-center justify-center">
                <i class="fas fa-truck text-gray-600"></i>
              </div>
            </div>
          </div>

          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm text-gray-600">Đã Giao</p>
                <p class="text-3xl font-semibold text-gray-900 mt-1">{{ stats.donHangTrangThai?.daGiao || 0 }}</p>
                <p class="text-sm text-gray-500 mt-1">Đơn hàng hoàn thành</p>
              </div>
              <div class="w-12 h-12 bg-gray-100 rounded-lg flex items-center justify-center">
                <i class="fas fa-check-circle text-gray-600"></i>
              </div>
            </div>
          </div>

          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm text-gray-600">Đã Hủy</p>
                <p class="text-3xl font-semibold text-gray-900 mt-1">{{ stats.donHangTrangThai?.daHuy || 0 }}</p>
                <p class="text-sm text-gray-500 mt-1">Đơn hàng đã hủy</p>
              </div>
              <div class="w-12 h-12 bg-gray-100 rounded-lg flex items-center justify-center">
                <i class="fas fa-times-circle text-gray-600"></i>
              </div>
            </div>
          </div>
        </div>

        <!-- Time Period Chart -->
        <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <div class="flex items-center justify-between mb-6">
            <div>
              <h3 class="text-lg font-semibold text-gray-900">Doanh Thu Theo Thời Gian</h3>
              <p class="text-sm text-gray-500 mt-1">{{ getChartDescription() }}</p>
            </div>
            <div class="flex space-x-2">
              <button 
                v-for="period in timePeriods" 
                :key="period.key"
                @click="selectedPeriod = period.key; updateChart()"
                :class="[
                  'px-4 py-2 rounded-lg text-sm font-medium transition-colors',
                  selectedPeriod === period.key 
                    ? 'bg-gray-800 text-white' 
                    : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                ]"
              >
                {{ period.label }}
              </button>
            </div>
          </div>
          
          <!-- Chart Container -->
          <div class="h-80 bg-gray-50 rounded-lg p-6">
            <div v-if="chartLoading" class="flex items-center justify-center h-full">
              <div class="text-center">
                <i class="fas fa-spinner fa-spin text-3xl text-gray-600 mb-4"></i>
                <p class="text-gray-600">Đang tải dữ liệu...</p>
              </div>
            </div>
            <div v-else-if="chartData.length === 0" class="flex items-center justify-center h-full">
              <div class="text-center">
                <i class="fas fa-chart-bar text-3xl text-gray-400 mb-4"></i>
                <p class="text-gray-500">Chưa có dữ liệu doanh thu</p>
              </div>
            </div>
            <div v-else>
              <!-- Y-axis labels -->
              <div class="flex h-64 mb-4">
                <div class="w-12 flex flex-col justify-between text-xs text-gray-500 pr-2">
                  <div v-for="(label, index) in yAxisLabels" :key="index" class="text-right">
                    {{ label }}
                  </div>
                </div>
                
                <!-- Chart area -->
                <div class="flex-1 relative">
                  <!-- Grid lines -->
                  <div class="absolute inset-0 flex flex-col justify-between">
                    <div v-for="i in 5" :key="i" class="border-t border-gray-200"></div>
                  </div>
                  
                  <!-- Chart Bars -->
                  <div class="flex items-end justify-between h-full space-x-1 relative z-10">
                    <div 
                      v-for="(value, index) in chartData" 
                      :key="index"
                      class="flex-1 bg-gray-600 rounded-t-sm flex items-end justify-center text-white text-xs font-medium hover:bg-gray-700 transition-colors cursor-pointer group relative"
                      :style="{ height: `${Math.max((value / maxValue) * 100, value > 0 ? 8 : 2)}%` }"
                      :title="`${chartLabels[index]}: ${formatCurrency(value)}`"
                    >
                      <!-- Value label on hover -->
                      <div class="absolute -top-8 left-1/2 transform -translate-x-1/2 bg-gray-800 text-white text-xs px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap z-20">
                        {{ formatCurrency(value) }}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- X-axis labels -->
              <div class="flex justify-between mt-2 ml-12">
                <div 
                  v-for="(label, index) in chartLabels" 
                  :key="index"
                  class="flex-1 text-center text-xs text-gray-600"
                  :class="{ 'font-semibold text-gray-800': label === 'Hôm nay' }"
                >
                  {{ label }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Content Grid -->
        <div class="grid grid-cols-1 xl:grid-cols-2 gap-6">
          <!-- Top Sản Phẩm Bán Chạy -->
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
            <div class="bg-gray-800 px-6 py-4">
              <div class="flex items-center justify-between">
                <h3 class="text-lg font-semibold text-white">Sản Phẩm Bán Chạy</h3>
                <i class="fas fa-trophy text-white"></i>
              </div>
            </div>
            
            <div class="p-6">
              <div v-if="!stats.sanPhamBanChay || stats.sanPhamBanChay.length === 0" class="text-center py-8">
                <i class="fas fa-book text-3xl text-gray-400 mb-4"></i>
                <p class="text-gray-500">Chưa có dữ liệu sản phẩm bán chạy</p>
              </div>
              
              <div v-else class="space-y-4">
                <div v-for="(product, index) in stats.sanPhamBanChay.slice(0, 5)" :key="product.id" 
                     class="flex items-center p-4 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors">
                  <div class="flex-shrink-0">
                    <div class="w-12 h-12 bg-white rounded-lg flex items-center justify-center shadow-sm">
                      <img
                        v-if="product.hinhAnh"
                        :src="product.hinhAnh"
                        :alt="product.tenSanPham"
                        class="w-10 h-10 object-cover rounded"
                        @error="handleImageError"
                      />
                      <i v-else class="fas fa-book text-gray-400"></i>
                    </div>
                  </div>
                  <div class="ml-4 flex-1">
                    <h4 class="font-semibold text-gray-900">{{ product.tenSanPham }}</h4>
                    <p class="text-sm text-gray-500">{{ product.maSanPham }}</p>
                    <div class="flex items-center mt-1">
                      <span class="bg-gray-200 text-gray-700 text-xs font-medium px-2 py-1 rounded">
                        #{{ index + 1 }}
                      </span>
                    </div>
                  </div>
                  <div class="text-right">
                    <div class="font-semibold text-gray-900">{{ product.soLuongBan }} bán</div>
                    <div class="text-sm text-gray-500">{{ formatCurrency(product.giaBan) }}</div>
                    <div class="text-xs text-gray-600 font-medium">{{ formatCurrency(product.doanhThu) }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Trạng Thái Đơn Hàng -->
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
            <div class="bg-gray-800 px-6 py-4">
              <div class="flex items-center justify-between">
                <h3 class="text-lg font-semibold text-white">Trạng Thái Đơn Hàng</h3>
                <i class="fas fa-chart-pie text-white"></i>
              </div>
            </div>
            
            <div class="p-6">
              <div v-if="!donHangTrangThai || Object.keys(donHangTrangThai).length === 0" class="text-center py-8">
                <i class="fas fa-chart-pie text-3xl text-gray-400 mb-4"></i>
                <p class="text-gray-500">Chưa có dữ liệu trạng thái đơn hàng</p>
              </div>
              
              <div v-else class="space-y-4">
                <div v-for="(segment, index) in pieChartSegments" :key="index" 
                     class="flex items-center justify-between p-4 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors">
                  <div class="flex items-center">
                    <div 
                      class="w-4 h-4 rounded-full mr-4"
                      :style="{ backgroundColor: segment.color }"
                    ></div>
                    <span class="font-medium text-gray-700">{{ segment.label }}</span>
                  </div>
                  <div class="text-right">
                    <div class="text-xl font-semibold text-gray-900">{{ segment.value }}</div>
                    <div class="text-sm text-gray-500">{{ segment.percentage }}%</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import ThongKeDoanhThuService from "@/service/ThongKeDoanhThuService";

export default {
  name: "DashboardReal",
  data() {
    return {
      loading: true,
      error: null,
      stats: {},
      donHangTrangThai: {},
      currentTime: "",
      currentDate: "",
      selectedPeriod: 'day',
      chartLoading: false,
      timePeriods: [
        { key: 'day', label: 'Theo Ngày' },
        { key: 'month', label: 'Theo Tháng' },
        { key: 'year', label: 'Theo Năm' }
      ],
    };
  },
  computed: {
    pieChartSegments() {
      if (!this.donHangTrangThai || Object.keys(this.donHangTrangThai).length === 0) {
        return [];
      }

      const total = Object.values(this.donHangTrangThai).reduce((sum, count) => sum + count, 0);
      if (total === 0) return [];

      const colors = ['#3B82F6', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6'];
      const labels = {
        daGiao: 'Đã giao',
        daHuy: 'Đã hủy', 
        dangGiao: 'Chờ giao',
        choXacNhan: 'Chờ xác nhận',
        daXacNhan: 'Đã xác nhận'
      };

      return Object.entries(this.donHangTrangThai)
        .filter(([, value]) => value > 0)
        .map(([key, value], index) => ({
          label: labels[key] || key,
          value: value,
          percentage: Math.round((value / total) * 100),
          color: colors[index % colors.length]
        }));
    },
    chartData() {
      if (!this.stats) return [];
      
      const dataMap = {
        day: this.stats.doanhThuNgay || [],
        month: this.stats.doanhThuThang || [],
        year: this.stats.doanhThuNam || []
      };
      
      let data = dataMap[this.selectedPeriod] || [];
      
      // Giới hạn số ngày hiển thị cho "Theo Ngày" để tránh quá nhiều thanh nhỏ
      if (this.selectedPeriod === 'day' && data.length > 14) {
        // Chỉ hiển thị 14 ngày gần nhất
        data = data.slice(-14);
      }
      
      return data.map(item => {
        return item.doanhThu || 0;
      });
    },
    chartLabels() {
      if (!this.stats) return [];
      
      const dataMap = {
        day: this.stats.doanhThuNgay || [],
        month: this.stats.doanhThuThang || [],
        year: this.stats.doanhThuNam || []
      };
      
      let data = dataMap[this.selectedPeriod] || [];
      
      // Giới hạn số ngày hiển thị cho "Theo Ngày" để tránh quá nhiều thanh nhỏ
      if (this.selectedPeriod === 'day' && data.length > 14) {
        // Chỉ hiển thị 14 ngày gần nhất
        data = data.slice(-14);
      }
      
      return data.map(item => {
        if (this.selectedPeriod === 'day') {
          return item.ngay || '';
        } else if (this.selectedPeriod === 'month') {
          return item.thang || '';
        } else {
          return item.nam || '';
        }
      });
    },
    maxValue() {
      if (this.chartData.length === 0) return 1;
      return Math.max(...this.chartData);
    },
    yAxisLabels() {
      if (this.maxValue === 0) return ['0', '0', '0', '0', '0'];
      
      const max = this.maxValue;
      return [
        this.formatCurrency(max),
        this.formatCurrency(max * 0.75),
        this.formatCurrency(max * 0.5),
        this.formatCurrency(max * 0.25),
        '0'
      ];
    }
  },
  mounted() {
    this.updateDateTime();
    this.loadData();
    setInterval(this.updateDateTime, 1000);
  },
  methods: {
    async loadData() {
      try {
        this.loading = true;
        this.error = null;
        
        // Load tất cả dữ liệu cơ bản
        const [
          tongQuan,
          donHangTrangThai,
          sanPhamBanChay,
          doanhThuNgay,
          doanhThuThang,
          doanhThuNam
        ] = await Promise.all([
          ThongKeDoanhThuService.getThongKeTongQuan(),
          ThongKeDoanhThuService.getDonHangTheoTrangThai(),
          ThongKeDoanhThuService.getSanPhamBanChay(),
          ThongKeDoanhThuService.getDoanhThuTheoNgay(),
          ThongKeDoanhThuService.getDoanhThuTheoThang(),
          ThongKeDoanhThuService.getDoanhThuTheoNam()
        ]);
        
        this.stats = {
          tongQuan,
          donHangTrangThai,
          sanPhamBanChay,
          doanhThuNgay,
          doanhThuThang,
          doanhThuNam
        };
        this.donHangTrangThai = donHangTrangThai || {};
        
        // Load dữ liệu chart cho period mặc định (day)
        await this.updateChart();
        
        console.log('Basic data loaded:', this.stats);
        console.log('Order status:', this.donHangTrangThai);
      } catch (error) {
        console.error('Error loading data:', error);
        this.error = 'Không thể tải dữ liệu. Vui lòng thử lại.';
        // Set default values để tránh lỗi
        this.stats = {};
        this.donHangTrangThai = {};
      } finally {
        this.loading = false;
      }
    },
    refreshData() {
      this.loadData();
    },
    updateDateTime() {
      const now = new Date();
      this.currentTime = now.toLocaleTimeString('vi-VN');
      this.currentDate = now.toLocaleDateString('vi-VN');
    },
    formatCurrency(amount) {
      if (!amount) return '0 ₫';
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount);
    },
    handleImageError(event) {
      event.target.style.display = 'none';
      const parent = event.target.parentElement;
      const icon = parent.querySelector('i');
      if (icon) {
        icon.style.display = 'block';
      }
    },
    async updateChart() {
      // Dữ liệu đã được load sẵn, chỉ cần cập nhật chart
      console.log('Updating chart for period:', this.selectedPeriod);
      try {
        this.chartLoading = true;
        
        // Dữ liệu đã có sẵn trong this.stats
        let chartData;
        switch (this.selectedPeriod) {
          case 'day':
            chartData = this.stats.doanhThuNgay || [];
            break;
          case 'month':
            chartData = this.stats.doanhThuThang || [];
            break;
          case 'year':
            chartData = this.stats.doanhThuNam || [];
            break;
        }
        
        console.log('Chart data for period:', this.selectedPeriod, chartData);
      } catch (error) {
        console.error('Error updating chart for period:', this.selectedPeriod, error);
        this.error = 'Không thể cập nhật biểu đồ cho ' + this.selectedPeriod;
      } finally {
        this.chartLoading = false;
      }
    },
    getThangNayDoanhThu() {
      if (!this.stats.doanhThuThang || this.stats.doanhThuThang.length === 0) return 0;
      const currentMonth = this.stats.doanhThuThang.find(item => item.thang === 'Tháng này');
      return currentMonth ? currentMonth.doanhThu : 0;
    },
    getNamNayDoanhThu() {
      if (!this.stats.doanhThuNam || this.stats.doanhThuNam.length === 0) return 0;
      const currentYear = this.stats.doanhThuNam.find(item => item.nam === 'Năm này');
      return currentYear ? currentYear.doanhThu : 0;
    },
    getChartDescription() {
      console.log('Selected period:', this.selectedPeriod);
      switch (this.selectedPeriod) {
        case 'day':
          return 'Hiển thị doanh thu của 14 ngày gần nhất (14 ngày)';
        case 'month':
          return 'Hiển thị doanh thu của 12 tháng trong năm hiện tại (12 tháng)';
        case 'year':
          return 'Hiển thị doanh thu của 5 năm gần nhất (5 năm)';
        default:
          return 'Hiển thị doanh thu theo thời gian';
      }
    }
  }
};
</script>

<style scoped>
/* Simple transitions */
.transition-colors {
  transition: background-color 0.2s ease, color 0.2s ease;
}

/* Custom scrollbar */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: #f9fafb;
}

::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #9ca3af;
}

/* Focus states */
button:focus {
  outline: none;
  box-shadow: 0 0 0 2px #374151;
}

/* Simple hover effects */
.hover\:bg-gray-100:hover {
  background-color: #f3f4f6;
}

.hover\:bg-gray-200:hover {
  background-color: #e5e7eb;
}

.hover\:bg-gray-700:hover {
  background-color: #374151;
}

.hover\:bg-gray-900:hover {
  background-color: #111827;
}
</style>
