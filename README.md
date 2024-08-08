## 博客介绍



## 目录结构

前端项目blog-web为前台，blog-admin为后台。

后端项目位于blog下。

SQL文件位于根目录下的 blog.sql

可直接导入该项目于本地，修改后端配置文件中的数据库等连接信息，项目中使用到的关于七牛云功能和第三方授权登录等需要自行开通。

当你克隆项目到本地后可使用账号：admin，密码：123456 进行登录

本地访问接口文档地址：http://127.0.0.1:8800/shiyi/doc.html

**ps：请先运行后端项目，再启动前端项目，前端项目配置由后端动态加载。** 

```
blog
├── shiyi-admin    --  后台管理系统的controller模块
├── shiyi-common        --  通用模块
├── shiyi-generate        --  代码生成模块
├── shiyi-quartz        --  定时任务模块
├── shiyi-server        --  博客启动类模块
├── shiyi-web           --  门户接口模块

```
## 项目特点

- 后台参考"vue-element-template"设计，侧边栏，历史标签，面包屑自动生成。
- 采用Markdown编辑器，写法简单。
- 评论支持表情输入回复等
- 前后端分离部署，适应当前潮流。
- 接入第三方登录，减少注册成本。
- 留言采用弹幕墙，更加炫酷。
- 支持代码高亮和复制，图片预览，深色模式等功能，提升用户体验。
- 搜索文章支持高亮分词，响应速度快。
- 新增文章目录、推荐文章等功能，优化用户体验。
- 新增aop注解实现日志管理
- 支持动态权限修改，采用RBAC模型，前端菜单和后台权限实时更新。
- 后台管理支持修改背景图片，博客配置等信息，操作简单，支持上传相册。
- 代码支持多种搜索模式（Elasticsearch或MYSQL），可支持配置。
- 代码支持多种文件上传模式（七牛云oss或本地上传），可支持配置。
- 代码遵循阿里巴巴开发规范，利于开发者学习。
- 使用netty+websocket技术实现博客群聊和单聊功能

## 技术介绍

**前端：** vue3.0 +  pinia + vue-router + axios + element-plus + echarts

**后端：** SpringBoot + nginx + docker + sa-token + Swagger2 + MyBatisPlus + Mysql + Redis + elasticsearch 

**其他：** 接入QQ、微博、码云、微信公众号等第三方登录，接入七牛云对象存储

## 运行环境



## 开发环境

|            开发工具            |           说明            |
| ----------------------------- | ------------------------- |
| IDEA2023                      | Java开发工具IDE            |
| VSCode                        | Vue开发工具IDE             |
| Navicat                       | MySQL远程连接工具          |
| Another Redis Desktop Manager | Redis远程连接工具          |
| Xshell                    | Linux远程连接和文件上传工具 |

|    开发环境    |  版本  |
| ------------- | ----- |
| JDK           | 1.8   |
| MySQL         | 8.0 |
| Redis         | 6.0.5 |
| Elasticsearch | 7.9.2 |