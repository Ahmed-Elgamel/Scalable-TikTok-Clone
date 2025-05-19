package com.example.VideoService.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;

import java.net.InetSocketAddress;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "videoservice";
    }

    @Override
    protected String getLocalDataCenter() {
        return "datacenter1";
    }

    @Override
    protected String getContactPoints() {
        return "cassandra";
    }

    @Override
    protected int getPort() {
        return 9042;
    }

    @Bean
    @Primary
    public CqlSessionFactoryBean session() {
        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
        session.setContactPoints("cassandra");
        session.setLocalDatacenter("datacenter1");
        session.setPort(9042);

        // 🔹 Initialize the Keyspace and Tables if they do not exist
        try (CqlSession tempSession = CqlSession.builder()
                .withLocalDatacenter("datacenter1")
                .addContactPoint(new InetSocketAddress("cassandra", 9042))
                .build()) {

            // ✅ Step 1: Create Keyspace
            tempSession.execute("""
                CREATE KEYSPACE IF NOT EXISTS videoservice 
                WITH replication = {
                    'class': 'SimpleStrategy',
                    'replication_factor': 1
                }
            """);
            System.out.println("✅ Keyspace 'videoservice' initialized successfully.");

            // ✅ Step 2: Create Tables
            tempSession.execute("""
                CREATE TABLE IF NOT EXISTS videoservice.user_videos (
                    user_id UUID,
                    upload_time TIMESTAMP,
                    video_id TEXT,
                    duration_seconds DOUBLE,
                    size_bytes BIGINT,
                    caption TEXT,
                    bucket_name TEXT,
                    tags LIST<TEXT>,
                    PRIMARY KEY ((user_id), upload_time)
                ) WITH CLUSTERING ORDER BY (upload_time DESC);
            """);
            System.out.println("✅ Table 'user_videos' initialized successfully.");

            tempSession.execute("""
                CREATE TABLE IF NOT EXISTS videoservice.user_saved_videos (
                    user_id UUID,
                    save_time TIMESTAMP,
                    saved_video_id TEXT,
                    PRIMARY KEY (user_id, save_time)
                ) WITH CLUSTERING ORDER BY (save_time DESC);
            """);
            System.out.println("✅ Table 'user_saved_videos' initialized successfully.");

            tempSession.execute("""
                CREATE TABLE IF NOT EXISTS videoservice.user_video_ratings (
                    user_id UUID,
                    video_id TEXT,
                    rating INT,
                    PRIMARY KEY (user_id, video_id)
                );
            """);
            System.out.println("✅ Table 'user_video_ratings' initialized successfully.");

        } catch (Exception e) {
            System.err.println("❌ Failed to create keyspace or tables: " + e.getMessage());
        }

        session.setKeyspaceName("videoservice");
        return session;
    }
}
