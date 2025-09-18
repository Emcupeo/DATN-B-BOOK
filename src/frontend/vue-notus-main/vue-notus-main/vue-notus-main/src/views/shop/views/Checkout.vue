<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header with Progress Steps -->
    <div class="bg-white shadow-sm border-b">
      <div class="container mx-auto px-4 py-4">
        <div class="flex items-center justify-between">
          <h1 class="text-xl font-semibold text-gray-900">{{ pageTitle }}</h1>
          <div class="flex items-center space-x-2 text-sm">
            <!-- Step 1: Giỏ hàng -->
            <div class="flex items-center">
              <div class="w-6 h-6 rounded-full flex items-center justify-center text-xs font-medium" 
                   :class="step1Class">
                <i v-if="currentStep >= 1" class="fas fa-check text-white text-xs"></i>
                <span v-else class="text-white">1</span>
              </div>
              <span class="ml-2 font-medium" :class="step1TextClass">Giỏ hàng</span>
            </div>
            
            <i class="fas fa-chevron-right text-gray-400 text-xs"></i>
            
            <!-- Step 2: Thanh toán -->
            <div class="flex items-center">
              <div class="w-6 h-6 rounded-full flex items-center justify-center text-xs font-medium" 
                   :class="step2Class">
                <i v-if="(currentStep === 2 && paymentStatus === 'success') || (currentStep === 3 && paymentStatus === 'success')" class="fas fa-check text-white text-xs"></i>
                <i v-else-if="currentStep === 2 && paymentStatus === 'failed'" class="fas fa-times text-white text-xs"></i>
                <i v-else-if="currentStep === 2 && paymentStatus === 'processing'" class="fas fa-spinner fa-spin text-white text-xs"></i>
                <span v-else class="text-white">2</span>
              </div>
              <span class="ml-2 font-medium" :class="step2TextClass">Thanh toán</span>
            </div>
            
            <i class="fas fa-chevron-right text-gray-400 text-xs"></i>
            
            <!-- Step 3: Hoàn thành -->
            <div class="flex items-center">
              <div class="w-6 h-6 rounded-full flex items-center justify-center text-xs font-medium" 
                   :class="step3Class">
                <i v-if="currentStep === 3 && paymentStatus === 'success'" class="fas fa-check text-white text-xs"></i>
                <i v-else-if="currentStep === 3 && paymentStatus === 'failed'" class="fas fa-times text-white text-xs"></i>
                <span v-else class="text-white">3</span>
              </div>
              <span class="ml-2 font-medium" :class="step3TextClass">Hoàn thành</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="container mx-auto px-4 py-6">
      <!-- Checkout Form State -->
      <div v-if="currentStep === 2 && paymentStatus === 'pending'" class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Checkout Form -->
        <div class="lg:col-span-2 space-y-4">
          <!-- Delivery Address Section -->
          <div class="bg-white rounded-lg shadow-sm border border-gray-200">
            <div class="px-4 py-3 border-b border-gray-100">
              <div class="flex items-center justify-between">
                <h2 class="text-base font-medium text-gray-900 flex items-center">
                  <i class="fas fa-map-marker-alt text-orange-500 mr-2"></i>
                  Địa chỉ giao hàng
                </h2>
                <button 
                  v-if="isAuthenticated"
                  @click="openAddressModal"
                  class="text-sm text-orange-600 hover:text-orange-700 font-medium"
                >
                  <i class="fas fa-plus mr-1"></i>
                  Thêm địa chỉ
                </button>
              </div>
        </div>

            <div class="p-4">
              <!-- For Authenticated Users: Show Saved Addresses -->
              <div v-if="isAuthenticated">
                <!-- Selected Address Display -->
                <div v-if="selectedAddressIndex >= 0 && savedAddresses[selectedAddressIndex]" class="mb-6">
                  <div class="flex items-start space-x-3 p-4 bg-gradient-to-r from-orange-50 to-orange-100 border-2 border-orange-300 rounded-xl shadow-sm">
                    <div class="flex-shrink-0">
                      <div class="w-8 h-8 bg-orange-500 rounded-full flex items-center justify-center">
                        <i class="fas fa-map-marker-alt text-white text-sm"></i>
                      </div>
                    </div>
                    <div class="flex-1">
                      <div class="flex items-center justify-between">
                        <div>
                          <div class="flex items-center space-x-2 mb-1">
                            <span class="font-semibold text-gray-900">{{ savedAddresses[selectedAddressIndex].fullName }}</span>
                            <span v-if="savedAddresses[selectedAddressIndex].isDefault" class="text-xs bg-orange-200 text-orange-800 px-2 py-1 rounded-full font-medium">Mặc định</span>
                          </div>
                          <div class="text-sm text-gray-600 mb-1">
                            <i class="fas fa-phone text-xs mr-1"></i>
                            {{ savedAddresses[selectedAddressIndex].phone }}
                          </div>
                          <div class="text-sm text-gray-700">
                            <i class="fas fa-map-marker-alt text-xs mr-1"></i>
                            {{ getFullAddressDisplay(savedAddresses[selectedAddressIndex]) }}
                          </div>
                        </div>
                        <div class="flex items-center space-x-2">
                          <button @click="showAddressSelection = !showAddressSelection" class="text-orange-600 hover:text-orange-700 px-3 py-1 text-sm border border-orange-300 rounded-lg hover:bg-orange-100 transition-colors">
                            <i class="fas fa-exchange-alt mr-1"></i>
                            Chọn địa chỉ khác
                          </button>
                          <button @click="openAddressModal" class="text-orange-600 hover:text-orange-700 p-2 hover:bg-orange-200 rounded-lg transition-colors">
                            <i class="fas fa-edit"></i>
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Address Selection List (Collapsible) -->
                <div v-if="showAddressSelection && savedAddresses.length > 0" class="space-y-3 mb-6">
                  <div class="flex items-center justify-between mb-4">
                    <h3 class="text-sm font-semibold text-gray-800 flex items-center">
                      <i class="fas fa-list-ul text-orange-500 mr-2"></i>
                      Chọn địa chỉ giao hàng
                    </h3>
                    <div class="flex items-center space-x-2">
                      <span class="text-xs text-gray-500">{{ savedAddresses.length }} địa chỉ</span>
                      <button @click="showAddressSelection = false" class="text-gray-400 hover:text-gray-600">
                        <i class="fas fa-times"></i>
                      </button>
                    </div>
                  </div>
                  
                  <div class="grid gap-3">
                    <div 
                      v-for="(address, index) in savedAddresses" 
                      :key="address.id"
                      @click="selectSavedAddress(address); showAddressSelection = false"
                      :class="[
                        'p-4 border-2 rounded-xl cursor-pointer transition-all duration-200 hover:shadow-md',
                        selectedAddressIndex === index 
                          ? 'border-orange-400 bg-orange-50 shadow-md' 
                          : 'border-gray-200 hover:border-orange-300 bg-white'
                      ]"
                    >
                      <div class="flex items-start justify-between">
                        <div class="flex-1">
                          <div class="flex items-center space-x-2 mb-2">
                            <span class="font-medium text-gray-900">{{ address.fullName }}</span>
                            <span v-if="address.isDefault" class="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded-full font-medium">Mặc định</span>
                          </div>
                          <div class="text-sm text-gray-600 mb-1">
                            <i class="fas fa-phone text-xs mr-1"></i>
                            {{ address.phone }}
                          </div>
                          <div class="text-sm text-gray-700">
                            <i class="fas fa-map-marker-alt text-xs mr-1"></i>
                            {{ getFullAddressDisplay(address) }}
                          </div>
                        </div>
                        <div class="flex items-center space-x-2">
                          <div v-if="selectedAddressIndex === index" class="w-6 h-6 bg-orange-500 rounded-full flex items-center justify-center shadow-sm">
                            <i class="fas fa-check text-white text-xs"></i>
                          </div>
                          <div v-else class="w-6 h-6 border-2 border-gray-300 rounded-full hover:border-orange-400 transition-colors"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- No Addresses Message - Only show when no addresses exist -->
                <div v-else-if="savedAddresses.length === 0" class="text-center py-12">
                  <div class="w-20 h-20 bg-gradient-to-br from-orange-100 to-orange-200 rounded-full flex items-center justify-center mx-auto mb-6">
                    <i class="fas fa-map-marker-alt text-orange-500 text-2xl"></i>
                  </div>
                  <h3 class="text-xl font-semibold text-gray-900 mb-3">Chưa có địa chỉ nào</h3>
                  <p class="text-gray-600 mb-6 max-w-sm mx-auto">Bạn chưa có địa chỉ nào được lưu. Hãy thêm địa chỉ đầu tiên để việc mua sắm trở nên thuận tiện hơn.</p>
                  <button 
                    @click="openAddressModal"
                    class="inline-flex items-center px-6 py-3 bg-gradient-to-r from-orange-500 to-orange-600 text-white rounded-lg hover:from-orange-600 hover:to-orange-700 transition-all duration-200 shadow-lg hover:shadow-xl"
                  >
                    <i class="fas fa-plus mr-2"></i>
                    Thêm địa chỉ đầu tiên
                  </button>
                </div>
              </div>

              <!-- For Non-Authenticated Users: Manual Address Input -->
              <div v-else class="space-y-6">
                <!-- Header -->
                <div class="text-center py-4">
                  <div class="w-12 h-12 bg-orange-100 rounded-full flex items-center justify-center mx-auto mb-3">
                    <i class="fas fa-user-plus text-orange-500 text-lg"></i>
                  </div>
                  <h3 class="text-lg font-semibold text-gray-900 mb-2">Thông tin giao hàng</h3>
                  <p class="text-sm text-gray-600">Vui lòng nhập thông tin để chúng tôi có thể giao hàng đến bạn</p>
                </div>

                <!-- Personal Information -->
                <div class="bg-gray-50 rounded-xl p-4">
                  <h4 class="text-sm font-semibold text-gray-800 mb-4 flex items-center">
                    <i class="fas fa-user text-orange-500 mr-2"></i>
                    Thông tin cá nhân
                  </h4>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
                      <label class="block text-sm font-medium text-gray-700 mb-2">
                        <i class="fas fa-user text-xs mr-1"></i>
                        Họ và tên *
                      </label>
              <input 
                type="text" 
                v-model="customerInfo.fullName"
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-colors"
                        placeholder="Nhập họ và tên đầy đủ"
                required
              >
            </div>
            <div>
                      <label class="block text-sm font-medium text-gray-700 mb-2">
                        <i class="fas fa-phone text-xs mr-1"></i>
                        Số điện thoại *
                      </label>
              <input 
                type="tel" 
                v-model="customerInfo.phone"
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-colors"
                        :class="{ 'border-red-500': customerInfo.phone && !validatePhoneNumber(customerInfo.phone) }"
                        placeholder="Nhập số điện thoại (VD: 0123456789)"
                required
                        @blur="validatePhoneField"
              >
                      <div v-if="customerInfo.phone && !validatePhoneNumber(customerInfo.phone)" class="text-red-500 text-xs mt-1">
                        <i class="fas fa-exclamation-triangle mr-1"></i>
                        Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại Việt Nam (10-11 số)
            </div>
            </div>
                  </div>
                  <div class="mt-4">
                    <label class="block text-sm font-medium text-gray-700 mb-2">
                      <i class="fas fa-envelope text-xs mr-1"></i>
                      Email (tùy chọn)
                    </label>
              <input 
                type="email" 
                v-model="customerInfo.email"
                      class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-colors"
                      placeholder="Nhập email để nhận thông báo đơn hàng"
              >
          </div>
        </div>

                <!-- Address Information -->
                <div class="bg-gray-50 rounded-xl p-4">
                  <h4 class="text-sm font-semibold text-gray-800 mb-4 flex items-center">
                    <i class="fas fa-map-marker-alt text-orange-500 mr-2"></i>
                    Địa chỉ giao hàng
                  </h4>
          <div class="space-y-4">
            <div>
                      <label class="block text-sm font-medium text-gray-700 mb-2">
                        <i class="fas fa-home text-xs mr-1"></i>
                        Địa chỉ chi tiết *
                      </label>
              <input 
                type="text" 
                v-model="shippingAddress.address"
                        class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-colors"
                        placeholder="Số nhà, tên đường, tòa nhà, căn hộ..."
                required
              >
            </div>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">
                          <i class="fas fa-map text-xs mr-1"></i>
                          Tỉnh/Thành phố *
                        </label>
                <select 
                  v-model="shippingAddress.city"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-colors"
                  required
                  :disabled="loadingProvinces"
                >
                          <option value="">Chọn tỉnh/thành phố</option>
                  <option v-for="p in provinces" :key="p.code" :value="p.code">{{ p.name }}</option>
                </select>
              </div>
              <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">
                          <i class="fas fa-map text-xs mr-1"></i>
                          Quận/Huyện *
                        </label>
                <select 
                  v-model="shippingAddress.district"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-colors disabled:bg-gray-100"
                  required
                  :disabled="!shippingAddress.city || loadingDistricts"
                >
                  <option value="">Chọn quận/huyện</option>
                  <option v-for="d in districts" :key="d.code" :value="d.code">{{ d.name }}</option>
                </select>
              </div>
              <div>
                        <label class="block text-sm font-medium text-gray-700 mb-2">
                          <i class="fas fa-map text-xs mr-1"></i>
                          Phường/Xã *
                        </label>
                <select 
                  v-model="shippingAddress.ward"
                          class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition-colors disabled:bg-gray-100"
                  required
                  :disabled="!shippingAddress.district || loadingWards"
                >
                  <option value="">Chọn phường/xã</option>
                  <option v-for="w in wards" :key="w.code" :value="w.code">{{ w.name }}</option>
                </select>
                      </div>
                    </div>
                  </div>
              </div>
            </div>
          </div>
        </div>

          <!-- Delivery Options Section -->
          <div class="bg-white rounded-lg shadow-sm border border-gray-200">
            <div class="px-4 py-3 border-b border-gray-100">
              <h2 class="text-base font-medium text-gray-900 flex items-center">
                <i class="fas fa-truck text-blue-500 mr-2"></i>
                Phương thức giao hàng
              </h2>
            </div>
            
            <div class="p-4">
          <div class="space-y-3">
                <div 
                  class="flex items-center p-3 border border-gray-200 rounded-lg cursor-pointer hover:border-blue-500 transition-colors"
                  :class="selectedDeliveryMethod === 'standard' ? 'border-blue-500 bg-blue-50' : ''"
                  @click="selectedDeliveryMethod = 'standard'"
                >
                  <input 
                    type="radio" 
                    v-model="selectedDeliveryMethod" 
                    value="standard"
                    class="text-blue-600 mr-3"
                  >
                  <div class="flex-1">
                    <div class="flex items-center justify-between">
                      <div>
                        <div class="font-medium text-gray-900">Giao hàng tiêu chuẩn</div>
                        <div class="text-sm text-gray-500">Giao hàng trong 2-5 ngày làm việc</div>
                      </div>
                      <div class="text-right">
                        <div class="font-medium text-gray-900">{{ formatPrice(standardShippingFee) }}</div>
                        <div class="text-xs text-gray-500">Phí vận chuyển</div>
                      </div>
                    </div>
                  </div>
                </div>
                
                <div 
                  class="flex items-center p-3 border border-gray-200 rounded-lg cursor-pointer hover:border-blue-500 transition-colors"
                  :class="selectedDeliveryMethod === 'express' ? 'border-blue-500 bg-blue-50' : ''"
                  @click="selectedDeliveryMethod = 'express'"
                >
                  <input 
                    type="radio" 
                    v-model="selectedDeliveryMethod" 
                    value="express"
                    class="text-blue-600 mr-3"
                  >
                  <div class="flex-1">
                    <div class="flex items-center justify-between">
                      <div>
                        <div class="font-medium text-gray-900">Giao hàng nhanh</div>
                        <div class="text-sm text-gray-500">Giao hàng trong 1-2 ngày làm việc</div>
                      </div>
                      <div class="text-right">
                        <div class="font-medium text-gray-900">{{ formatPrice(expressShippingFee) }}</div>
                        <div class="text-xs text-gray-500">Phí vận chuyển</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>


          <!-- Payment Method Section -->
          <div class="bg-white rounded-lg shadow-sm border border-gray-200">
            <div class="px-4 py-3 border-b border-gray-100">
              <h2 class="text-base font-medium text-gray-900 flex items-center">
                <i class="fas fa-credit-card text-green-500 mr-2"></i>
                Phương thức thanh toán
              </h2>
            </div>
            
            <div class="p-4">
              <div class="space-y-3">
                <div 
                  @click="paymentMethod = 'cod'"
                  class="flex items-center p-3 border border-gray-200 rounded-lg cursor-pointer hover:border-green-500 transition-colors"
                  :class="paymentMethod === 'cod' ? 'border-green-500 bg-green-50' : ''"
                >
              <input 
                type="radio" 
                v-model="paymentMethod" 
                value="cod"
                    class="text-green-600 mr-3"
                  >
                  <div class="flex items-center flex-1">
                    <div class="w-8 h-8 bg-green-100 rounded-full flex items-center justify-center mr-3">
                      <i class="fas fa-money-bill-wave text-green-600 text-sm"></i>
                    </div>
                    <div>
                      <div class="font-medium text-gray-900">Thanh toán khi nhận hàng (COD)</div>
                      <div class="text-sm text-gray-500">Thanh toán bằng tiền mặt khi nhận hàng</div>
                    </div>
                  </div>
                </div>
                
                <div 
                  @click="paymentMethod = 'bank'"
                  class="flex items-center p-3 border border-gray-200 rounded-lg cursor-pointer hover:border-green-500 transition-colors"
                  :class="paymentMethod === 'bank' ? 'border-green-500 bg-green-50' : ''"
                >
              <input 
                type="radio" 
                v-model="paymentMethod" 
                value="bank"
                    class="text-green-600 mr-3"
                  >
                  <div class="flex items-center flex-1">
                    <div class="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center mr-3">
                      <i class="fas fa-university text-blue-600 text-sm"></i>
                    </div>
                    <div>
                      <div class="font-medium text-gray-900">Thanh toán online (VNPAY)</div>
                      <div class="text-sm text-gray-500">Thanh toán qua VNPAY - An toàn & Nhanh chóng</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Order Notes -->
          <div class="bg-white rounded-lg shadow-sm border border-gray-200">
            <div class="px-4 py-3 border-b border-gray-100">
              <h2 class="text-base font-medium text-gray-900 flex items-center">
                <i class="fas fa-sticky-note text-yellow-500 mr-2"></i>
                Ghi chú đơn hàng
              </h2>
            </div>
            
            <div class="p-4">
              <textarea
                v-model="ghiChu"
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-yellow-500 focus:border-transparent resize-none"
                rows="3"
                placeholder="Nhập ghi chú cho đơn hàng (tùy chọn)..."
              ></textarea>
          </div>
        </div>
      </div>

      <!-- Order Summary -->
        <div class="bg-white rounded-lg shadow-sm border border-gray-200 sticky top-6">
          <div class="px-4 py-3 border-b border-gray-100">
            <h2 class="text-base font-medium text-gray-900 flex items-center">
              <i class="fas fa-shopping-bag text-orange-500 mr-2"></i>
              Đơn hàng của bạn
            </h2>
          </div>
          
          <div class="p-4">
            <!-- Voucher Section -->
          <div class="mb-4">
              <div class="flex items-center justify-between mb-3">
                <h3 class="text-sm font-medium text-gray-700">Mã giảm giá</h3>
                <button 
                  @click="openVoucherModal"
                  class="text-sm text-orange-600 hover:text-orange-700 font-medium"
                >
                  <i class="fas fa-ticket-alt mr-1"></i>
                  Chọn mã
                </button>
              </div>

              <!-- Applied Voucher -->
              <div v-if="discountInfo" class="p-2 bg-green-50 border border-green-200 rounded-lg mb-2">
                <div class="flex items-center justify-between">
                  <div class="flex items-center">
                    <i class="fas fa-check-circle text-green-600 mr-2 text-sm"></i>
                    <div>
                      <div class="text-sm font-medium text-green-800">{{ discountInfo.tenPhieuGiamGia }}</div>
                      <div class="text-xs text-green-600">
                        <span v-if="discountInfo.soPhanTramGiam">Giảm {{ discountInfo.soPhanTramGiam }}%</span>
                        <span v-else-if="discountInfo.giaTriGiam">Trừ {{ formatPrice(discountInfo.giaTriGiam) }}</span>
                      </div>
                    </div>
                  </div>
                  <button 
                    @click="removeDiscount"
                    class="text-red-500 hover:text-red-700 text-sm"
                  >
                    <i class="fas fa-times"></i>
                  </button>
                </div>
              </div>

              <!-- Manual Code Input -->
              <div class="flex space-x-2">
              <input
                v-model="discountCode"
                type="text"
                  placeholder="Nhập mã giảm giá"
                  class="flex-1 px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent text-sm"
              >
              <button
                @click="applyDiscount"
                  class="px-3 py-2 bg-orange-600 text-white rounded-lg hover:bg-orange-700 font-medium text-sm whitespace-nowrap"
                type="button"
              >
                Áp dụng
              </button>
            </div>
            <div v-if="discountError" class="text-red-500 text-xs mt-1">{{ discountError }}</div>
          </div>
          
            <!-- Cart Items -->
            <div class="mb-4">
              <h3 class="text-sm font-medium text-gray-700 mb-3">Sản phẩm ({{ cartItems.length }})</h3>
              <div class="space-y-3 max-h-64 overflow-y-auto">
            <div 
              v-for="item in cartItems" 
              :key="item.id"
                  class="flex items-center space-x-3 p-2 bg-gray-50 rounded-lg"
            >
                <img 
                  :src="item.image" 
                  :alt="item.title"
                  class="w-12 h-16 object-cover rounded"
                >
                  <div class="flex-1 min-w-0">
                    <h4 class="font-medium text-sm text-gray-900 truncate">{{ item.title }}</h4>
                    <p class="text-xs text-gray-500 truncate">{{ item.author }}</p>
                    <div class="text-xs mt-1">
                      <span v-if="item.originalPrice && item.originalPrice > item.price" class="text-gray-500 line-through mr-2">
                        {{ formatPrice(item.originalPrice) }}
                      </span>
                      <span class="text-red-600 font-medium">{{ formatPrice(item.price) }}</span>
                    </div>
                    <div class="flex items-center justify-between mt-1">
                      <!-- Quantity Controls -->
                      <div class="flex items-center space-x-2">
                        <button 
                          @click="decreaseQuantity(item.id)"
                          :disabled="item.quantity <= 1"
                          class="w-6 h-6 rounded-full border border-gray-300 flex items-center justify-center hover:bg-gray-50 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 12H4"></path>
                          </svg>
                        </button>
                        <span class="text-sm font-medium w-8 text-center">{{ item.quantity }}</span>
                        <button 
                          @click="increaseQuantity(item.id)"
                          :disabled="item.stockQuantity && item.quantity >= item.stockQuantity"
                          class="w-6 h-6 rounded-full border border-gray-300 flex items-center justify-center hover:bg-gray-50 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                          </svg>
                        </button>
                </div>
                      <div class="text-right">
                        <div v-if="item.originalPrice && item.originalPrice > item.price" class="text-xs text-gray-500 line-through">
                          {{ formatPrice(item.originalPrice * item.quantity) }}
                        </div>
                        <div class="text-sm font-semibold text-gray-900">
                          {{ formatPrice(item.price * item.quantity) }}
                        </div>
                      </div>
              </div>
              </div>
                </div>
            </div>
          </div>

            <!-- Order Summary -->
            <div class="space-y-2 border-t pt-4">
              <div class="flex justify-between text-sm">
                <span class="text-gray-600">Tạm tính:</span>
                <span class="text-gray-900">{{ formatPrice(cartTotal) }}</span>
            </div>
              <div v-if="discountInfo" class="flex justify-between text-sm">
                <span class="text-gray-600">Giảm giá:</span>
                <span class="text-green-600 font-medium">-{{ formatPrice(discountAmount) }}</span>
            </div>
              <div class="flex justify-between text-sm">
                <span class="text-gray-600">Phí vận chuyển:</span>
                <span class="text-gray-900">
                <span v-if="shippingFeeLoading">Đang tính...</span>
                <span v-else-if="shippingFeeError">{{ shippingFeeError }}</span>
                <span v-else-if="shippingFee > 0">{{ formatPrice(shippingFee) }}</span>
                <span v-else>Chưa chọn</span>
              </span>
            </div>
              <div class="border-t pt-3">
                <div class="flex justify-between items-center">
                  <span class="text-base font-semibold text-gray-900">Tổng cộng:</span>
                  <span class="text-lg font-bold text-orange-600">{{ formatPrice(finalTotal) }}</span>
                </div>
            </div>
          </div>

          <button 
            @click="onPlaceOrder"
            :disabled="!isFormValid"
            :class="[
                'w-full py-3 px-4 rounded-lg font-semibold text-white transition-all duration-200 mt-4',
              isFormValid 
                  ? 'bg-orange-500 hover:bg-orange-600 shadow-md' 
                  : 'bg-gray-400 cursor-not-allowed'
              ]"
            >
              <div class="flex items-center justify-center space-x-2">
                <i v-if="paymentMethod === 'bank'" class="fas fa-university"></i>
                <i v-else class="fas fa-shopping-cart"></i>
                <span>{{ paymentMethod === 'bank' ? 'Thanh toán VNPAY' : 'Đặt hàng COD' }}</span>
              </div>
          </button>
          </div>
        </div>
      </div>
    </div>
    <!-- Modal QR chuyển khoản ngân hàng -->
    <div v-if="showBankQR" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg shadow-lg p-8 max-w-md w-full relative">
        <h2 class="text-xl font-bold mb-4 text-center">Chuyển khoản ngân hàng</h2>
        <div class="flex flex-col items-center mb-4">
          <img :src="bankQRUrl" alt="QR chuyển khoản" class="w-64 h-64 object-contain mb-2" />
          <div class="text-sm text-gray-700 mb-1">Số tài khoản: <span class="font-semibold">{{ bankAccount }}</span></div>
          <div class="text-sm text-gray-700 mb-1">Ngân hàng: <span class="font-semibold">{{ bankName }}</span></div>
          <div class="text-sm text-gray-700 mb-1">Số tiền: <span class="font-semibold text-blue-600">{{ formatPrice(transferAmount) }}</span></div>
          <div class="text-sm text-gray-700 mb-1">Nội dung chuyển khoản: <span class="font-semibold text-green-600">{{ transferContent }}</span></div>
        </div>
        <button @click="confirmBankTransfer" class="w-full bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 transition-colors">Tôi đã chuyển tiền</button>
        <button @click="showBankQR = false" class="absolute top-2 right-2 text-gray-400 hover:text-red-500 text-xl">×</button>
      </div>
    </div>

    <!-- Address Management Modal -->
    <div v-if="showAddressModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-xl p-6 max-w-4xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-xl font-semibold text-gray-900">Quản lý địa chỉ</h2>
          <button @click="showAddressModal = false; editingAddress = null" class="text-gray-400 hover:text-gray-600">
            <i class="fas fa-times text-xl"></i>
          </button>
        </div>
        
        <!-- Add/Edit Address Form -->
        <div class="mb-6 p-4 bg-gray-50 rounded-lg">
          <h3 class="text-lg font-medium mb-4">
            {{ editingAddress ? 'Chỉnh sửa địa chỉ' : 'Thêm địa chỉ mới' }}
          </h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Họ và tên *</label>
              <input 
                v-model="newAddress.fullName" 
                type="text" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Nhập họ và tên"
              >
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Số điện thoại *</label>
              <input 
                v-model="newAddress.phone" 
                type="tel" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                :class="{ 'border-red-500': newAddress.phone && !validatePhoneNumber(newAddress.phone) }"
                placeholder="Nhập số điện thoại (VD: 0123456789)"
                @blur="validateNewAddressPhone"
              >
              <div v-if="newAddress.phone && !validatePhoneNumber(newAddress.phone)" class="text-red-500 text-xs mt-1">
                <i class="fas fa-exclamation-triangle mr-1"></i>
                Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại Việt Nam (10-11 số)
              </div>
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-2">Địa chỉ chi tiết *</label>
              <input 
                v-model="newAddress.address" 
                type="text" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Số nhà, tên đường, tòa nhà..."
              >
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Tỉnh/Thành phố *</label>
              <select 
                v-model="newAddress.city" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                @change="loadDistricts(newAddress.city)"
              >
                <option value="">Chọn tỉnh/thành phố</option>
                <option v-for="p in provinces" :key="p.code" :value="p.code">{{ p.name }}</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Quận/Huyện *</label>
              <select 
                v-model="newAddress.district" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                @change="loadWards(newAddress.district)"
              >
                <option value="">Chọn quận/huyện</option>
                <option v-for="d in districts" :key="d.code" :value="d.code">{{ d.name }}</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Phường/Xã *</label>
              <select 
                v-model="newAddress.ward" 
                class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Chọn phường/xã</option>
                <option v-for="w in wards" :key="w.code" :value="w.code">{{ w.name }}</option>
              </select>
            </div>
            <div class="md:col-span-2">
              <label class="flex items-center">
                <input v-model="newAddress.isDefault" type="checkbox" class="mr-2">
                <span class="text-sm text-gray-700">Đặt làm địa chỉ mặc định</span>
              </label>
            </div>
          </div>
          <div class="mt-4 flex space-x-3">
            <button 
              @click="saveNewAddress" 
              class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
            >
              {{ editingAddress ? 'Cập nhật' : 'Lưu địa chỉ' }}
            </button>
            <button 
              v-if="editingAddress"
              @click="cancelEditAddress" 
              class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50"
            >
              Hủy
            </button>
          </div>
        </div>

        <!-- Saved Addresses List -->
        <div v-if="savedAddresses.length > 0">
          <h3 class="text-lg font-medium mb-4">Địa chỉ đã lưu</h3>
          <div class="space-y-3">
            <div v-for="address in savedAddresses" :key="address.id" class="p-4 border border-gray-200 rounded-lg">
              <div class="flex items-start justify-between">
                <div class="flex-1">
                  <div class="flex items-center mb-2">
                    <span class="font-medium">{{ address.fullName }}</span>
                    <span class="ml-2 text-sm text-gray-500">{{ address.phone }}</span>
                    <span v-if="address.isDefault" class="ml-2 text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded-full">Mặc định</span>
                  </div>
                  <p class="text-sm text-gray-600">{{ getFullAddressDisplay(address) }}</p>
                </div>
                <div class="flex space-x-2">
                  <button @click="selectSavedAddress(address)" class="text-blue-600 hover:text-blue-700 text-sm px-2 py-1 rounded hover:bg-blue-50">
                    <i class="fas fa-check mr-1"></i>Chọn
                  </button>
                  <button @click="editAddress(address)" class="text-green-600 hover:text-green-700 text-sm px-2 py-1 rounded hover:bg-green-50">
                    <i class="fas fa-edit mr-1"></i>Sửa
                  </button>
                  <button @click="deleteAddress(address.id)" class="text-red-600 hover:text-red-700 text-sm px-2 py-1 rounded hover:bg-red-50">
                    <i class="fas fa-trash mr-1"></i>Xóa
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Customer Search Modal -->
    <div v-if="showCustomerModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl shadow-xl p-6 max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-xl font-semibold text-gray-900">Tìm kiếm khách hàng</h2>
          <button @click="showCustomerModal = false" class="text-gray-400 hover:text-gray-600">
            <i class="fas fa-times text-xl"></i>
          </button>
        </div>

        <!-- Search Input -->
        <div class="mb-4">
          <div class="flex gap-2">
            <input 
              type="text" 
              v-model="customerSearchTerm"
              @input="searchCustomers"
              class="flex-1 px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Nhập số điện thoại, email hoặc tên khách hàng..."
            >
            <button 
              @click="searchCustomers"
              :disabled="searchingCustomers"
              class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50"
            >
              <i v-if="searchingCustomers" class="fas fa-spinner fa-spin mr-2"></i>
              <i v-else class="fas fa-search mr-2"></i>
              Tìm kiếm
            </button>
          </div>
        </div>
        
        <!-- Search Results -->
        <div v-if="searchResults.length > 0" class="max-h-64 overflow-y-auto border border-gray-200 rounded-lg">
          <div 
            v-for="customer in searchResults" 
            :key="customer.id"
            @click="selectCustomer(customer)"
            class="p-4 hover:bg-blue-50 cursor-pointer border-b border-gray-100 last:border-b-0"
          >
            <div class="flex items-center justify-between">
              <div>
                <div class="font-medium text-gray-900">{{ customer.hoTen }}</div>
                <div class="text-sm text-gray-500">
                  {{ customer.soDienThoai }} • {{ customer.email }}
                </div>
                <div v-if="customer.danhSachDiaChi && customer.danhSachDiaChi.length > 0" class="text-xs text-gray-400">
                  {{ customer.danhSachDiaChi.length }} địa chỉ đã lưu
                </div>
              </div>
              <i class="fas fa-chevron-right text-gray-400"></i>
            </div>
          </div>
        </div>
        
        <!-- No Results -->
        <div v-if="customerSearchTerm && searchResults.length === 0 && !searchingCustomers" class="text-center py-8 text-gray-500">
          <i class="fas fa-search text-4xl mb-2"></i>
          <p>Không tìm thấy khách hàng nào</p>
        </div>

        <div class="mt-6 flex justify-end space-x-3">
          <button @click="showCustomerModal = false" class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
            Đóng
          </button>
        </div>
      </div>
    </div>

    <!-- Voucher Selection Modal -->
    <div v-if="showVoucherModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div class="bg-white rounded-lg shadow-xl w-full max-w-2xl max-h-[80vh] flex flex-col">
        <!-- Header -->
        <div class="flex items-center justify-between p-6 border-b border-gray-200">
          <h2 class="text-xl font-semibold text-gray-900">Chọn phiếu giảm giá</h2>
          <button @click="showVoucherModal = false" class="text-gray-400 hover:text-gray-600">
            <i class="fas fa-times text-xl"></i>
          </button>
        </div>

        <!-- Content -->
        <div class="flex-1 overflow-y-auto p-6">
          <!-- Voucher Count -->
          <div v-if="availableVouchers.length > 0" class="mb-4 p-3 bg-blue-50 border border-blue-200 rounded-lg">
            <div class="flex items-center">
              <i class="fas fa-info-circle text-blue-600 mr-2"></i>
              <span class="text-sm text-blue-800">
                Có <strong>{{ availableVouchers.length }}</strong> phiếu giảm giá khả thi
              </span>
            </div>
          </div>

          <!-- Voucher List -->
          <div v-if="availableVouchers.length > 0" class="space-y-3">
            <!-- Public Vouchers -->
            <div v-if="availableVouchers.filter(v => v.loaiApDung === 'PUBLIC').length > 0">
              <div class="flex items-center mb-3">
                <i class="fas fa-gift text-blue-600 mr-2"></i>
                <h3 class="text-sm font-semibold text-gray-700">Ưu đãi dành cho tất cả</h3>
                <span class="ml-2 px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full">
                  {{ availableVouchers.filter(v => v.loaiApDung === 'PUBLIC').length }}
                </span>
              </div>
              
              <div class="space-y-2">
                <div v-for="voucher in availableVouchers.filter(v => v.loaiApDung === 'PUBLIC')" 
                     :key="voucher.id" 
                     @click="selectVoucher(voucher)"
                     class="p-4 border rounded-lg cursor-pointer transition-all"
                     :class="selectedVoucher?.id === voucher.id ? 'border-blue-500 bg-blue-50' : 'border-gray-200 hover:border-blue-300'">
                  
                  <div class="flex items-center">
                    <!-- Radio Button -->
                    <div class="flex-shrink-0 mr-3">
                      <div class="w-5 h-5 rounded-full border-2 flex items-center justify-center"
                           :class="selectedVoucher?.id === voucher.id ? 'border-blue-500 bg-blue-500' : 'border-gray-300'">
                        <div v-if="selectedVoucher?.id === voucher.id" class="w-2 h-2 bg-white rounded-full"></div>
                      </div>
                    </div>
                    
                    <!-- Voucher Info -->
                    <div class="flex-1">
                      <div class="flex items-start justify-between">
                        <div class="flex-1">
                          <h4 class="font-semibold text-gray-900">{{ voucher.tenPhieuGiamGia }}</h4>
                          <p class="text-sm text-gray-500">Mã: {{ voucher.maPhieuGiamGia }}</p>
                          
                          <div class="flex items-center mt-2 space-x-4">
                            <span class="text-sm font-semibold text-green-600">
                              <span v-if="voucher.loaiGiamGia === 'PERCENTAGE' || voucher.loaiPhieu === 'PERCENT'">
                                -{{ voucher.giaTriGiamGia || voucher.soPhanTramGiam || voucher.giaTriGiam || 0 }}%
                              </span>
                              <span v-else>
                                -{{ formatCurrency(voucher.giaTriGiamGia || voucher.giaTriGiam || voucher.soTienGiam || 0) }}
                              </span>
                            </span>
                            <span class="text-xs text-gray-600">Đơn tối thiểu {{ formatCurrency(voucher.donToiThieu || voucher.giaTriDonHangToiThieu || 0) }}</span>
                            <span class="text-xs text-orange-600">HSD: {{ formatDate(voucher.ngayKetThuc) }}</span>
                            <span class="text-xs text-blue-600">Còn lại: {{ voucher.soLuong || 'Không giới hạn' }}</span>
                          </div>
                        </div>
                        
                        <!-- Badge -->
                        <span class="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full ml-2">
                          Dành cho tất cả
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- Personal Vouchers -->
            <div v-if="availableVouchers.filter(v => v.loaiApDung === 'CUSTOMER').length > 0" class="mt-6">
              <div class="flex items-center mb-3">
                <i class="fas fa-star text-amber-500 mr-2"></i>
                <h3 class="text-sm font-semibold text-gray-700">Ưu đãi đặc biệt dành riêng cho bạn</h3>
                <span class="ml-2 px-2 py-1 bg-amber-100 text-amber-800 text-xs rounded-full">
                  {{ availableVouchers.filter(v => v.loaiApDung === 'CUSTOMER').length }}
                </span>
              </div>
              
              <div class="space-y-2">
                <div v-for="voucher in availableVouchers.filter(v => v.loaiApDung === 'CUSTOMER')" 
                     :key="voucher.id" 
                     @click="selectVoucher(voucher)"
                     class="p-4 border rounded-lg cursor-pointer transition-all"
                     :class="selectedVoucher?.id === voucher.id ? 'border-amber-500 bg-amber-50' : 'border-gray-200 hover:border-amber-300'">
                  
                  <div class="flex items-center">
                    <!-- Radio Button -->
                    <div class="flex-shrink-0 mr-3">
                      <div class="w-5 h-5 rounded-full border-2 flex items-center justify-center"
                           :class="selectedVoucher?.id === voucher.id ? 'border-amber-500 bg-amber-500' : 'border-gray-300'">
                        <div v-if="selectedVoucher?.id === voucher.id" class="w-2 h-2 bg-white rounded-full"></div>
                      </div>
                    </div>
                    
                    <!-- Voucher Info -->
                    <div class="flex-1">
                      <div class="flex items-start justify-between">
                        <div class="flex-1">
                          <h4 class="font-semibold text-gray-900">{{ voucher.tenPhieuGiamGia }}</h4>
                          <p class="text-sm text-gray-500">Mã: {{ voucher.maPhieuGiamGia }}</p>
                          
                          <div class="flex items-center mt-2 space-x-4">
                            <span class="text-sm font-semibold text-green-600">
                              <span v-if="voucher.loaiGiamGia === 'PERCENTAGE' || voucher.loaiPhieu === 'PERCENT'">
                                -{{ voucher.giaTriGiamGia || voucher.soPhanTramGiam || voucher.giaTriGiam || 0 }}%
                              </span>
                              <span v-else>
                                -{{ formatCurrency(voucher.giaTriGiamGia || voucher.giaTriGiam || voucher.soTienGiam || 0) }}
                              </span>
                            </span>
                            <span class="text-xs text-gray-600">Đơn tối thiểu {{ formatCurrency(voucher.donToiThieu || voucher.giaTriDonHangToiThieu || 0) }}</span>
                            <span class="text-xs text-orange-600">HSD: {{ formatDate(voucher.ngayKetThuc) }}</span>
                            <span class="text-xs text-blue-600">Còn lại: {{ voucher.soLuong || 'Không giới hạn' }}</span>
                          </div>
                        </div>
                        
                        <!-- Badge -->
                        <span class="px-2 py-1 bg-amber-100 text-amber-800 text-xs rounded-full ml-2">
                          Dành riêng cho bạn
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- No Vouchers -->
          <div v-else class="text-center py-8">
            <div class="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
              <i class="fas fa-gift text-gray-400 text-2xl"></i>
            </div>
            <h3 class="text-lg font-medium text-gray-900 mb-2">Không có phiếu giảm giá</h3>
            <p class="text-gray-500">Hiện tại không có phiếu giảm giá nào khả thi cho đơn hàng của bạn.</p>
          </div>
        </div>

        <!-- Footer -->
        <div class="flex justify-end space-x-3 p-6 border-t border-gray-200">
          <button @click="showVoucherModal = false" 
                  class="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50">
            Hủy
          </button>
          <button @click="applySelectedVoucher" 
                  class="px-4 py-2 bg-orange-600 text-white rounded-lg hover:bg-orange-700">
            Áp dụng phiếu
          </button>
        </div>
      </div>



      <!-- Processing State -->
      <div v-if="currentStep === 2 && paymentStatus === 'processing'" class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- Processing Content -->
        <div class="lg:col-span-2">
          <div class="bg-white rounded-3xl shadow-2xl p-8 border border-gray-100">
            <div class="text-center">
              <!-- Processing Animation -->
              <div class="w-24 h-24 mx-auto mb-6 bg-gradient-to-r from-blue-400 to-blue-600 rounded-full flex items-center justify-center shadow-lg">
                <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-white"></div>
              </div>
              
              <h1 class="text-4xl font-bold text-gray-900 mb-4">⏳ Đang xử lý thanh toán</h1>
              <p class="text-xl text-gray-600 mb-6">Vui lòng chờ trong giây lát...</p>
              
              <!-- Processing Details -->
              <div class="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-2xl p-6 mb-8">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div class="text-center">
                    <div class="text-sm text-gray-500 mb-2">Mã đơn hàng</div>
                    <div class="text-lg font-semibold text-gray-900">{{ orderCode }}</div>
                  </div>
                  <div class="text-center">
                    <div class="text-sm text-gray-500 mb-2">Phương thức thanh toán</div>
                    <div class="text-lg font-semibold text-gray-900">{{ paymentMethod }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Processing Info Sidebar -->
        <div class="lg:col-span-1">
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 sticky top-6">
            <h3 class="text-lg font-semibold text-gray-900 mb-4">Thông tin</h3>
            <div class="space-y-4">
              <div class="flex items-center text-sm text-gray-600">
                <i class="fas fa-info-circle text-blue-500 mr-2"></i>
                <span>Đang xử lý giao dịch</span>
              </div>
              <div class="flex items-center text-sm text-gray-600">
                <i class="fas fa-clock text-blue-500 mr-2"></i>
                <span>Thời gian: 30-60 giây</span>
              </div>
              <div class="flex items-center text-sm text-gray-600">
                <i class="fas fa-shield-alt text-blue-500 mr-2"></i>
                <span>Bảo mật SSL</span>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useRealDataStore } from '../store/realDataStore'
