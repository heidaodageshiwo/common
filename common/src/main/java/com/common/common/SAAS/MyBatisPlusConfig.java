/*
package com.common.common.SAAS;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * @ProjectName: common1
 * @PackageName: com.common.common.SAAS
 * @ClassName: MyBatisPlusConfig
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-07-30  14:36
 * @UpdateDate: 2022-07-30  14:36
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 *//*

@Configuration
public class MyBatisPlusConfig {
    */
/**
     * 分页插件
     *
     * @return
     *//*

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        // 创建SQL解析器集合
        List<ISqlParser> sqlParserList = new ArrayList<>();

        // 创建租户SQL解析器
        TenantSqlParser tenantSqlParser = new TenantSqlParser();

        // 设置租户处理器
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                // 设置当前租户ID，实际情况你可以从cookie、或者缓存中拿都行
                return new StringValue("jiannan");
            }

            @Override
            public String getTenantIdColumn() {
                // 对应数据库租户ID的列名
                return "tenant_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 是否需要需要过滤某一张表
              */
/*  List<String> tableNameList = Arrays.asList("sys_user");
                if (tableNameList.contains(tableName)){
                    return true;
                }*//*

                return false;
            }
        });

        sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);

        return paginationInterceptor;
    }
}*/
