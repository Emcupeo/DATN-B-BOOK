<template>
  <div class="container">
    <h1>List Sản phẩm</h1>
    <table class="table table-striped">
      <thead>
        <th>ID sách</th>
        <th>Mã sách</th>
        <th>Tên sách</th>
        <th>Mô tả sách</th>
        <th>Hành động</th>
      </thead>
      <tbody>
        <tr v-for="sanPham in listSanPham" v-bind:key="sanPham.id">
          <td>{{ sanPham.id }}</td>
          <td>{{ sanPham.maSanPham }}</td>
          <td>{{ sanPham.tenSanPham }}</td>
          <td>{{ sanPham.moTa }}</td>
          <td><button></button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import SanPhamService from "../../service/SanPhamService";

export default {
  name: "ListSach",
  data() {
    return {
      listSanPham: [],
    };
  },
  methods: {
    getListSanPham() {
      SanPhamService.getListSanPham().then((response) => {
        this.listSanPham = response.data;
      });
    },
  },
  created() {
    this.getListSanPham();
  },
};
</script>
