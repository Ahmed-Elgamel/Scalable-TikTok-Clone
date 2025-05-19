package com.example.News.Feed.Service.config;


import com.example.News.Feed.Service.dto.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaTemplate<String, RequestFolloweesEvent> kafkaFetchUserFolloweesTemplate() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // serialize the ket into byes
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // serialize the value into bytes
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }

    @Bean
    public KafkaTemplate<String, FetchUserVideosEventRequest> kafkaVideoUploadTemplate() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // serialize the ket into byes
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // serialize the value into bytes
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FetchUserVideosEventResponse> fetchUserVideosKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FetchUserVideosEventResponse> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(fetchUserVideosConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, FetchUserVideosEventResponse> fetchUserVideosConsumerFactory() {
        JsonDeserializer<FetchUserVideosEventResponse> deserializer = new JsonDeserializer<>(FetchUserVideosEventResponse.class,false);
        deserializer.addTrustedPackages("com.example.VideoService.dto", "com.example.News.Feed.Service.dto");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "newsfeed-videos-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }





    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FolloweesResponseEvent> fetchUserFolloweesKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FolloweesResponseEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(fetchUserFolloweesConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, FolloweesResponseEvent> fetchUserFolloweesConsumerFactory() {
        JsonDeserializer<FolloweesResponseEvent> deserializer = new JsonDeserializer<>(FolloweesResponseEvent.class,false);
        deserializer.addTrustedPackages("com.example.VideoService.dto", "com.example.News.Feed.Service.dto");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "newsfeed-followees-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, VideoDTO> videoUploadedKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, VideoDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(videoUploadedConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, VideoDTO> videoUploadedConsumerFactory() {
        JsonDeserializer<VideoDTO> deserializer = new JsonDeserializer<>(VideoDTO.class,false);
        deserializer.addTrustedPackages("com.example.VideoService.dto", "com.example.News.Feed.Service.dto");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "newsfeed-video-upload-events-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }








}
