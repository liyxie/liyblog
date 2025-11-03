<template>
  <div class="loading-container" v-if="show">
    <div class="loader"></div>
    <div class="tips">正在加载中....</div>
  </div>
</template>

<script setup name="Loading">
const show = ref(false);

const showLoading = () => {
  show.value = true;
  document.body.style.overflow = "hidden";
  document.addEventListener("touchmove", () => {}, true);
};
const hideLoading = () => {
  show.value = false;
  var mo = function (e) {
    e.preventDefault();
  };
  document.body.style.overflow = "";
  document.removeEventListener("touchmove", mo, true);
};

onMounted(() => {});

defineExpose({
  show,
  showLoading,
  hideLoading,
});
</script>

<style scoped>
.loading-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  background-color: rgba(24, 24, 24, 0.9);
}
.tips {
  font-family: "Open Sans";
  color: #52b852;
  font-size: 1rem;
  width: 100%;
  text-align: center;
  position: absolute;
  top: 55%;
  line-height: 30px;
}
.loader {
  width: 48px;
  height: 48px;
  border: 4px solid rgba(82, 184, 82, 0.2);
  border-top-color: #52b852;
  border-radius: 50%;
  position: absolute;
  left: 50%;
  top: 45%;
  margin: -24px 0 0 -24px;
  animation: spin 0.8s linear infinite;
}
@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
