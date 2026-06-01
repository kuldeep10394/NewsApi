# Spring Boot + Vue JWT Starter

## What is included
- Spring Boot backend (Maven) with JWT auth (login/register) and a protected `/api/hello` endpoint.
- Vue 3 + Vite frontend with Tailwind CSS.
- Frontend builds to `frontend/dist` which can be copied into `backend/src/main/resources/static` for an embedded UI.

## Quick start (local)

1. Start PostgreSQL:
   ```bash
   docker run -d --name pg -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=demo -p 5432:5432 postgres
   ```

2. (Optional) Build frontend (for production embedding):
   ```bash
   cd frontend
   npm install
   npm run build
   # Copy the generated /dist content into backend/src/main/resources/static
   ```

3. Build and run backend:
   ```bash
   cd backend
   mvn clean package
   mvn spring-boot:run
   ```

4. Open http://localhost:8080 in your browser (or run frontend dev server with `npm run dev` in `frontend`).

## Notes
- Replace the JWT secret in `backend/src/main/resources/application.yml` before production.
- This is a minimal starter; add validation, error handling, CORS, HTTPS, and other production hardening as required.
