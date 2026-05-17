package com.example.QuoraApp.Repositories;

import com.example.QuoraApp.Models.QuestionElasticDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDocumentRepository extends ElasticsearchRepository<QuestionElasticDocument,String> {
    @Query("""
    {
      "multi_match": {
        "query": "?0",
        "fields": ["title", "content"]
      }
    }
    """)
    List<QuestionElasticDocument>searchByText(String title,String content);
}