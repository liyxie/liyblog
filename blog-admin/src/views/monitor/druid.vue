<template>
  <i-frame :src="url" />
</template>

<script setup lang="ts">
defineOptions({
  name: "Druid",
  inheritAttrs: false,
});

const url = ref(import.meta.env.VITE_APP_BASE_API + "/druid/login.html");
</script>
