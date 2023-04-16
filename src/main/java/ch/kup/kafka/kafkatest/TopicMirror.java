package ch.kup.kafka.kafkatest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class TopicMirror<K, V> {

    private final Queue<V> messages = new ConcurrentLinkedQueue<>();

    public final String topicName;

    public TopicMirror(String topicName) {
        this.topicName = topicName;
    }

    @KafkaListener(topicPartitions = @TopicPartition(
            topic = "#{__listener.topicName}",
            partitions = "#{@finder.partitions(__listener.topicName)}",
            partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0")))
    public void listen(ConsumerRecord<K, V> consumerRecord) {
        messages.add(consumerRecord.value());
    }

    public String findMessages(String search) {
        return messages.stream()
                .map(Object::toString)
                .filter(s -> s.contains(search))
                .collect(Collectors.joining("\n"));
    }

}
