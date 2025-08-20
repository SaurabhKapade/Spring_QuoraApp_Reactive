package com.example.QuoraApp.Producers;

import com.example.QuoraApp.Config.KafkaConfig;
import com.example.QuoraApp.Events.ViewCountEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void publishViewCountEvent(ViewCountEvent viewCountEvent){
        kafkaTemplate.send(KafkaConfig.TOPIC_NAME,viewCountEvent.getTargetId(),viewCountEvent)
                .whenComplete((response,error)->{
                    if(error != null){
                        System.out.print("Error while publishing view count event "+ error);
                    }
                });
    }
}
