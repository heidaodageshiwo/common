package com.common.common.ES8.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.ES8.entity
 * @ClassName: Product
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-09-24  16:50
 * @UpdateDate: 2022-09-24  16:50
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String sku;
    private String name;
    private double price;

//    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 反序列化
//    @JsonSerialize(using = LocalDateTimeSerializer.class) // 序列化
//    private LocalDateTime time;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}