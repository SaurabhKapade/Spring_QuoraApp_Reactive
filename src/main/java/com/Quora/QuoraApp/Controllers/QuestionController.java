package com.Quora.QuoraApp.Controllers;


import com.Quora.QuoraApp.DTO.QuestionRequestDTO;
import com.Quora.QuoraApp.DTO.QuestionResponseDTO;
import com.Quora.QuoraApp.Services.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final IQuestionService questionService;

    @PostMapping
    Mono<QuestionResponseDTO> createQuestion(@ModelAttribute QuestionRequestDTO questionRequestDTO){
        System.out.print("controller called");
        return questionService.createQuestion(questionRequestDTO)
                .doOnSuccess(response->System.out.println("Question created successfully"+response))
                .doOnError(error->System.out.println("Error while creating Question "+ error));
    }
    @DeleteMapping("{id}")
    Mono<?> deleteQuestionById(@PathVariable String id){
        return questionService.deleteQuestionById(id)
                .doOnSuccess(response->System.out.println("Question deleted Successfully"+id))
                .doOnError(response->System.out.println("Cant delete question with given id"+id));
    }

    @GetMapping("{id}")
    Mono<QuestionResponseDTO>getQuestionById(@PathVariable String id){
        return questionService.getQuestionById(id)
                .doOnSuccess(response->System.out.println("question fetched successfully "+response))
                .doOnError(error->System.out.println("error while fetching question "+error));
    }

    // offSet Paging
    @GetMapping("/ques")
    Flux<QuestionResponseDTO> searchQuestion(
            @RequestParam String query,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size
    ){
        return questionService.searchQuestion(query,page,size);
    }

    @GetMapping
    Flux<QuestionResponseDTO>getAllQuestions(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size
    ){
        return questionService.getAllQuestions(cursor,size);
    }
}
