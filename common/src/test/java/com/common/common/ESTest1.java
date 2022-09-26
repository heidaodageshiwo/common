package com.common.common;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.TimeUnit;
import co.elastic.clients.elasticsearch._types.aggregations.HistogramBucket;
import co.elastic.clients.elasticsearch._types.mapping.*;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperationVariant;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.elasticsearch.indices.get_alias.IndexAliases;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.util.ObjectBuilder;
import com.common.common.ES8.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;
import shadow.org.elasticsearch.xpack.sql.proto.core.TimeValue;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common
 * @ClassName: ESTest1
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-09-24  14:04
 * @UpdateDate: 2022-09-24  14:04
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@SpringBootTest
class ESTest1 {
    @Resource(name = "clientByPasswd")
    private ElasticsearchClient elasticsearchClient;

    //创建索引
    @Test
    void addIndex() throws IOException {
        CreateIndexRequest a = new CreateIndexRequest.Builder().index("a").build();
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(a);
        String index = createIndexResponse.index();
        System.out.println("index:" + index);
        boolean acknowledged = createIndexResponse.acknowledged();
        System.out.println("acknowledged:" + acknowledged);
        System.out.println("createIndexResponse:" + createIndexResponse);

    }

    @Test
    void addIndex1() throws IOException {
//        https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/8.1/building-objects.html
//        https://blog.csdn.net/weixin_44282094/article/details/125622021
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(
                new CreateIndexRequest.Builder()
                        .index("b")
                        .aliases("bs", new Alias.Builder().isWriteIndex(true).build())
                        .build()
        );
        Alias ad = new Alias.Builder().isWriteIndex(true).build();
        CreateIndexResponse createIndexResponse1 = elasticsearchClient.indices().create(
                a -> a.index("c")
                        .aliases("as", ad)
                        .timeout((Function<Time.Builder, ObjectBuilder<Time>>) TimeValue.timeValueMinutes(2))
        );
//        IndexRequest.of()
        System.out.println("createIndexResponse:" + createIndexResponse);
    }

    //插入数据
    @Test
    void addIndex2() throws IOException {
//        https://github.com/elastic/elasticsearch-java/blob/8.1/java-client/src/test/java/co/elastic/clients/documentation/usage/IndexingTest.java
//        https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/8.1/blocking-and-async.html
//        file:///C:/Users/Administrator/Desktop/elasticsearch-head-master/index.html
//        https://192.168.56.213:9200/?auth_user=elastic&auth_password=123456
//        https://blog.csdn.net/yscjhghngh/article/details/123620860
        Product product = new Product("bk-2", "City bike", 123.0);
        IndexRequest<Product> a1 = IndexRequest.of(a -> a.index("a")
                .id("2")
                .document(product)
        );
        IndexResponse index = elasticsearchClient.index(a1);
        System.out.println("index:" + index);
    }

    //更新数据
    @Test
    void updatedata() throws IOException {
        Product product = new Product("bk-2", "City bike", 1233.0);
        Product product1 = new Product("bk-21", "City bike1", 12331.0);
        UpdateRequest<Product, Product> a1 = UpdateRequest.of(a -> a.index("a")
                .id("2")
                .doc(product)
        );
        UpdateResponse<Product> update = elasticsearchClient.update(a1, Product.class);

        elasticsearchClient.update(
                a -> a.index("a")
                        .id("2")
                        .doc(product1)
                , Product.class);
        System.out.println("update:" + update);
    }

    //判断数据是否存在
    @Test
    void existsdata() throws IOException {
        BooleanResponse a = elasticsearchClient.exists(e -> e.index("a").id("2"));
        System.out.println("a:" + a.value());
    }

    //查询单个数据
    @Test
    void getdata() throws IOException {
        GetResponse<Product> a1 = elasticsearchClient.get(a -> a.index("a").id("2"), Product.class);
        System.out.println("a1:" + a1);
        System.out.println("a1:" + a1.source());
    }

