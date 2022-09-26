package com.common.common;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.TimeUnit;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperationVariant;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.Alias;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.util.ObjectBuilder;
import com.common.common.ES8.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import shadow.org.elasticsearch.xpack.sql.proto.core.TimeValue;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
            System.out.println("bbbb:"+hit.source());
        }

        System.out.println("a1:" + search);
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
        }
        BulkResponse bulk = elasticsearchClient.bulk(
                a -> a.index("a").operations(bulkOperationArrayList)
        );
        System.out.println("bulk:" + bulk);


    }

}
