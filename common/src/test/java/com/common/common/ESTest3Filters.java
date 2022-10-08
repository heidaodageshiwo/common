package com.common.common;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.FieldCapsRequest;
import co.elastic.clients.elasticsearch.core.FieldCapsResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.util.ObjectBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
//https://elasticstack.blog.csdn.net/article/details/99621105
@SpringBootTest
public class ESTest3Filters {
    @Resource(name = "clientByPasswd")
    ElasticsearchClient elasticsearchClient;



    //    https://blog.csdn.net/UbuntuTouch/article/details/99621105
    /*在上面，我们使用 ranges 把数据分成不同的 bucket。通常这样的方法只适合字段为数字的字段。我们按照同样的思路，可以使用 filter 来对数据进行分类。在这种方法中，我们甚至可以针对非数字字段来进行建立不同的 bucket。这类聚合我们称之为 Filter aggregagation。定义一个多存储桶聚合，其中每个存储桶都与一个过滤器相关联。 每个存储桶将收集与其关联的过滤器匹配的所有文档。我们可以使用如下的例子：
            ————————————————
    版权声明：本文为CSDN博主「Elastic 中国社区官方博客」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
    原文链接：https://blog.csdn.net/UbuntuTouch/article/details/99621105*/
    @Test
    public void Filtersaggs() throws IOException {
        StringReader stringReader = new StringReader("{\n" +
                "  \"size\": 0,\n" +
                "  \"aggs\": {\n" +
                "    \"by_cities\": {\n" +
                "      \"filters\": {\n" +
                "        \"filters\": {\n" +
                "          \"beijing\": {\n" +
                "            \"match\": {\n" +
                "              \"city\": \"北京\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"shanghai\": {\n" +
                "            \"match\": {\n" +
                "              \"city\": \"上海\"\n" +
                "            }\n" +
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
    //https://blog.csdn.net/UbuntuTouch/article/details/99621105
    @Test
    public void filtersaggs1() throws IOException {
        Map<String, Query> s=new HashMap<>();
        Query build1 = new Query.Builder().match(MatchQuery.of(fn -> fn.field("city").query("北京"))).build();
        s.put("beijing",build1);
        Query build2= new Query.Builder().match(MatchQuery.of(fn -> fn.field("city").query("上海"))).build();
        s.put("shanghai",build2);

        FiltersAggregation build = new FiltersAggregation.Builder().filters(
                f->f.keyed(s)
        ).build();
        SearchResponse<HashMap> search = elasticsearchClient.search(
                sa -> sa.index("twitter")
                        .aggregations("by_cities",
                                fn->fn.filters(build)
                                )
                , HashMap.class
        );
        System.out.println(search);
    }

//    上面显示来自 “北京” 的有 5 个文档，而来自 “上海” 的只有一个文档。在上面我们为每个 filter 都取了一个名字，当然，我们如果不关心名称的话，我们甚至可以有如下的简洁格式：
/*GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "by_cities": {
            "filters": {
                "filters": [
                {
                    "match": {
                    "city": "北京"
                }
                },
                {
                    "match": {
                    "city": "上海"
                }
                }
        ]
            }
        }
    }
    }*/






}
