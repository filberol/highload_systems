#!/bin/bash

# Stop the PostgreSQL database container
echo "Stopping PostgreSQL database..."
docker-compose -f docker-compose-local.yaml --env-file .local.env stop postgres

if [ $? -eq 0 ]; then
  echo "PostgreSQL database stopped successfully."
else
  echo "Failed to stop PostgreSQL database."
fi
