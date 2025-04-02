<template>
  <div class="container main-container">
    <div class="main">

      <vue-cal v-bind="config" />


    </div>
  </div>

</template>

<script setup name="Calendar">
import { ref } from 'vue'
import { useSiteStore } from "@/store/moudel/site.js";
import { VueCal } from 'vue-cal';
import 'vue-cal/style'
import { de } from 'element-plus/es/locale/index.mjs';

const config = {
  hideWeekends: true,
  time: true
}


</script>

<style lang="scss" scoped>
.main-container {
  @media screen and (max-width: 1118px) {
    padding-left: 10px;
    padding-right: 10px;

    .top-right,
    .rightBox,
    .articleImg {
      display: none;
    }


  }

  .main {
    margin-top: 40px;
  }
}
</style>