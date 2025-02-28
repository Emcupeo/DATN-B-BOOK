<template>
    <div class="flex gap-6">
        <!-- Form thêm/sửa thuộc tính sách -->
        <div class="w-1/3 bg-white p-4 shadow rounded">
            <h2 class="text-lg font-bold mb-4">
                {{ isEditing ? "Sửa thuộc tính sách" : "Thêm thuộc tính sách" }}
            </h2>
            <form @submit.prevent="handleSubmit">
                <div class="mb-4">
                    <label class="block text-gray-700 mb-1">Tên thuộc tính</label>
                    <input v-model="newAttribute.name" type="text" placeholder="Nhập tên thuộc tính (ví dụ: Tác giả)"
                        class="w-full border px-3 py-2 rounded focus:ring focus:border-blue-500" required />
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700 mb-1">Mô tả thuộc tính</label>
                    <textarea v-model="newAttribute.description" placeholder="Nhập mô tả (ví dụ: Tên của tác giả)"
                        class="w-full border px-3 py-2 rounded focus:ring focus:border-blue-500" rows="3"
                        required></textarea>
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700 mb-1">Giá trị của thuộc tính</label>
                    <div v-for="(field, index) in newAttribute.subAttributes" :key="index"
                        class="flex items-center mb-2">
                        <input v-model="newAttribute.subAttributes[index]" type="text"
                            placeholder="Nhập giá trị (ví dụ: J.K. Rowling)"
                            class="w-full border px-3 py-2 rounded focus:ring focus:border-blue-500" />
                        <button type="button" @click="removeSubAttribute(index)"
                            class="ml-2 text-red-600 hover:text-red-800 text-xs">
                            ✖
                        </button>
                    </div>
                    <button type="button" @click="addSubAttribute"
                        class="bg-blue-500 text-white px-3 py-1 rounded text-xs hover:bg-blue-600 transition">
                        + Thêm giá trị
                    </button>
                </div>
                <div class="flex justify-end gap-2">
                    <button v-if="isEditing" type="button" @click="cancelEdit"
                        class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600 transition text-sm">
                        Hủy
                    </button>
                    <button type="submit"
                        class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 transition text-sm">
                        {{ isEditing ? "Cập nhật" : "Lưu" }}
                    </button>
                </div>
            </form>
        </div>

        <!-- Bảng danh sách thuộc tính sách -->
        <div class="w-2/3 bg-white p-4 shadow rounded">
            <h2 class="text-lg font-bold mb-4">Danh sách thuộc tính sách</h2>
            <div class="flex items-center">
                <input type="text" v-model="searchQuery" placeholder="Tìm kiếm thuộc tính..."
                    class="block w-full p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-xs focus:ring-blue-500 focus:border-blue-500 mb-3" />
            </div>

            <div class="relative overflow-x-auto sm:rounded-lg">
                <table class="w-full text-sm text-left text-gray-500">
                    <thead class="text-xs text-gray-700 uppercase bg-gray-50">
                        <tr>
                            <th scope="col" class="px-6 py-3">Tên thuộc tính</th>
                            <th scope="col" class="px-6 py-3">Mô tả</th>
                            <th scope="col" class="px-6 py-3">Giá trị</th>
                            <th scope="col" class="px-6 py-3">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(attr, index) in filteredAttributes" :key="index"
                            class="bg-white border-b hover:bg-gray-50">
                            <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
                                {{ attr.name }}
                            </td>
                            <td class="px-6 py-4">{{ attr.description }}</td>
                            <td class="px-6 py-4">
                                <span v-for="(value, i) in attr.subAttributes" :key="i" class="mr-1">
                                    {{ value }}
                                </span>
                            </td>
                            <td class="px-6 py-4">
                                <a href="#" @click.prevent="editAttribute(index)"
                                    class="font-medium text-blue-600 hover:underline">Edit</a>
                                <a href="#" @click.prevent="removeAttribute(index)"
                                    class="font-medium text-red-600 hover:underline ml-3">Remove</a>
                            </td>
                        </tr>
                        <tr v-if="!attributes.length" class="bg-white border-b hover:bg-gray-50">
                            <td class="px-6 py-4" colspan="4" align="center">Không có dữ liệu</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, computed } from "vue";

export default {
    setup() {
        const newAttribute = ref({
            name: "",
            description: "",
            subAttributes: [],
        });

        const attributes = ref([
            { name: "Tác giả", description: "Tên của tác giả", subAttributes: ["J.K. Rowling"] },
            { name: "Thể loại", description: "Phân loại sách", subAttributes: ["Tiểu thuyết"] },
            { name: "Nhà xuất bản", description: "Tên nhà xuất bản", subAttributes: ["NXB Trẻ"] },
        ]);

        const searchQuery = ref("");
        const isEditing = ref(false);
        const editingIndex = ref(null);

        const filteredAttributes = computed(() => {
            if (!searchQuery.value) return attributes.value;
            return attributes.value.filter((attr) =>
                attr.name.toLowerCase().includes(searchQuery.value.toLowerCase())
            );
        });

        const addSubAttribute = () => {
            newAttribute.value.subAttributes.push("");
        };

        const removeSubAttribute = (index) => {
            newAttribute.value.subAttributes.splice(index, 1);
        };

        const handleSubmit = () => {
            if (!newAttribute.value.name.trim()) return;

            if (isEditing.value) {
                attributes.value[editingIndex.value] = { ...newAttribute.value };
                isEditing.value = false;
                editingIndex.value = null;
            } else {
                attributes.value.push({ ...newAttribute.value });
            }

            resetForm();
        };

        const editAttribute = (index) => {
            newAttribute.value = { ...attributes.value[index] };
            isEditing.value = true;
            editingIndex.value = index;
        };

        const removeAttribute = (index) => {
            attributes.value.splice(index, 1);
            if (isEditing.value && editingIndex.value === index) {
                cancelEdit();
            }
        };

        const cancelEdit = () => {
            resetForm();
        };

        const resetForm = () => {
            newAttribute.value = { name: "", description: "", subAttributes: [] };
            isEditing.value = false;
            editingIndex.value = null;
        };

        return {
            newAttribute,
            attributes,
            addSubAttribute,
            removeSubAttribute,
            handleSubmit,
            editAttribute,
            removeAttribute,
            cancelEdit,
            searchQuery,
            filteredAttributes,
            isEditing,
        };
    },
};
</script>