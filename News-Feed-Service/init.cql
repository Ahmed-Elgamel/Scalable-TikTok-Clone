-- this script is used to initialize the cassandra database for the newsfeed service

-- Step 1: Create a keyspace
CREATE KEYSPACE IF NOT EXISTS newsfeed
WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};

-- Step 2: Use the new keyspace
USE newsfeed;

-- Step 3: Create a user_feed table
CREATE TABLE IF NOT EXISTS user_feed (
    user_id TEXT,
    timestamp TIMESTAMP,
    video_id TEXT,
    PRIMARY KEY (user_id, timestamp)
) WITH CLUSTERING ORDER BY (timestamp DESC);
