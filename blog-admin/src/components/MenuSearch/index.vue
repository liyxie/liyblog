<template>
  <div>
    <el-icon v-if="isShow"><Search /></el-icon>
    <el-select
      filterable
      remote
      reserve-keyword
      placeholder="输入菜单名称进行检索"
      :remote-method="remoteMethod"
      :loading="loading"
      @change="handleJumpRouter"
      class="header-search-select"
      ref="selectRef"
      :style="{ width: width }"
      @blur="close"
    >
      <el-option
        v-for="item in options"
        :key="item"
        :label="item.meta.title"
        :value="item"
      />
    </el-select>
  </div>
</template>

<script setup lang="ts">
const router = useRouter();

const isShow = ref(true);
const loading = ref(false);

const routers = ref(router.getRoutes());
const options = ref();

const selectRef = ref();
const width = ref<String>("0");

const close = () => {
  width.value = "0";
  isShow.value = true;
};

const showMenuSearch = () => {
  width.value = "210px";
  selectRef.value.focus();
  isShow.value = false;
};

defineExpose({ showMenuSearch });

const remoteMethod = (query: string) => {
  if (query) {
    loading.value = true;
    setTimeout(() => {
      loading.value = false;
      options.value = routers.value.filter((item: any) => {
        if (item.meta.title && !item.meta.hidden) {
          return item.meta.title.toLowerCase().includes(query.toLowerCase());
        }
      });
    }, 200);
  } else {
    options.value = [];
  }
};

function handleJumpRouter(value: any) {
  if (value.children.length) {
    router.push({ path: value.redirect });
  } else {
    router.push({ path: value.path });
  }
}
</script>
<style lang="scss" scoped>
.el-icon {
  vertical-align: -2px;
}

.header-search-select {
  overflow: hidden;
  transition: width 0.2s;
  font-size: 18px;
  background: transparent;
  border-radius: 0;
  vertical-align: middle;
  :v-deep(.el-input__inner) {
    border-radius: 0;
    border: 0;
    padding-left: 0;
    padding-right: 0;
    box-shadow: none !important;
    border-bottom: 1px solid #d9d9d9;
    vertical-align: middle;
  }
}
</style>
