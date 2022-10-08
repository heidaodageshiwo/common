package com.common.common;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch.core.FieldCapsRequest;
import co.elastic.clients.elasticsearch.core.FieldCapsResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.util.ObjectBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * https://blog.csdn.net/UbuntuTouch/article/details/99621105
 * DELETE twitter
 * <p>
 * PUT twitter
 * {
 * "mappings": {
 * "properties": {
 * "DOB": {
 * "type": "date"
 * },
 * "address": {
 * "type": "text",
 * "fields": {
 * "keyword": {
 * "type": "keyword",
 * "ignore_above": 256
 * }
 * }
 * },
 * "age": {
 * "type": "long"
 * },
 * "city": {
 * "type": "keyword"
 * },
 * "country": {
 * "type": "keyword"
 * },
 * "location": {
 * "type": "geo_point"
 * },
 * "message": {
 * "type": "text",
 * "fields": {
 * "keyword": {
 * "type": "keyword",
 * "ignore_above": 256
 * }
 * }
 * },
 * "province": {
 * "type": "keyword"
 * },
 * "uid": {
 * "type": "long"
 * },
 * "user": {
 * "type": "text",
 * "fields": {
 * "keyword": {
 * "type": "keyword",
 * "ignore_above": 256
 * }
 * }
 * }
 * }
 * }
 * }
 * <p>
 * POST _bulk
 * {"index":{"_index":"twitter","_id":1}}
 * {"user":"张三","message":"今儿天气不错啊，出去转转去","uid":2,"age":20,"city":"北京","province":"北京","country":"中国","address":"中国北京市海淀区","location":{"lat":"39.970718","lon":"116.325747"}, "DOB": "1999-04-01"}
 * {"index":{"_index":"twitter","_id":2}}
 * {"user":"老刘","message":"出发，下一站云南！","uid":3,"age":22,"city":"北京","province":"北京","country":"中国","address":"中国北京市东城区台基厂三条3号","location":{"lat":"39.904313","lon":"116.412754"}, "DOB": "1997-04-01"}
 * {"index":{"_index":"twitter","_id":3}}
 * {"user":"李四","message":"happy birthday!","uid":4,"age":25,"city":"北京","province":"北京","country":"中国","address":"中国北京市东城区","location":{"lat":"39.893801","lon":"116.408986"}, "DOB": "1994-04-01"}
 * {"index":{"_index":"twitter","_id":4}}
 * {"user":"老贾","message":"123,gogogo","uid":5,"age":30,"city":"北京","province":"北京","country":"中国","address":"中国北京市朝阳区建国门","location":{"lat":"39.718256","lon":"116.367910"}, "DOB": "1989-04-01"}
 * {"index":{"_index":"twitter","_id":5}}
 * {"user":"老王","message":"Happy BirthDay My Friend!","uid":6,"age":26,"city":"北京","province":"北京","country":"中国","address":"中国北京市朝阳区国贸","location":{"lat":"39.918256","lon":"116.467910"}, "DOB": "1993-04-01"}
 * {"index":{"_index":"twitter","_id":6}}
 * {"user":"老吴","message":"好友来了都今天我生日，好友来了,什么 birthday happy 就成!","uid":7,"age":28,"city":"上海","province":"上海","country":"中国","address":"中国上海市闵行区","location":{"lat":"31.175927","lon":"121.383328"}, "DOB": "1991-04-01"}
 * <p>
 * GET twitter/_field_caps?fields=country
 */
@SpringBootTest
public class ESTest2 {
    @Resource(name = "clientByPasswd")
    ElasticsearchClient elasticsearchClient;

