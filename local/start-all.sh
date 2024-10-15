#!/bin/bash

# Load environment variables from .env file
if [ -f .local.env ]; then
  echo "Using .local.env file"
else
  echo ".env file not found"
  exit 1
fi

# Start both PostgreSQL and Spring Boot services
echo "Starting services..."
docker-compose -f docker-compose-local.yaml --env-file .local.env up -d

if [ $? -eq 0 ]; then
  echo "Services started."
else
  echo "Failed to start the services."
fi