    //查看索引信息
    @Test
    void getindex() throws IOException {
        GetIndexResponse a1 = elasticsearchClient.indices().get(a -> a.index("a"));
        Map<String, IndexState> result = a1.result();
        System.out.println(result);
    }

    //添加别名 用处 可以检索多个索引
//    https://blog.csdn.net/laoyang360/article/details/93693440
    @Test
    void addAliases() throws IOException {
        ArrayList<String> objects = new ArrayList<>();
        objects.add("a");
        objects.add("a2");
        UpdateAliasesResponse updateAliasesResponse = elasticsearchClient.indices().updateAliases(
                update -> update.actions(action -> action.add(add -> add.indices(objects).aliases("a1a")))
        );
        //移除别名
//        elasticsearchClient.indices().deleteAlias(del -> del
//                        .index(indexName)
//                        .name(aliasName)
//                );
        //修改 ：先删除  在新增
//        根据别名查询索引信息
        GetAliasResponse a1a = elasticsearchClient.indices()
                .getAlias(a -> a
                        .name("a1a")
                );
        Map<String, IndexAliases> result = a1a.result();
        System.out.println(result);
        System.out.println(updateAliasesResponse.acknowledged());
//        根据别名查询索引名称
        GetAliasResponse a1a1 = elasticsearchClient
                .indices()
                .getAlias(a -> a
                        .name("a1a")
                );
        Map<String, IndexAliases> resultss = a1a1.result();
        List<String> indexList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(resultss)) {
            indexList = resultss.keySet().stream().collect(Collectors.toList());
            System.out.println(indexList);
        }

    }

    //删除单个数据
    @Test
    void deldata() throws IOException {
        DeleteResponse a1 = elasticsearchClient.delete(a -> a.index("a").id("2"));
        System.out.println("a1:" + a1);
    }

    //search数据
    @Test
    void searchdata() throws IOException {
        SearchResponse<Product> search = elasticsearchClient.search(
                a -> a.index("a")
                        //查询name字段包含hello的document(不使用分词器精确查找)
                        .query(q -> q.term(t -> t.field("sku.keyword").value(v -> v.stringValue("bk-22"))))
                        .from(0)
                        .size(100)
                        .sort(s -> s.field(f -> f.field("price").order(SortOrder.Asc)))
                ,
                Product.class);
        for (Hit<Product> hit : search.hits().hits()) {
            System.out.println("bbbb:" + hit.source());
        }

        System.out.println("a1:" + search);

        //根据别名检索
        SearchResponse<Product> search1 = elasticsearchClient.search(
                s -> s.index("a1a")
                        .from(0)
                        .size(100),
                Product.class
        );
        for (Hit<Product> hit : search1.hits().hits()) {
            System.out.println("==============hit.id:" + hit.id());
            System.out.println("根据别名检索多个索引:" + hit.source());
        }

    }

    //嵌套搜索查询
    @Test
    void searchdata1() throws IOException {
        //全文匹配“name”字段包含检索词“bike”
        Query query = MatchQuery.of(
                m -> m.field("name").query("bike")
        )._toQuery();
        //范围匹配“price”字段大于1000
        Query rangeQuery = RangeQuery.of(r -> r
                .field("price")
                // Elasticsearch range query accepts a large range of value types.
                // We create here a JSON representation of the maximum price.
                .gt(JsonData.of(1111))

        )._toQuery();
        SearchResponse<Product> a = elasticsearchClient.search(
                s -> s.index("a")
                        .query(
                                q -> q.bool(b -> b.must(query, rangeQuery))
                        )
                , Product.class
        );
    }

    @Test
    void searintervals() throws IOException {
//        https://blog.csdn.net/weixin_28906733/article/details/106755209
    }

    //query 简单的聚合查询
    @Test
    void searchdata2() throws IOException {
        String searchText = "bike";

        Query query = MatchQuery.of(m -> m
                .field("name")
                .query(searchText)
        )._toQuery();

        SearchResponse<Void> response = elasticsearchClient.search(b -> b
                        .index("products")
                        .size(0)/* Set the number of matching documents to zero as we only use the price histogram.
                                 将原始数据返回设置为零，因为我们只是用价格直方图 */
                        .query(query)
                        /* Create an aggregation named "price-histogram". You can add as many named aggregations as needed.
                        创建一个名为“价格直方图”的聚合，可以根据需要添加任意多的命名聚合。*/
                        .aggregations("price-histogram", a -> a
                                .histogram(h -> h
                                        .field("price")
                                        .interval(100.0)
                                )
                        ),
                Void.class //We do not care about matches (size is set to zero), using Void will ignore any document in the response.
        );

        List<HistogramBucket> buckets = response.aggregations()
                .get("price-histogram")
                .histogram()    //Cast it down to the histogram variant results. This has to be consistent with the aggregation definition.
                .buckets().array(); //Bucket 可以表示为数组或映射，这将强制转换为数组变量(默认值)。

        //遍历输出
//        buckets.stream().forEach(bucket -> log.info("There are " + bucket.docCount() + " bikes under " + bucket.key()));

    }

    //批量添加数据
    @Test
    void bulkdata() throws IOException {
        ArrayList<Product> objects = new ArrayList<>();
        Product product1 = new Product("bk-22", "City bike22", 1233.0);
        Product product2 = new Product("bk-23", "City bike23", 1233.0);
        Product product3 = new Product("bk-24", "City bike24", 1233.0);
        Product product4 = new Product("bk-25", "City bike25", 1233.0);
        objects.add(product1);
        objects.add(product2);
        objects.add(product3);
        objects.add(product4);
        List<BulkOperation> bulkOperationArrayList = new ArrayList<>();
        for (Product object : objects) {
            bulkOperationArrayList.add(BulkOperation.of(o -> o.index(b -> b.document(object))));
            // bulk批量删除文档记录
//            bulkOperationArrayList.add(BulkOperation.of(o -> o.delete(b->b.index("b").id("id"))));
//            bluk批量更新数据
//            bulkOperationArrayList.add(BulkOperation.of(o -> o.update(b->b.index("b").id("id").action(a->a.doc("df")))));
        }
        for (Product doc : objects) {
            bulkOperationArrayList.add(new BulkOperation.Builder().create(d -> d.document(doc)).build());
        }
        BulkResponse bulk = elasticsearchClient.bulk(
                a -> a.index("a2").operations(bulkOperationArrayList)
        );
        System.out.println("bulk:" + bulk);
    }

    //创建索引带mapping
    @Test
    void createIndexandmapping() throws IOException {
        Map<String, Property> documentMap = new HashMap<>();
        documentMap.put("title", Property.of(property ->
                                property.text(TextProperty.of(p ->
                                                        p.index(true)
//                                                .analyzer("ik_max_word")
                                        )
                                )
                )
        );
        documentMap.put("id", Property.of(property ->
                        property.long_(LongNumberProperty.of(p ->
                                        p.index(true)
                                )
                        )
                )
        );

        documentMap.put("content", Property.of(property ->
                                property.text(TextProperty.of(textProperty ->
                                                        textProperty.index(true)
//                                                .analyzer("ik_max_word")
                                        )
                                )
                )
        );
        documentMap.put("createUserId", Property.of(property ->
                        property.keyword(KeywordProperty.of(p ->
                                        p.index(true)
                                )
                        )
                )
        );

        documentMap.put("createTime", Property.of(property ->
                        property.date(DateProperty.of(p ->
                                        p.index(true)
                                )
                        )
                )
        );
        // 创建索引
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(c -> {
            c.index("a2")
                    .mappings(mappings -> mappings.properties(documentMap));
            //.aliases(SystemConstant.INDEX_DOC_ALL, aliases -> aliases.isWriteIndex(true));
            return c;
        });
    }

    @Test
    void getAllIndex() throws IOException {
        IndicesResponse indicesResponse = elasticsearchClient.cat().indices();
        indicesResponse.valueBody().forEach(info -> System.out.println("info = " + info.health() + "\t" + info.status() + "\t" + info.index() + "\t" + info.uuid() + "\t" + info.pri() + "\t" + info.rep() + "\t" + info.docsCount()));
    }

}
