package com.example.VideoService.config;

import com.example.VideoService.dto.FetchUserVideosEventRequest;
import com.example.VideoService.dto.FetchUserVideosEventResponse;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;



import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Bean
    public KafkaTemplate<String, String> kafkaVideoUploadTemplate() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // serialize the ket into byes
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // serialize the value into bytes
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }

    @Bean
    public KafkaTemplate<String, FetchUserVideosEventResponse> kafkaUserVideosResponseTemplate() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // serialize the key into bytes
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // serialize the value into JSON
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProps));
    }





    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FetchUserVideosEventRequest> fetchUserVideosKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FetchUserVideosEventRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(fetchUserVideosConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, FetchUserVideosEventRequest> fetchUserVideosConsumerFactory() {
        JsonDeserializer<FetchUserVideosEventRequest> deserializer = new JsonDeserializer<>(FetchUserVideosEventRequest.class,false);
        deserializer.addTrustedPackages("com.example.VideoService.dto", "com.example.News.Feed.Service.dto");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "newsfeed-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
//        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, FetchUserVideosEventRequest.class);


        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }


}
