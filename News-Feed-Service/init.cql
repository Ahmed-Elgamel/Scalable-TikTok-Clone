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
CREATE TABLE IF NOT EXISTS feed_items (
    user_id TEXT,
    upload_time TIMESTAMP,
    video_id TEXT,
    bucket_name TEXT,
    caption TEXT,
    PRIMARY KEY (user_id, upload_time)
) WITH CLUSTERING ORDER BY (upload_time DESC);

