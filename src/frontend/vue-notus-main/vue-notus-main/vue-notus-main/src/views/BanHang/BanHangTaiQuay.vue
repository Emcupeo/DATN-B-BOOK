<template>
    <div class="w-full">

        <!-- Nếu chưa có hóa đơn nào -->
        <div v-if="!orderActive" class="w-full min-h-screen bg-gray-100 flex flex-col items-center justify-center">
            <img src="https://upload.wikimedia.org/wikipedia/commons/7/76/Simple_cardboard_box.svg" alt="No Invoice"
                class=" max-w-4xl mb-6" />
            <p class="text-xl text-gray-700 mb-4">Oops! Ở đây chẳng có gì cả</p>
            <button @click="orderActive = true"
                class="bg-green-600 text-white text-xs px-6 py-3 rounded hover:bg-green-700 transition">
                Tạo hóa đơn mới
            </button>
        </div>

        <!-- Nếu đã có hóa đơn, hiển thị giao diện bán hàng -->
        <div v-else>
            <div>
                <div class="w-full mb-4">
                    <div
                        class="flex items-center justify-between bg-white border border-gray-200 rounded-lg shadow-sm sm:p-8">
                        <h5 class="text-l font-bold text-gray-900 dark:text-white">
                            Quản lý bán hàng
                        </h5>
                        <button class="bg-green-600 text-white text-xs px-4 py-2 rounded hover:bg-green-700 transition">
                            Tạo đơn hàng mới
                        </button>
                    </div>
                </div>
            </div>
            <div class="flex flex-col lg:flex-row gap-4 w-full">

                <!-- Phần bên trái: Giỏ hàng sản phẩm -->
                <div class="w-full bg-white shadow rounded p-4 border">

                    <div class="w-full lg:w-2/3 bg-white shadow rounded p-4 border">
                        <!-- Ô quét mã QR và nút thêm sản phẩm -->
                        <div class="flex flex-col md:flex-row items-center justify-between mb-4">
                            <button class="border border-gray-300 text-xs rounded px-3 py-2 focus:outline-none"
                                @click="scanQR">
                                Quét mã QR
                            </button>
                            <button @click="addProductByQR"
                                class="bg-blue-500 text-white text-xs px-3 py-2 rounded ml-2 mt-2 md:mt-0">
                                Thêm sản phẩm
                            </button>
                        </div>

                        <!-- Bảng sản phẩm đã chọn -->
                        <div class="relative overflow-x-auto">
                            <table class="w-full text-sm text-left text-gray-500">
                                <thead class="text-xs text-gray-700 uppercase bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3">Sản phẩm</th>
                                        <th class="px-6 py-3">Số lượng</th>
                                        <th class="px-6 py-3">Kho</th>
                                        <th class="px-6 py-3">Giá đã giảm</th>
                                        <th class="px-6 py-3">Giá chưa giảm</th>
                                        <th class="px-6 py-3">Tổng tiền</th>
                                        <th class="px-6 py-3">Hành động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="(prod, idx) in paginatedProducts" :key="idx" class="bg-white">
                                        <td class="px-6 py-4">{{ prod.name }}</td>
                                        <td class="px-6 py-4">
                                            <input type="number" v-model.number="prod.quantity" min="1"
                                                @change="updateProductTotal(prod)"
                                                class="w-16 border rounded px-2 py-1 text-xs" />
                                        </td>
                                        <td class="px-6 py-4">{{ prod.warehouse }}</td>
                                        <td class="px-6 py-4">{{ formatCurrency(prod.discountedPrice) }}</td>
                                        <td class="px-6 py-4">{{ formatCurrency(prod.originalPrice) }}</td>
                                        <td class="px-6 py-4">{{ formatCurrency(prod.total) }}</td>
                                        <td class="px-6 py-4">
                                            <button @click="editProduct(_prod)"
                                                class="text-blue-600 hover:text-blue-800 mr-2 text-xs">
                                                <i class="fas fa-pencil-alt"></i>
                                            </button>
                                            <button @click="removeProduct(idx)"
                                                class="text-red-600 hover:text-red-800 text-xs">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </td>
                                    </tr>
                                    <tr v-if="paginatedProducts.length === 0">
                                        <td colspan="7" class="px-6 py-4 text-center text-gray-500">
                                            Chưa có sản phẩm
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>

                        <!-- Phân trang cho bảng sản phẩm -->
                        <div class="mt-4 flex justify-between items-center">
                            <button @click="prevPage" :disabled="currentPage === 1"
                                class="bg-gray-300 text-gray-700 text-xs px-3 py-1 rounded hover:bg-gray-400 transition">
                                Prev
                            </button>
                            <span class="text-xs">Trang {{ currentPage }} / {{ totalPages }}</span>
                            <button @click="nextPage" :disabled="currentPage === totalPages"
                                class="bg-gray-300 text-gray-700 text-xs px-3 py-1 rounded hover:bg-gray-400 transition">
                                Next
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Phần bên phải: Thông tin khách hàng & thanh toán -->
                <div class="w-fullmd:w-[500px] lg:w-1/3 bg-white shadow rounded p-4 border">
                    <!-- Thông tin khách hàng -->
                    <div class="mb-4">
                        <h2 class="text-lg font-semibold mb-2">Thông tin khách hàng</h2>
                        <div class="text-sm text-gray-600">
                            <p v-if="customer.id">Khách hàng: {{ customer.name }}</p>
                            <p v-else>Khách lẻ</p>
                            <p v-if="customer.phone">SĐT: {{ customer.phone }}</p>
                            <p v-if="customer.address">Địa chỉ: {{ customer.address }}</p>
                        </div>
                        <div class="mt-2">
                            <button @click="chooseCustomer"
                                class="bg-blue-500 text-white text-xs px-3 py-1 rounded hover:bg-blue-600">
                                Chọn khách hàng
                            </button>
                            <button @click="addNewCustomer"
                                class="bg-green-500 text-white text-xs px-3 py-1 rounded hover:bg-green-600 ml-2">
                                Thêm mới khách hàng
                            </button>
                        </div>
                    </div>

                    <!-- Hình thức nhận hàng -->
                    <div class="mb-4">
                        <h3 class="font-semibold mb-2">Hình thức nhận hàng</h3>
                        <div class="space-y-2">
                            <label class="flex items-center space-x-2 text-sm">
                                <input type="radio" value="quầy" v-model="deliveryMethod" />
                                <span>Tại quầy</span>
                            </label>
                            <label class="flex items-center space-x-2 text-sm">
                                <input type="radio" value="giao" v-model="deliveryMethod" />
                                <span>Giao hàng</span>
                            </label>
                        </div>
                        <!-- Nếu giao hàng, hiển thị thêm thông tin -->
                        <div v-if="deliveryMethod === 'giao'" class="mt-2 p-2 border rounded">
                            <label class="block text-sm mb-1">Địa chỉ giao hàng</label>
                            <input type="text" v-model="shippingInfo.address" class="w-full border rounded px-2 py-1" />
                            <label class="block text-sm mt-2 mb-1">SĐT người nhận</label>
                            <input type="text" v-model="shippingInfo.phone" class="w-full border rounded px-2 py-1" />
                        </div>
                    </div>

                    <!-- Thông tin thanh toán -->
                    <div class="mb-4">
                        <h3 class="font-semibold mb-2">Hình thức thanh toán</h3>
                        <div class="space-y-2">
                            <label class="flex items-center space-x-2 text-sm">
                                <input type="radio" value="chuyển khoản" v-model="paymentMethod" />
                                <span>Chuyển khoản</span>
                            </label>
                            <label class="flex items-center space-x-2 text-sm">
                                <input type="radio" value="tiền mặt" v-model="paymentMethod" />
                                <span>Tiền mặt</span>
                            </label>
                        </div>
                        <!-- Nếu chuyển khoản, hiển thị QR thanh toán -->
                        <div v-if="paymentMethod === 'chuyển khoản'" class="mt-2">
                            <img src="https://via.placeholder.com/150" alt="QR thanh toán" class="mx-auto" />
                            <p class="text-center text-sm text-gray-500 mt-1">Quét mã để thanh toán</p>
                        </div>
                    </div>

                    <!-- Tóm tắt thanh toán -->
                    <div class="border-t pt-4">
                        <div class="flex justify-between mb-2">
                            <span class="text-gray-600">Tạm tính:</span>
                            <span class="font-medium">{{ formatCurrency(subtotal) }}</span>
                        </div>
                        <div class="flex justify-between mb-2">
                            <span class="text-gray-600">Phí vận chuyển:</span>
                            <span class="font-medium">{{ formatCurrency(shippingFee) }}</span>
                        </div>
                        <div class="flex justify-between mb-2">
                            <span class="text-gray-600">Giảm giá:</span>
                            <span class="font-medium">{{ formatCurrency(discount) }}</span>
                        </div>
                        <div class="flex justify-between text-xl font-bold">
                            <span>Tổng thanh toán:</span>
                            <span>{{ formatCurrency(total) }}</span>
                        </div>
                        <!-- Phần nhập/chọn voucher -->
                        <div class="mt-2">
                            <input type="text" v-model="discountCode" placeholder="Nhập voucher..."
                                class="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring" />
                        </div>
                    </div>

                    <!-- Nút xác nhận đơn hàng -->
                    <div class="mt-4">
                        <button @click="confirmOrder"
                            class="w-full bg-purple-600 text-white py-3 rounded text-xs font-semibold hover:bg-purple-700 transition">
                            Xác nhận đơn hàng
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "BanHangTaiQuay",
    data() {
        return {
            orderActive: false,
            qrCode: "",
            products: [
                {
                    name: "Sản phẩm A",
                    price: 100000,
                    quantity: 1,
                    total: 100000,
                    warehouse: "Kho A",
                    discountedPrice: 90000,
                    originalPrice: 100000,
                },
            ],
            customer: {
                id: null,
                name: "",
                phone: "",
                address: "",
            },
            deliveryMethod: "quầy",
            paymentMethod: "tiền mặt",
            shippingInfo: {
                address: "",
                phone: "",
            },
            discountCode: "",
            currentPage: 1,
            itemsPerPage: 5,
            color: "light",
            searchQuery: "",
        };
    },
    computed: {
        paginatedProducts() {
            const start = (this.currentPage - 1) * this.itemsPerPage;
            return this.products.slice(start, start + this.itemsPerPage);
        },
        totalPages() {
            return Math.ceil(this.products.length / this.itemsPerPage);
        },
        subtotal() {
            return this.products.reduce((sum, prod) => sum + prod.total, 0);
        },
        shippingFee() {
            return this.deliveryMethod === "giao" ? 20000 : 0;
        },
        discount() {
            return this.discountCode.toUpperCase() === "SALE50" ? 50000 : 0;
        },
        total() {
            return this.subtotal + this.shippingFee - this.discount;
        },
    },
    methods: {
        scanQR() {
            const code = prompt("Quét mã QR (nhập mã giả lập):");
            if (code) {
                this.qrCode = code;
            }
        },
        addProductByQR() {
            if (this.qrCode.trim() !== "") {
                const newProduct = {
                    name: "Sản phẩm " + this.qrCode,
                    price: 150000,
                    quantity: 1,
                    total: 150000,
                    warehouse: "Kho B",
                    discountedPrice: 130000,
                    originalPrice: 150000,
                };
                this.products.push(newProduct);
                this.qrCode = "";
            }
        },
        updateProductTotal(prod) {
            prod.total = prod.price * prod.quantity;
        },
        // Sử dụng _prod để bỏ lỗi unused variable
        editProduct() {
            alert("Chức năng chỉnh sửa chưa được triển khai");
        },
        removeProduct(index) {
            this.products.splice(index, 1);
        },
        addProduct() {
            alert("Thêm sản phẩm vào giỏ hàng (chức năng demo)");
        },
        formatCurrency(value) {
            return new Intl.NumberFormat("vi-VN", {
                style: "currency",
                currency: "VND",
            }).format(value);
        },
        chooseCustomer() {
            alert("Chọn khách hàng");
        },
        addNewCustomer() {
            alert("Thêm mới khách hàng");
        },
        confirmOrder() {
            alert(`Đơn hàng được xác nhận! Tổng thanh toán: ${this.formatCurrency(this.total)}`);
        },
    },
};
</script>

<style scoped>
/* Bạn có thể thêm CSS tùy chỉnh nếu cần */
</style>