<template>
    <div>
        <div class="relative flex flex-col min-w-0 break-words w-full mb-6 shadow-lg rounded overflow-hidden"
        :class="[color === 'light' ? 'bg-white' : 'bg-emerald-900 text-white']">
        <div class="rounded-t mb-0 px-4 py-3 border-0 flex items-center justify-between">
            <div class="flex items-center space-x-2">
                <span @click="$router.push('/admin/sach')"                class="cursor-pointer text-lg">
                    <svg @click="$router.push('/admin/sach')"
                    xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6 cursor-pointer">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
                </svg>
                </span>
                <h3 class="font-semibold text-lg" :class="[color === 'light' ? 'text-blueGray-700' : 'text-white']">
                    Thêm sách
                </h3>
            </div>
        </div>
        <form @submit.prevent="handleSubmit" class="px-4 py-3">
            <div class="mb-4">
                <label class="block mb-1 font-semibold">Ảnh bìa:</label>
                <ImageKitUpload 
                    @upload-complete="handleImageUpload"
                    :maxFiles="1"
                />
                <div v-if="book.coverUrl" class="mt-2">
                    <img :src="book.coverUrl" class="h-24 w-auto rounded" alt="Ảnh bìa" />
                </div>
            </div>
            <div class="mb-4">
                <label for="title" class="block mb-1 font-semibold">Tên sách:</label>
                <input v-model="book.title" type="text" id="title" placeholder="Nhập tên sách"
                    class="w-full border rounded px-3 py-2" />
            </div>
            <div class="mb-4">
                <label for="description" class="block mb-1 font-semibold">Mô tả:</label>
                <textarea v-model="book.description" id="description" placeholder="Nhập mô tả về sách"
                    class="w-full border rounded px-3 py-2 h-24"></textarea>
            </div>
            <div class="mb-4 grid grid-cols-2 gap-4">
                <div>
                    <label for="price" class="block mb-1 font-semibold">Giá:</label>
                    <input v-model.number="book.price" type="number" id="price" placeholder="Nhập giá"
                        class="w-full border rounded px-3 py-2" />
                </div>
                <div>
                    <label for="oldPrice" class="block mb-1 font-semibold">Giá cũ:</label>
                    <input v-model.number="book.oldPrice" type="number" id="oldPrice"
                        placeholder="Nhập giá cũ (nếu có)" class="w-full border rounded px-3 py-2" />
                </div>
            </div>
            <div class="mb-4 grid grid-cols-2 gap-4">
                <div>
                    <label for="status" class="block mb-1 font-semibold">Trạng thái:</label>
                    <select v-model="book.status" id="status" class="w-full border rounded px-3 py-2">
                        <option value="Còn hàng">Còn hàng</option>
                        <option value="Hết hàng">Hết hàng</option>
                    </select>
                </div>
                <div>
                    <label for="quantity" class="block mb-1 font-semibold">Số lượng:</label>
                    <input v-model.number="book.quantity" type="number" id="quantity" placeholder="Nhập số lượng"
                        class="w-full border rounded px-3 py-2" />
                </div>
            </div>
            <div class="mb-4">
                <label for="categorySelect" class="block mb-1 font-semibold">Danh mục:</label>
                <select v-model="book.category" id="categorySelect" class="w-full border rounded px-3 py-2" :disabled="loading">
                    <option value="">Chọn danh mục</option>
                    <option v-for="danhMuc in danhMucList" :key="danhMuc.id" :value="danhMuc.id">
                        {{ danhMuc.tenDanhMuc }}
                    </option>
                </select>
                <div v-if="loading" class="text-sm text-gray-500 mt-1">Đang tải danh mục...</div>
                <div v-if="error" class="text-sm text-red-500 mt-1">{{ error }}</div>
            </div>
            <div class="mb-4">
                <label class="block mb-1 font-semibold">Thuộc tính:</label>
                <div class="flex items-center space-x-4">
                    <label class="inline-flex items-center">
                        <input type="checkbox" value="Featured" v-model="book.attributes" class="form-checkbox" />
                        <span class="ml-2">Nổi bật</span>
                    </label>
                    <label class="inline-flex items-center">
                        <input type="checkbox" value="Bestseller" v-model="book.attributes" class="form-checkbox" />
                        <span class="ml-2">Bán chạy</span>
                    </label>
                    <label class="inline-flex items-center">
                        <input type="checkbox" value="New" v-model="book.attributes" class="form-checkbox" />
                        <span class="ml-2">Mới phát hành</span>
                    </label>
                </div>
            </div>
            <div class="flex justify-end">
                <button type="submit"
                    class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition">
                    Lưu chỉnh sửa
                </button>
            </div>
        </form>
    </div>
    </div>
</template>





<script>
import ImageKitUpload from '../../components/ImageKitUpload.vue'
import DanhMucService from '@/service/DanhMucService'

export default {
    name: "ThemSach",
    components: {
        ImageKitUpload
    },
    props: {
        color: {
            type: String,
            default: "light",
            validator: (value) => ["light", "dark"].includes(value),
        },
    },
    data() {
        return {
            book: {
                coverUrl: null,
                title: "",
                description: "",
                price: null,
                oldPrice: null,
                status: "Còn hàng",
                quantity: 0,
                category: null,
                attributes: [],
            },
            danhMucList: [],
            loading: false,
            error: null,
        };
    },
    async created() {
        await this.loadDanhMucList();
    },
    methods: {
        async loadDanhMucList() {
            try {
                this.loading = true;
                this.danhMucList = await DanhMucService.getAll();
                console.log('[DEBUG] Loaded danh muc list:', this.danhMucList);
            } catch (error) {
                console.error('[ERROR] Error loading danh muc list:', error);
                this.error = 'Có lỗi xảy ra khi tải danh mục';
            } finally {
                this.loading = false;
            }
        },
        handleImageUpload(uploadedImages) {
            if (uploadedImages.length > 0) {
                this.book.coverUrl = uploadedImages[0].url;
            }
        },
        handleSubmit() {
            console.log("Thêm sách:", this.book);
            alert("Thêm sách thành công!");
            this.$router.push("/admin/sach");
        },
    },
};
</script>

<style scoped>
</style>
