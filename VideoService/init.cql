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
  tags LIST<TEXT>,          -- list of tags of the video
  PRIMARY KEY ((user_id), upload_time)
) WITH CLUSTERING ORDER BY (upload_time DESC);


-- Create Saved Videos Table
CREATE TABLE IF NOT EXISTS user_saved_videos (
    user_id UUID,
    save_time TIMESTAMP,
    saved_video_id TEXT,
    PRIMARY KEY (user_id, save_time)
) WITH CLUSTERING ORDER BY (save_time DESC);


-- Create Ratings Table
CREATE TABLE IF NOT EXISTS user_video_ratings (
    user_id UUID,
    video_id TEXT,
    rating INT,
    PRIMARY KEY (user_id, video_id)
);