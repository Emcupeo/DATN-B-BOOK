<template>
    <div class="border rounded-lg p-4 shadow-md hover:shadow-lg cursor-pointer">
      <img :src="product.image" class="w-full h-40 object-cover mb-2 rounded-lg" />
      <h2 class="text-lg font-bold">{{ product.name }}</h2>
      <p class="text-gray-600">{{ product.price.toLocaleString() }} VND</p>
      <button 
        @click="$emit('view-detail', product.id)" 
        class="mt-2 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700">
        Xem chi tiết
      </button>
    </div>
  </template>
  
  <script>
  export default {
    props: ['product']
  };
  </script>
  