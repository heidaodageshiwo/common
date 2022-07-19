package com.common.common.rocketmq;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("orders")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Order {
    @TableId(type = IdType.ASSIGN_UUID)
    private Integer id;

//    @TableField(value = "user_id")
    private Integer userId;

//    @TableField(value = "goods_name")
    private String goodsName;

    @TableField(value = "total")
    private Integer total;


}
