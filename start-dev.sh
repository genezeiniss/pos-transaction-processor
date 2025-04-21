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

TEST_MODE=false
if [[ "$1" == "--test" ]]; then
  TEST_MODE=true
  DB_NAME="${DB_NAME}_test"
  DB_PORT="5433"
  CONTAINER_NAME="postgres-test"
else
  CONTAINER_NAME="postgres-dev"
fi

# ---- Cleanup ----
echo "Cleaning up existing container..."
podman stop $CONTAINER_NAME >/dev/null 2>&1 || true
podman rm $CONTAINER_NAME >/dev/null 2>&1 || true

# ---- Start PostgreSQL ----
echo "Starting PostgreSQL container..."
podman run -d \
  --name $CONTAINER_NAME \
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
mvn liquibase:update -Dliquibase.url=jdbc:postgresql://localhost:${DB_PORT}/${DB_NAME}

# ---- Generate jOOQ classes ----
echo "Generating jOOQ classes..."
mvn generate-sources -Dliquibase.url=jdbc:postgresql://localhost:${DB_PORT}/${DB_NAME}

if $TEST_MODE; then
  # ---- Run Tests ----
  echo "Executing integration tests..."
  mvn verify -Pintegration-tests \
    -Dspring.datasource.url=jdbc:postgresql://localhost:${DB_PORT}/${DB_NAME}
else
  # ---- Start Application ----
  echo "Starting application..."
  mvn spring-boot:run \
    -Dspring.datasource.url=jdbc:postgresql://localhost:${DB_PORT}/${DB_NAME}
fi

# ---- Cleanup on exit ----
function cleanup {
  echo "Stopping PostgreSQL container..."
  podman stop postgres-dev >/dev/null 2>&1 || true
}
trap cleanup EXIT