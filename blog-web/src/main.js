import { createApp } from 'vue'
import './style.css'
import './assets/font/iconfont.css'
import './assets/font/iconfont.js'

import App from './App.vue'
const app = createApp(App)


import router from './router'
app.use(router)

import store from './store';
app.use(store);

import SvgIcon from '@/components/SvgIcon.vue'// svg component
app.component('svg-icon', SvgIcon)

// pinia缓存
import { createPinia } from 'pinia';
const pinia = createPinia()
import piniaPersist from 'pinia-plugin-persistedstate'
pinia.use(piniaPersist)
app.use(pinia)


//分页
import pagination from '@/components/pagination/index.vue'
app.component("sy-pagination", pagination);

//空状态
import empty from '@/components/empty/index.vue'
app.component("sy-empty", empty);

// 注册指令
import plugins from './plugins' // plugins
app.use(plugins)


//代码高亮
import hljs from 'highlight.js';
import 'highlight.js/styles/atom-one-dark-reasonable.css' //样式
//创建v-highlight全局指令
app.directive('highlight', function (el) {
  let blocks = el.querySelectorAll('pre code');
  blocks.forEach((block) => {
   // hljs.highlightBlock(block)
  })
})

//图片预览
import VueViewer from 'v-viewer'
import 'viewerjs/dist/viewer.css'
app.use(VueViewer)

//图片懒加载
import VueLazyLoad from 'vue-lazyload'
// 2.注册插件
app.use(VueLazyLoad, {
  preLoad: 1,
  // 懒加载默认加载图片
  loading: 'https://liy1900-1258789570.cos.ap-guangzhou.myqcloud.com/www/wwwroot/blog/system/20240823161551.gif',
  // 加载失败后加载的图片
  error: 'https://liy1900-1258789570.cos.ap-guangzhou.myqcloud.com/www/wwwroot/blog/common/JiaZaiShiBai.svg',
  attempt: 1
})

import Loading from '@/components/loading/Loading.js'
app.use(Loading)

import defaultSetting from "@/setting";
app.config.globalProperties.$setting = defaultSetting


//全局导入el图标
import * as components from '@element-plus/icons-vue'
for (const key in components) {
    const componentConfig = components[key];
    app.component(componentConfig.name, componentConfig);
}
app.mount('#app')
