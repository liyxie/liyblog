<template>
  <vue-cal v-bind="config" />
</template>

<script setup name="Calendar">
import { ref } from 'vue'
import { useSiteStore } from "@/store/moudel/site.js";
import { VueCal } from 'vue-cal';
import 'vue-cal/style'

const config = {
  hideWeekends: true,
  time: true
}


</script>

<style lang="scss" scoped>


</style>