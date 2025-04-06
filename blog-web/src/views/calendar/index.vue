<template>
  <div style="margin-top: 40px;">
    <vue-cal v-bind="config" locale="zh-cn" />
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
// 获取存储的主题
const siteStore = useSiteStore();

// 配置
const config = computed(() => ({
    // 是否隐藏周末
    hideWeekends: false,
    // 是否显示当前时间点
    time: true,
    // 是否显示月周数
    weekNumbers: true,
    // 显示时间视图选择范围
    views: ['year', 'month', 'week', 'day'],
    // 默认显示日历视图
    view: 'week',
    dark: siteStore.isDark,

  })
)


</script>

<style lang="scss" scoped>
.vuecal {
  // 主题色
  --vuecal-primary-color: var(--calendar-background-color);
  // 高度
  --vuecal-height: 700px;
  // 边框半径
  --vuecal-border-radius: 15px;

  overflow: hidden;

}
</style>