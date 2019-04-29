package cn.gmw.api.meiyou.service;

import cn.gmw.api.meiyou.entity.ReleaseLib;
import cn.gmw.api.meiyou.utils.DateUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
*  原静态方法均改为非静态 public static String XXX
*/

@Slf4j
@Service
@Scope("prototype")
public class ElasticSearchService {
    @Autowired
    private RestHighLevelClient client;

    //查询ES参数准备
    public List<ReleaseLib> stringQueryByES(String index, String type, String masterids, int from, int size) {
        List<ReleaseLib> list = null;
        //构建查询  //SearchSourceBuilder?
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        //QueryBuilders.boolQuery?
        //QueryBuilders.termsQuery?
        //QueryBuilders.rangeQuery？
        //sourceBuilder.postFilter?
        //boolQuery.filter？
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (masterids != null) {
            if (masterids.indexOf(",") != -1) {
//                    String[] ms = masterIds.split(",");
                boolQuery.filter(QueryBuilders.termsQuery("masterId", masterids.split(",")));
            } else {
                boolQuery.filter(QueryBuilders.termQuery("masterId", masterids));
            }
        }
       sourceBuilder.postFilter(QueryBuilders.rangeQuery("pubTime")
					.from(DateUtil.getToday0OclockMillis())
                .to(System.currentTimeMillis() - 3 * 60 * 1000));
        /* sourceBuilder.postFilter(QueryBuilders.rangeQuery("pubTime")
                .from(DateUtil.getTimemills("2019-04-01 00:00:00"))
                .to(DateUtil.getTimemills("2019-04-30 00:00:00")));*/
        sourceBuilder.sort("pubTime", SortOrder.DESC);

        sourceBuilder.query(boolQuery);
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type);

        list = esSearch(sourceBuilder, searchRequest);
        // TODO Auto-generated catch block
        return list;
    }

    //ES查询，将结果转化为List
    public List<ReleaseLib> esSearch(SearchSourceBuilder sourceBuilder, SearchRequest searchRequest) {
        searchRequest.source(sourceBuilder);
        SearchResponse response = null;
        List<ReleaseLib> list = new ArrayList<>();

        try {
            response = client.search(searchRequest);
            SearchHits hits = response.getHits();

            for (SearchHit hit : hits) {
                Map<String, Object> source = hit.getSourceAsMap();
                Gson gson = new Gson();
                String s = gson.toJson(source);
                ReleaseLib article = gson.fromJson(s, ReleaseLib.class);
                list.add(article);
            }
        } catch (IOException e) {
            log.error("Query Failed:" + e.getMessage());
        }

        return list;
    }

    //根据ID，检索某一个索引文档
    public String getDocById(String id, String index, String type) {
        GetRequest searchRequest = new GetRequest(index, type, id);
        GetResponse response = null;
        String json = "";
        try {
            response = client.get(searchRequest);
            json = response.getSourceAsString();
            //
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }
}
