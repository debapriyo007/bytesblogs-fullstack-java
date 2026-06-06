# 🐛 BugBlogs: Full-Stack Developer Blog Platform

BugBlogs is a high-performance, full-stack blogging application tailored for developers. It features a modern React Single Page Application (SPA) frontend and a secure, robust Spring Boot backend. Authors can write using Markdown, organize content via categories and tags, upload images, sign in with Google OAuth2 or OTP-verified emails, and administrators can manage users and posts using a sleek admin dashboard.

---

## 🚀 Key Features

*   **Secure Authentication & Session Management**:
    *   JWT-based session authentication using custom **HttpOnly & Secure cookies** for maximum CSRF protection.
    *   **Google OAuth2 Single Sign-On** integration.
    *   Password-less and verified email logins using an **OTP verification code** sent via email.
*   **Developer-Friendly Rich Editor**:
    *   Full Markdown editing support with live parsing.
    *   Syntax highlighting for various programming languages inside code snippets.
*   **Media & Image Uploads**:
    *   Integrated with **Cloudinary** for scalable cloud image storage and quick deliveries.
*   **Content Cataloging**:
    *   Hierarchical classification using categories and labels/tags.
    *   Interactable elements (likes, views count).
*   **Admin Dashboard**:
    *   Statistical oversight widgets (total users, post count).
    *   User role management and account termination controls.
*   **Robust Architecture**:
    *   Spring Boot JPA / Hibernate mapping with a MySQL database.
    *   Vite-powered React SPA with customized tailwind-styled layout.

---

## 🛠️ Technology Stack

### Backend
*   **Framework**: Spring Boot 4.0.6 (Java 25)
*   **Security**: Spring Security, OAuth2 Client, JJWT (JWT token generation/parsing)
*   **Database**: MySQL, Spring Data JPA, Hibernate
*   **Mailing Service**: Spring Boot Mail Starter (SMTP server integration)
*   **Cloud Integrations**: Cloudinary SDK (Image Storage)
*   **Build Tool**: Maven

### Frontend
*   **Framework**: React 19, Vite
*   **Routing**: React Router DOM 7
*   **Styling**: TailwindCSS 3.4, Radix UI Primitives (Dropdown, Dialog, Label, Slot)
*   **Libraries**: Lucide React / React Icons (iconography), Marked (Markdown parser), Highlight.js (syntax coloring)

---

## ⚙️ Environment Configurations

Both components require specific configurations to run. For security, these settings should be managed via environment variables.

### Backend Configurations (`spring-backend`)

Define the following environment variables when running in production or modify your local profile:

| Variable Name | Description | Default / Example |
| :--- | :--- | :--- |
| `PORT` | Backend server port | `8080` |
| `DB_URL` | MySQL Connection URL | `jdbc:mysql://localhost:3306/blog_db` |
| `DB_USERNAME` | Database username | `root` |
| `DB_PASSWORD` | Database password | `yourpassword` |
| `JWT_SECRET` | 256-bit Base64 JWT Secret Key | *Generate a secure random string* |
| `JWT_EXPIRATION` | JWT token expiration time (in ms) | `86400000` (1 day) |
| `GOOGLE_CLIENT_ID` | Google Console OAuth Client ID | `your-google-client-id` |
| `GOOGLE_CLIENT_SECRET` | Google Console OAuth Client Secret | `your-google-client-secret` |
| `MAIL_HOST` | SMTP Server Host Address | `smtp.gmail.com` or Brevo SMTP |
| `MAIL_PORT` | SMTP Server Connection Port | `587` |
| `MAIL_USERNAME` | Mailer account username | `example@gmail.com` |
| `MAIL_PASSWORD` | Mailer account SMTP password / app password | `your-app-password` |
| `MAIL_FROM` | Origin mail address for notifications | `no-reply@bugblogs.com` |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary Account Cloud Name | `your-cloud-name` |
| `CLOUDINARY_API_KEY` | Cloudinary API Key | `your-api-key` |
| `CLOUDINARY_API_SECRET` | Cloudinary API Secret Key | `your-api-secret` |
| `FRONTEND_URL` | Frontend client origin (CORS/Redirects) | `http://localhost:5173` |
| `COOKIE_SECURE` | Enable secure flag for HttpOnly Cookies | `true` in Production / `false` in Dev |

---

## 📦 Deployment Topologies

Choose one of the two strategies to deploy the application:

### Topology A: Unified Deployment (Simplest & Recommended)
Compile the React frontend static pages and package them directly inside the Spring Boot JAR resources.
1.  Navigate to the `frontend` folder and build the assets:
    ```bash
    cd frontend && npm install && npm run build
    ```
2.  Copy the compiled files inside `frontend/dist/*` to the backend's static directory:
    ```bash
    cp -r dist/* ../spring-backend/src/main/resources/static/
    ```
3.  Build the unified Spring Boot JAR:
    ```bash
    cd ../spring-backend
    ./mvnw clean package -DskipTests
    ```
4.  Run the backend JAR. Your frontend will be served directly on port `8080` (e.g. `http://localhost:8080/`), meaning zero CORS issues!
    ```bash
    java -jar target/spring-backend-0.0.1-SNAPSHOT.jar
    ```

### Topology B: Decoupled Deployment (AWS, GCP, Render, Vercel)
Run frontend and backend as two isolated applications.
1.  **Deploy Backend (Spring Boot)**:
    *   You can containerize it using the existing `Dockerfile` in `spring-backend`.
    *   Ensure to supply the required database and OAuth2 environment variables to the container configuration.
2.  **Deploy Frontend (Vercel / Netlify / Cloudflare Pages)**:
    *   Point Vite requests directly to the production backend by configuring your frontend domain settings or routing proxy.

---

## 🛠️ Local Development Setup

### Prerequisite Checklist
*   Java Development Kit (JDK) 25
*   Node.js (v18+) & npm
*   MySQL Server running locally

### Running Backend
1.  Create a MySQL database named `blog_db`.
2.  Navigate to `spring-backend` and configure your credentials inside `src/main/resources/application-dev.yaml`.
3.  Launch the Spring Boot server:
    ```bash
    ./mvnw spring-boot:run
    ```

### Running Frontend
1.  Navigate to `frontend`.
2.  Install dependencies:
    ```bash
    npm install
    ```
3.  Start the Vite development server:
    ```bash
    npm run dev
    ```
4.  Access the UI at `http://localhost:5173/`. Vite will proxy API requests automatically to `http://localhost:8080/`.
