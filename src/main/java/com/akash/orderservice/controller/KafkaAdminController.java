package com.akash.orderservice.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/kafka")
@AllArgsConstructor
public class KafkaAdminController {

    private final AdminClient adminClient;

    /**
     * Delete and recreate a topic (clears all messages)
     */
    @PostMapping("/topics/{topic}/reset")
    public String resetTopic(@PathVariable String topic) throws ExecutionException, InterruptedException {
        // Delete topic
        KafkaFuture<Void> deleteFuture = adminClient.deleteTopics(Collections.singleton(topic)).all();
        deleteFuture.get();

        // Recreate topic with default partitions/replication
        NewTopic newTopic = new NewTopic(topic, 3, (short) 1);
        KafkaFuture<Void> createFuture = adminClient.createTopics(Collections.singleton(newTopic)).all();
        createFuture.get();

        return "Topic '" + topic + "' has been deleted and recreated (all messages cleared).";
    }

    /**
     * Reset consumer group offsets for a topic (does not delete messages)
     */
    @PostMapping("/topics/{topic}/resetOffsets")
    public String resetConsumerOffsets(
            @PathVariable String topic,
            @RequestParam String groupId
    ) {
        // Implement offset reset logic if needed (uses AdminClient or KafkaConsumer)
        // For brevity, we’ll just return a placeholder
        return "Consumer offsets for group '" + groupId + "' on topic '" + topic + "' would be reset.";
    }
}
