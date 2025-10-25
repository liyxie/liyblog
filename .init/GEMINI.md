# GEMINI Project Analysis

**对话过程使用中文**

## Project Overview

This repository contains a full-stack blog application. It is composed of a Java backend, a Vue.js admin frontend, and a separate Vue.js public-facing web frontend. The project also includes a `uniapp-blog` directory, suggesting a mobile application component as well.

**Core Technologies:**

*   **Backend:** Java 17, Spring Boot, Maven, Sa-Token, MyBatisPlus, MySQL, Redis, Elasticsearch (optional).
*   **Frontend (Admin & Web):** Vue 3, Vite, Pinia, Element Plus, Axios.
*   **Development:** The admin frontend uses `pnpm` as a package manager and has strict linting and commit conventions.

The `README.md` file states that the backend must be running before starting the frontend applications, as the frontends fetch their configuration from the backend.

---

## 1. Backend (`blog` directory)

This is a multi-module Spring Boot application built with Maven.

*   **Modules:**
    *   `liy-server`: The main application entry point.
    *   `liy-admin`: Contains backend controllers for the admin system.
    *   `liy-web`: Contains controllers for the public-facing blog API.
    *   `liy-common`: A common module for shared code.
    *   `liy-generate`: A code generation module.
    *   `liy-quartz`: A module for scheduled tasks.
*   **Database:** A `blog.sql` file is located in the root directory to set up the initial database schema in MySQL.

### Building and Running (Backend)

1.  **Prerequisites:** JDK 17, Maven, MySQL, and Redis must be installed and running.
2.  **Configuration:** Update the database connection details in the backend's `application.yml` file (likely located in `blog/liy-server/src/main/resources`).
3.  **Build:** Navigate to the `E:\MyWenJian\AnewBlog\liy-blog\blog` directory and run the Maven build command:
    ```bash
    mvn clean package
    ```
4.  **Run:** After a successful build, run the application from the `blog` directory:
    ```bash
    java -jar liy-server/target/liy-blog.jar
    ```
    The API documentation will be available at `http://127.0.0.1:8800/shiyi/doc.html`.

---

## 2. Admin Frontend (`blog-admin` directory)

This is the administration interface for the blog, built with Vue 3 and Vite. It uses `pnpm` for package management.

### Development (Admin Frontend)

1.  **Navigate to directory:**
    ```bash
    cd E:\MyWenJian\AnewBlog\liy-blog\blog-admin
    ```
2.  **Install dependencies:**
    ```bash
    pnpm install
    ```
3.  **Run development server:**
    ```bash
    pnpm dev
    ```

### Building for Production (Admin Frontend)

1.  **Navigate to directory:**
    ```bash
    cd E:\MyWenJian\AnewBlog\liy-blog\blog-admin
    ```
2.  **Run the build command:**
    ```bash
    pnpm build:prod
    ```

### Development Conventions

This project enforces strict code quality and commit message standards:
*   **Linting:** Uses ESLint, Prettier, and Stylelint. Run `pnpm lint:lint-staged` to check files.
*   **Commits:** Uses `commitizen` for conventional commits. Use `pnpm commit` to create a guided commit message.

---

## 3. Public Frontend (`blog-web` directory)

This is the public-facing website for the blog, also built with Vue 3 and Vite.

### Development (Public Frontend)

1.  **Navigate to directory:**
    ```bash
    cd E:\MyWenJian\AnewBlog\liy-blog\blog-web
    ```
2.  **Install dependencies (use `pnpm` if available):**
    ```bash
    pnpm install 
    # or npm install
    ```
3.  **Run development server:**
    ```bash
    pnpm dev
    # or npm run dev
    ```

### Building for Production (Public Frontend)

1.  **Navigate to directory:**
    ```bash
    cd E:\MyWenJian\AnewBlog\liy-blog\blog-web
    ```
2.  **Run the build command:**
    ```bash
    pnpm build
    # or npm run build
    ```
