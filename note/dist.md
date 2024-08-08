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

