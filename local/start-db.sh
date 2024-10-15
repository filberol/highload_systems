#!/bin/bash

# Load environment variables from .env file
if [ -f .local.env ]; then
  echo "Using .local.env file"
else
  echo ".env file not found"
  exit 1
fi

# Start the PostgreSQL database container
echo "Starting PostgreSQL database..."
docker-compose -f docker-compose-local.yaml --env-file .local.env up -d postgres

if [ $? -eq 0 ]; then
  echo "PostgreSQL database started successfully."
else
  echo "Failed to start PostgreSQL database."
fi
