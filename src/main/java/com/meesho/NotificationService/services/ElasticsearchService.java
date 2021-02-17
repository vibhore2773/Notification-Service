package com.meesho.NotificationService.services;

import com.meesho.NotificationService.model.elasticsearch.SearchData;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface ElasticsearchService {
    public void save(SearchData data);
    public List<SearchData> findByMessage(String message, int page_number, int size) throws IOException;
    public List<SearchData> findByRange(Date from, Date to, int page_number, int size) throws IOException;
}
