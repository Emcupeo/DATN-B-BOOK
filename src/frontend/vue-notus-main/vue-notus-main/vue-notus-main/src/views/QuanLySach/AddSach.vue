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
                <div class="border-dashed border-2 border-gray-300 rounded px-3 py-10 text-center cursor-pointer relative"
                    @dragover.prevent @drop.prevent="handleFileDrop" @click="triggerFileInput">
                    <p v-if="!book.cover">Kéo thả file hoặc click để chọn file</p>
                    <div v-else class="relative w-fit mx-auto">
                        <img :src="book.coverPreview" class="h-24 w-auto" alt="Ảnh bìa" />
                        <span class="absolute -top-2 -right-2 bg-red-500 text-white rounded-full px-2 py-0.5 cursor-pointer" @click="removeCover">❌</span>
                    </div>
                </div>
                <input ref="fileInput" type="file" class="hidden" @change="handleFileChange" />
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
                <select v-model="book.category" id="categorySelect" class="w-full border rounded px-3 py-2">
                    <option value="">Chọn danh mục</option>
                    <option value="Công nghệ">Công nghệ</option>
                    <option value="Văn học">Văn học</option>
                    <option value="Kinh doanh">Kinh doanh</option>
                    <option value="Giáo dục">Giáo dục</option>
                </select>
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
export default {
    name: "ThemSach",
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
                cover: null,
                coverPreview: null,
                title: "",
                description: "",
                price: null,
                oldPrice: null,
                status: "Còn hàng",
                quantity: 0,
                category: "",
                attributes: [],
            },
        };
    },
    methods: {
        triggerFileInput() {
            this.$refs.fileInput.click();
        },
        handleFileChange(event) {
            const file = event.target.files[0];
            if (file) {
                this.book.cover = file;
                this.book.coverPreview = URL.createObjectURL(file);
            }
        },
        handleFileDrop(event) {
            const file = event.dataTransfer.files[0];
            if (file) {
                this.book.cover = file;
                this.book.coverPreview = URL.createObjectURL(file);
            }
        },

        removeCover() {
            this.book.cover = null;
            this.book.coverPreview = null;
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
