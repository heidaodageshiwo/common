package com.common.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class User {
  @TableId(type = IdType.ASSIGN_UUID)
  private Long id;
  @TableField(value = "name")
  private String name;
  private Integer age;
  private String email;
}