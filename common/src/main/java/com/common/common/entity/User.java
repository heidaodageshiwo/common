package com.common.common.entity;

import lombok.Data;

/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.entity
 * @ClassName: User
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-11-18 14:03
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-11-18 14:03
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
@Data
public class User {
  private Long id;
  private String name;
  private Integer age;
  private String email;
}