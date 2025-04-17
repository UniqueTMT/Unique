//package com.unique.config;
//
//import com.unique.kafka.AnswerKafkaDTO;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.*;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.*;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//
//import java.util.*;
//
//@Configuration
//public class KafkaProducerConfig {
//
//  @Value("${spring.kafka.bootstrap-servers}")
//  private String bootstrapServers;
//
//  // Producer 구성 ---------------------------------------------------------------------------------------------------------------
//
//  @Bean
//  public ProducerFactory<String, Object> producerFactory() {
//    Map<String, Object> config = new HashMap<>();
//    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//    return new DefaultKafkaProducerFactory<>(config);
//  }
//
//  @Bean
//  public KafkaTemplate<String, Object> kafkaTemplate() {
//    return new KafkaTemplate<>(producerFactory());
//  }
//
//  // Consumer 구성 ---------------------------------------------------------------------------------------------------------------
//
//  @Bean
//  public ConsumerFactory<String, AnswerKafkaDTO> consumerFactory() {
//    Map<String, Object> config = new HashMap<>();
//    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//    config.put(ConsumerConfig.GROUP_ID_CONFIG, "gpt-consumer");
//    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
//    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//    config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//    return new DefaultKafkaConsumerFactory<>(config);
//  }
//
//  @Bean
//  public ConcurrentKafkaListenerContainerFactory<String, AnswerKafkaDTO> kafkaListenerContainerFactory() {
//    ConcurrentKafkaListenerContainerFactory<String, AnswerKafkaDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
//    factory.setConsumerFactory(consumerFactory());
//    return factory;
//  }
//}
