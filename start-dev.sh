#!/bin/bash

# -------------------------------------------------------------------
# DEVELOPMENT DATABASE STARTUP SCRIPT
# - Automates PostgreSQL container, Liquibase, jOOQ, and app startup
# - Safe for repeated execution (auto-cleans old containers)
# -------------------------------------------------------------------

# Configurable variables (or set via environment variables)
DB_USER="${DB_USER:-postgres}"
DB_PASS="${DB_PASS:-postgres}"
DB_NAME="${DB_NAME:-point_of_sale}"
DB_PORT="${DB_PORT:-5432}"

# ---- Cleanup any existing container ----
echo "Cleaning up existing PostgreSQL container..."
podman stop postgres-dev >/dev/null 2>&1 || true
podman rm postgres-dev >/dev/null 2>&1 || true

# ---- Start PostgreSQL ----
echo "Starting PostgreSQL container..."
podman run -d \
  --name postgres-dev \
  -e POSTGRES_USER="${DB_USER}" \
  -e POSTGRES_PASSWORD="${DB_PASS}" \
  -e POSTGRES_DB="${DB_NAME}" \
  -p "${DB_PORT}:5432" \
  postgres:latest

# ---- Wait for database to be ready ----
echo -n "Waiting for PostgreSQL to be ready..."
timeout 30s bash -c 'until podman exec postgres-dev pg_isready -U $DB_USER -d $DB_NAME; do echo -n "."; sleep 1; done'
echo " Database ready!"

# ---- Run Liquibase ----
echo "Running database migrations..."
mvn liquibase:update

# ---- Generate jOOQ classes ----
echo "Generating jOOQ classes..."
mvn generate-sources

# ---- Start Spring Boot application ----
echo "Starting application..."
mvn spring-boot:run

# ---- Cleanup on exit ----
function cleanup {
  echo "Stopping PostgreSQL container..."
  podman stop postgres-dev >/dev/null 2>&1 || true
}
trap cleanup EXIT