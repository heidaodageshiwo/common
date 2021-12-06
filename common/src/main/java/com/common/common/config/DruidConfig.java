package com.common.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.config
 * @ClassName: DruidConfig
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-12-06 16:30
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-12-06 16:30
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */

@Configuration
public class DruidConfig {
  @Resource
  private JdbcParamConfig jdbcParamConfig ;

  @Bean
  public DataSource dataSource() {
    DruidDataSource datasource = new DruidDataSource();
    datasource.setUrl(jdbcParamConfig.getUrl());
    datasource.setDriverClassName(jdbcParamConfig.getDriverClassName());
    datasource.setInitialSize(jdbcParamConfig.getInitialSize());
    datasource.setMinIdle(jdbcParamConfig.getMinIdle());
    datasource.setMaxActive(jdbcParamConfig.getMaxActive());
    datasource.setMaxWait(jdbcParamConfig.getMaxWait());
    datasource.setUsername(jdbcParamConfig.getUsername());
    datasource.setPassword(jdbcParamConfig.getPassword());
    return datasource;
  }
}
