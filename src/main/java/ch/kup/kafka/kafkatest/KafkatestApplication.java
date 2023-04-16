package ch.kup.kafka.kafkatest;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;

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

    @Bean
    public TopicMirror<String, String> topic1Mirror() {
        return new TopicMirror<>("topic1");
    }

    @Bean
    public PartitionFinder finder(ConsumerFactory<String, String> consumerFactory) {
        return new PartitionFinder(consumerFactory);
    }

    public static class PartitionFinder {

        public PartitionFinder(ConsumerFactory<String, String> consumerFactory) {
            this.consumerFactory = consumerFactory;
        }

        private final ConsumerFactory<String, String> consumerFactory;

        public String[] partitions(String topic) {
            try (Consumer<String, String> consumer = consumerFactory.createConsumer()) {
                return consumer.partitionsFor(topic).stream()
                        .map(pi -> Integer.toString(pi.partition()))
                        .toArray(String[]::new);
            }
        }

    }

}

