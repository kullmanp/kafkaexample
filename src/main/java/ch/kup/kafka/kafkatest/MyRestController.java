package ch.kup.kafka.kafkatest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@RestController
public class MyRestController {

    @Autowired
    private KafkaTemplate<String, String> template;
    @Autowired
    private MyTopicMirror myTopicMirror;

    @GetMapping("/")
    public String hello() {
        String msg = "Hello " + LocalDateTime.now();
        sendALotOfMessages(msg);
        return msg;
    }

    private void sendALotOfMessages(String msg) {
        IntStream.range(0, 1_000_000).forEach(
                i -> template.send("topic1", i + " " + msg)
        );
    }

    @GetMapping(value = "find", produces = "text/plain")
    public String find(@RequestParam("q") String search) {
        return myTopicMirror.findMessages(search);
    }
}
