

2025.03.26
## 切换主题色，
先通过 `./style` 中的  `html[data-theme='dark']` 和 `html.dark ` 配置好相应的浅色和深色
再通过 var() 引用
例如要设置某段文字的主题色
// style.css
html.dark {
    --text-color: #fff
}
html[data-theme='dark'] {
    --text-color: #f5f5f5
}
// index.vue
<text class="myText"> 123 </text>
<style lang="scss">
    .myText{
        color: var(--text-color);
    }
</style>


# Vue 3 + Vite

This template should help get you started developing with Vue 3 in Vite. The template uses Vue 3 `<script setup>` SFCs, check out the [script setup docs](https://v3.vuejs.org/api/sfc-script-setup.html#sfc-script-setup) to learn more.

## Recommended IDE Setup

- [VS Code](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur) + [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin).
