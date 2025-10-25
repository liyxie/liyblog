# GEMINI 项目分析

**对话过程使用中文**

## 项目概述

该代码库包含一个全栈博客应用程序。它由一个 Java 后端、一个 Vue.js 管理前端和一个独立的 Vue.js 面向公众的 Web 前端组成。该项目还包括一个 `uniapp-blog` 目录，这表明它也可能有一个移动应用程序组件。

**核心技术:**

*   **后端:** Java 17, Spring Boot, Maven, Sa-Token, MyBatisPlus, MySQL, Redis, Elasticsearch (可选)。
*   **前端 (管理端 & 网站端):** Vue 3, Vite, Pinia, Element Plus, Axios。
*   **开发:** 管理端前端使用 `pnpm` 作为包管理器，并有严格的代码检查和提交规范。

`README.md` 文件指出，在启动前端应用程序之前，后端必须正在运行，因为前端会从后端获取其配置。

---

## 1. 后端 (`blog` 目录)

这是一个使用 Maven 构建的多模块 Spring Boot 应用程序。

*   **模块:**
    *   `liy-server`: 主应用程序入口点。
    *   `liy-admin`: 包含管理系统的后端控制器。
    *   `liy-web`: 包含面向公众的博客 API 的控制器。
    *   `liy-common`: 用于共享代码的通用模块。
    *   `liy-generate`: 代码生成模块。
    *   `liy-quartz`: 定时任务模块。
*   **数据库:** 根目录中有一个 `blog.sql` 文件，用于在 MySQL 中设置初始数据库模式。

### 构建和运行 (后端)

1.  **先决条件:** 必须安装并运行 JDK 17、Maven、MySQL 和 Redis。
2.  **配置:** 在后端的 `application.yml` 文件中更新数据库连接详细信息 (可能位于 `blog/liy-server/src/main/resources` 中)。
3.  **构建:** 导航到 `E:\MyWenJian\AnewBlog\liy-blog\blog` 目录并运行 Maven 构建命令：
    ```bash
    mvn clean package
    ```
4.  **运行:** 成功构建后，从 `blog` 目录运行应用程序：
    ```bash
    java -jar liy-server/target/liy-blog.jar
    ```
    API 文档将位于 `http://127.0.0.1:8800/shiyi/doc.html`。

---

## 2. 管理端前端 (`blog-admin` 目录)

这是博客的管理界面，使用 Vue 3 和 Vite 构建。它使用 `pnpm` 进行包管理。

### 开发 (管理端前端)

1.  **导航到目录:**
    ```bash
    cd E:\MyWenJian\AnewBlog\liy-blog\blog-admin
    ```
2.  **安装依赖:**
    ```bash
    pnpm install
    ```
3.  **运行开发服务器:**
    ```bash
    pnpm dev
    ```

### 构建生产版本 (管理端前端)

1.  **导航到目录:**
    ```bash
    cd E:\MyWenJian\AnewBlog\liy-blog\blog-admin
    ```
2.  **运行构建命令:**
    ```bash
    pnpm build:prod
    ```

### 开发规范

该项目强制执行严格的代码质量和提交消息标准：
*   **代码检查:** 使用 ESLint、Prettier 和 Stylelint。运行 `pnpm lint:lint-staged` 来检查文件。
*   **提交:** 使用 `commitizen` 进行规范化提交。使用 `pnpm commit` 创建引导式提交消息。

---

## 3. 面向公众的前端 (`blog-web` 目录)

这是博客的面向公众的网站，也使用 Vue 3 和 Vite 构建。

### 开发 (面向公众的前端)

1.  **导航到目录:**
    ```bash
    cd E:\MyWenJian\AnewBlog\liy-blog\blog-web
    ```
2.  **安装依赖 (如果可用，请使用 `pnpm`):**
    ```bash
    pnpm install
    # 或 npm install
    ```
3.  **运行开发服务器:**
    ```bash
    pnpm dev
    # 或 npm run dev
    ```

### 构建生产版本 (面向公众的前端)

1.  **导航到目录:**
    ```bash
    cd E:\MyWenJian\AnewBlog\liy-blog\blog-web
    ```
2.  **运行构建命令:**
    ```bash
    pnpm build
    # 或 npm run build
    ```
