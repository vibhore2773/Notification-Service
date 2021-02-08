package com.meesho.NotificationService.repository;

import com.meesho.NotificationService.model.SearchData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchRepository extends ElasticsearchRepository<SearchData,String> {
    List<SearchData> findByMessage(String text);
}
