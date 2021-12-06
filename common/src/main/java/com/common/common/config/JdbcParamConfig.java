package com.common.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.config
 * @ClassName: JdbcParamConfig
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-12-06 16:29
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-12-06 16:29
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */

@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource.click")
public class JdbcParamConfig {
  private String driverClassName ;
  private String url ;
  private String username ;
  private String password ;
  private Integer initialSize ;
  private Integer maxActive ;
  private Integer minIdle ;
  private Integer maxWait ;

}
