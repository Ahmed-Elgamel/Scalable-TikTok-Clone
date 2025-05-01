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
        return "cassandra"; // Docker hostname
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
        session.setKeyspaceName("newsfeed");
        session.setLocalDatacenter("datacenter1");
        session.setPort(9042);
        return session;
    }
}

