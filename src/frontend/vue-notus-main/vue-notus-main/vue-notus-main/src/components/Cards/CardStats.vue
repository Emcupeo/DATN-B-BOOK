<template>
  <div
    class="relative flex flex-col min-w-0 break-words bg-white rounded mb-6 xl:mb-0 shadow-lg"
  >
    <div class="flex-auto p-4">
      <div class="flex flex-wrap">
        <div class="relative w-full pr-4 max-w-full flex-grow flex-1">
          <h5 class="text-blueGray-400 uppercase font-bold text-xs">
            {{ tieuDePhu }}
          </h5>
          <span class="font-semibold text-xl text-blueGray-700">
            {{ tieuDeChinh }}
          </span>
        </div>
        <div class="relative w-auto pl-4 flex-initial">
          <div
            class="text-white p-3 text-center inline-flex items-center justify-center w-12 h-12 shadow-lg rounded-full"
            :class="[mauBieuTuong]"
          >
            <i :class="[tenBieuTuong]"></i>
          </div>
        </div>
      </div>
      <p class="text-sm text-blueGray-400 mt-4">
        <span class="mr-2" :class="[mauPhanTram]">
          <i
            :class="[
              muiTen === 'len' ? `fas fa-arrow-up` : `fas fa-arrow-down`,
            ]"
          ></i>
          {{ phanTram }}%
        </span>
        <span class="whitespace-nowrap">{{ moTa }}</span>
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "card-stats",
  props: {
    tieuDePhu: {
      type: String,
      default: "Lưu lượng truy cập",
    },
    tieuDeChinh: {
      type: String,
      default: "350,897",
    },
    muiTen: {
      default: "len",
      validator: function (value) {
        return ["len", "xuong"].indexOf(value) !== -1;
      },
    },
    phanTram: {
      type: String,
      default: "3.48",
    },
    mauPhanTram: {
      type: String,
      default: "text-emerald-500",
    },
    moTa: {
      type: String,
      default: "So với tháng trước",
    },
    tenBieuTuong: {
      type: String,
      default: "far fa-chart-bar",
    },
    mauBieuTuong: {
      type: String,
      default: "bg-red-500",
    },
  },
};
</script>
