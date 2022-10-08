package com.common.common;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
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
import java.util.HashMap;

//https://elasticstack.blog.csdn.net/article/details/99621105
@SpringBootTest
public class ESTest8cardinality {
    @Resource(name = "clientByPasswd")
    ElasticsearchClient elasticsearchClient;


/*
    我们也可以使用 cardinality 聚合来统计到底有多少个城市：
*/


    @Test
    public void Filteraggs() throws IOException {
        StringReader stringReader = new StringReader("{\n" +
                "  \"size\": 0,\n" +
                "  \"aggs\": {\n" +
                "    \"number_of_cities\": {\n" +
                "      \"cardinality\": {\n" +
                "        \"field\": \"city\"\n" +
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
   /* 运行上面的查询，我们可以看到结果是：
    {
        "took" : 6,
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
        "number_of_cities" : {
            "value" : 2
        }
    }
    }
    它显示我们有两个城市：北京 及 上海。它们在文档中虽然出现多次，但是从唯一性上，只有两个城市。
    Metric 聚合
    我们可以使用 Metrics 来统计我们的数值数据，比如我们想知道所有用户的平均年龄是多少？我们可以用下面的聚合：

    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "average_age": {
            "avg": {
                "field": "age"
            }
        }
    }
    }
    我们的返回的结果是：

            "aggregations" : {
        "average_age" : {
            "value" : 25.166666666666668
        }
    }
    所有人的平均年龄是 25.166666666666668岁。
            ————————————————
    我们可以针对这个字段做其它的指标聚合，比如 sum：

    GET twitter/_search?filter_path=aggregations
    {
        "size": 0,
            "aggs": {
        "sum_age": {
            "sum": {
                "field": "age"
            }
        }
    }
    }
    上面的命令查询所有年龄相加的结果：

    {
        "aggregations": {
        "sum_age": {
            "value": 151
        }
    }
    }
    我们还可以查询这些文档里的年龄最大的值：

    GET twitter/_search?filter_path=aggregations
    {
        "size": 0,
            "aggs": {
        "max_age": {
            "max": {
                "field": "age"
            }
        }
    }
    }
    上面的命令返回：

    {
        "aggregations": {
        "max_age": {
            "value": 30
        }
    }
    }
    上面表明，文档中 age 的最大值为 30。我们还可以得到 age 的最小值：

    GET twitter/_search?filter_path=aggregations
    {
        "size": 0,
            "aggs": {
        "min_age": {
            "min": {
                "field": "age"
            }
        }
    }
    }
    上面的命令返回：

    {
        "aggregations": {
        "min_age": {
            "value": 20
        }
    }
    }
    除了上面的最为基本的指标聚合，我们还可以使用脚本来进行指标聚合。我们来使用 script 来实现 age 的2倍相加的结果：

    GET twitter/_search?filter_path=aggregations
    {
        "size": 0,
            "aggs": {
        "two_times_age_sum": {
            "sum": {
                "script": {
                    "source": "doc['age'].value * 2"
                }
            }
        }
    }
    }
    在上面，我们把 age 都乘以 2，再进行相加。这样得到的聚合为：

    {
        "aggregations": {
        "two_times_age_sum": {
            "value": 302
        }
    }
    }
    这个显然是我们之前的没有乘以2 的聚合的两倍。这个脚本在实际的使用中非常有用。比如，我们的字段中有价格及数量，那么我们很容通过这个脚本算出来总价：数量 x 价钱。
            ————————————————
    在 Elastic Stack 7.11 的发布版之后，我们可以使用 runtime fields 来实现同样的功能：

    GET twitter/_search?filter_path=aggregations
    {
        "size": 0,
            "runtime_mappings": {
        "age.corrected": {
            "type": "double",
                    "script": {
                "source": "emit(doc['age'].value * params.correction)",
                        "params": {
                    "correction": 2.0
                }
            }
        }
    },
        "aggs": {
        "two_times_age_sum": {
            "sum": {
                "field": "age.corrected"
            }
        }
    }
    }
    在上面，我们定义了一个叫做 age.corrected 的运行时字段。这种方法是在未来的版本中被推荐的方法。如果你想了解更关于 runtime fields 的知识，请参考文章 “Elastic：开发者上手指南” 中的 runtime field 系列文章。上面命令返回的结果和上一个脚本命令是一样的。
    我们也可以对只在北京的用户文档进行统计：

    POST twitter/_search
    {
        "size": 0,
            "query": {
        "match": {
            "city": "北京"
        }
    },
        "aggs": {
        "average_age_beijing": {
            "avg": {
                "field": "age"
            }
        }
    }
    }
    上面我们先查询到所有在北京的用户，然后再对这些文档进行求年龄的平均值。返回的结果：

            "aggregations" : {
        "average_age_beijing" : {
            "value" : 24.6
        }
    }
    聚合通常在查询搜索结果上执行。 Elasticsearch 提供了一个特殊的 global 聚合，该全局全局对所有文档执行，而不受查询的影响。

    POST twitter/_search
    {
        "size": 0,
            "query": {
        "match": {
            "city": "北京"
        }
    },
        "aggs": {
        "average_age_beijing": {
            "avg": {
                "field": "age"
            }
        },
        "average_age_all": {
            "global": {},
            "aggs": {
                "age_global_avg": {
                    "avg": {
                        "field": "age"
                    }
                }
            }
        }
    }
    }
    在上面我们在 average_age_all 里添加了一个 gobal 的聚合，这个平均值将会使用所有的6个文档而不是限于在这个查询的5个北京的文档。返回的结果是：

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
            "value" : 5,
                    "relation" : "eq"
        },
        "max_score" : null,
                "hits" : [ ]
    },
        "aggregations" : {
        "average_age_beijing" : {
            "value" : 24.6
        },
        "average_age_all" : {
            "doc_count" : 6,
                    "age_global_avg" : {
                "value" : 25.166666666666668
            }
        }
    }
    }
    我们也可以对整个年龄进行一个统计，比如：

    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "age_stats": {
            "stats": {
                "field": "age"
            }
        }
    }
    }
    统计的结果如下：

            "aggregations" : {
        "age_stats" : {
            "count" : 6,
                    "min" : 20.0,
                    "max" : 30.0,
                    "avg" : 25.166666666666668,
                    "sum" : 151.0
        }
    }
    在这里，我们可以看到到底有多少条数据，并且最大，最小的，平均值及加起来的合都在这里一起显示。

    如果你想了解更多的细节，你可以使用 extended_stats：
    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "age_stats": {
            "extended_stats": {
                "field": "age"
            }
        }
    }
    }
    上面返回的结果为：

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
        "age_stats" : {
            "count" : 6,
                    "min" : 20.0,
                    "max" : 30.0,
                    "avg" : 25.166666666666668,
                    "sum" : 151.0,
                    "sum_of_squares" : 3869.0,
                    "variance" : 11.472222222222248,
                    "std_deviation" : 3.3870669054835996,
                    "std_deviation_bounds" : {
                "upper" : 31.940800477633868,
                        "lower" : 18.392532855699468
            }
        }
    }
    }
    对于一些不知道上面的 std_derivation 和 varianace 的开发者来说，我们举如下的一个例子：
    假如你有一个系列的数据： 32，111，138，28，59，77，97，那么它的平均值为 (32+111+138+28+59+77+97) / 7 = 77.4。针对每个值，找出和平均值之间的差异：

            32 - 77.4 = -45.4
            111 - 77.4 =  33.6
            138 - 77.4 =  60.6
            28 - 77.4 = -49.4
            59 - 77.4 = -18.4
            77 - 77.4 = - 0.4
            97 - 77.4 =  19.6
    针对上面的每个值，他们的平方值为：

            (-45.4)^2 = 2061.16
            (33.6)^2 = 1128.96
            (60.6)^2 = 3672.36
            (-49.4)^2 = 2440.36
            (-18.4)^2 =  338.56
            (- 0.4)^2 =    0.16
            (19.6)^2 =  384.16
    variance 的值其实就是上面的差值平方和的平均值：

    variance = (2061.16+1128.96+3672.36+2440.36+338.56+0.16+384.16) / 7 = 1432.2
    那么 std_derivation 的值其实就是上面值的平方根值：std_derivation = sqrt(variance) = sqrt(1432.2) = 37.84。
    如果你想对多个指标进行统计并显示它们之间的关系，你可以使用 matrix_stats：

    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "matrix_stats": {
            "matrix_stats": {
                "fields": ["age", "uid"]
            }
        }
    }
    }
    上面，我们把两个整型字段 uid 及 age 放入进行统计。上面的查询显示的结果为：

    {
        "took" : 7,
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
        "matrix_stats" : {
            "doc_count" : 6,
                    "fields" : [
            {
                "name" : "uid",
                    "count" : 6,
                    "mean" : 4.5,
                    "variance" : 3.5,
                    "skewness" : 0.0,
                    "kurtosis" : 1.7314285714285715,
                    "covariance" : {
                "uid" : 3.5,
                        "age" : 5.7
            },
                "correlation" : {
                "uid" : 1.0,
                        "age" : 0.8211574455173661
            }
            },
            {
                "name" : "age",
                    "count" : 6,
                    "mean" : 25.166666666666668,
                    "variance" : 13.76666666666667,
                    "skewness" : -0.143450283024544,
                    "kurtosis" : 1.8030533098042432,
                    "covariance" : {
                "uid" : 5.7,
                        "age" : 13.76666666666667
            },
                "correlation" : {
                "uid" : 0.8211574455173661,
                        "age" : 1.0
            }
            }
      ]
        }
    }
    }

    我们也可以只得到这个年龄的最大值：

    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "age_max": {
            "max": {
                "field": "age"
            }
        }
    }
    }
    显示的结果:

            "aggregations" : {
        "age_max" : {
            "value" : 30.0
        }
    }
    我们也可以在同一个请求里聚合多个指标，比如在如下的请求中，我们可以同时得到最大及最小的值：

    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "Min": {
            "min": {
                "field": "age"
            }
        },
        "Max": {
            "max": {
                "field": "age"
            }
        }
    }
    }
    聚合通常适用于从聚合文档集中提取的值。 可以使用聚合体内的字段键从特定字段提取这些值，也可以使用脚本提取这些值。我们可以通过 script 的方法来对我们的 aggregtion 结果进行重新计算：

    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "average_age_1.5": {
            "avg": {
                "field": "age",
                        "script": {
                    "source": "_value * params.correction",
                            "params": {
                        "correction": 1.5
                    }
                }
            }
        }
    }
    }
    上面的这个聚合可以帮我们计算平均值再乘以 1.5 倍的结果。运行一下的结果如下：

            "aggregations" : {
        "average_age_1.5" : {
            "value" : 37.75
        }
    }
    显然我们的结果是之前的 25.166666666666668 的1.5倍。
    我们也可以直接使用 script 的方法来进行聚合。在这种情况下，我们可以不指定特定的 field。我们可能把很多项进行综合处理，并把这个结果来进行聚合：

    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "average_2_times_age": {
            "avg": {
                "script": {
                    "source": "doc['age'].value * params.times",
                            "params": {
                        "times": 2.0
                    }
                }
            }
        }
    }
    }
    在这里我们完全没有使用 field 这个项。我们直接使用 script 来形成我们的聚合：

            "aggregations" : {
        "average_2_times_age" : {
            "value" : 50.333333333333336
        }
    }
    Percentile aggregation
    百分位数（percentile）表示观察值出现一定百分比的点。 例如，第 95 个百分位数是大于观察值的 95％ 的值。该聚合针对从聚合文档中提取的数值计算一个或多个百分位数。 这些值可以从文档中的特定数字字段中提取，也可以由提供的脚本生成。

    百分位通常用于查找离群值。 在正态分布中，第 0.13 和第 99.87 个百分位数代表与平均值的三个标准差。 任何超出三个标准偏差的数据通常被视为异常。这在统计的角度是非常有用的。

    假如我们考虑如下的一个系列的数据：

            77, 78, 85, 86, 86, 86, 87, 87, 88, 94, 99, 103, 111

    在上面的 13 个数据中，它的中间位数据为 87，这是因为这个系列的数据是奇数个。我们再来考虑下面偶数个系列的数据：

            77, 78, 85, 86, 86, 86, 87, 87, 94, 98, 99, 103

    那么显然上面数据的中间为： (86 + 87) / 2 = 86.5。

    我们现在来通过一个简单的例子来展示 Percentile aggregation 的用法：

    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "age_quartiles": {
            "percentiles": {
                "field": "age",
                        "percents": [
                25,
                        50,
                        75,
                        100
        ]
            }
        }
    }
    }

    在上面，我们使用了以叫做 age 的字段。它是一个数值的字段。我们通过 percentile aggregation 可以得到 25%，50% 及75% 的人在什么范围。显示结果是:

            "aggregations" : {
        "age_quartiles" : {
            "values" : {
                "25.0" : 22.0,
                        "50.0" : 25.5,
                        "75.0" : 28.0,
                        "100.0" : 30.0
            }
        }
    }
    我们可以看到25%的人平均年龄是低于22.0岁，而50%的人的年龄是低于25.5岁，而所有的人的年龄都是低于30岁的。这里的50%的年龄和我们之前计算的平均年龄是不一样的。

    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "avarage_age": {
            "avg": {
                "field": "age"
            }
        }
    }
    }
    这个平均年龄是：

            "aggregations" : {
        "avarage_age" : {
            "value" : 25.166666666666668
        }
    }
    在上面，我们能够查出来，50%的人的年龄是多少，但是在实际的应用中，我们有时也很希望知道满足我们的 SLA (Service Level Aggreement) 百分比是多少，这样你可以找到自己服务的差距，比如达到一个标准的百分比是多少。针对我们的例子，我们可以使用 Percentile Ranks Aggregation。我们使用如下的查询：

    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "age_40_percentage": {
            "percentile_ranks": {
                "field": "age",
                        "values": [
                40
        ]
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
        "age_40_percentage" : {
            "values" : {
                "40.0" : 62.5
            }
        }
    }
    }

    上面的结果表明，有62.5%的人的年龄是在40岁以下的。在我们的实际应用中，比如我们输入一个指标，我们可以看出来有多少比例是在那个指标以内的。这样我们可以看出来我们的 SLA 是否满足条件。

    我们可以使用 string stats 聚合来对 string 类型的数据进行统计。

    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "message_stats": {
            "string_stats": {
                "field": "message.keyword"
            }
        }
    }
    }
    上面显示的结果为：

            "aggregations" : {
        "message_stats" : {
            "count" : 6,
                    "min_length" : 9,
                    "max_length" : 37,
                    "avg_length" : 18.166666666666668,
                    "entropy" : 5.406152357759698
        }
    }
    它表明 message 字段的最短长度为 9，最长长度为 37，而它的平均长度为 18.17。
    更为复杂的聚合
    我们可以结合上面的 bucket 聚合及 metric 聚合形成更为复杂的搜索：

    GET twitter/_search
    {
        "size": 0,
            "aggs": {
        "cities": {
            "terms": {
                "field": "city",
                        "order": {
                    "average_age": "desc"
                },
                "size": 5
            },
            "aggs": {
                "average_age": {
                    "avg": {
                        "field": "age"
                    }
                }
            }
        }
    }
    }
    在上面，我们首先通过 terms 来生成每个城市的桶聚合，让后在每个桶里计算所有文档的平均年龄。在正常的情况下，这个排序是按照每个城市里文档的多少由多到少来排序的。在我们上面的搜索中，我们特意添加 average_age 来进行降序排序。这样返回的结果如下：

            "aggregations" : {
        "cities" : {
            "doc_count_error_upper_bound" : 0,
                    "sum_other_doc_count" : 0,
                    "buckets" : [
            {
                "key" : "上海",
                    "doc_count" : 1,
                    "average_age" : {
                "value" : 28.0
            }
            },
            {
                "key" : "北京",
                    "doc_count" : 5,
                    "average_age" : {
                "value" : 24.6
            }
            }
      ]
        }
        上面显示，有两个城市：上海及北京。在上海城市中有1个文档，而在北京城市里有5个文档。同时，我们也计算出来每个城市的平均年龄。由于我们使用了 average_age 来进行降排序，在我们的结果中，我们可以看到 “上海” 城市排在前面，这是因为上海城市的平均年龄比北京的平均年龄高。
        Missing 聚合
        我们可以通过这个聚合来统计出来缺失某个字段的文档个数。我们先添加如下的一个文档：

        PUT twitter/_doc/7
        {
            "user": "张三",
                "message": "今儿天气不错啊，出去转转去",
                "uid": 2,
                "city": "北京",
                "province": "北京",
                "country": "中国",
                "address": "中国北京市海淀区",
                "location": {
            "lat": "39.970718",
                    "lon": "116.325747"
        },
            "DOB": "1999-04-01"
        }
        在上面的文档中，我们故意漏掉 age 这个字段。我们使用如下的聚合来查询有多少文档缺失 age 这个字段：

        GET twitter/_search
        {
            "size": 0,
                "aggs": {
            "total_missing_age": {
                "missing": {
                    "field": "age"
                }
            }
        }
        }
        上面聚合显示的结果为：

        {
            "took" : 603,
                "timed_out" : false,
                "_shards" : {
            "total" : 1,
                    "successful" : 1,
                    "skipped" : 0,
                    "failed" : 0
        },
            "hits" : {
            "total" : {
                "value" : 7,
                        "relation" : "eq"
            },
            "max_score" : null,
                    "hits" : [ ]
        },
            "aggregations" : {
            "total_missing_age" : {
                "doc_count" : 1
            }
        }
        }
        上面显示一个没有 age 这个字段的文档。
*/



}
