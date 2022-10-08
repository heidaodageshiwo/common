package com.common.common;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.DateRangeAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.DateRangeExpression;
import co.elastic.clients.elasticsearch._types.aggregations.FieldDateMath;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//https://elasticstack.blog.csdn.net/article/details/99621105
@SpringBootTest
public class ESTest5daterange {
    @Resource(name = "clientByPasswd")
    ElasticsearchClient elasticsearchClient;



  /*  date_range 聚合
    我们可以使用 date_range 来统计在某个时间段里的文档数：*/
    @Test
    public void Filteraggs() throws IOException {
        StringReader stringReader = new StringReader("{\n" +
                "  \"size\": 0,\n" +
                "  \"aggs\": {\n" +
                "    \"birth_range\": {\n" +
                "      \"date_range\": {\n" +
                "        \"field\": \"DOB\",\n" +
                "        \"format\": \"yyyy-MM-dd\",\n" +
                "        \"ranges\": [\n" +
                "          {\n" +
                "            \"from\": \"1989-01-01\",\n" +
                "            \"to\": \"1990-01-01\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"from\": \"1991-01-01\",\n" +
                "            \"to\": \"1992-01-01\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");
        SearchRequest twitter = SearchRequest.of(
                o -> o.index("twitter").withJson(stringReader)
        );
        SearchResponse<Void> search = elasticsearchClient.search(twitter, Void.class);
        System.out.println(search);
    }
//    在上面我们查询出生年月（DOB）从1989-01-01到1990-01-01及从1991-01-01到1992-01-01的文档。显示的结果是：
  /*"aggregations" : {
        "birth_range" : {
            "buckets" : [
            {
                "key" : "1989-01-01-1990-01-01",
                    "from" : 5.99616E11,
                    "from_as_string" : "1989-01-01",
                    "to" : 6.31152E11,
                    "to_as_string" : "1990-01-01",
                    "doc_count" : 1
            },
            {
                "key" : "1991-01-01-1992-01-01",
                    "from" : 6.62688E11,
                    "from_as_string" : "1991-01-01",
                    "to" : 6.94224E11,
                    "to_as_string" : "1992-01-01",
                    "doc_count" : 1
            }
      ]
        }*/

    //https://blog.csdn.net/UbuntuTouch/article/details/99621105
    @Test
    public void filteraggs1() throws IOException {
        List<DateRangeExpression> list=new ArrayList<>();
        DateRangeExpression build1 = new DateRangeExpression.Builder()
                .from(FieldDateMath.of(fn -> fn.expr("1989-01-01"))).to(FieldDateMath.of(fn -> fn.expr("1990-01-01"))).build();
        DateRangeExpression build2 = new DateRangeExpression.Builder()
                .from(FieldDateMath.of(fn -> fn.expr("1991-01-01"))).to(FieldDateMath.of(fn -> fn.expr("1992-01-01"))).build();
        list.add(build1);
        list.add(build2);
        DateRangeAggregation build = new DateRangeAggregation.Builder().field("DOB").format("yyyy-MM-dd")
                .ranges(list)
                .build();
        SearchResponse<HashMap> search = elasticsearchClient.search(
                sa -> sa.index("twitter")
                        .aggregations("birth_range",
                                fn->fn.dateRange(build)
                                )
                , HashMap.class
        );
        System.out.println(search);
    }
//    SearchResponse: {"took":1,"timed_out":false,"_shards":{"failed":0.0,"successful":1.0,"total":1.0,"skipped":0.0},"hits":{"total":{"relation":"eq","value":6},"hits":[{"_index":"twitter","_id":"1","_score":1.0,"_source":"{uid=2, country=中国, address=中国北京市海淀区, province=北京, city=北京, DOB=1999-04-01, location={lat=39.970718, lon=116.325747}, message=今儿天气不错啊，出去转转去, user=张三, age=20}"},{"_index":"twitter","_id":"2","_score":1.0,"_source":"{uid=3, country=中国, address=中国北京市东城区台基厂三条3号, province=北京, city=北京, DOB=1997-04-01, location={lat=39.904313, lon=116.412754}, message=出发，下一站云南！, user=老刘, age=22}"},{"_index":"twitter","_id":"3","_score":1.0,"_source":"{uid=4, country=中国, address=中国北京市东城区, province=北京, city=北京, DOB=1994-04-01, location={lat=39.893801, lon=116.408986}, message=happy birthday!, user=李四, age=25}"},{"_index":"twitter","_id":"4","_score":1.0,"_source":"{uid=5, country=中国, address=中国北京市朝阳区建国门, province=北京, city=北京, DOB=1989-04-01, location={lat=39.718256, lon=116.367910}, message=123,gogogo, user=老贾, age=30}"},{"_index":"twitter","_id":"5","_score":1.0,"_source":"{uid=6, country=中国, address=中国北京市朝阳区国贸, province=北京, city=北京, DOB=1993-04-01, location={lat=39.918256, lon=116.467910}, message=Happy BirthDay My Friend!, user=老王, age=26}"},{"_index":"twitter","_id":"6","_score":1.0,"_source":"{uid=7, country=中国, address=中国上海市闵行区, province=上海, city=上海, DOB=1991-04-01, location={lat=31.175927, lon=121.383328}, message=好友来了都今天我生日，好友来了,什么 birthday happy 就成!, user=老吴, age=28}"}],"max_score":1.0},"aggregations":{"date_range#birth_range":{"buckets":[{"doc_count":1,"from":5.99616E11,"to":6.31152E11,"from_as_string":"1989-01-01","to_as_string":"1990-01-01","key":"1989-01-01-1990-01-01"},{"doc_count":1,"from":6.62688E11,"to":6.94224E11,"from_as_string":"1991-01-01","to_as_string":"1992-01-01","key":"1991-01-01-1992-01-01"}]}}}
//在上面我们查询出生年月（DOB）从1989-01-01到1990-01-01及从1991-01-01到1992-01-01的文档。显示的结果是：
}
