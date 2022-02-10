package com.common.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.entity
 * @ClassName: menu
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateDate: 2022-02-10 17:17
 * @UpdateUser: zhangqiang
 * @UpdateDate: 2022-02-10 17:17
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("menu")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Menu {
  @TableId(type = IdType.ASSIGN_UUID)
  private String id;
  private String name;
  private String pid;

  @TableField(exist = false)
  List<Menu> children;
}
