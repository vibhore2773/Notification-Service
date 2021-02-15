package com.meesho.NotificationService.repository;

import com.meesho.NotificationService.model.elasticsearch.SearchData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface SearchRepository extends ElasticsearchRepository<SearchData,String> {

    Page<SearchData> findByMessageContains(String text, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"message\": \"?0\"}}]}}")
    Page<SearchData> findByMessageUsingCustomQuery(String message, Pageable pageable);
}
