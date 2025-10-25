# AGENTS.md

This file provides guidance to agents when working with code in this repository.

## 项目特定的非显而易见信息

### 构建和运行命令
- **后端**: `mvn clean package` (生成 liy-blog.jar，默认跳过测试)
- **后端启动**: `java -jar target/liy-blog.jar` (主类: com.liy.Application)
- **管理后台**: `pnpm install && pnpm dev` (强制使用 pnpm)
- **用户前台**: `npm install && npm run dev`

### 关键项目架构模式
- **多模块Maven结构**: liy-server(启动器)、liy-common(核心业务)、liy-web(前端API)、liy-admin(管理API)、liy-generate(代码生成)、liy-quartz(定时任务)
- **策略模式实现**: 文件上传策略(本地/七牛云/阿里云/腾讯云)、搜索策略(MySQL/ElasticSearch)
- **事件驱动架构**: DataEvent系统用于解耦操作
- **Netty WebSocket**: 独立端口运行的实时消息服务器

### 重要的非标准配置
- **日志文件位置**: `./logs/` (非标准位置)
- **文件上传路径**: 生产环境 `/root/blog/fileUpload/`，本地环境 `H://my-project/`
- **MyBatis XML映射**: 必须在 `pom.xml` 中配置resources才能正确编译
- **主类位置**: `com.liy.Application` (在liy-server模块中)

### 前端项目特定的约定
- **blog-admin**: 使用TypeScript，强制使用pnpm，ESLint + Prettier + StyleLint代码规范
- **blog-web**: 使用JavaScript，使用npm，没有代码检查工具
- **API响应处理**: 统一使用ResponseResult类，code=200表示成功
- **认证方式**: localStorage存储accessToken，请求头使用Authorization字段

### 后端开发注意事项
- **数据库配置**: 主数据源在application.yml中，MyBatis-Plus XML映射在mapper/目录
- **限流注解**: `@AccessLimit` 自定义注解实现接口限流
- **文件上传**: 使用策略模式，支持多种云存储提供商
- **定时任务**: 使用Quartz框架，在liy-quartz模块中

### 测试和部署
- **测试**: Maven默认跳过测试(`skipTests=true`)
- **构建产物**: 后端生成liy-blog.jar，前端生成dist/目录
- **端口配置**: 后端默认8800端口，上下文路径`/liy`

### 特殊工具和实用程序
- **代码生成**: 使用Beetl模板，在liy-admin/src/main/resources/beetl-back-end/
- **API文档**: Knife4j (Swagger 3) - `http://localhost:8800/liy/doc.html`
- **数据库监控**: Druid - `http://localhost:8800/liy/druid/`
- **WebSocket**: 独立端口，自动ping/pong机制，非活跃连接清理

### 环境变量和配置
- **前端代理**: 使用Vite代理后端API，通过环境变量配置
- **认证token**: 存储在localStorage中，键名为accessToken
- **响应拦截**: 统一处理401错误，自动跳转登录页面