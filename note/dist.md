# 待做



文件存储换七牛云，腾讯云

admin标签请求响应过慢时无法加载到页面（同步）

# 已解决

## main.js提示缺失viewerjs

```shell
This dependency was not found:

- viewerjs/dist/viewer.css in ./src/main.js
  To install it, you can run: npm install --save viewerjs/dist/viewer.css
```

显式安装viewerjs

```shell
npm install viewerjs --save
```

## @element-plus/icons-vue缺失

```shell
] Failed to resolve import "@element-plus/icons-vue" from "src/main.js". Does the file exist?
E:/MyWenJian/AnewBlog/shiyi-blog/blog-web/src/main.js:71:12
69 |  import * as components from '@element-plus/icons-vue'
70 |  for (const key in components) {
71 |      const componentConfig = components[key];
   |             ^
72 |      app.component(componentConfig.name, componentConfig);
73 |  }
```

显式安装

```shell
pnpm install @element-plus/icons-vue
```



# Vue3

onMounted() 内调用函数 与 立即调用函数

立即调用函数：函数会在组件初始化阶段立即执行。也就是在其他生命周期钩子之前执行，在模板和DOM被渲染之前；

无法访问DOM，无法保证子组件和其他异步数据是否加载；



onMounted() ：Vue生命周期函数，组件挂载后执行；

onMounted 的另一个问题，就是在 Vite 调试环境下，如果用了 onMounted，一旦触发了 HMR，那么有些情况下，里面的回调不会执行。因为 HMR 只是热加载它认为变化的模块，如果没有涉及当前的模块，那么很可能不会再次触发 onMounted 的回调，但是页面又真的被刷新了，导致出现了数据空白，必须在  onUpdated回调中调用，才能解决。

# nodejs版本管理nvm

win用nvm，Linux可以用n

## nvm

删除原有nodejs

下载 setup.zip或者exe [Releases · coreybutler/nvm-windows (github.com)](https://github.com/coreybutler/nvm-windows/releases)

解压安装，第一个选nvm安装位置，第二个nodejs安装位置（最好选原位置，不用再配环境）

cmd用nvm -v测试

设置淘宝镜像（可不用，原速度感觉还行）

nvm安装目录下打开setting.txt，直接加

```json
node_mirror: https://npm.taobao.org/mirrors/node/
node_mirror: https://npm.taobao.org/mirrors/npm/
```

使用方式

```shell
nvm install v16.20.2 //安装16.20.2版
nvm uninstall v16.20.0 //卸载
nvm ls // 查看本地全部node版本
nvm list zvailable // 显示可安装所有node版本
nvm use 版本 // 切换版本
```

