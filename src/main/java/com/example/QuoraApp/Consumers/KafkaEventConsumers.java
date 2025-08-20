package com.example.QuoraApp.Consumers;

import com.example.QuoraApp.Config.KafkaConfig;
import com.example.QuoraApp.Events.ViewCountEvent;
import com.example.QuoraApp.Repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventConsumers {
    private final QuestionRepository questionRepository;

    @KafkaListener(
            topics = KafkaConfig.TOPIC_NAME,
            groupId = "view-count-consumer",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleViewCount(ViewCountEvent viewCountEvent){
        questionRepository.findById(viewCountEvent.getTargetId())
                .flatMap(question -> {
                    question.setViews(question.getViews()==null ? 0 : question.getViews()+1);
                    return questionRepository.save(question);
                })
                .subscribe(updatedQuestion-> {
                    System.out.print("ViewCount Inceremented for question " + updatedQuestion.getId());
                },error->{
                    System.out.println("Error incrementing View count for question "+error.getMessage());
                });
    }
}
