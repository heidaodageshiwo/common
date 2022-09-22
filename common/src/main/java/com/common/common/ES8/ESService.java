package com.common.common.ES8;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.ES8
 * @ClassName: ESService
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-09-22  15:50
 * @UpdateDate: 2022-09-22  15:50
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class ESService {

    @Resource(name="clientByPasswd")
    private ElasticsearchClient elasticsearchClient;

    public void addIndex(String name) throws IOException {
        elasticsearchClient.indices().create(c -> c.index(name));
    }

    public boolean indexExists(String name) throws IOException {
        return elasticsearchClient.indices().exists(b -> b.index(name)).value();
    }

    public void delIndex(String name) throws IOException {
        elasticsearchClient.indices().delete(c -> c.index(name));
    }
}