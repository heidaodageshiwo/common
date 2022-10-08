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
public class ESTest7HistogramAggregation {
    @Resource(name = "clientByPasswd")
    ElasticsearchClient elasticsearchClient;

}
  /*  Histogram Aggregation
基于多桶值源的汇总，可以应用于从文档中提取的数值或数值范围值。 它根据值动态构建固定大小（也称为间隔）的存储桶。

        GET twitter/_search
        {
        "size": 0,
        "aggs": {
        "age_distribution": {
        "histogram": {
        "field": "age",
        "interval": 2
        }
        }
        }
        }
        显示结果：

        "aggregations" : {
        "age_distribution" : {
        "buckets" : [
        {
        "key" : 20.0,
        "doc_count" : 1
        },
        {
        "key" : 22.0,
        "doc_count" : 1
        },
        {
        "key" : 24.0,
        "doc_count" : 1
        },
        {
        "key" : 26.0,
        "doc_count" : 1
        },
        {
        "key" : 28.0,
        "doc_count" : 1
        },
        {
        "key" : 30.0,
        "doc_count" : 1
        }
        ]
        }
        }
        上面显示从 20-22 年龄段，有一个文档。从 22-24 也有一个文档。

        同样地，我们也可以进行 sub aggregation，并按照 sub aggregation 的值进行排序：

        GET twitter/_search
        {
        "size": 0,
        "aggs": {
        "age_distribution": {
        "histogram": {
        "field": "age",
        "interval": 2,
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
        上面返回的结果是：

        {
        "took" : 2,
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
        "age_distribution" : {
        "buckets" : [
        {
        "key" : 30.0,
        "doc_count" : 1,
        "avg_age" : {
        "value" : 30.0
        }
        },
        {
        "key" : 28.0,
        "doc_count" : 1,
        "avg_age" : {
        "value" : 28.0
        }
        },
        {
        "key" : 26.0,
        "doc_count" : 1,
        "avg_age" : {
        "value" : 26.0
        }
        },
        {
        "key" : 24.0,
        "doc_count" : 1,
        "avg_age" : {
        "value" : 25.0
        }
        },
        {
        "key" : 22.0,
        "doc_count" : 1,
        "avg_age" : {
        "value" : 22.0
        }
        },
        {
        "key" : 20.0,
        "doc_count" : 1,
        "avg_age" : {
        "value" : 20.0
        }
        }
        ]
        }
        }
        }
        显然这个是按照 avg_age 的值进行降序排列的。

        date_histogram
        这种聚合类似于正常的直方图，但只能与日期或日期范围值一起使用。 由于日期在 Elasticsearch 中内部以 long 类型值表示，因此也可以但不准确地对日期使用正常的直方图。

        GET twitter/_search
        {
        "size": 0,
        "aggs": {
        "age_distribution": {
        "date_histogram": {
        "field": "DOB",
        "interval": "year"
        }
        }
        }
        }
        请注意：在新的一些版本中，interval 不再适用。取而代之的是 calendar_interval。你需要改写你的搜索为如下的格式：

        GET twitter/_search?filter_path=aggregations
        {
        "size": 0,
        "aggs": {
        "age_distribution": {
        "date_histogram": {
        "field": "DOB",
        "calendar_interval": "year"
        }
        }
        }
        }
        在上面我们使用 DOB 来作为 date_histogram 的字段来进行聚合统计。我们按照每隔一年这样的时间间隔来进行。显示结果：

        "aggregations" : {
        "age_distribution" : {
        "buckets" : [
        {
        "key_as_string" : "1989-01-01T00:00:00.000Z",
        "key" : 599616000000,
        "doc_count" : 1
        },
        {
        "key_as_string" : "1990-01-01T00:00:00.000Z",
        "key" : 631152000000,
        "doc_count" : 0
        },
        {
        "key_as_string" : "1991-01-01T00:00:00.000Z",
        "key" : 662688000000,
        "doc_count" : 1
        },
        {
        "key_as_string" : "1992-01-01T00:00:00.000Z",
        "key" : 694224000000,
        "doc_count" : 0
        },
        {
        "key_as_string" : "1993-01-01T00:00:00.000Z",
        "key" : 725846400000,
        "doc_count" : 1
        },
        {
        "key_as_string" : "1994-01-01T00:00:00.000Z",
        "key" : 757382400000,
        "doc_count" : 1
        },
        {
        "key_as_string" : "1995-01-01T00:00:00.000Z",
        "key" : 788918400000,
        "doc_count" : 0
        },
        {
        "key_as_string" : "1996-01-01T00:00:00.000Z",
        "key" : 820454400000,
        "doc_count" : 0
        },
        {
        "key_as_string" : "1997-01-01T00:00:00.000Z",
        "key" : 852076800000,
        "doc_count" : 1
        },
        {
        "key_as_string" : "1998-01-01T00:00:00.000Z",
        "key" : 883612800000,
        "doc_count" : 0
        },
        {
        "key_as_string" : "1999-01-01T00:00:00.000Z",
        "key" : 915148800000,
        "doc_count" : 1
        }
        ]
        }
        上面的结果显示 DOB 从 1989-01-01 到 1990-01-01 有一个文档。从 1990-01-01 到 1991-01-01 区间没有一个文档。

        估计日期直方图聚合将产生的桶数非常困难*//* 通常，生成大量存储桶并存在内存和性能问题是很常见的，不仅在 Elasticsearch 端，而且在应用程序级别。 为了防止这个问题，Elasticsearch 引入了一个日期直方图聚合，它可以自动调整间隔以生成所需数量的桶：auto_date_histogram。

        GET twitter/_search?filter_path=aggregations
        {
        "size": 0,
        "aggs": {
        "age_distribution": {
        "auto_date_histogram": {
        "field": "DOB",
        "buckets": 10,
        "format": "yyy-MM-dd"
        }
        }
        }
        }
        上面的聚合生成的结果为：

        {
        "aggregations": {
        "age_distribution": {
        "buckets": [
        {
        "key_as_string": "1989-01-01",
        "key": 599616000000,
        "doc_count": 3
        },
        {
        "key_as_string": "1994-01-01",
        "key": 757382400000,
        "doc_count": 2
        },
        {
        "key_as_string": "1999-01-01",
        "key": 915148800000,
        "doc_count": 1
        }
        ],
        "interval": "5y"
        }
        }
        }

        如上所示，在返回的结果中，我们可以看到 interval 被自动计算为 5y，也即5年。
        ————————————————
        版权声明：本文为CSDN博主「Elastic 中国社区官方博客」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
        原文链接：https://blog.csdn.net/UbuntuTouch/article/details/99621105*/