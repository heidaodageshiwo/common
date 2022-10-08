package com.common.common;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.DateRangeAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.DateRangeExpression;
import co.elastic.clients.elasticsearch._types.aggregations.FieldDateMath;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
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
public class ESTest6terms {
    @Resource(name = "clientByPasswd")
    ElasticsearchClient elasticsearchClient;



    /*terms 聚合
    我们也可以通过 terms 聚合来查询某一个关键字出现的频率。在如下的 terms 聚合中，我们想寻找在所有的文档出现 ”Happy birthday” 里按照城市进行分类的一个聚合。*/
    @Test
    public void Filteraggs() throws IOException {
        StringReader stringReader = new StringReader("{\n" +
                "  \"query\": {\n" +
                "    \"match\": {\n" +
                "      \"message\": \"happy birthday\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"size\": 0,\n" +
                "  \"aggs\": {\n" +
                "    \"city\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \"city\",\n" +
                "        \"size\": 10\n" +
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
//    注意这里的 10 指的是前 10 名的城市。聚合的结果是：
//    SearchResponse: {"took":3,"timed_out":false,"_shards":{"failed":0.0,"successful":1.0,"total":1.0,"skipped":0.0},"hits":{"total":{"relation":"eq","value":3},"hits":[],"max_score":null},"aggregations":{"sterms#city":{"buckets":[{"doc_count":2,"key":"北京"},{"doc_count":1,"key":"上海"}],"doc_count_error_upper_bound":0,"sum_other_doc_count":0}}}

/*{
    "took" : 1,
        "timed_out" : false,
        "_shards" : {
    "total" : 1,
            "successful" : 1,
            "skipped" : 0,
            "failed" : 0
},
    "hits" : {
    "total" : {
        "value" : 3,
                "relation" : "eq"
    },
    "max_score" : null,
            "hits" : [ ]
},
    "aggregations" : {
    "city" : {
        "doc_count_error_upper_bound" : 0,
                "sum_other_doc_count" : 0,
                "buckets" : [
        {
            "key" : "北京",
                "doc_count" : 2
        },
        {
            "key" : "上海",
                "doc_count" : 1
        }
      ]
    }
}
}*/

    @Test
    public void filteraggs1() throws IOException {
        SearchResponse<HashMap> search = elasticsearchClient.search(
                sa -> sa.index("twitter")
                        .query(
                          new Query.Builder().match(new MatchQuery.Builder().query("happy birthday").field("message").build()).build()
                        )
                        .aggregations("city",
                                fn->fn.terms(new TermsAggregation.Builder().field("city").size(10).build())
                                )
                , HashMap.class
        );
        System.out.println(search);
    }
//   SearchResponse: {"took":4,"timed_out":false,"_shards":{"failed":0.0,"successful":1.0,"total":1.0,"skipped":0.0},"hits":{"total":{"relation":"eq","value":3},"hits":[{"_index":"twitter","_id":"3","_score":1.9936416,"_source":"{uid=4, country=中国, address=中国北京市东城区, province=北京, city=北京, DOB=1994-04-01, location={lat=39.893801, lon=116.408986}, message=happy birthday!, user=李四, age=25}"},{"_index":"twitter","_id":"5","_score":1.7332871,"_source":"{uid=6, country=中国, address=中国北京市朝阳区国贸, province=北京, city=北京, DOB=1993-04-01, location={lat=39.918256, lon=116.467910}, message=Happy BirthDay My Friend!, user=老王, age=26}"},{"_index":"twitter","_id":"6","_score":0.8476808,"_source":"{uid=7, country=中国, address=中国上海市闵行区, province=上海, city=上海, DOB=1991-04-01, location={lat=31.175927, lon=121.383328}, message=好友来了都今天我生日，好友来了,什么 birthday happy 就成!, user=老吴, age=28}"}],"max_score":1.9936416},"aggregations":{"sterms#city":{"buckets":[{"doc_count":2,"key":"北京"},{"doc_count":1,"key":"上海"}],"doc_count_error_upper_bound":0,"sum_other_doc_count":0}}}
    /*在上面，我们可以看出来，在所有的含有 "Happy birthday" 的文档中，有两个是来自北京的，有一个是来自上海。

    在正常的情况下，聚合是按照 doc_count 来进行排序的，也就是说哪一个 key 的 doc_count 越多，那么它就排在第一位，以后依次排序。如果你想按照 key 进行排序的话，你可以尝试如下的方法：
    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "top_cities": {
            "terms": {
                "field": "city",
                        "order": {
                    "_key": "asc"
                }
            }
        }
    }
    }
    上面返回的结果是：
    {
        "took" : 0,
            "timed_out" : false,
            "_shards" : {
        "total" : 1,
                "successful" : 1,
                "skipped" : 0,
                "failed" : 0
    },
        "hits" : {
        "total" : {
            "value" : 6,
                    "relation" : "eq"
        },
        "max_score" : null,
                "hits" : [ ]
    },
        "aggregations" : {
        "top_cities" : {
            "doc_count_error_upper_bound" : 0,
                    "sum_other_doc_count" : 0,
                    "buckets" : [
            {
                "key" : "上海",
                    "doc_count" : 1
            },
            {
                "key" : "北京",
                    "doc_count" : 5
            }
      ]
        }
    }
    }
    在这里，我们看到和之前不一样的排序。doc_count 为 1 的 “上海” 反而排到前面去了，虽然这个和我们之前的 asc （上升）排序是有点不太一致的地方 （按照拼音 shanghai 应该比  beijing 要大）。这个和语言的处理有关，但是我们确实看到我们是可以控制这个排序的。你也可以使用 _count 来进行升序的排列：
            ————————————————
    版权声明：本文为CSDN博主「Elastic 中国社区官方博客」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
    原文链接：https://blog.csdn.net/UbuntuTouch/article/details/99621105
    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "top_cities": {
            "terms": {
                "field": "city",
                        "order": {
                    "_count": "asc"
                }
            }
        }
    }
    }
    你也可以通过在 terms 聚合中指定顺序来使用 nested aggreation 的结果进行排序，而不是按计数对结果进行排序：
    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "top_cities": {
            "terms": {
                "field": "city",
                        "order": {
                    "avg_age": "desc"
                }
            },
            "aggs": {
                "avg_age": {
                    "avg": {
                        "field": "age"
                    }
                }
            }
        }
    }
    }
    在上面排序是按照 nested aggregation 的结果 avg_age 来按照降序来排列的。显示的结果为：
            "aggregations" : {
        "top_cities" : {
            "doc_count_error_upper_bound" : 0,
                    "sum_other_doc_count" : 0,
                    "buckets" : [
            {
                "key" : "上海",
                    "doc_count" : 1,
                    "avg_age" : {
                "value" : 28.0
            }
            },
            {
                "key" : "北京",
                    "doc_count" : 5,
                    "avg_age" : {
                "value" : 24.6
            }
            }
      ]
        }
    }
    我们也可以使用 script 来生成一个在索引里没有的术语来进行统计。比如，我们可以通过如下的 script 来生成一个对文档人出生年份的统计：
    POST twitter/_search
    {
        "size": 0,
            "aggs": {
        "birth_year": {
            "terms": {
                "script": {
                    "source": "2019 - doc['age'].value"
                },
                "size": 10
            }
        }
    }
    }
    根据年龄来生成出生的年月来进行统计：
            "aggregations" : {
        "birth_year" : {
            "doc_count_error_upper_bound" : 0,
                    "sum_other_doc_count" : 0,
                    "buckets" : [
            {
                "key" : "1989",
                    "doc_count" : 1
            },
            {
                "key" : "1991",
                    "doc_count" : 1
            },
            {
                "key" : "1993",
                    "doc_count" : 1
            },
            {
                "key" : "1994",
                    "doc_count" : 1
            },
            {
                "key" : "1997",
                    "doc_count" : 1
            },
            {
                "key" : "1999",
                    "doc_count" : 1
            }
      ]
        }
    }
    在上面我们可以看到 key 为1991，1993，1994等。这些 key 在我们原有的字段中根本就不存在。

    在实际的使用中，我们甚至可以通过查询来替换我们的 key。我们用一个例子来进行展示：
    GET twitter/_search?filter_path=aggregations
    {
        "size": 0,
            "aggs": {
        "city_distribution": {
            "terms": {
                "field": "city",
                        "size": 2,
                        "script": {
                    "source": """
            if(params.replace.containsKey(_value)) {
              params.replace[_value]
            } else {
              "Unknown city"
            }
          """,
                            "params": {
                        "replace": {
                            "上海": "Shanghai",
                                    "北京": "Beijing"
                        }
                    }
                }
            }
        }
    }
    }
    在上面，我们可能想把中文的 key 改为 英文的 key，这样在我们的统计中是 Beijing 及 Shanghai 而不是 “北京” 及 “上海”。请注意上面的 _value 指的是 city 字段的值。运行上面的聚合，我们可以看到：
    {
        "aggregations": {
        "city_distribution": {
            "doc_count_error_upper_bound": 0,
                    "sum_other_doc_count": 0,
                    "buckets": [
            {
                "key": "Beijing",
                    "doc_count": 5
            },
            {
                "key": "Shanghai",
                    "doc_count": 1
            }
      ]
        }
    }
    }
    如我们所愿，我们的 key 现在变成英文的了，而不再是中文的。*/


}