import phieuGiamGiaService from '@/service/phieuGiamGiaService'
import hoaDonService from '@/service/hoaDonService'
import CustomerIntegrationService from '@/service/CustomerIntegrationService'
import AddressService from '@/service/AddressService'

export default {
  name: 'Checkout',
  setup() {
    const route = useRoute()
    const store = useRealDataStore()

    // Checkout state management
    const currentStep = ref(2) // 1: Cart, 2: Payment, 3: Complete
    const paymentStatus = ref('pending') // pending, success, failed
    const orderCode = ref('')
    const deliveryTime = ref('')
    const cancelReason = ref('')

    const customerInfo = ref({
      fullName: '',
      phone: '',
      email: ''
    })

    const ghiChu = ref('')

    const shippingAddress = ref({
      address: '',
      city: '', // province code
      district: '', // district code
      ward: '' // ward code
    })

    // New features
    const showAddressModal = ref(false)
    const showAddressSelection = ref(false)
    const showVoucherModal = ref(false)
    const showCustomerModal = ref(false)
    const savedAddresses = ref([])
    const selectedAddressIndex = ref(-1)
    const availableVouchers = ref([])
    const selectedVoucher = ref(null)
    const editingAddress = ref(null)
    const newAddress = ref({
      fullName: '',
      phone: '',
      address: '',
      city: '',
      district: '',
      ward: '',
      isDefault: false
    })
    const newAddressDistricts = ref([])
    const newAddressWards = ref([])

    // User info from auth
    const isAuthenticated = ref(false)
    const userInfo = ref(null)

    // Customer search features
    const showCustomerSearch = ref(false)
    const customerSearchTerm = ref('')
    const searchResults = ref([])
    const searchingCustomers = ref(false)
    const selectedCustomer = ref(null)

    // Địa chỉ động
    const provinces = ref([])
    const districts = ref([])
    const wards = ref([])
    const loadingProvinces = ref(false)
    const loadingDistricts = ref(false)
    const loadingWards = ref(false)

    // Load provinces from AddressService
    const loadProvinces = async () => {
      if (provinces.value.length > 0) {
        console.log('Provinces already loaded:', provinces.value.length)
        return
      }
      try {
      loadingProvinces.value = true
        console.log('Loading provinces...')
        provinces.value = await AddressService.getProvinces()
        console.log('Provinces loaded:', provinces.value.length)
        console.log('First few provinces:', provinces.value.slice(0, 3))
      } catch (error) {
        console.error('Error loading provinces:', error)
      } finally {
      loadingProvinces.value = false
    }
    }

    // Load districts from AddressService
    const loadDistricts = async (provinceCode) => {
      if (!provinceCode) {
        districts.value = []
        return
      }
      try {
      loadingDistricts.value = true
        console.log('Loading districts for province:', provinceCode)
        districts.value = await AddressService.getDistricts(provinceCode)
        console.log('Districts loaded:', districts.value.length)
        console.log('First few districts:', districts.value.slice(0, 3))
      } catch (error) {
        console.error('Error loading districts:', error)
      } finally {
      loadingDistricts.value = false
      }
    }

    // Load wards from AddressService
    const loadWards = async (districtCode) => {
      if (!districtCode) {
      wards.value = []
        return
    }
      try {
      loadingWards.value = true
        console.log('Loading wards for district:', districtCode)
        wards.value = await AddressService.getWards(districtCode)
        console.log('Wards loaded:', wards.value.length)
        console.log('First few wards:', wards.value.slice(0, 3))
      } catch (error) {
        console.error('Error loading wards:', error)
      } finally {
      loadingWards.value = false
      }
    }

    // Open address modal and load provinces
    const openAddressModal = async () => {
      showAddressModal.value = true
      await loadProvinces()
    }

    // Open voucher modal and reload vouchers
    const openVoucherModal = async () => {
      showVoucherModal.value = true
      await loadAvailableVouchers()
    }


    watch(() => shippingAddress.value.city, (val) => {
      shippingAddress.value.district = ''
      shippingAddress.value.ward = ''
      loadDistricts(val)
    })
    watch(() => shippingAddress.value.district, (val) => {
      shippingAddress.value.ward = ''
      loadWards(val)
    })

    const paymentMethod = ref('cod')
    const selectedDeliveryMethod = ref('standard')
    
    // Xác định mode và sử dụng đúng giỏ hàng
    const isBuyNowMode = computed(() => route.query.mode === 'buynow')
    
    // Khôi phục buyNowCart từ localStorage nếu cần
    onMounted(() => {
      console.log('🚀 Checkout mounted, isBuyNowMode:', isBuyNowMode.value)
      console.log('🚀 Current buyNowCart:', store.buyNowCart.value)
      console.log('🚀 Current normal cart:', store.cart.value)
      
      if (isBuyNowMode.value) {
        const backup = localStorage.getItem('buyNowCartBackup')
        console.log('🚀 buyNowCartBackup from localStorage:', backup)
        
        if (backup && store.buyNowCart.value.length === 0) {
          try {
            const cartData = JSON.parse(backup)
            store.buyNowCart.value.splice(0, store.buyNowCart.value.length, ...cartData)
            console.log('🔄 Restored buyNowCart from localStorage:', cartData)
          } catch (error) {
            console.error('❌ Error restoring buyNowCart:', error)
          }
        }
      }
    })
    
    const cartItems = computed(() => {
      const items = isBuyNowMode.value ? store.buyNowCart.value : store.cart.value
      console.log('🛒 Checkout cartItems:', {
        mode: isBuyNowMode.value ? 'buynow' : 'normal',
        items: items,
        buyNowCart: store.buyNowCart.value,
        normalCart: store.cart.value
      })
      return items
    })
    const cartTotal = computed(() => isBuyNowMode.value ? store.buyNowCartTotal.value : store.cartTotal.value)

    // Base shipping fee calculation
    const baseShippingFee = computed(() => {
      const cityCode = shippingAddress.value.city
      console.log('Calculating base shipping fee for cityCode:', cityCode)
      
      if (!cityCode) {
        console.log('No city code, returning 0')
        return 0
      }
      
      // Try to find city by code first, then by name
      let city = provinces.value.find(p => p.code == cityCode)
      if (!city) {
        city = provinces.value.find(p => p.name === cityCode)
      }
      
      console.log('Found city:', city)
      
      if (!city) {
        console.log('City not found in provinces, returning 0')
        return 0
      }
      
      let baseFee = 0
      if (city.name.includes('Hồ Chí Minh') || city.name.includes('Hà Nội')) {
        baseFee = 20000
      } else if (city.name.includes('Đà Nẵng')) {
        baseFee = 30000
      } else {
        baseFee = 40000
      }
      
      console.log('Base fee calculated:', baseFee)
      return baseFee
    })

    // Standard shipping fee
    const standardShippingFee = computed(() => {
      return baseShippingFee.value
    })

    // Express shipping fee
    const expressShippingFee = computed(() => {
      return baseShippingFee.value * 1.5
    })

    // Current shipping fee based on selected method
    const shippingFee = computed(() => {
      console.log('Current delivery method:', selectedDeliveryMethod.value)
      if (selectedDeliveryMethod.value === 'express') {
        console.log('Express delivery, final fee:', expressShippingFee.value)
        return expressShippingFee.value
      }
      console.log('Standard delivery, final fee:', standardShippingFee.value)
      return standardShippingFee.value
    })
    const shippingFeeLoading = ref(false)
    const shippingFeeError = ref('')
    const shippingServices = ref([])
    const selectedServiceIndex = ref(0)

    // Discount logic
    const discountCode = ref('')
    const discountPercent = ref(0)
    const discountError = ref('')
    const discountInfo = ref(null)
    const discountAmount = computed(() => {
      if (discountInfo.value) {
        if (discountInfo.value.loaiPhieu === 'PERCENT') {
          return Math.round(cartTotal.value * discountInfo.value.soPhanTramGiam / 100)
        } else if (discountInfo.value.loaiPhieu === 'AMOUNT') {
          return Math.min(discountInfo.value.giaTriGiam, cartTotal.value)
        }
      }
      return 0
    })
    const finalTotal = computed(() => {
      return cartTotal.value - discountAmount.value + shippingFee.value
    })

    // Progress steps computed properties
    const pageTitle = computed(() => {
      if (currentStep.value === 2 && paymentStatus.value === 'pending') return 'Thanh toán'
      if (currentStep.value === 2 && paymentStatus.value === 'processing') return 'Đang xử lý thanh toán'
      if (currentStep.value === 3 && paymentStatus.value === 'success') return 'Đặt hàng thành công'
      if (currentStep.value === 2 && paymentStatus.value === 'failed') return 'Thanh toán thất bại'
      return 'Thanh toán'
    })

    const step1Class = computed(() => {
      if (currentStep.value >= 1) return 'bg-green-500 text-white'
      return 'bg-gray-300 text-gray-600'
    })

    const step1TextClass = computed(() => {
      if (currentStep.value >= 1) return 'text-green-600'
      return 'text-gray-500'
    })

    const step2Class = computed(() => {
      if ((currentStep.value === 2 && paymentStatus.value === 'success') || (currentStep.value === 3 && paymentStatus.value === 'success')) return 'bg-green-500 text-white'
      if (currentStep.value === 2 && paymentStatus.value === 'failed') return 'bg-red-500 text-white'
      if (currentStep.value === 2 && paymentStatus.value === 'processing') return 'bg-yellow-500 text-white'
      if (currentStep.value === 2) return 'bg-blue-500 text-white'
      return 'bg-gray-300 text-gray-600'
    })

    const step2TextClass = computed(() => {
      if ((currentStep.value === 2 && paymentStatus.value === 'success') || (currentStep.value === 3 && paymentStatus.value === 'success')) return 'text-green-600'
      if (currentStep.value === 2 && paymentStatus.value === 'failed') return 'text-red-600'
      if (currentStep.value === 2 && paymentStatus.value === 'processing') return 'text-yellow-600'
      if (currentStep.value === 2) return 'text-blue-600'
      return 'text-gray-500'
    })

    const step3Class = computed(() => {
      if (currentStep.value === 3 && paymentStatus.value === 'success') return 'bg-green-500 text-white'
      if (currentStep.value === 3 && paymentStatus.value === 'failed') return 'bg-red-500 text-white'
      return 'bg-gray-300 text-gray-600'
    })

    const step3TextClass = computed(() => {
      if (currentStep.value === 3 && paymentStatus.value === 'success') return 'text-green-600'
      if (currentStep.value === 3 && paymentStatus.value === 'failed') return 'text-red-600'
      return 'text-gray-500'
    })

    async function applyDiscount() {
      const code = discountCode.value.trim().toUpperCase()
      discountError.value = ''
      discountInfo.value = null
      if (!code) {
        discountError.value = ''
        return
      }
      try {
        const res = await phieuGiamGiaService.getAll({ searchQuery: code, trangThai: true })
        const list = res.data.content || res.data
        if (!list.length) {
          discountError.value = 'Mã giảm giá không hợp lệ hoặc đã hết hạn!'
          return
        }
        const voucher = list[0]
        // Kiểm tra ngày hiệu lực
        const now = new Date()
        if (voucher.ngayBatDau && new Date(voucher.ngayBatDau) > now) {
          discountError.value = 'Mã giảm giá chưa bắt đầu!'
          return
        }
        if (voucher.ngayKetThuc && new Date(voucher.ngayKetThuc) < now) {
          discountError.value = 'Mã giảm giá đã hết hạn!'
          return
        }
        if (voucher.soLuong !== null && voucher.soLuong <= 0) {
          discountError.value = 'Mã giảm giá đã hết lượt sử dụng!'
          return
        }
        if (voucher.giaTriDonHangToiThieu && cartTotal.value < voucher.giaTriDonHangToiThieu) {
          discountError.value = `Đơn hàng phải từ ${formatPrice(voucher.giaTriDonHangToiThieu)} mới được áp dụng mã này!`
          return
        }
        discountInfo.value = voucher
        discountError.value = ''
      } catch (e) {
        discountError.value = 'Có lỗi khi kiểm tra mã giảm giá!'
      }
    }

    const isFormValid = computed(() => {
      // Kiểm tra thông tin khách hàng
      const customerValid = customerInfo.value.fullName.trim() !== '' &&
             customerInfo.value.phone.trim() !== '' &&
             /^[0-9]{10,11}$/.test(customerInfo.value.phone.replace(/\s/g, '')) &&
                           customerInfo.value.email.trim() !== ''
      
      // Kiểm tra địa chỉ
      let addressValid = false
      if (isAuthenticated.value) {
        // User đã đăng nhập: kiểm tra địa chỉ đã chọn
        addressValid = selectedAddressIndex.value >= 0 && savedAddresses.value[selectedAddressIndex.value]
      } else {
        // User chưa đăng nhập: kiểm tra địa chỉ nhập thủ công
        addressValid = shippingAddress.value.address.trim() !== '' &&
             shippingAddress.value.city !== '' &&
             shippingAddress.value.district !== '' &&
             shippingAddress.value.ward !== ''
      }
      
      return customerValid && addressValid
    })

    const formatPrice = (price) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(price)
    }

    const formatCurrency = (amount) => {
      return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
      }).format(amount)
    }

    // Quantity control functions
    const increaseQuantity = (productId) => {
      const item = cartItems.value.find(item => item.id === productId)
      if (item) {
        // Kiểm tra số lượng tồn kho
        if (item.stockQuantity && item.quantity >= item.stockQuantity) {
          alert(`Số lượng tồn kho chỉ còn ${item.stockQuantity} sản phẩm. Không thể thêm nữa!`)
          return
        }
        // Sử dụng đúng hàm dựa trên mode
        if (isBuyNowMode.value) {
          store.updateBuyNowCartQuantity({ productId: productId, quantity: item.quantity + 1 })
        } else {
          store.updateCartQuantity({ productId: productId, quantity: item.quantity + 1 })
        }
      }
    }

    const decreaseQuantity = (productId) => {
      const item = cartItems.value.find(item => item.id === productId)
      if (item && item.quantity > 1) {
        // Sử dụng đúng hàm dựa trên mode
        if (isBuyNowMode.value) {
          store.updateBuyNowCartQuantity({ productId: productId, quantity: item.quantity - 1 })
        } else {
          store.updateCartQuantity({ productId: productId, quantity: item.quantity - 1 })
        }
      }
    }

    const formatDate = (dateString) => {
      return new Date(dateString).toLocaleDateString('vi-VN')
    }

    const placeOrder = async () => {
      if (!isFormValid.value) return
      
      // Chuyển sang trạng thái processing
      paymentStatus.value = 'processing'
      
      try {
        // 1. Tạo hóa đơn mới (hoặc sử dụng hóa đơn đã tạo cho chuyển khoản)
        let hoaDonId
        if (createdHoaDonId.value) {
          // Đã tạo hóa đơn trước đó (cho chuyển khoản)
          hoaDonId = createdHoaDonId.value
        } else {
          // Tạo hóa đơn mới (cho COD)
          const res = await hoaDonService.createHoaDon({ loaiHoaDon: 'Online' })
          const hoaDon = res.data
          hoaDonId = hoaDon.id
          orderCode.value = hoaDon.maHoaDon // Lưu mã đơn hàng để dùng cho nội dung chuyển khoản
        }
        // 2. Thêm sản phẩm vào hóa đơn
        let hasValidItems = false
        console.log('🛒 Processing cartItems for order:', cartItems.value)
        console.log('🛒 isBuyNowMode:', isBuyNowMode.value)
        
        // Validate cartItems trước khi xử lý
        if (!cartItems.value || cartItems.value.length === 0) {
          throw new Error('Không có sản phẩm nào trong giỏ hàng')
        }
        
        for (const item of cartItems.value) {
          console.log('🛒 Processing item:', item.title, 'Full item data:', item)
          
          // Validate item data
          if (!item.id) {
            console.error('❌ Item missing ID:', item)
            throw new Error(`Sản phẩm "${item.title || 'Unknown'}" thiếu thông tin ID`)
          }
          
          if (!item.quantity || item.quantity <= 0) {
            console.error('❌ Item invalid quantity:', item)
            throw new Error(`Sản phẩm "${item.title || 'Unknown'}" có số lượng không hợp lệ`)
          }
          
          let chiTietSanPhamId = item.id // Fallback
          
          if (item.category === 'Bộ sách') {
            // Xử lý riêng cho bộ sách - sử dụng endpoint riêng
            console.log('🔍 Processing BoSach item:', item)
            try {
              const BoSachService = (await import('@/service/BoSachService')).default
              console.log('🔍 Calling BoSachService.getById with id:', item.id)
              const boSach = await BoSachService.getById(item.id)
              console.log('🔍 BoSach response:', boSach)
              console.log('🔍 BoSach soLuong:', boSach?.soLuong, 'Item quantity:', item.quantity)
              if (boSach && boSach.soLuong >= item.quantity) {
                // Sử dụng endpoint riêng cho bộ sách
                const productPayload = {
                  boSachId: item.id,
                  soLuong: item.quantity,
                  giaSanPham: item.price
                }
                
                console.log('🛒 Adding BoSach to order:', productPayload)
                try {
                  await hoaDonService.addBoSachToOrder(hoaDonId, productPayload)
                  console.log('✅ addBoSachToOrder thành công cho item:', item.title)
                  hasValidItems = true
                  continue // Bỏ qua logic xử lý chiTietSanPhamId
                } catch (error) {
                  console.error('❌ addBoSachToOrder lỗi cho item:', item.title)
                  console.error('Payload:', productPayload)
                  console.error('Error:', error.response?.data || error.message)
                  
                  // Nếu có lỗi, hủy hóa đơn và dừng toàn bộ quá trình
                  try {
                    await hoaDonService.huyDon(hoaDonId)
                    console.log('✅ Đã hủy hóa đơn do lỗi thêm bộ sách')
                  } catch (cancelError) {
                    console.error('❌ Lỗi khi hủy hóa đơn:', cancelError)
                  }
                  
                  // Reset các biến
                  orderCode.value = ''
                  createdHoaDonId.value = null
                  
                  throw new Error(`Không thể thêm bộ sách "${item.title}" vào đơn hàng. Lỗi: ${error.response?.data?.message || error.message}`)
                }
              } else {
                console.warn('⚠️ Bỏ qua bộ sách:', item.title, '- không đủ số lượng')
                alert(`Bộ sách "${item.title}" không đủ số lượng và sẽ được bỏ qua khỏi đơn hàng.`)
                continue
              }
            } catch (error) {
              console.error('Lỗi khi lấy chi tiết bộ sách:', error)
              continue
            }
          } else {
            // Xử lý cho sản phẩm thường
            console.log('DEBUG: Processing regular product:', item.title, 'productDetailItems:', item.productDetailItems)
            if (item.productDetailItems && item.productDetailItems.length > 0) {
              const availableItem = item.productDetailItems.find(detail => detail.soLuongTon > 0)
              if (availableItem) {
                chiTietSanPhamId = availableItem.id
                hasValidItems = true
                console.log('DEBUG: Found available item with ID:', availableItem.id, 'soLuongTon:', availableItem.soLuongTon)
              } else {
                // Nếu không có chi tiết nào có tồn kho, bỏ qua item này
                console.warn('⚠️ Bỏ qua item:', item.title, '- không có tồn kho')
                alert(`Sản phẩm "${item.title}" đã hết hàng và sẽ được bỏ qua khỏi đơn hàng.`)
                continue
              }
            } else {
              // Nếu không có productDetailItems, cần kiểm tra tồn kho trước
              console.log('DEBUG: No productDetailItems, checking stock for item.id:', item.id)
              console.log('DEBUG: Item details:', item)
              
              // Fallback: sử dụng item.id trực tiếp nếu không có productDetailItems
              if (isBuyNowMode.value) {
                console.log('🛒 BuyNow mode: Using item.id directly as chiTietSanPhamId:', item.id)
                chiTietSanPhamId = item.id
                hasValidItems = true
              } else {
              try {
                // Gọi API để kiểm tra tồn kho
                const ChiTietSanPhamService = (await import('@/service/ChiTietSanPhamService')).default
                const chiTietSanPham = await ChiTietSanPhamService.getChiTietSanPhamById(item.id)
                if (chiTietSanPham && chiTietSanPham.soLuongTon > 0) {
                  chiTietSanPhamId = item.id
                  hasValidItems = true
                  console.log('DEBUG: Item has stock, soLuongTon:', chiTietSanPham.soLuongTon)
                } else {
                  console.warn('⚠️ Bỏ qua item:', item.title, '- không có tồn kho (soLuongTon:', chiTietSanPham?.soLuongTon || 0, ')')
                  alert(`Sản phẩm "${item.title}" đã hết hàng và sẽ được bỏ qua khỏi đơn hàng.`)
                  continue
                }
              } catch (error) {
                console.error('Lỗi khi kiểm tra tồn kho:', error)
                continue
                }
              }
            }
          }
          
          // Validate price
          if (!item.price || item.price <= 0) {
            console.error('❌ Item invalid price:', item)
            throw new Error(`Sản phẩm "${item.title || 'Unknown'}" có giá không hợp lệ`)
          }
          
          const productPayload = {
            chiTietSanPhamId: chiTietSanPhamId,
            soLuong: item.quantity,
            giaSanPham: item.price
          }
          
          console.log('🛒 Adding product to order:', productPayload)
          console.log('addProductToOrder payload:', productPayload)
          console.log('item object:', item)
          try {
            await hoaDonService.addProductToOrder(hoaDonId, productPayload)
            console.log('✅ addProductToOrder thành công cho item:', item.title)
          } catch (error) {
            console.error('❌ addProductToOrder lỗi cho item:', item.title)
            console.error('Payload:', productPayload)
            console.error('Error:', error.response?.data || error.message)
            console.error('Error Response:', error.response)
            console.error('Error Status:', error.response?.status)
            
            // Nếu có lỗi, hủy hóa đơn và dừng toàn bộ quá trình
            try {
              await hoaDonService.huyDon(hoaDonId)
              console.log('✅ Đã hủy hóa đơn do lỗi thêm sản phẩm')
            } catch (cancelError) {
              console.error('❌ Lỗi khi hủy hóa đơn:', cancelError)
            }
            
            // Reset các biến
            orderCode.value = ''
            createdHoaDonId.value = null
            
            throw new Error(`Không thể thêm sản phẩm "${item.title}" vào đơn hàng. Lỗi: ${error.response?.data?.message || error.message}`)
          }
        }
        
        // Kiểm tra nếu không có items nào hợp lệ
        if (!hasValidItems) {
          alert('Tất cả sản phẩm trong giỏ hàng đều đã hết hàng. Không thể đặt đơn hàng.')
          return
        }
        // 3. Cập nhật thông tin khách nhận
        console.log('🏠 Building address for COD order:')
        console.log('🏠 shippingAddress.value:', shippingAddress.value)
        console.log('🏠 address:', shippingAddress.value.address)
        console.log('🏠 city:', shippingAddress.value.city)
        console.log('🏠 district:', shippingAddress.value.district)
        console.log('🏠 ward:', shippingAddress.value.ward)
        
        // Đảm bảo provinces được load trước khi tạo địa chỉ
        console.log('🏠 Current provinces.length:', provinces.value.length)
        if (provinces.value.length === 0) {
          console.log('🏠 Loading provinces before building address...')
          await loadProvinces()
          console.log('🏠 After loading provinces.length:', provinces.value.length)
        }
        
        // Load districts nếu có city
        console.log('🏠 Current districts.length:', districts.value.length)
        if (shippingAddress.value.city && districts.value.length === 0) {
          console.log('🏠 Loading districts for city:', shippingAddress.value.city)
          await loadDistricts(shippingAddress.value.city)
          console.log('🏠 After loading districts.length:', districts.value.length)
        }
        
        // Load wards nếu có district
        console.log('🏠 Current wards.length:', wards.value.length)
        if (shippingAddress.value.district && wards.value.length === 0) {
          console.log('🏠 Loading wards for district:', shippingAddress.value.district)
          await loadWards(shippingAddress.value.district)
          console.log('🏠 After loading wards.length:', wards.value.length)
        }
        
        console.log('🏠 ward:', getWardName())
        console.log('🏠 district:', getDistrictName())
        console.log('🏠 province:', getProvinceName())
        
        // Kiểm tra xem có địa chỉ nào được chọn không
        console.log('🏠 selectedAddressIndex:', selectedAddressIndex.value)
        console.log('🏠 savedAddresses.length:', savedAddresses.value.length)
        if (selectedAddressIndex.value >= 0 && savedAddresses.value[selectedAddressIndex.value]) {
          console.log('🏠 Selected address:', savedAddresses.value[selectedAddressIndex.value])
        }
        
        const customerUpdateData = {
          tenNguoiNhan: customerInfo.value.fullName,
          soDienThoaiNguoiNhan: customerInfo.value.phone,
          emailNguoiNhan: customerInfo.value.email, // Thêm email vào customerUpdateData ban đầu
          diaChi: `${shippingAddress.value.address}, ${getWardName()}, ${getDistrictName()}, ${getProvinceName()}`
        }
        
        console.log('🛒 Initial customerUpdateData:', customerUpdateData)
        console.log('📧 DEBUG: customerInfo.value.email:', customerInfo.value.email)
        console.log('🚨 DEBUG: Address components - city:', shippingAddress.value.city, 'district:', shippingAddress.value.district, 'ward:', shippingAddress.value.ward)
        console.log('🚨 DEBUG: selectedAddressIndex:', selectedAddressIndex.value, 'savedAddresses.length:', savedAddresses.value.length)
        if (selectedAddressIndex.value >= 0 && savedAddresses.value[selectedAddressIndex.value]) {
          console.log('🚨 DEBUG: Selected address from list:', savedAddresses.value[selectedAddressIndex.value])
        } else {
          console.log('🚨 DEBUG: No address selected from list, using manual input')
        }

        // Xử lý thông tin khách hàng
        console.log('🛒 Processing customer info:')
        console.log('🛒 selectedCustomer:', selectedCustomer.value)
        console.log('🛒 isAuthenticated:', isAuthenticated.value)
        console.log('🛒 userInfo:', userInfo.value)
        console.log('🛒 userInfo type:', typeof userInfo.value)
        console.log('🛒 userInfo keys:', userInfo.value ? Object.keys(userInfo.value) : 'null')
        
        if (selectedCustomer.value) {
          // Sử dụng khách hàng đã chọn
          console.log('🛒 Using selected customer path')
          customerUpdateData.idKhachHang = selectedCustomer.value.id  // Sửa từ khachHangId thành idKhachHang
          // emailNguoiNhan đã được set ở trên, không cần ghi đè
          console.log('🛒 Using selected customer:', selectedCustomer.value.id)
        } else if (isAuthenticated.value && userInfo.value) {
          console.log('🛒 Using authenticated user path')
          // Nếu đã đăng nhập, KHÔNG tạo khách hàng mới, sử dụng ID từ user info
          console.log('🛒 Using authenticated user for order:', userInfo.value)
          console.log('🛒 User khachHangId:', userInfo.value.khachHangId)
          console.log('🛒 User id:', userInfo.value.id)
          
          // Ưu tiên khachHangId, nếu không có thì dùng id
          const customerId = userInfo.value.khachHangId || userInfo.value.id
          
          if (customerId) {
            customerUpdateData.idKhachHang = customerId  // Sửa từ khachHangId thành idKhachHang
            // Ưu tiên email từ userInfo, nếu không có thì dùng email từ form
            customerUpdateData.emailNguoiNhan = userInfo.value.email || customerInfo.value.email
            console.log('🛒 Set idKhachHang for authenticated user:', customerId)
            console.log('🛒 Customer email:', customerUpdateData.emailNguoiNhan)
          } else {
            console.error('❌ No customer ID found in user info:', userInfo.value)
            throw new Error('Không thể xác định thông tin khách hàng. Vui lòng đăng nhập lại.')
          }
        } else {
          console.log('🛒 User not authenticated, creating new customer')
          console.log('🛒 isAuthenticated.value:', isAuthenticated.value)
          console.log('🛒 userInfo.value:', userInfo.value)
          // Khách hàng chưa đăng nhập, tạo khách hàng mới
          try {
            console.log('🛒 Creating new customer with data:', customerInfo.value)
            const newCustomer = await CustomerIntegrationService.createCustomerFromCheckout(
              customerInfo.value,
              shippingAddress.value
            )
            customerUpdateData.idKhachHang = newCustomer.id  // Sửa từ khachHangId thành idKhachHang
            // emailNguoiNhan đã được set ở trên, không cần ghi đè
            selectedCustomer.value = newCustomer
            console.log('🛒 Created new customer with ID:', newCustomer.id)
          } catch (error) {
            console.warn('❌ Không thể tạo khách hàng mới:', error)
            // Tiếp tục đặt hàng mà không liên kết khách hàng
          }
        }

        console.log('🛒 Final customerUpdateData before API call:', customerUpdateData)
        console.log('🛒 idKhachHang in payload:', customerUpdateData.idKhachHang)
        console.log('🛒 API endpoint: PUT /api/hoa-don/' + hoaDonId + '/cap-nhat-thong-tin-khach-hang')
        
        try {
          console.log('🛒 About to call updateCustomerInfo API...')
          const response = await hoaDonService.updateCustomerInfo(hoaDonId, customerUpdateData)
          console.log('✅ Customer info updated successfully for order:', hoaDonId)
          console.log('✅ API response:', response.data)
        } catch (error) {
          console.error('❌ Error updating customer info:', error)
          console.error('❌ Error response:', error.response?.data)
          console.error('❌ Error status:', error.response?.status)
          console.error('❌ Error message:', error.message)
          throw error
        }
        // 3.5. Cập nhật phí ship và tổng tiền
        await hoaDonService.updateHoaDon(hoaDonId, {
          phiShip: shippingFee.value,
          tongTien: finalTotal.value
        })
        // 4. Cập nhật thanh toán
        const paymentPayload = {
          tienMat: paymentMethod.value === 'cod' ? finalTotal.value : 0,
          chuyenKhoan: paymentMethod.value === 'bank' ? finalTotal.value : 0,
          phuongThucThanhToanId: paymentMethod.value === 'cod' ? 4 : 1, // 4: Tiền mặt (COD), 1: Chuyển khoản (Bank)
          ghiChu: ghiChu.value || '',
          loaiHoaDon: 'Online',
          tienKhachDua: paymentMethod.value === 'cod' ? finalTotal.value : 0,
          phieuGiamGiaId: discountInfo.value ? discountInfo.value.id : null,
          phuongThucThanhToan: paymentMethod.value === 'cod' ? 'COD' : 'VNPAY'
        }
        await hoaDonService.updatePayment(hoaDonId, paymentPayload)
        
        // 5. Cập nhật ngày đặt hàng
        const updateData = {
          ngayDatHang: new Date().toISOString()
        }
        await hoaDonService.updateHoaDon(hoaDonId, updateData)
        // 6. Clear cart và chuyển sang Step 3 (Success)
        // Chỉ clear buyNowCart nếu đang ở BuyNow mode, giữ nguyên cart chính
        if (isBuyNowMode.value) {
          store.clearBuyNowCart()
          localStorage.removeItem('buyNowCartBackup')
          console.log('🧹 Cleared buyNowCart after successful order')
        } else {
        store.clearCart()
          console.log('🧹 Cleared main cart after successful order')
        }
        
        // Cập nhật thông tin đơn hàng
        paymentMethod.value = paymentMethod.value === 'bank' ? 'VNPAY (Thanh toán online)' : 'COD (Thanh toán khi nhận hàng)'
        deliveryTime.value = selectedDeliveryMethod.value === 'express' ? '1-2 ngày làm việc' : '2-5 ngày làm việc'
        
        // Lưu thông tin đơn hàng vào localStorage
        localStorage.setItem('lastOrderCode', orderCode.value)
        localStorage.setItem('lastPaymentMethod', paymentMethod.value)
        localStorage.setItem('lastDeliveryTime', deliveryTime.value)
        
        console.log('🎉 COD Order placed successfully:', { 
          orderCode: orderCode.value, 
          paymentMethod: paymentMethod.value,
          deliveryTime: deliveryTime.value
        })
        
        // Redirect đến trang thành công
        // window.location.href = `/order-success?orderCode=${orderCode.value}&paymentMethod=${paymentMethod.value}&deliveryTime=${deliveryTime.value}`
        
        // Redirect ngay lập tức đến trang thành công
        window.location.href = `/order-success?orderCode=${orderCode.value}&paymentMethod=${paymentMethod.value}&deliveryTime=${deliveryTime.value}`
      } catch (e) {
        // Lưu thông tin lỗi vào localStorage
        localStorage.setItem('lastOrderCode', orderCode.value || 'HD001')
        localStorage.setItem('lastErrorMessage', 'Có lỗi khi đặt hàng: ' + (e.response?.data?.message || e.message))
        
        console.error('❌ COD Order failed:', e)
        console.error('❌ Error details:', {
          message: e.message,
          response: e.response?.data,
          status: e.response?.status,
          cartItems: cartItems.value,
          isBuyNowMode: isBuyNowMode.value
        })
        
        // Clear buyNowCart nếu có lỗi trong BuyNow mode
        if (isBuyNowMode.value) {
          store.clearBuyNowCart()
          localStorage.removeItem('buyNowCartBackup')
        }
        
        // Redirect đến trang lỗi
        window.location.href = `/payment-error?orderCode=${orderCode.value || 'HD001'}&errorMessage=${encodeURIComponent('Có lỗi khi đặt hàng: ' + (e.response?.data?.message || e.message))}`
      }
    }

    const createdHoaDonId = ref(null) // Lưu ID hóa đơn đã tạo (cho chuyển khoản)

    // Tạo hóa đơn mới sau khi VNPAY thanh toán thành công
    const createPaidOrder = async (tempOrderData) => {
      console.log('🏗️ Creating paid order with temp data:', tempOrderData)
      try {
        // 1. Tạo hóa đơn mới với trạng thái đã thanh toán
        const res = await hoaDonService.createHoaDon({ loaiHoaDon: 'Online' })
        const hoaDon = res.data
        const hoaDonId = hoaDon.id
        const realOrderCode = hoaDon.maHoaDon

        // 2. Thêm sản phẩm vào hóa đơn
        let hasValidItems = false
        for (const item of tempOrderData.cartItems) {
          if (item.category === 'Bộ sách') {
            // Xử lý bộ sách với endpoint riêng
            try {
              const BoSachService = (await import('@/service/BoSachService')).default
              const boSach = await BoSachService.getById(item.id)
              if (boSach && boSach.soLuong >= item.quantity) {
                await hoaDonService.addBoSachToOrder(hoaDonId, {
                  boSachId: item.id,
                  soLuong: item.quantity,
                  giaSanPham: item.price
                })
                hasValidItems = true
              }
            } catch (_) { 
              // bỏ qua item lỗi
            }
          } else {
            // Xử lý sản phẩm thường
            let chiTietSanPhamId = item.id
            if (item.productDetailItems && item.productDetailItems.length > 0) {
              const availableItem = item.productDetailItems.find(detail => detail.soLuongTon > 0)
              if (availableItem) { 
                chiTietSanPhamId = availableItem.id
                hasValidItems = true 
              } else { 
                continue 
              }
            }
            try {
              await hoaDonService.addProductToOrder(hoaDonId, {
                chiTietSanPhamId,
                soLuong: item.quantity,
                giaSanPham: item.price
              })
              hasValidItems = true
            } catch (_) {
              // bỏ qua item lỗi
            }
          }
        }
        if (!hasValidItems) {
          // Hủy hóa đơn nếu không có sản phẩm hợp lệ
          await hoaDonService.huyDon(hoaDonId)
          return
        }

        // 3. Cập nhật thông tin khách/địa chỉ
        console.log('🏠 Building address for VNPAY order:')
        console.log('🏠 tempOrderData.shippingAddress:', tempOrderData.shippingAddress)
        console.log('🏠 address:', tempOrderData.shippingAddress.address)
        
        // Đảm bảo provinces được load trước khi tạo địa chỉ
        if (provinces.value.length === 0) {
          console.log('🏠 Loading provinces before building address...')
          await loadProvinces()
        }
        
        // Load districts nếu có city
        if (tempOrderData.shippingAddress.city && districts.value.length === 0) {
          console.log('🏠 Loading districts for city:', tempOrderData.shippingAddress.city)
          await loadDistricts(tempOrderData.shippingAddress.city)
        }
        
        // Load wards nếu có district
        if (tempOrderData.shippingAddress.district && wards.value.length === 0) {
          console.log('🏠 Loading wards for district:', tempOrderData.shippingAddress.district)
          await loadWards(tempOrderData.shippingAddress.district)
        }
        
        console.log('🏠 ward:', getWardName(tempOrderData.shippingAddress.ward))
        console.log('🏠 district:', getDistrictName(tempOrderData.shippingAddress.district))
        console.log('🏠 province:', getProvinceName(tempOrderData.shippingAddress.city))
        
        const fullAddress = [
          tempOrderData.shippingAddress.address,
          getWardName(tempOrderData.shippingAddress.ward), 
          getDistrictName(tempOrderData.shippingAddress.district), 
          getProvinceName(tempOrderData.shippingAddress.city)
        ].filter(Boolean).join(', ')
        
        console.log('🏠 Final fullAddress:', fullAddress)
        
        const customerUpdateData = {
          tenNguoiNhan: tempOrderData.customerInfo.fullName || 'Khách lẻ',
          soDienThoaiNguoiNhan: tempOrderData.customerInfo.phone || '',
          emailNguoiNhan: tempOrderData.customerInfo.email, // Thêm email vào customerUpdateData ban đầu
          diaChi: fullAddress
        }

        // Xử lý thông tin khách hàng cho VNPAY
        if (tempOrderData.selectedCustomer) {
          // Sử dụng khách hàng đã chọn
          customerUpdateData.idKhachHang = tempOrderData.selectedCustomer.id
          // emailNguoiNhan đã được set ở trên, không cần ghi đè
          console.log('🛒 VNPAY: Using selected customer:', tempOrderData.selectedCustomer.id)
        } else if (tempOrderData.userInfo) {
          // Sử dụng thông tin user từ tempOrderData - KHÔNG tạo khách hàng mới
          console.log('🛒 VNPAY: Using user info from tempOrderData:', tempOrderData.userInfo)
          console.log('🛒 VNPAY: User khachHangId:', tempOrderData.userInfo.khachHangId)
          console.log('🛒 VNPAY: User id:', tempOrderData.userInfo.id)
          
          // Ưu tiên khachHangId, nếu không có thì dùng id
          const customerId = tempOrderData.userInfo.khachHangId || tempOrderData.userInfo.id
          
          if (customerId) {
            customerUpdateData.idKhachHang = customerId
            // Ưu tiên email từ userInfo, nếu không có thì dùng email từ form
            customerUpdateData.emailNguoiNhan = tempOrderData.userInfo.email || tempOrderData.customerInfo.email
            console.log('🛒 VNPAY: Set khachHangId for authenticated user:', customerId)
            console.log('🛒 VNPAY: Customer email:', customerUpdateData.emailNguoiNhan)
          } else {
            console.error('❌ VNPAY: No customer ID found in user info:', tempOrderData.userInfo)
            throw new Error('Không thể xác định thông tin khách hàng. Vui lòng đăng nhập lại.')
          }
        } else {
          // Tự động tạo khách hàng mới nếu chưa có
          try {
            const newCustomer = await CustomerIntegrationService.createCustomerFromCheckout(
              tempOrderData.customerInfo,
              tempOrderData.shippingAddress
            )
            customerUpdateData.idKhachHang = newCustomer.id
            // emailNguoiNhan đã được set ở trên, không cần ghi đè
            console.log('🛒 VNPAY: Created new customer:', newCustomer.id)
          } catch (error) {
            console.warn('Không thể tạo khách hàng mới:', error)
            // Tiếp tục đặt hàng mà không liên kết khách hàng
          }
        }

        console.log('🛒 VNPAY: Final customerUpdateData before API call:', customerUpdateData)
        console.log('🛒 VNPAY: idKhachHang in payload:', customerUpdateData.idKhachHang)
        console.log('🛒 VNPAY: API endpoint: PUT /api/hoa-don/' + hoaDonId + '/cap-nhat-thong-tin-khach-hang')
        
        try {
          const response = await hoaDonService.updateCustomerInfo(hoaDonId, customerUpdateData)
          console.log('✅ VNPAY: Customer info updated successfully for order:', hoaDonId)
          console.log('✅ VNPAY: API response:', response.data)
        } catch (error) {
          console.error('❌ VNPAY: Error updating customer info:', error)
          console.error('❌ VNPAY: Error response:', error.response?.data)
          throw error
        }

        // 4. Cập nhật phí ship + tổng tiền
        await hoaDonService.updateHoaDon(hoaDonId, {
          phiShip: tempOrderData.shippingFee,
          tongTien: tempOrderData.finalTotal
        })

        // 5. Cập nhật thanh toán: chuyển khoản (đã thanh toán)
        await hoaDonService.updatePayment(hoaDonId, {
          tienMat: 0,
          chuyenKhoan: tempOrderData.finalTotal,
          phuongThucThanhToanId: 1, // Chuyển khoản
          ghiChu: tempOrderData.ghiChu || '',
          loaiHoaDon: 'Online',
          tienKhachDua: 0,
          phieuGiamGiaId: tempOrderData.discountInfo ? tempOrderData.discountInfo.id : null,
          phuongThucThanhToan: 'VNPAY'
        })

        // 6. Ghi nhận ngày đặt hàng (trạng thái đã được set trong updatePayment)
        await hoaDonService.updateHoaDon(hoaDonId, { 
          ngayDatHang: new Date().toISOString()
        })

        // 7. Dọn giỏ và chuyển sang Step 3 (Success)
        // Chỉ clear buyNowCart nếu đang ở BuyNow mode, giữ nguyên cart chính
        if (isBuyNowMode.value) {
          store.clearBuyNowCart()
          localStorage.removeItem('buyNowCartBackup')
          console.log('🧹 Cleared buyNowCart after successful VNPAY order')
        } else {
          store.clearCart()
          console.log('🧹 Cleared main cart after successful VNPAY order')
        }
        
        // Cập nhật thông tin đơn hàng
        orderCode.value = realOrderCode
        paymentMethod.value = 'VNPAY (Thanh toán online)'
        deliveryTime.value = selectedDeliveryMethod.value === 'express' ? '1-2 ngày làm việc' : '2-5 ngày làm việc'
        
        // Lưu thông tin đơn hàng vào localStorage
        localStorage.setItem('lastOrderCode', realOrderCode)
        localStorage.setItem('lastPaymentMethod', paymentMethod.value)
        localStorage.setItem('lastDeliveryTime', deliveryTime.value)
        
        console.log('🎉 Paid order created successfully:', { 
          orderCode: realOrderCode, 
          paymentMethod: paymentMethod.value,
          deliveryTime: deliveryTime.value
        })
        
        // Redirect đến trang thành công
        window.location.href = `/order-success?orderCode=${realOrderCode}&paymentMethod=${paymentMethod.value}&deliveryTime=${deliveryTime.value}`
      } catch (e) {
        console.error('Create paid order error:', e)
        
        // Lưu thông tin lỗi vào localStorage
        localStorage.setItem('lastOrderCode', orderCode.value || 'HD001')
        localStorage.setItem('lastErrorMessage', 'Có lỗi khi tạo hóa đơn sau thanh toán: ' + (e.response?.data?.message || e.message))
        
        // Redirect đến trang lỗi
        window.location.href = `/payment-error?orderCode=${orderCode.value || 'HD001'}&errorMessage=${encodeURIComponent('Có lỗi khi tạo hóa đơn sau thanh toán: ' + (e.response?.data?.message || e.message))}`
      }
    }

    const onPlaceOrder = async () => {
      if (!isFormValid.value) return

      // Validate số điện thoại nếu user chưa đăng nhập
      if (!isAuthenticated.value && customerInfo.value.phone && !validatePhoneNumber(customerInfo.value.phone)) {
        alert('Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại Việt Nam (10-11 số)')
        return
      }
      if (paymentMethod.value === 'bank') {
        // VNPAY: Lưu thông tin tạm thời, không tạo hóa đơn trước
        try {
          // Tạo mã đơn hàng tạm thời (không lưu DB)
          const tempOrderCode = 'TEMP_' + Date.now()
          
          // Lưu thông tin đơn hàng tạm thời để xử lý sau khi VNPAY trả về
          const tempOrderData = {
            customerInfo: { ...customerInfo.value },
            shippingAddress: { ...shippingAddress.value },
            cartItems: [...cartItems.value],
            ghiChu: ghiChu.value,
            finalTotal: finalTotal.value,
            shippingFee: shippingFee.value,
            discountInfo: discountInfo.value,
            selectedCustomer: selectedCustomer.value,
            selectedDeliveryMethod: selectedDeliveryMethod.value,
            userInfo: userInfo.value, // Thêm userInfo để sử dụng trong createPaidOrder
            tempOrderCode: tempOrderCode
          }
          localStorage.setItem('pendingVnpayOrder', JSON.stringify(tempOrderData))

          // Gọi backend tạo URL VNPAY
          const apiBase = window.location.origin.includes(':3000') ? 'http://localhost:8080' : ''
          const resp = await fetch(`${apiBase}/api/payment/vnpay/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ 
              amount: Math.round(finalTotal.value), // Đảm bảo là số nguyên
              orderCode: tempOrderCode
            })
          })
          if (!resp.ok) {
            throw new Error('Không tạo được URL thanh toán VNPAY')
          }
          const data = await resp.json()
          if (!data.paymentUrl) throw new Error('Thiếu paymentUrl từ server')
          window.location.href = data.paymentUrl
        } catch (error) {
          alert('Có lỗi khi khởi tạo VNPAY: ' + (error.response?.data?.message || error.message))
        }
        return
      }
      // COD: đặt hàng luôn
      placeOrder()
    }


    // Hàm lấy tên địa chỉ từ mã
    function getProvinceName(customCityCode = null) {
      const cityCode = customCityCode || shippingAddress.value.city
      console.log('🔍 getProvinceName - cityCode:', cityCode)
      console.log('🔍 getProvinceName - provinces.value:', provinces.value)
      const p = provinces.value.find(p => p.code == cityCode)
      console.log('🔍 getProvinceName - found province:', p)
      return p ? p.name : ''
    }
    function getDistrictName(customDistrictCode = null) {
      const districtCode = customDistrictCode || shippingAddress.value.district
      console.log('🔍 getDistrictName - districtCode:', districtCode)
      console.log('🔍 getDistrictName - districts.value:', districts.value)
      const d = districts.value.find(d => d.code == districtCode)
      console.log('🔍 getDistrictName - found district:', d)
      return d ? d.name : ''
    }
    function getWardName(customWardCode = null) {
      const wardCode = customWardCode || shippingAddress.value.ward
      console.log('🔍 getWardName - wardCode:', wardCode)
      console.log('🔍 getWardName - wards.value:', wards.value)
      const w = wards.value.find(w => w.code == wardCode)
      console.log('🔍 getWardName - found ward:', w)
      return w ? w.name : ''
    }

    // New functions for enhanced features
    const fillFromUserInfo = () => {
      if (userInfo.value) {
        customerInfo.value.fullName = userInfo.value.hoTen || ''
        customerInfo.value.phone = userInfo.value.soDienThoai || ''
        customerInfo.value.email = userInfo.value.email || ''
      }
    }

    const selectSavedAddress = async (address) => {
      console.log('🏠 selectSavedAddress called with address:', address)
      
      customerInfo.value.fullName = address.fullName
      customerInfo.value.phone = address.phone
      shippingAddress.value.address = address.address
      
      // Ưu tiên sử dụng mã (code) thay vì tên (name) để lookup
      shippingAddress.value.city = address.cityCode || address.city
      shippingAddress.value.district = address.districtCode || address.district
      shippingAddress.value.ward = address.wardCode || address.ward
      
      console.log('🏠 Set shippingAddress.value:', shippingAddress.value)
      console.log('🏠 address.cityCode:', address.cityCode, 'address.city:', address.city)
      console.log('🏠 address.districtCode:', address.districtCode, 'address.district:', address.district)
      console.log('🏠 address.wardCode:', address.wardCode, 'address.ward:', address.ward)
      
      // Đảm bảo provinces được load trước khi tìm mã
      if (provinces.value.length === 0) {
        console.log('🏠 Loading provinces for address selection...')
        await loadProvinces()
      }
      
      // Nếu không có mã, cần tìm mã từ tên
      if (!address.cityCode && address.city) {
        console.log('🏠 No cityCode found, need to find code for city name:', address.city)
        // Tìm mã từ tên tỉnh
        const province = provinces.value.find(p => p.name === address.city)
        if (province) {
          shippingAddress.value.city = province.code
          console.log('🏠 Found city code:', province.code, 'for city name:', address.city)
          
          // Load districts cho tỉnh này
          if (districts.value.length === 0) {
            console.log('🏠 Loading districts for selected city...')
            await loadDistricts(province.code)
          }
        }
      }
      
      if (!address.districtCode && address.district) {
        console.log('🏠 No districtCode found, need to find code for district name:', address.district)
        // Tìm mã từ tên quận/huyện
        const district = districts.value.find(d => d.name === address.district)
        if (district) {
          shippingAddress.value.district = district.code
          console.log('🏠 Found district code:', district.code, 'for district name:', address.district)
          
          // Load wards cho quận/huyện này
          if (wards.value.length === 0) {
            console.log('🏠 Loading wards for selected district...')
            await loadWards(district.code)
          }
        }
      }
      
      if (!address.wardCode && address.ward) {
        console.log('🏠 No wardCode found, need to find code for ward name:', address.ward)
        // Tìm mã từ tên phường/xã
        const ward = wards.value.find(w => w.name === address.ward)
        if (ward) {
          shippingAddress.value.ward = ward.code
          console.log('🏠 Found ward code:', ward.code, 'for ward name:', address.ward)
        }
      }
      
      selectedAddressIndex.value = savedAddresses.value.indexOf(address)
      showAddressModal.value = false
      
      // Force reactivity update for shipping fee calculation
      console.log('Selected address:', address)
      console.log('Shipping address updated:', shippingAddress.value)
      console.log('Using codes - city:', shippingAddress.value.city, 'district:', shippingAddress.value.district, 'ward:', shippingAddress.value.ward)
    }

    const saveNewAddress = async () => {
      if (!newAddress.value.fullName || !newAddress.value.phone || !newAddress.value.address) {
        alert('Vui lòng điền đầy đủ thông tin')
        return
      }

      // Validate số điện thoại
      if (!validatePhoneNumber(newAddress.value.phone)) {
        alert('Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại Việt Nam (10-11 số)')
        return
      }

      try {
        const token = localStorage.getItem('token')
        
        if (editingAddress.value) {
          // Update existing address
          const addressData = AddressService.transformAddressForAPI(newAddress.value, provinces.value, districts.value, wards.value)
          console.log('Updating address:', editingAddress.value.id, 'with data:', addressData)
          
          const result = await AddressService.saveAddress(addressData, null, token, true, editingAddress.value.id)
          
          // Update local array
          const index = savedAddresses.value.findIndex(addr => addr.id === editingAddress.value.id)
          if (index !== -1) {
            savedAddresses.value[index] = AddressService.transformAddressFromAPI(result)
          }
          alert('Đã cập nhật địa chỉ thành công!')
        } else {
          // Create new address
          const customerId = selectedCustomer.value ? selectedCustomer.value.id : (userInfo.value?.khachHangId || userInfo.value?.id)
          const addressData = AddressService.transformAddressForAPI(newAddress.value, provinces.value, districts.value, wards.value)
          
          console.log('Creating address for customer:', customerId, 'with data:', addressData)
          
          const result = await AddressService.saveAddress(addressData, customerId, token, false)
          const address = AddressService.transformAddressFromAPI(result)
          
          if (address.isDefault) {
            savedAddresses.value.forEach(addr => addr.isDefault = false)
          }
          savedAddresses.value.push(address)
          alert('Đã lưu địa chỉ thành công!')
        }
        
        // Reset form
        resetNewAddressForm()
        editingAddress.value = null
      } catch (error) {
        console.error('Error saving address:', error)
        alert('Có lỗi xảy ra khi lưu địa chỉ')
      }
    }

    const deleteAddress = async (addressId) => {
      if (!confirm('Bạn có chắc muốn xóa địa chỉ này?')) return
      
      try {
        const token = localStorage.getItem('token')
        await AddressService.deleteAddress(addressId, token)
        
        // Remove from local array
        const index = savedAddresses.value.findIndex(addr => addr.id === addressId)
        if (index !== -1) {
          savedAddresses.value.splice(index, 1)
        }
        alert('Đã xóa địa chỉ thành công!')
      } catch (error) {
        console.error('Error deleting address:', error)
        alert('Có lỗi xảy ra khi xóa địa chỉ')
      }
    }

    const getFullAddress = (address) => {
      const province = provinces.value.find(p => p.code == address.city)
      const district = districts.value.find(d => d.code == address.district)
      const ward = wards.value.find(w => w.code == address.ward)
      
      let fullAddress = address.address
      if (ward) fullAddress += `, ${ward.name}`
      if (district) fullAddress += `, ${district.name}`
      if (province) fullAddress += `, ${province.name}`
      
      return fullAddress
    }

    // Sử dụng AddressService
    const getFullAddressDisplay = AddressService.getFullAddressDisplay
    const validatePhoneNumber = AddressService.validatePhoneNumber

    const validatePhoneField = () => {
      // Function để validate khi blur khỏi field
      if (customerInfo.value.phone && !validatePhoneNumber(customerInfo.value.phone)) {
        console.log('Invalid phone number:', customerInfo.value.phone)
      }
    }

    const validateNewAddressPhone = () => {
      // Function để validate số điện thoại trong modal địa chỉ
      if (newAddress.value.phone && !validatePhoneNumber(newAddress.value.phone)) {
        console.log('Invalid new address phone number:', newAddress.value.phone)
      }
    }

    const selectVoucher = (voucher) => {
      selectedVoucher.value = voucher
    }

    const applySelectedVoucher = () => {
      if (selectedVoucher.value) {
        discountCode.value = selectedVoucher.value.maPhieuGiamGia
        applyDiscount()
        showVoucherModal.value = false
      }
    }

    const removeDiscount = () => {
      discountInfo.value = null
      discountCode.value = ''
      discountError.value = ''
    }

    const loadSavedAddresses = async () => {
      console.log('Loading saved addresses...', { isAuthenticated: isAuthenticated.value, userInfo: userInfo.value })
      
      if (!isAuthenticated.value || !userInfo.value) {
        console.log('Not authenticated or no user info')
        return
      }
      
      try {
        // Use khachHangId from auth store if available
        const customerId = userInfo.value.khachHangId || userInfo.value.id
        console.log('Customer ID:', customerId)
        
        // Sử dụng AddressService
        const token = localStorage.getItem('token')
        savedAddresses.value = await AddressService.loadCustomerAddresses(customerId, token)
        console.log('Loaded addresses:', savedAddresses.value)
      } catch (error) {
        console.log('Error loading addresses:', error)
        // Fallback to localStorage for non-customer users
        const saved = localStorage.getItem('savedAddresses')
        if (saved) {
          savedAddresses.value = JSON.parse(saved)
          console.log('Using localStorage addresses:', savedAddresses.value)
        }
      }
    }

    const loadAvailableVouchers = async () => {
      try {
        console.log('Loading available vouchers...')
        const response = await phieuGiamGiaService.getAll()
        console.log('Voucher API response:', response)
        
        if (response.status === 200) {
          console.log('All vouchers:', response.data)
          console.log('Response data type:', typeof response.data)
          console.log('Response data structure:', response.data)
          console.log('Current cart total:', cartTotal.value)
          
          // Handle different response data structures
          let vouchers = []
          if (Array.isArray(response.data)) {
            vouchers = response.data
          } else if (response.data && Array.isArray(response.data.content)) {
            // Paginated response
            vouchers = response.data.content
          } else if (response.data && Array.isArray(response.data.data)) {
            // Wrapped response
            vouchers = response.data.data
          } else {
            console.error('Unexpected response data structure:', response.data)
            return
          }
          
          console.log('Processed vouchers array:', vouchers)
          
          // Filter vouchers that are applicable to current cart
          const applicableVouchers = vouchers.filter(voucher => {
            const now = new Date()
            const startDate = new Date(voucher.ngayBatDau)
            const endDate = new Date(voucher.ngayKetThuc)
            
            console.log(`Checking voucher ${voucher.maPhieuGiamGia}:`, {
              now: now.toISOString(),
              startDate: startDate.toISOString(),
              endDate: endDate.toISOString(),
              minOrderValue: voucher.giaTriDonHangToiThieu,
              cartTotal: cartTotal.value,
              quantity: voucher.soLuong,
              loaiApDung: voucher.loaiApDung,
              isAuthenticated: isAuthenticated.value,
              userInfo: userInfo.value,
              trangThai: voucher.trangThai,
              active: voucher.active,
              isActive: voucher.isActive
            })
            
            // Check if voucher is active (not disabled)
            if (voucher.trangThai === false || voucher.trangThai === 0 || voucher.active === false || voucher.isActive === false) {
              console.log(`Voucher ${voucher.maPhieuGiamGia} is disabled`)
              return false
            }
            
            // Check if voucher is within date range
            if (now < startDate || now > endDate) {
              console.log(`Voucher ${voucher.maPhieuGiamGia} is not within date range`)
              return false
            }
            
            // Check minimum order value
            if (voucher.giaTriDonHangToiThieu && cartTotal.value < voucher.giaTriDonHangToiThieu) {
              console.log(`Voucher ${voucher.maPhieuGiamGia} requires minimum order value: ${voucher.giaTriDonHangToiThieu}`)
              return false
            }
            
            // Check remaining quantity
            if (voucher.soLuong !== null && voucher.soLuong <= 0) {
              console.log(`Voucher ${voucher.maPhieuGiamGia} has no remaining quantity`)
              return false
            }
            
            // Check application type
            if (voucher.loaiApDung === 'PUBLIC') {
              console.log(`Voucher ${voucher.maPhieuGiamGia} is public - applicable to all customers`)
              return true
            } else if (voucher.loaiApDung === 'CUSTOMER') {
              // Only show to authenticated users
              if (!isAuthenticated.value || !userInfo.value) {
                console.log(`Voucher ${voucher.maPhieuGiamGia} is for specific customers but user is not authenticated`)
                return false
              }
              
              // Check if voucher is for current user
              if (voucher.khachHangId && voucher.khachHangId !== userInfo.value.khachHangId && voucher.khachHangId !== userInfo.value.id) {
                console.log(`Voucher ${voucher.maPhieuGiamGia} is not for current user`)
                return false
              }
              
              console.log(`Voucher ${voucher.maPhieuGiamGia} is for specific customer and matches current user`)
              return true
            }
            
            console.log(`Voucher ${voucher.maPhieuGiamGia} is applicable`)
            return true
          })
          
          console.log('Applicable vouchers:', applicableVouchers)
          console.log('Setting availableVouchers.value to:', applicableVouchers)
          
          // Debug voucher structure
          if (applicableVouchers.length > 0) {
            console.log('First voucher structure:', applicableVouchers[0])
            console.log('Voucher fields:', Object.keys(applicableVouchers[0]))
            
            // Debug specific fields for discount display
            const firstVoucher = applicableVouchers[0]
            console.log('Discount type fields:', {
              loaiGiamGia: firstVoucher.loaiGiamGia,
              loaiPhieu: firstVoucher.loaiPhieu,
              giaTriGiamGia: firstVoucher.giaTriGiamGia,
              soPhanTramGiam: firstVoucher.soPhanTramGiam,
              giaTriGiam: firstVoucher.giaTriGiam,
              soTienGiam: firstVoucher.soTienGiam,
              donToiThieu: firstVoucher.donToiThieu,
              giaTriDonHangToiThieu: firstVoucher.giaTriDonHangToiThieu,
              trangThai: firstVoucher.trangThai,
              active: firstVoucher.active,
              isActive: firstVoucher.isActive
            })
          }
          
          availableVouchers.value = applicableVouchers
          console.log('availableVouchers.value after setting:', availableVouchers.value)
        }
      } catch (error) {
        console.error('Error loading vouchers:', error)
      }
    }

    // Check authentication status
    const checkAuthStatus = () => {
      const token = localStorage.getItem('token')
      const user = localStorage.getItem('user') // Fixed: use 'user' instead of 'userInfo'
      
      console.log('🔍 Auth check - token:', token ? 'exists' : 'missing')
      console.log('🔍 Auth check - user:', user ? 'exists' : 'missing')
      
      if (token && user) {
        isAuthenticated.value = true
        userInfo.value = JSON.parse(user)
        console.log('✅ User authenticated:', userInfo.value) // Debug log
        console.log('🔍 User khachHangId:', userInfo.value?.khachHangId) // Debug log
        console.log('🔍 User id:', userInfo.value?.id) // Debug log
        console.log('🔍 User email:', userInfo.value?.email) // Debug log
        console.log('🔍 User full object:', JSON.stringify(userInfo.value, null, 2)) // Debug log
      } else {
        isAuthenticated.value = false
        userInfo.value = null
        console.log('❌ User not authenticated') // Debug log
      }
      
      // Reload vouchers when auth status changes
      loadAvailableVouchers()
    }

    // Watch for authentication changes
    watch([isAuthenticated, userInfo], () => {
      loadAvailableVouchers()
    }, { deep: true })

    // Debug watcher for currentStep
    watch(currentStep, (newVal, oldVal) => {
      console.log('🔍 currentStep changed:', { from: oldVal, to: newVal })
    })

    // Load data on mount
    onMounted(async () => {
      await loadProvinces() // Ensure provinces are loaded first
      checkAuthStatus()
      await loadSavedAddresses()
      loadAvailableVouchers()
      
      // Auto-fill user info if authenticated
      if (isAuthenticated.value && userInfo.value) {
        fillFromUserInfo()
        
        // Auto-select default address if available
        const defaultAddress = savedAddresses.value.find(addr => addr.isDefault)
        if (defaultAddress) {
          selectSavedAddress(defaultAddress)
        }
      }
      
      // Kiểm tra VNPAY callback từ URL parameters
      await handleVnpayCallbackFromUrl()
      
      // Existing onMounted logic
      try {
        const savedQuery = localStorage.getItem('vnpayCallbackQuery')
        if (savedQuery) {
          const params = new URLSearchParams(savedQuery)
          const responseCode = params.get('vnp_ResponseCode')
          const tempOrderStr = localStorage.getItem('pendingVnpayOrder')
          const tempOrderData = tempOrderStr ? JSON.parse(tempOrderStr) : null
          
          if (responseCode === '00') {
            // VNPAY thanh toán thành công - tạo hóa đơn mới
            if (tempOrderData) {
              // Tạo hóa đơn mới với trạng thái đã thanh toán
              await createPaidOrder(tempOrderData)
            }
            // Dọn dẹp
            localStorage.removeItem('pendingVnpayOrder')
            localStorage.removeItem('vnpayCallbackQuery')
            
            // Redirect sẽ được xử lý trong createPaidOrder
          } else {
            // VNPAY thanh toán thất bại hoặc bị hủy
            let reason = 'Payment failed'
            let orderCode = 'VNPAY_FAILED'
            
            if (responseCode === '24') {
              reason = 'Bạn đã hủy quá trình thanh toán'
            } else if (responseCode === '07') {
              reason = 'Giao dịch hết thời gian chờ'
            } else if (responseCode === '09') {
              reason = 'Giao dịch không hợp lệ'
            } else {
              reason = 'Thanh toán thất bại'
            }
            
            if (tempOrderData && tempOrderData.tempOrderCode) {
              orderCode = tempOrderData.tempOrderCode
            }
            
            // Dọn dẹp
            localStorage.removeItem('pendingVnpayOrder')
            localStorage.removeItem('vnpayCallbackQuery')
            
            // Lưu thông tin vào localStorage
            localStorage.setItem('lastOrderCode', orderCode)
            localStorage.setItem('lastCancelReason', reason)
            
            // Redirect đến trang cancel
            window.location.href = `/payment-cancel?orderCode=${orderCode}&cancelReason=${encodeURIComponent(reason)}`
          }
        }
      } catch (e) {
        // ignore
      }
    })

    // Customer search functions
    const searchCustomers = async () => {
      if (!customerSearchTerm.value.trim()) {
        searchResults.value = []
        return
      }

      searchingCustomers.value = true
      try {
        const results = await CustomerIntegrationService.searchCustomer(customerSearchTerm.value.trim())
        searchResults.value = results
      } catch (error) {
        console.error('Error searching customers:', error)
        searchResults.value = []
      } finally {
        searchingCustomers.value = false
      }
    }

    const selectCustomer = async (customer) => {
      try {
        // Fill customer info
        customerInfo.value = {
          fullName: customer.hoTen,
          phone: customer.soDienThoai,
          email: customer.email
        }

        // Load customer addresses
        const customerWithAddresses = await CustomerIntegrationService.getCustomerWithAddresses(customer.id)
        if (customerWithAddresses && customerWithAddresses.danhSachDiaChi) {
          savedAddresses.value = customerWithAddresses.danhSachDiaChi.map(addr => ({
            id: addr.id,
            fullName: customer.hoTen,
            phone: customer.soDienThoai,
            address: addr.diaChiChiTiet,
            city: addr.tinhThanh,
            district: addr.quanHuyen,
            ward: addr.xaPhuong,
            isDefault: addr.macDinh
          }))

          // Auto-select default address if available
          const defaultAddress = savedAddresses.value.find(addr => addr.isDefault)
          if (defaultAddress) {
            selectSavedAddress(defaultAddress)
          }
        }

        selectedCustomer.value = customer
        searchResults.value = []
        customerSearchTerm.value = ''
        showCustomerSearch.value = false

        // Save to localStorage for future use
        CustomerIntegrationService.saveCustomerToStorage(customer)
      } catch (error) {
        console.error('Error selecting customer:', error)
        alert('Có lỗi xảy ra khi chọn khách hàng')
      }
    }

    const createNewCustomer = async () => {
      try {
        // Validate required fields
        if (!customerInfo.value.fullName || !customerInfo.value.phone) {
          alert('Vui lòng nhập đầy đủ thông tin khách hàng')
          return
        }

        // Validate address
        if (!shippingAddress.value.address || !shippingAddress.value.city || 
            !shippingAddress.value.district || !shippingAddress.value.ward) {
          alert('Vui lòng nhập đầy đủ địa chỉ giao hàng')
          return
        }

        // Create customer with address
        const newCustomer = await CustomerIntegrationService.createCustomerFromCheckout(
          customerInfo.value,
          shippingAddress.value
        )

        selectedCustomer.value = newCustomer
        alert('Đã tạo khách hàng mới thành công!')
      } catch (error) {
        console.error('Error creating new customer:', error)
        alert('Có lỗi xảy ra khi tạo khách hàng mới')
      }
    }

    const clearSelectedCustomer = () => {
      selectedCustomer.value = null
      customerInfo.value = { fullName: '', phone: '', email: '' }
      savedAddresses.value = []
      selectedAddressIndex.value = -1
    }

    // Address management functions
    const fetchDistrictsForNewAddress = async () => {
      if (!newAddress.value.city) {
        newAddressDistricts.value = []
        newAddressWards.value = []
        return
      }
      try {
        newAddressDistricts.value = await AddressService.getDistricts(newAddress.value.city)
        newAddressWards.value = []
        newAddress.value.district = ''
        newAddress.value.ward = ''
      } catch (error) {
        console.error('Error fetching districts:', error)
      }
    }

    const fetchWardsForNewAddress = async () => {
      if (!newAddress.value.district) {
        newAddressWards.value = []
        return
      }
      try {
        newAddressWards.value = await AddressService.getWards(newAddress.value.district)
        newAddress.value.ward = ''
      } catch (error) {
        console.error('Error fetching wards:', error)
      }
    }

    const editAddress = async (address) => {
      editingAddress.value = address
      
      // Use code if available, otherwise use name
      const cityCode = address.cityCode || address.city
      const districtCode = address.districtCode || address.district
      const wardCode = address.wardCode || address.ward
      
      newAddress.value = {
        fullName: address.fullName,
        phone: address.phone,
        address: address.address,
        city: cityCode,
        district: districtCode,
        ward: wardCode,
        isDefault: address.isDefault
      }
      
      console.log('Editing address:', address)
      console.log('Using codes - city:', cityCode, 'district:', districtCode, 'ward:', wardCode)
      
      // Load provinces, districts, and wards for the form
      try {
        await loadProvinces()
        
        // Load districts if city is selected
        if (cityCode) {
          await loadDistricts(cityCode)
        }
        
        // Load wards if district is selected
        if (districtCode) {
          await loadWards(districtCode)
        }
      } catch (error) {
        console.error('Error loading address data for edit:', error)
      }
    }

    const cancelEditAddress = () => {
      editingAddress.value = null
      resetNewAddressForm()
    }

    const resetNewAddressForm = () => {
      newAddress.value = {
        fullName: '',
        phone: '',
        address: '',
        city: '',
        district: '',
        ward: '',
        isDefault: false
      }
      newAddressDistricts.value = []
      newAddressWards.value = []
    }

    // Handle VNPAY callback from URL parameters
    const handleVnpayCallbackFromUrl = async () => {
      const urlParams = new URLSearchParams(window.location.search)
      const responseCode = urlParams.get('vnp_ResponseCode')
      
      console.log('🔍 Checking VNPAY callback:', { responseCode, url: window.location.href })
      
      if (responseCode) {
        // Có VNPAY callback parameters
        const tempOrderStr = localStorage.getItem('pendingVnpayOrder')
        const tempOrderData = tempOrderStr ? JSON.parse(tempOrderStr) : null
        
        console.log('📦 Temp order data:', tempOrderData)
        
        if (responseCode === '00') {
          // VNPAY thanh toán thành công
          console.log('✅ VNPAY Success - Creating order')
          
          if (tempOrderData) {
            await createPaidOrder(tempOrderData)
          }
          
          // Cập nhật thông tin đơn hàng
          orderCode.value = tempOrderData?.tempOrderCode || 'VNPAY_SUCCESS'
          paymentMethod.value = 'VNPAY'
          deliveryTime.value = tempOrderData?.selectedDeliveryMethod === 'express' ? '1-2 ngày làm việc' : '2-5 ngày làm việc'
          
          // Dọn dẹp
          localStorage.removeItem('pendingVnpayOrder')
          
          // Chuyển sang Step 3 - Success
          currentStep.value = 3
          paymentStatus.value = 'success'
          
          console.log('🎉 Switched to success state')
        } else {
          // VNPAY thanh toán thất bại hoặc bị hủy
          console.log('❌ VNPAY Failed - Response code:', responseCode)
          
          let reason = 'Payment failed'
          let orderCodeValue = 'VNPAY_FAILED'
          
          if (responseCode === '24') {
            reason = 'Bạn đã hủy quá trình thanh toán'
          } else if (responseCode === '07') {
            reason = 'Giao dịch hết thời gian chờ'
          } else if (responseCode === '09') {
            reason = 'Giao dịch không hợp lệ'
          } else {
            reason = 'Thanh toán thất bại'
          }
          
          if (tempOrderData && tempOrderData.tempOrderCode) {
            orderCodeValue = tempOrderData.tempOrderCode
          }
          
          // Dọn dẹp
          localStorage.removeItem('pendingVnpayOrder')
          
          // Cập nhật thông tin và chuyển sang trạng thái failed
          orderCode.value = orderCodeValue
          paymentMethod.value = 'VNPAY'
          cancelReason.value = reason
          
          // Chuyển sang Step 2 - Failed
          currentStep.value = 2
          paymentStatus.value = 'failed'
          
          console.log('💥 Switched to failed state:', { reason, orderCode: orderCodeValue })
        }
        
        // Clear URL parameters
        window.history.replaceState({}, document.title, window.location.pathname)
        console.log('🧹 Cleared URL parameters')
      }
    }

    // Retry payment method
    const retryPayment = () => {
      currentStep.value = 2
      paymentStatus.value = 'pending'
      orderCode.value = ''
      paymentMethod.value = 'cod' // Reset về COD
      deliveryTime.value = ''
      cancelReason.value = ''
      
      // Clear any pending VNPAY data
      localStorage.removeItem('pendingVnpayOrder')
      localStorage.removeItem('vnpayCallbackQuery')
    }

    return {
      customerInfo,
      ghiChu,
      shippingAddress,
      paymentMethod,
      selectedDeliveryMethod,
      cartItems,
      cartTotal,
      isFormValid,
      formatPrice,
      formatCurrency,
      formatDate,
      increaseQuantity,
      decreaseQuantity,
      isBuyNowMode,
      placeOrder,
      // Discount
      discountCode,
      discountPercent,
      discountError,
      discountAmount,
      finalTotal,
      applyDiscount,
      discountInfo,
      // Địa chỉ động
      provinces,
      districts,
      wards,
      loadingProvinces,
      loadingDistricts,
      loadingWards,
      loadProvinces,
      loadDistricts,
      loadWards,
      openAddressModal,
      openVoucherModal,
      shippingFee,
      standardShippingFee,
      expressShippingFee,
      shippingFeeLoading,
      shippingFeeError,
      shippingServices,
      selectedServiceIndex,
      orderCode,
      createdHoaDonId,
      // Checkout state
      currentStep,
      paymentStatus,
      deliveryTime,
      cancelReason,
      pageTitle,
      step1Class,
      step1TextClass,
      step2Class,
      step2TextClass,
      step3Class,
      step3TextClass,
      retryPayment,
      handleVnpayCallbackFromUrl,
      onPlaceOrder,
      // New features
      showAddressModal,
      showAddressSelection,
      showVoucherModal,
      showCustomerModal,
      savedAddresses,
      selectedAddressIndex,
      availableVouchers,
      selectedVoucher,
      newAddress,
      editingAddress,
      newAddressDistricts,
      newAddressWards,
      isAuthenticated,
      userInfo,
      fillFromUserInfo,
      selectSavedAddress,
      saveNewAddress,
      deleteAddress,
      editAddress,
      cancelEditAddress,
      resetNewAddressForm,
      fetchDistrictsForNewAddress,
      fetchWardsForNewAddress,
      getFullAddress,
      getFullAddressDisplay,
      validatePhoneNumber,
      validatePhoneField,
      validateNewAddressPhone,
      selectVoucher,
      applySelectedVoucher,
      removeDiscount,
      loadSavedAddresses,
      loadAvailableVouchers,
      // Customer search features
      showCustomerSearch,
      customerSearchTerm,
      searchResults,
      searchingCustomers,
      selectedCustomer,
      searchCustomers,
      selectCustomer,
      createNewCustomer,
      clearSelectedCustomer,
    }
  }
}
</script>