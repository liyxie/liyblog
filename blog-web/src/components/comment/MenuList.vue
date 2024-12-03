<template>
    <template v-for="(item) in items">
        <template v-if="item.type === 2">
            <el-sub-menu :index="item.id">
                <template #title>
                    <el-icon>
                        <Coin />
                    </el-icon>
                    <span>{{ item.name }}</span>
                </template>
                <menu-list :items="item.childToolList" :isFirst="false"></menu-list>
            </el-sub-menu>
        </template>

        <template v-else>

            <el-menu-item :index="item.id">
                <el-tooltip effect="dark" :content="item.describe" placement="top-start">
                    <span>
                        {{ item.name }}
                    </span>
                </el-tooltip>
            </el-menu-item>
        </template>

    </template>
</template>

<script setup name="MenuList">

import { defineProps } from 'vue';
const props = defineProps({
    items: [],
    isFirst: ''
});
</script>

<style lang="scss" scoped>
.menu-class {
    border-radius: 4px;
    max-height: 85%;
    background-color: var(--background-color);
    overflow: auto;
    box-shadow: var(--el-box-shadow-light);
    border-right: rgba(0, 0, 0, 0.3);

    ::v-deep(.el-sub-menu__title) {
        color: var(--text-color);
    }

    ::v-deep(.el-menu-item) {
        color: var(--text-color);
    }

    ::v-deep(.el-menu) {
        background-color: var(--background-color);
    }


}
</style>
