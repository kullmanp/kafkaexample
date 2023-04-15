package ch.kup.kafka.kafkatest;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class KafkatestApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkatestApplication.class, args);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("topic1")
                .partitions(3)
                .replicas(1)
                .build();
    }


}
