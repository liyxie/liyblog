<template>
  <div class="container main-container">
    <div class="main">

      <vue-cal v-bind="config" locale="zh-cn" :dark="true" />


    </div>
  </div>

</template>

<script setup name="Calendar">
import { ref } from 'vue'
import { useSiteStore } from "@/store/moudel/site.js";
import { VueCal, useLocale } from 'vue-cal';
// 预加载中文
import zhCn from '/node_modules/vue-cal/dist/i18n/zh-cn.js'
useLocale(zhCn)
import 'vue-cal/style'
// 判断主题色
const theme = ref(sessionStorage.getItem("theme"));
import { de } from 'element-plus/es/locale/index.mjs';

const config = {
  hideWeekends: true,
  time: true
}


</script>

<style lang="scss" scoped>
.vuecal {
  --vuecal-primary-color: var(--calendar-background-color);
}

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