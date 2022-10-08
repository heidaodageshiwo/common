package com.common.common;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

//https://elasticstack.blog.csdn.net/article/details/99621105
@SpringBootTest
public class ESTest4Filter {
    @Resource(name = "clientByPasswd")
    ElasticsearchClient elasticsearchClient;



   /* Filter 聚合
    在当前文档集上下文中定义与指定过滤器匹配的所有文档的单个存储桶。 通常，这将用于将当前聚合上下文缩小到一组特定的文档。这类聚合被称之为 Filter aggregation。你也可以理解为是上面的 Filters aggregation 的特殊情况，在它里面只含有一个 filter 的 Filters aggregation。我们以一个例子来讲：
            ————————————————
    版权声明：本文为CSDN博主「Elastic 中国社区官方博客」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
    原文链接：https://blog.csdn.net/UbuntuTouch/article/details/99621105*/
    @Test
    public void Filteraggs() throws IOException {
        StringReader stringReader = new StringReader("{\n" +
                "  \"size\": 0,\n" +
                "  \"aggs\": {\n" +
                "    \"beijing\": {\n" +
                "      \"filter\": {\n" +
                "        \"match\": {\n" +
                "          \"city\": \"北京\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"aggs\": {\n" +
                "        \"avg_age\": {\n" +
                "          \"avg\": {\n" +
                "            \"field\": \"age\"\n" +
                "          }\n" +
                "        }\n" +
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
//    在上面，我们针对 “北京” 的文档，并求一个平均年龄。上面查询的结果为：


    //https://blog.csdn.net/UbuntuTouch/article/details/99621105
    @Test
    public void filteraggs1() throws IOException {
        Query build1 = new Query.Builder().match(MatchQuery.of(fn -> fn.field("city").query("北京"))).build();
        HashMap<String, Aggregation> objectObjectHashMap = new HashMap<>();
        Aggregation age3 = new Aggregation.Builder().avg(m -> m.field("age")).build();
        objectObjectHashMap.put("avg_age",age3);

        SearchResponse<HashMap> search = elasticsearchClient.search(
                sa -> sa.index("twitter")
                        .aggregations("beijing",
                                fn->fn.filter(build1).aggregations(objectObjectHashMap)
                                )
                , HashMap.class
        );
        System.out.println(search);
    }

}
