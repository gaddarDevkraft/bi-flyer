# Project Setup Guide

This guide helps you set up the local development environment for `bi-flyer`.

## Prerequisites

- **Java 21**: Ensure Java 21 is installed (`java -version`).
- **Docker**: Ensure Docker and Docker Compose are installed and running.

## Local Environment Setup (Docker Dependencies)

We use Docker Compose to run the required dependencies: **Keycloak** and **PostgreSQL**.

1. **Start Services**:
   Run the following command in the project root:
   ```bash
   docker-compose up -d
   ```
   This will start:
   - Keycloak on port `9090` (Console: http://localhost:9090)
   - PostgreSQL on port `5432`

2. **Verify Services**:
   Check if containers are running:
   ```bash
   docker ps
   ```

## Application Setup

### Option 1: Run Locally (Default Profile)
This profiles uses the **local** Keycloak and **local** Postgres.
1. Ensure `docker-compose` services are running.
2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   Or with the specific profile explicitly:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=default
   ```

### Option 2: Run with Dev Profile
This profile uses the **remote** Keycloak (`kc-genaiapps.indegene.com`) and **local** Postgres.
1. Ensure `docker-compose` is running (at least for Postgres).
2. Run the application:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

## Database Information
- **URL**: `jdbc:postgresql://localhost:5432/bi_flyer`
- **Username**: `postgres`
- **Password**: `postgres`
- **Database Name**: `bi_flyer`

## Keycloak Information (Local)
- **URL**: `http://localhost:9090`
- **Admin User**: `admin`
- **Admin Password**: `password`
- **Realm**: The `pm` realm is expected by the application configuration. You may need to create this manually or import it if the persistent volume is empty.

## Troubleshooting
- **Port Conflicts**: Ensure ports `9090` and `5432` are free.
- **Java Version**: If you see build errors, verify you are using Java 21.
