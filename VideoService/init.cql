-- Create keyspace if it doesn't exist
CREATE KEYSPACE IF NOT EXISTS videoservice WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};

-- Use the keyspace
USE videoservice;

-- Create the user_videos table
CREATE TABLE IF NOT EXISTS user_videos (
  user_id UUID,             -- Partition key
  upload_time TIMESTAMP,    -- Clustering key
  video_id TEXT,            -- Unique identifier of the video (as string)
  duration_seconds DOUBLE,  -- Duration of the video
  size_bytes BIGINT,        -- Size in bytes
  caption TEXT,             -- Video caption
  bucket_name TEXT,         -- Bucket name video is stored in
  PRIMARY KEY ((user_id), upload_time)
) WITH CLUSTERING ORDER BY (upload_time DESC);
