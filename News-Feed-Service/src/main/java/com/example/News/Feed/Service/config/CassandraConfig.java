package com.example.News.Feed.Service.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;

import java.net.InetSocketAddress;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "newsfeed";
    }

    @Override
    protected String getLocalDataCenter() {
        return "datacenter1";
    }

    @Override
    protected String getContactPoints() {
        return "cassandra"; // Docker hostname (i think it is the service name not the docker container name)
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

        try (CqlSession tempSession = CqlSession.builder()
                .withLocalDatacenter("datacenter1")
                .addContactPoint(new InetSocketAddress("cassandra", 9042))
                .build()) {

            // ✅ Step 1: Create Keyspace
            tempSession.execute("""
                CREATE KEYSPACE IF NOT EXISTS newsfeed 
                WITH replication = {
                    'class': 'SimpleStrategy',
                    'replication_factor': 1
                }
            """);
            System.out.println("✅ Keyspace 'newsfeed' initialized successfully.");

            // ✅ Step 2: Create the Table
            tempSession.execute("""
                CREATE TABLE IF NOT EXISTS newsfeed.feed_items (
                    user_id TEXT,
                    upload_time TIMESTAMP,
                    video_id TEXT,
                    bucket_name TEXT,
                    caption TEXT,
                    tags LIST<TEXT>,
                    duration_seconds DOUBLE,
                    PRIMARY KEY (user_id, upload_time)
                ) WITH CLUSTERING ORDER BY (upload_time DESC);
            """);
            System.out.println("✅ Table 'feed_items' initialized successfully.");

        } catch (Exception e) {
            System.err.println("❌ Failed to create keyspace or table: " + e.getMessage());
        }


        session.setKeyspaceName("newsfeed");
        return session;
    }
}

