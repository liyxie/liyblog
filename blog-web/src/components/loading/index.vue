<template>
  <div class="loading-container" v-if="show">
    <div class="loader"></div>
    <div class="tips">正在快马加鞭的加载中....</div>
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
  background-color: rgba(255, 255, 255, .9);
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
  width: 40px;
  aspect-ratio: 0.577;
  clip-path: polygon(0 0, 100% 100%, 0 100%, 100% 0);
  position: relative;
  animation: l19 2s infinite linear;
  overflow: hidden;
  position: relative;
  left: 50%;
  top: 45%;
  margin: 0 0 0 -25px;
}
.loader:before {
  content: "";
  position: absolute;
  inset: -150% -150%;
  background: repeating-conic-gradient(
    from 30deg,
    #ffabab 0 60deg,
    #abe4ff 0 120deg,
    #ff7373 0 180deg
  );
  animation: inherit;
  animation-direction: reverse;
}
@keyframes l19 {
  100% {
    transform: rotate(360deg);
  }
}
</style>
