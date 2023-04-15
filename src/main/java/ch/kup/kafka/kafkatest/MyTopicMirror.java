package ch.kup.kafka.kafkatest;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Component
public class MyTopicMirror {

    private final Queue<String> messages = new ConcurrentLinkedQueue<>();

    @KafkaListener(topicPartitions = @TopicPartition(topic = "topic1", partitions = "0-2",
            partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0")))
    public void listen(String in) {
        messages.add(in);
    }

    public String findMessages(String search) {
        return messages.stream()
                .filter(s -> s.contains(search))
                .collect(Collectors.joining("\n"));
    }

}
