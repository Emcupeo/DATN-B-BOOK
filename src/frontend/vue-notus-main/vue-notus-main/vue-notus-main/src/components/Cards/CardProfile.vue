<template>
  <div
    class="relative flex flex-col min-w-0 break-words bg-white w-full mb-6 shadow-xl rounded-lg mt-16"
  >
    <div class="px-6">
      <div class="flex flex-wrap justify-center">
        <div class="w-full px-4 flex justify-center">
          <div class="relative">
            <img
              alt="..."
              :src="team2"
              class="shadow-xl rounded-full h-auto align-middle border-none absolute -m-16 -ml-20 lg:-ml-16 max-w-150-px"
            />
          </div>
        </div>
        <div class="w-full px-4 text-center mt-20">
          <div class="flex justify-center py-4 lg:pt-4 pt-8">
            <div class="mr-4 p-3 text-center">
              <span
                class="text-xl font-bold block uppercase tracking-wide text-blueGray-600"
              >
                22
              </span>
              <span class="text-sm text-blueGray-400">Bạn bè</span>
            </div>
            <div class="mr-4 p-3 text-center">
              <span
                class="text-xl font-bold block uppercase tracking-wide text-blueGray-600"
              >
                10
              </span>
              <span class="text-sm text-blueGray-400">Hình ảnh</span>
            </div>
            <div class="lg:mr-4 p-3 text-center">
              <span
                class="text-xl font-bold block uppercase tracking-wide text-blueGray-600"
              >
                89
              </span>
              <span class="text-sm text-blueGray-400">Bình luận</span>
            </div>
          </div>
        </div>
      </div>
      <div class="text-center mt-12">
        <h3
          class="text-xl font-semibold leading-normal mb-2 text-blueGray-700 mb-2"
        >
          Jenna Stones
        </h3>
        <div
          class="text-sm leading-normal mt-0 mb-2 text-blueGray-400 font-bold uppercase"
        >
          <i class="fas fa-map-marker-alt mr-2 text-lg text-blueGray-400"></i>
          Los Angeles, California
        </div>
        <div class="mb-2 text-blueGray-600 mt-10">
          <i class="fas fa-briefcase mr-2 text-lg text-blueGray-400"></i>
          Quản lý giải pháp - Creative Tim Officer
        </div>
        <div class="mb-2 text-blueGray-600">
          <i class="fas fa-university mr-2 text-lg text-blueGray-400"></i>
          Đại học Khoa học Máy tính
        </div>
      </div>
      <div class="mt-10 py-10 border-t border-blueGray-200 text-center">
        <div class="flex flex-wrap justify-center">
          <div class="w-full lg:w-9/12 px-4">
            <p class="mb-4 text-lg leading-relaxed text-blueGray-700">
              Một nghệ sĩ có phạm vi sáng tạo đáng kể, Jenna - tên thật của Nick Murphy, 
              một người lớn lên ở Melbourne và hiện sống tại Brooklyn, sáng tác, biểu diễn và 
              thu âm tất cả các bài nhạc của mình, mang đến cảm giác ấm áp, gần gũi với 
              cấu trúc nhịp điệu vững chắc.
            </p>
            <a href="javascript:void(0);" class="font-normal text-emerald-500">
              Xem thêm
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import team2 from "@/assets/img/team-2-800x800.jpg";

export default {
  data() {
    return {
      team2,
    };
  },
};
</script>
