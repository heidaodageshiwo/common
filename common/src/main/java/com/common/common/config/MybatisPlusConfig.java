package com.common.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.config
 * @ClassName: MybatisPlusConfig
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-11-19 8:48
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-11-19 8:48
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.common.common.mapper")
public class MybatisPlusConfig {

  // 最新版
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
  }

}
