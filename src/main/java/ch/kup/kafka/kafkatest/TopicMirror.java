package ch.kup.kafka.kafkatest;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class TopicMirror {

    private final Queue<String> messages = new ConcurrentLinkedQueue<>();

    public final String topicName;

    public TopicMirror(String topicName) {
        this.topicName = topicName;
    }

    @KafkaListener(topicPartitions = @TopicPartition(
            topic = "#{__listener.topicName}",
            partitions = "#{@finder.partitions(__listener.topicName)}",
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
