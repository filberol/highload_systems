#!/bin/bash

echo "Stopping services..."
docker-compose -f docker-compose-local.yaml --env-file .local.env down

if [ $? -eq 0 ]; then
  echo "Services stopped."
else
  echo "Failed to stop the services."
fi
