package com.meesho.NotificationService.services.serviceImpl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.meesho.NotificationService.model.elasticsearch.SearchData;
import com.meesho.NotificationService.services.ElasticsearchService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
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

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {
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
    public List<SearchData> findByMessage(String message, int page_number, int size) throws IOException {

        QueryBuilder matchQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("message",message).fuzziness(Fuzziness.AUTO));
                                                                ;
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(matchQuery).from(page_number*size).size(size);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);

        SearchResponse response = client.search(searchRequest,RequestOptions.DEFAULT);

        List<SearchData> data = new ArrayList<>();
        for(SearchHit hit : response.getHits().getHits()){
            ObjectMapper objectMapper = new ObjectMapper();
            SearchData item = objectMapper.readValue(hit.getSourceAsString(),SearchData.class);
            data.add(item);
        }
        return data;
    }
    public List<SearchData> findByRange(Date from, Date to, int page_number, int size) throws IOException {

        long gte = from.getTime();
        long lte = to.getTime();
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("created_at").gte(gte).lte(lte));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(queryBuilder).from(page_number*size).size(size);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        List<SearchData> data = new ArrayList<>();

        for(SearchHit hit : response.getHits().getHits() ){
            ObjectMapper objectMapper = new ObjectMapper();
            SearchData item = objectMapper.readValue(hit.getSourceAsString(),SearchData.class);
            data.add(item);
        }
        return data;
    }
}
