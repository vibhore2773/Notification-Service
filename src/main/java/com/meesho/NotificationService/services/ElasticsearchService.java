package com.meesho.NotificationService.services;


import com.meesho.NotificationService.model.elasticsearch.SearchData;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchService {
    @Autowired
    RestHighLevelClient client;
    public void save(SearchData data){
        try{
            final IndexRequest indexRequest = new IndexRequest();
            indexRequest.id(data.getId());
            indexRequest.source(data, XContentType.JSON);
            final IndexResponse response = client.index(indexRequest,RequestOptions.DEFAULT);
        }
        catch (Exception E){
            System.out.println("Couldn't save data in Elasticsearch repository");
        }
    }
    public List<Map> findByMessage(String message, int page_number, int size) throws IOException {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("message",message));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(queryBuilder).from(page_number*size).size(size);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);
        List<Map> data = new ArrayList<>();
        for(SearchHit hit : response.getHits().getHits()){
            data.add(hit.getSourceAsMap());
        }
        return data;
    }
    public List<Map> findByRange(Date from, Date to, int page_number, int size) throws IOException {

        long gte = from.getTime();
        long lte = to.getTime();
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("created_at").gte(gte).lte(lte));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(queryBuilder).from(page_number*size).size(size);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        List<Map> data = new ArrayList<>();

        for(SearchHit hit : response.getHits().getHits() ){

            data.add(hit.getSourceAsMap());
        }
        return data;
    }
}