    @Test
    public void field_caps() throws IOException {
//        GET twitter/_field_caps?fields=country
//        FieldCapsResponse fieldCapsResponse = elasticsearchClient.fieldCaps(FieldCapsRequest.of(f -> f.index("twitter").fields("country")));
        //1
        /* FieldCapsRequest build = new FieldCapsRequest.Builder().index("twitter").fields("country").build();
        FieldCapsResponse fieldCapsResponse = elasticsearchClient.fieldCaps(build);
        System.out.println(fieldCapsResponse);*/
        //2
        /*FieldCapsResponse fieldCapsResponse = elasticsearchClient.fieldCaps(f -> f.index("twitter").fields("country"));
        System.out.println(fieldCapsResponse);*/
        //3
        FieldCapsResponse fieldCapsResponse = elasticsearchClient.fieldCaps(FieldCapsRequest.of(f -> f.index("twitter").fields("country")));
        //4 https://blog.csdn.net/yangbindxj/article/details/122708639
        //4 https://langyastudio.blog.csdn.net/article/details/119177995
        Function<FieldCapsRequest.Builder, ObjectBuilder<FieldCapsRequest>> fn = i -> i.index("twitter").fields("country");
        elasticsearchClient.fieldCaps(FieldCapsRequest.of(fn));
    }

    //    我们可以把用户进行年龄分段，查出来在不同的年龄段的用户：https://blog.csdn.net/UbuntuTouch/article/details/99621105
    @Test
    public void rangeaggs() throws IOException {
        StringReader stringReader = new StringReader("{\n" +
                "  \"size\": 0,\n" +
                "  \"aggs\": {\n" +
                "    \"age\": {\n" +
                "      \"range\": {\n" +
                "        \"field\": \"age\",\n" +
                "        \"ranges\": [\n" +
                "          {\n" +
                "            \"from\": 20,\n" +
                "            \"to\": 22\n" +
                "          },\n" +
                "          {\n" +
                "            \"from\": 22,\n" +
                "            \"to\": 25\n" +
                "          },\n" +
                "          {\n" +
                "            \"from\": 25,\n" +
                "            \"to\": 30\n" +
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
    //https://blog.csdn.net/UbuntuTouch/article/details/99621105
    @Test
    public void rangeaggs1() throws IOException {
       /* elasticsearchClient.search(
                s->s.index("twitter")
                        .aggregations("aggs",
                                fn->fn.range(r1->r1.field("age").ranges(b->b.from("20").to("22"))))
                ,HashMap.class
        );*/
        List<AggregationRange> objects = new ArrayList<>();
        AggregationRange age = new AggregationRange.Builder().key("age").from("20").to("22").build();
        AggregationRange age1 = new AggregationRange.Builder().key("age").from("22").to("25").build();
        AggregationRange age2 = new AggregationRange.Builder().key("age").from("25").to("30").build();
        objects.add(age);
        objects.add(age1);
        objects.add(age2);

        SearchResponse<HashMap> search = elasticsearchClient.search(
                s -> s.index("twitter")
                        .aggregations("aggs",
                                fn -> fn.range(r1 -> r1.field("age").ranges(objects)))
                , HashMap.class
        );
        System.out.println(search);
//        上面的意思是我们针对每个桶 20-22，22-25，25-30，分别计算它们的平均年龄。上面显示的结果是：
        HashMap<String, Aggregation> objectObjectHashMap = new HashMap<>();
        Aggregation age3 = new Aggregation.Builder().min(m -> m.field("age")).build();
        Aggregation age4 = new Aggregation.Builder().max(m -> m.field("age")).build();
        objectObjectHashMap.put("sub-avg_age1",age3);
        objectObjectHashMap.put("sub-avg_age2",age4);
        SearchResponse<HashMap> search1 = elasticsearchClient.search(
                s -> s.index("twitter")
                        .aggregations("aggs",
                                fn -> fn.range(r1 -> r1.field("age").ranges(objects)).aggregations(
//                                        "sub-avg_age",ss->ss.avg(a->a.field("age")
//                                        Collections.singletonMap().
                                        objectObjectHashMap

                                ))
                , HashMap.class
        );
        System.out.println(search1);
    }








}
