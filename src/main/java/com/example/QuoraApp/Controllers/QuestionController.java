package com.example.QuoraApp.Controllers;

import com.example.QuoraApp.DTO.QuestionRequestDTO;
import com.example.QuoraApp.DTO.QuestionResponseDTO;
import com.example.QuoraApp.Models.Question;
import com.example.QuoraApp.Models.QuestionElasticDocument;
import com.example.QuoraApp.Service.IQuestionIndexService;
import com.example.QuoraApp.Service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final IQuestionIndexService questionIndexService;
    @PostMapping
    public Mono<QuestionResponseDTO> createQuestion(@ModelAttribute QuestionRequestDTO questionRequestDTO){
        return questionService.createQuestion(questionRequestDTO)
                .doOnSuccess(response-> System.out.println("question created successfully "+response))
                .doOnError(response->System.out.println("Error while creating Question "+response));
    }

    @GetMapping("/{id}")
    public Mono<QuestionResponseDTO> getQuestionById(@PathVariable String id){
        return questionService.getQuestionById(id)
                .doOnSuccess(response->System.out.println("Question fetched successfully  "+response))
                .doOnError(response->System.out.println("Error while fething question "+response));
    }

    @GetMapping
    public Flux<QuestionResponseDTO> getAllQuestions(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size
    ){

        return questionService.getAllQuestions(cursor,size)
                .doOnError(error -> System.out.println("Error fetching questions: " + error))
                .doOnComplete(() -> System.out.println("Questions fetched successfully"));
    }

    @DeleteMapping("/{id}")
    public Mono<?> deleteQuestionById(@PathVariable  String id){
        return questionService.deleteQuestionById(id);
    }


    @GetMapping("/ques")
    public Flux<QuestionResponseDTO> searchQuestion(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size
    ){
        return questionService.searchQuestion(query,page,size);
    }

    @GetMapping("/elasticsearch")
    public List<QuestionElasticDocument>searchQuestion(@RequestParam String query){
        System.out.print("searching" + query);
        return questionService.searchQuestion(query);
    }
}
