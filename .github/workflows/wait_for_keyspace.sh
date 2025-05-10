#!/bin/bash

# List of keyspaces you want to check
KEYSPACES=("newsfeed" "videoservice" )

# Function to check if a keyspace exists
check_keyspace_exists() {
  local keyspace=$1
  docker exec cassandra cqlsh -e "DESCRIBE KEYSPACES;" | grep -q "$keyspace"
}

# Loop to check for each keyspace
for keyspace in "${KEYSPACES[@]}"; do
  echo "Waiting for keyspace '$keyspace' to be available..."

  # Check until the keyspace is available
  until check_keyspace_exists "$keyspace"; do
    echo "Keyspace '$keyspace' not found. Retrying..."
    sleep 10
  done

  echo "Keyspace '$keyspace' is available!"
done

# Restart the required containers once all keyspaces are available
echo "All keyspaces are available. Restarting containers..."
docker-compose restart video-ms newsfeed-ms  
