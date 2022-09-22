package com.common.common;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common
 * @ClassName: ESServiceTest
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-09-22  15:51
 * @UpdateDate: 2022-09-22  15:51
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */

import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.common.common.ES8.ESService;
import com.common.common.ES8.ESServiceApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ESServiceTest {

    @Autowired
    ESService esService;
   @Autowired
   ESServiceApi esServiceApi;

    @Test
    void addIndex() throws Exception {
       /* String indexName = "test_index";
        esService.addIndex(indexName);
        boolean b = esService.indexExists(indexName);
        System.out.println("b:"+b);*/
        /*IndexRequest.Builder<Object> objectBuilder = new IndexRequest.Builder<>();
        objectBuilder.index("").*/

        String indexName = "test_index1";
        esServiceApi.addIndex(indexName);
        boolean b = esServiceApi.indexExists(indexName);
        System.out.println("b:"+b);

        /*Assertions.assertFalse(esService.indexExists(indexName));
        esService.addIndex(indexName);
        Assertions.assertTrue(esService.indexExists(indexName));
        esService.delIndex(indexName);
        Assertions.assertFalse(esService.indexExists(indexName));*/
    }
}
